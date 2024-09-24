/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Category;
import com.ttn.jobapp.Repositories.CategoryRepository;
import com.ttn.jobapp.Services.CategoryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class CategoryServiceImpl implements CategoryService{
    
    @Autowired
    private CategoryRepository cr;

    @Override
    public Category save(Category category) {
        return cr.save(category);
    }

    @Override
    public List<Category> getCategories() {
        return cr.findAll();
    }

    @Override
    public void delete(Long id) {
        cr.deleteById(id);
    }
    
}
