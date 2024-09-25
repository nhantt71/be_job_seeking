/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.CategoryDto;
import com.ttn.jobapp.Pojo.Category;
import com.ttn.jobapp.Services.CategoryService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Win11
 */
@RestController
@CrossOrigin
@RequestMapping("api/category")
public class ApiCategoryController {
    
    @Autowired
    private CategoryService cs;
    
    @GetMapping
    public ResponseEntity<List<Category>> allCategories(){
        return new ResponseEntity<>(this.cs.getCategories(), HttpStatus.OK);
    }
    
    @GetMapping("/get-job-amount")
    public ResponseEntity<List<CategoryDto>> getJobAmountWithCate(){
        List<CategoryDto> cateDtos = new ArrayList<>();
        
        List<Category> cates = cs.getCategories();
        
        cates.forEach(x -> {
            int jobs = cs.countJobsByCate(x);
            CategoryDto cDto = new CategoryDto();
            cDto.setName(x.getName());
            cDto.setIcon(x.getIcon());
            cDto.setJobs(jobs);
            cateDtos.add(cDto);
        });
        
        if (cateDtos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } return new ResponseEntity<>(cateDtos, HttpStatus.OK);
    }
}
