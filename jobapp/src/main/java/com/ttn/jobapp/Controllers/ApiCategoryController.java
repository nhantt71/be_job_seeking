/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.CategoryDto;
import com.ttn.jobapp.Pojo.Category;
import com.ttn.jobapp.Services.CategoryService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
    public ResponseEntity<List<Category>> allCategories() {
        return new ResponseEntity<>(this.cs.getCategories(), HttpStatus.OK);
    }

    @GetMapping("/get-job-amount")
    public ResponseEntity<List<CategoryDto>> getJobAmountWithCate() {
        List<Category> cates = cs.getCategories();

        List<CategoryDto> cateDtos = cates.stream()
                .map(category -> {
                    int jobs = cs.countJobsByCate(category);
                    CategoryDto cDto = new CategoryDto();
                    cDto.setId(category.getId());
                    cDto.setName(category.getName());
                    cDto.setIcon(category.getIcon());
                    cDto.setJobs(jobs);
                    return cDto;
                })
                .sorted(Comparator.comparingInt(CategoryDto::getJobs).reversed())
                .collect(Collectors.toList());

        if (!cateDtos.isEmpty()) {
            return new ResponseEntity<>(cateDtos, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
