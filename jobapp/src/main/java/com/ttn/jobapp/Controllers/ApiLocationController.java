/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("api/v1/locations")
@CrossOrigin
public class ApiLocationController {
    @GetMapping("/provinces")
    public ResponseEntity<List<String>> getProvinces() {
        List<String> provinces = Arrays.asList(
            "An Giang", "Ba Ria - Vung Tau", "Bac Giang", "Bac Kan", "Bac Lieu", "Bac Ninh",
            "Ben Tre", "Binh Dinh", "Binh Duong", "Binh Phuoc", "Binh Thuan", "Ca Mau",
            "Can Tho", "Cao Bang", "Da Nang", "Dak Lak", "Dak Nong", "Dien Bien", "Dong Nai",
            "Dong Thap", "Gia Lai", "Ha Giang", "Ha Nam", "Ha Noi", "Ha Tinh", "Hai Duong",
            "Hai Phong", "Hau Giang", "Hoa Binh", "Hung Yen", "Khanh Hoa", "Kien Giang", 
            "Kon Tum", "Lai Chau", "Lam Dong", "Lang Son", "Lao Cai", "Long An", "Nam Dinh", 
            "Nghe An", "Ninh Binh", "Ninh Thuan", "Phu Tho", "Phu Yen", "Quang Binh", 
            "Quang Nam", "Quang Ngai", "Quang Ninh", "Quang Tri", "Soc Trang", "Son La", 
            "Tay Ninh", "Thai Binh", "Thai Nguyen", "Thanh Hoa", "Thua Thien - Hue", 
            "Tien Giang", "TP Ho Chi Minh", "Tra Vinh", "Tuyen Quang", "Vinh Long", 
            "Vinh Phuc", "Yen Bai"
        );

        List<String> provincesNoAccent = provinces.stream()
            .map(this::removeAccent)
            .collect(Collectors.toList());

        return new ResponseEntity<>(provincesNoAccent, HttpStatus.OK);
    }

    private String removeAccent(String s) {
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").replaceAll("Đ", "D").replaceAll("đ", "d");
    }
}
