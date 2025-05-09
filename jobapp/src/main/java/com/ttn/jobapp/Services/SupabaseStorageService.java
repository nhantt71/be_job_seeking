/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author PC
 */
public interface SupabaseStorageService {

    String uploadFile(String bucket, String filename, InputStream fileInputStream,
            String contentType) throws Exception;

    InputStream downloadFile(String bucket, String filePath) throws IOException;

}
