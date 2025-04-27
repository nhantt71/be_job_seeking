/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import java.io.InputStream;

/**
 *
 * @author PC
 */
public interface TikaOcrService {
    String extractText(InputStream stream) throws Exception;
}
