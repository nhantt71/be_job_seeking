/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;

/**
 *
 * @author PC
 */
import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}