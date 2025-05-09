/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.CategoryDto;
import com.ttn.jobapp.Pojo.Category;
import com.ttn.jobapp.Repositories.CategoryRepository;
import com.ttn.jobapp.Services.CategoryService;
import com.ttn.jobapp.Services.SupabaseStorageService;
import com.ttn.jobapp.Utils.GenerateUniqueFileName;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Win11
 */
@Controller
@RequestMapping("admin/category")
public class CategoryController {

    @Autowired
    private CategoryRepository cr;

    @Autowired
    private CategoryService cs;

    @Autowired
    private SupabaseStorageService supabaseStorageService;

    @GetMapping
    public String category(Model model) {
        model.addAttribute("categories", this.cs.getCategories());
        return "admin/category/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        CategoryDto categoryDto = new CategoryDto();
        model.addAttribute("categoryDto", categoryDto);
        return "admin/category/form";
    }

    @GetMapping("/delete")
    public String deleteCategory(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.cs.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete this category because it is associated with a  job.");
        }
        return "redirect:/admin/category";
    }

    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute CategoryDto categoryDto, BindingResult result) throws IOException, Exception {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/category/form";
        }

        if (categoryDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("categoryDto", "imageFile", "The image file is required"));
        }

        Category category = new Category();
        category.setName(categoryDto.getName());

        String categoryUrl = supabaseStorageService.uploadFile(
                "category-icon",
                GenerateUniqueFileName.generateUniqueFileName(categoryDto.getImageFile().getOriginalFilename()),
                categoryDto.getImageFile().getInputStream(),
                categoryDto.getImageFile().getContentType()
        );

        category.setIcon(categoryUrl);

        this.cs.save(category);

        return "redirect:/admin/category";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            Category category = cr.findById(id).get();
            model.addAttribute("category", category);

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(category.getName());

            model.addAttribute("categoryDto", categoryDto);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/category";
        }

        return "admin/category/edit";
    }

    @PostMapping("/edit")
    public String updateCategory(Model model, @Valid @ModelAttribute CategoryDto categoryDto,
            BindingResult result, @RequestParam Long id) throws Exception {

        Category category = cr.findById(id).get();
        model.addAttribute("category", category);
        if (result.hasErrors()) {
            return "admin/category/edit";
        }
        if (!categoryDto.getImageFile().isEmpty()) {
            String categoryUrl = supabaseStorageService.uploadFile(
                    "category-icon",
                    GenerateUniqueFileName.generateUniqueFileName(categoryDto.getImageFile().getOriginalFilename()),
                    categoryDto.getImageFile().getInputStream(),
                    categoryDto.getImageFile().getContentType()
            );
            category.setIcon(categoryUrl);
        }

        category.setName(categoryDto.getName());
        this.cs.save(category);

        return "redirect:/admin/category";
    }

}
