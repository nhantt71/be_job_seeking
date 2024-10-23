/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Repositories;

import com.ttn.jobapp.Pojo.MongoExtractCV;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Win11
 */
@Repository
public interface MongoExtractCVRepository extends MongoRepository<MongoExtractCV, String>{}
