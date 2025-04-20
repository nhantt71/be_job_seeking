/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Services.SupabaseStorageService;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.ContentType;
import org.springframework.stereotype.Service;
import java.io.InputStream;

/**
 *
 * @author PC
 */
@Service
public class SupabaseStorageServiceImpl implements SupabaseStorageService {

    private static final String SUPABASE_PROJECT_URL = "https://iawvtssjyglptjrgmbfc.supabase.co";
    private static final String SERVICE_ROLE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imlhd3Z0c3NqeWdscHRqcmdtYmZjIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTc0NDE4MTg2MSwiZXhwIjoyMDU5NzU3ODYxfQ.iHmBkzYlDbaiMbRjsaLtZffDVyDkz74qMc57Cdu46L0";

    @Override
    public String uploadFile(String bucket, String filename, InputStream fileInputStream, String mimeType) throws Exception {
        String url = SUPABASE_PROJECT_URL + "/storage/v1/object/" + bucket + "/" + filename;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(url);
            request.addHeader("Authorization", "Bearer " + SERVICE_ROLE_KEY);
            request.addHeader("x-upsert", "true");

            InputStreamEntity entity = new InputStreamEntity(
                    fileInputStream,
                    -1, // unknown content length
                    ContentType.parse(mimeType) // parse MIME type (e.g. "image/png", "application/pdf")
            );

            request.setEntity(entity);

            var response = client.execute(request);
            int status = response.getCode();

            if (status == 200 || status == 201) {
                return SUPABASE_PROJECT_URL + "/storage/v1/object/public/" + bucket + "/" + filename;
            } else {
                throw new RuntimeException("Upload failed with status: " + status);
            }
        }
    }

}
