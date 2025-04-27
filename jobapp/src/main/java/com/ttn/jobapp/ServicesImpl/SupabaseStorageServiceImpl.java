/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Services.SupabaseStorageService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author PC
 */
@Slf4j
@Service
public class SupabaseStorageServiceImpl implements SupabaseStorageService {

    @Value("${supabase.url}")
    private String SUPABASE_PROJECT_URL;

    @Value("${supabase.service_role}")
    private String SERVICE_ROLE_KEY;

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    @Override
    public String uploadFile(String bucket, String filename, InputStream fileInputStream, String mimeType) throws Exception {
        String url = SUPABASE_PROJECT_URL + "/storage/v1/object/" + bucket + "/" + filename;

        try (fileInputStream) {
            HttpPut request = new HttpPut(url);
            request.addHeader("Authorization", "Bearer " + SERVICE_ROLE_KEY);
            request.addHeader("x-upsert", "true");

            InputStreamEntity entity = new InputStreamEntity(
                    fileInputStream,
                    -1, // unknown content length
                    ContentType.parse(mimeType) // parse MIME type (e.g. "image/png", "application/pdf")
            );

            request.setEntity(entity);

            var response = httpClient.execute(request);
            int status = response.getCode();

            if (status == 200 || status == 201) {
                return SUPABASE_PROJECT_URL + "/storage/v1/object/public/" + bucket + "/" + filename;
            } else {
                throw new RuntimeException("Upload failed with status: " + status);
            }
        }
    }

    @Override
    public InputStream downloadFile(String bucket, String filePath) throws IOException {
        String url = String.format("%s/storage/v1/object/%s/%s", SUPABASE_PROJECT_URL, bucket, filePath);
        HttpGet request = new HttpGet(url);
        request.addHeader("Authorization", "Bearer " + SERVICE_ROLE_KEY);

        CloseableHttpResponse response = httpClient.execute(request);
        if (response.getCode() == 200) {
            return response.getEntity().getContent();
        }
        throw new IOException("Failed to download file: HTTP " + response.getCode());
    }

    @PreDestroy
    public void cleanup() throws IOException {
        httpClient.close();
    }
}
