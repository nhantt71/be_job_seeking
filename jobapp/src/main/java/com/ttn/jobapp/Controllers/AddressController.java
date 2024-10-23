/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.AddressDto;
import com.ttn.jobapp.Pojo.Address;
import com.ttn.jobapp.Repositories.AddressRepository;
import com.ttn.jobapp.Services.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Win11
 */
@Controller
@RequestMapping("admin/address")
public class AddressController {

    @Autowired
    private AddressRepository ar;

    @Autowired
    private AddressService as;

    @GetMapping
    public String address(Model model) {
        model.addAttribute("addresses", this.as.getAddress());
        return "admin/address/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        AddressDto addressDto = new AddressDto();
        model.addAttribute("addressDto", addressDto);
        return "admin/address/form";
    }

    @GetMapping("/delete")
    public String deleteAddress(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            this.as.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Address deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete this address because it is associated with a company.");
        }
        return "redirect:/admin/address";
    }

    @PostMapping("/create")
    public String createAddress(@Valid @ModelAttribute AddressDto addressDto, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "admin/address/form";
        }

        Address address = new Address();
        address.setDetail(addressDto.getDetail());
        address.setCity(addressDto.getCity());
        address.setProvince(addressDto.getProvince());

        this.as.save(address);

        return "redirect:/admin/address";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam Long id) {
        try {
            Address address = ar.findById(id).get();
            model.addAttribute("address", address);

            AddressDto addressDto = new AddressDto();
            addressDto.setDetail(address.getDetail());
            addressDto.setCity(address.getCity());
            addressDto.setProvince(address.getProvince());

            model.addAttribute("addressDto", addressDto);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "redirect:/admin/address";
        }

        return "admin/address/edit";
    }

    @PostMapping("/edit")
    public String updateAddress(Model model, @Valid @ModelAttribute AddressDto addressDto,
            BindingResult result, @RequestParam Long id) {

        Address address = ar.findById(id).get();
        model.addAttribute("address", address);
        if (result.hasErrors()) {
            return "admin/address/edit";
        }
        address.setDetail(addressDto.getDetail());
        address.setCity(addressDto.getCity());
        address.setProvince(addressDto.getProvince());
        this.as.save(address);

        return "redirect:/admin/address";
    }

}
