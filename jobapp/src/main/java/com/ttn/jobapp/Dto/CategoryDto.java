/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;

import lombok.Data;

/**
 *
 * @author MyLaptop
 */
@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String icon;
    private int jobs;
}
