/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.Category;
import java.util.List;

/**
 *
 * @author Win11
 */
public interface CategoryService {
    
    Category save(Category category);

    List<Category> getCategories();

    void delete(Long id);
}
