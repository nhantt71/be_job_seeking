/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Address;
import com.ttn.jobapp.Repositories.AddressRepository;
import com.ttn.jobapp.Services.AddressService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class AddressServiceImpl implements AddressService{
    
    @Autowired
    private AddressRepository ar;

    @Override
    public Address save(Address address) {
        return ar.save(address);
    }

    @Override
    public List<Address> getAddress() {
        return ar.findAll();
    }

    @Override
    public void delete(Long id) {
        ar.deleteById(id);
    }
    
}
