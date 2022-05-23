package org.stibits.rnft.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.stibits.rnft.errors.StorageUnacceptedMediaType;
import org.stibits.rnft.utils.RandomGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

@Component
public class StorageService {
    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    private final Path uploadDirPath;
    
    @Autowired
    private RandomGenerator randomGenerator;

    public StorageService (@Value("${file.uploadDir}") String uploadDir) {
        this.uploadDirPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadDirPath);
        } catch (Exception ex) {
            logger.error("StorageService() - Could not create upload dir", ex);
        } 
    }

    public String storeFile (MultipartFile file, String subdir) throws StorageUnacceptedMediaType {
        return this.storeFile(file, null, subdir);
    }

    public String storeFile (MultipartFile file, Pattern accept) throws StorageUnacceptedMediaType {
        return this.storeFile(file, accept, null);
    }

    public String storeFile (MultipartFile file) throws StorageUnacceptedMediaType {
        return this.storeFile(file, null, null);
    }

    public String storeFile (MultipartFile file, Pattern accept, String subdir) throws StorageUnacceptedMediaType {
        try {
            MagicMatch match = Magic.getMagicMatch(file.getBytes());
            String contentType = match.getMimeType();
            String ext = match.getExtension();

            if (accept != null && !accept.matcher(contentType).matches()) {
                throw new StorageUnacceptedMediaType(contentType);
            }

            if (ext == null || ext.isEmpty()) ext = contentType.split("\\/")[1];
            
            String filename = StringUtils.cleanPath(this.randomGenerator.generateRandomStr(50) + "." + ext);
            Path targetLocation = subdir != null ? this.uploadDirPath.resolve(subdir).resolve(filename) : this.uploadDirPath.resolve(filename);

            Files.createDirectories(targetLocation.getParent());

            file.transferTo(targetLocation);
            
            return filename;
        } catch (MagicParseException|MagicMatchNotFoundException|MagicException|IOException ex) {
            logger.error("storeFile() - " + ex.getMessage(), ex);
        }
        
        return null;
    }
}
