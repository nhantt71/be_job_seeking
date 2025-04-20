/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Utils;

import java.util.UUID;

/**
 *
 * @author PC
 */
public class GenerateUniqueFileName {

    public static String generateUniqueFileName(String originalName) {
        String extension = originalName.substring(originalName.lastIndexOf('.'));
        return UUID.randomUUID() + extension;
    }

}
