/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;

/**
 *
 * @author Win11
 */
public interface JobStatisticsProjection {
    int getPeriod();
    long getJobCount();
    long getApplicationCount();
}
