/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Pojo.Address;
import com.ttn.jobapp.Services.AddressService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("api/address")
@CrossOrigin
public class ApiAddressController {

    @Autowired
    private AddressService as;

    @PostMapping("/create")
    public ResponseEntity<?> createAddress(@RequestParam Map<String, String> params) {

        if (!params.containsKey("province") || params.get("province").isEmpty()) {
            return new ResponseEntity<>("Province is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("detail") || params.get("detail").isEmpty()) {
            return new ResponseEntity<>("Detail is required!", HttpStatus.BAD_REQUEST);
        }

        if (!params.containsKey("city") || params.get("city").isEmpty()) {
            return new ResponseEntity<>("City is required!", HttpStatus.BAD_REQUEST);
        }

        Address address = new Address();
        address.setCity(params.get("city"));
        address.setDetail(params.get("detail"));
        address.setProvince(params.get("province"));
        

        try {
            return new ResponseEntity<>(this.as.save(address), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while creating the address.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
