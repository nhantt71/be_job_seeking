/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author MyLaptop
 */
@UtilityClass
public class CloudinaryUtils {

    @Autowired
    private Cloudinary cloudinary;

    public Map deleteImage(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public Map deleteImageByUrl(String imageUrl) throws IOException {
        String publicId = extractPublicIdFromUrl(imageUrl);
        return deleteImage(publicId);
    }

    public String extractPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/upload/");
        if (parts.length == 2) {
            String publicIdWithExtension = parts[1];
            return publicIdWithExtension.split("\\.")[0];
        }
        throw new IllegalArgumentException("Invalid Cloudinary URL");
    }
}
