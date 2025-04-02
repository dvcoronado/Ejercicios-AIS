package es.codeurjc.web.nitflex.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ImageUtils {

    // Definimos la constante para el mensaje de error
    private static final String IMAGE_PROCESSING_ERROR = "Error at processing the image";

    public Blob remoteImageToBlob(String imageUrl){
        try {
            Resource image = new UrlResource(imageUrl);
            return BlobProxy.generateProxy(image.getInputStream(), image.contentLength());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, IMAGE_PROCESSING_ERROR);
        }
    }

    public Blob localImageToBlob(String localFilePath){
        File imageFile = new File(localFilePath);
        if (imageFile.exists()) {
            try {
                return BlobProxy.generateProxy(imageFile.toURI().toURL().openStream(), imageFile.length());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, IMAGE_PROCESSING_ERROR);
            }
        }
        return null;
    }

    public Blob multiPartFileImageToBlob(MultipartFile image){
        if (image != null && !image.isEmpty()) {
            try {
                return BlobProxy.generateProxy(image.getInputStream(), image.getSize());
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, IMAGE_PROCESSING_ERROR);
            }
        }
        return null;
    }
}

