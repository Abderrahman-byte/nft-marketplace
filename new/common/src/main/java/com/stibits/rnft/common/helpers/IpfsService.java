package com.stibits.rnft.common.helpers;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.stibits.rnft.common.api.IpfsSuccessResponse;
import com.stibits.rnft.common.utils.IpfsServiceConfig;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class IpfsService {
    private String apiUrl;
    private String apiAuth;
    private URI endpointUrl;
    private String ipfsGateway;
    
    public IpfsService (IpfsServiceConfig config) {
        this.apiUrl = config.getApiUrl();
        this.apiAuth = config.getApiAuthorization();
        this.endpointUrl = UriComponentsBuilder.fromHttpUrl(this.apiUrl).build().toUri();
        this.ipfsGateway = config.getGateway();
    }

    public Mono<String> uploadFile (InputStream inputStream) {
        return this.uploadFile(new InputStreamResource(inputStream));
    }

    public Mono<String> uploadFile (File file) {
        return this.uploadFile(new FileSystemResource(file));
    }

    public Mono<String> uploadFile (FilePart filePart) {
        MultipartBodyBuilder multipartBuilder = new MultipartBodyBuilder();

        multipartBuilder.part("file1", filePart).headers(headers -> {
            for (Entry<String, List<String>> entry : filePart.headers().entrySet())
                headers.addAll(entry.getKey(), entry.getValue());
        });

        return this.uploadFile(multipartBuilder);
    }

    public Mono<String> uploadFile (InputStreamSource inputStream) {
        MultipartBodyBuilder multipartBuilder = new MultipartBodyBuilder();
        multipartBuilder.part("file1", inputStream);

        return this.uploadFile(multipartBuilder);
    }

    private Mono<String> uploadFile (MultipartBodyBuilder multipartBuilder) {
        return WebClient.create()
            .post()
            .uri(this.endpointUrl)
            .header(HttpHeaders.AUTHORIZATION, this.apiAuth)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(multipartBuilder.build()))
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.OK))  {
                    return response.bodyToMono(IpfsSuccessResponse.class);
                }

                log.info("Got Status : " + response.statusCode());

                response.bodyToMono(String.class)
                    .subscribe(body -> log.info("Got body : " + body));

                return Mono.empty();
            }).flatMap(response -> {
                if (response.getBody() == null) return Mono.empty();

                return Mono.just(response.getBody().getStibitsipfs());
            });
    }

    public String resolveHashUrl (String hash) {
        if (!StringUtils.hasText(this.ipfsGateway)) return hash;

        try {
            return UriComponentsBuilder.fromHttpUrl(this.ipfsGateway)
                .path("/ipfs/" + hash)
                .build()
                .toUriString();
        } catch (IllegalArgumentException ex) {
            log.info("[resolveHashUrl] " + ex.getMessage());
            return hash;
        }
    }
}
