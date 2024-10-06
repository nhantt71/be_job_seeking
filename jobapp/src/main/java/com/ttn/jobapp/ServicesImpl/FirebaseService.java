/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class FirebaseService {

    private final FirebaseDatabase firebaseDatabase;

    @Autowired
    public FirebaseService() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-adminsdk.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://jobapp-6eac3-default-rtdb.asia-southeast1.firebasedatabase.app")
                .build();

        FirebaseApp.initializeApp(options);
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void sendNotification(Long jobId, String applicantName, String applicantEmail) {
        String id = String.valueOf(jobId);
        DatabaseReference ref = firebaseDatabase.getReference("notifications").child(id);
        Map<String, String> notification = new HashMap<>();
        notification.put("message", applicantName + " đã apply vào công việc này.");
        notification.put("applicantEmail", applicantEmail);

        ApiFuture<Void> future = ref.push().setValueAsync(notification);

        try {
            future.get();
            System.out.println("Notification sent to Firebase successfully.");
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Failed to send notification to Firebase: " + e.getMessage());
        }
    }

}
