package com.stibits.rnft.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.tika.Tika;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileTypeService {
    private Tika tika = new Tika();

    public boolean checkMimeType (InputStream inputStream, Pattern expected) {
        String mimeType = this.getMimeType(inputStream);

        if (!StringUtils.hasText(mimeType)) return false;

        return expected.matcher(mimeType).matches() || expected.matcher(mimeType).find();
    }

    public boolean checkMimeType (InputStream inputStream, String expected) {
        String mimeType = this.getMimeType(inputStream);

        if (!StringUtils.hasText(mimeType)) return false;

        return expected.equals(mimeType);
    }

    public String getMimeType (InputStream inputStream) {
        try {
            return tika.detect(inputStream);
        } catch (IOException ex) {
            log.error("getMimeType : " + ex.getMessage(), ex);
            return null;
        }
    }
}
