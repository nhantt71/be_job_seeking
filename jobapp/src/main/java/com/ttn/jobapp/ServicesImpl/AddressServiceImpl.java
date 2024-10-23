/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Address;
import com.ttn.jobapp.Pojo.Company;
import com.ttn.jobapp.Repositories.AddressRepository;
import com.ttn.jobapp.Repositories.CompanyRepository;
import com.ttn.jobapp.Services.AddressService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository ar;

    @Autowired
    private CompanyRepository cr;

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

    @Override
    public List<Address> getAddressNotAttach() {
        List<Company> companies = this.cr.findAll();

        Set<Long> attachedAddressIds = new HashSet<>();
        for (Company company : companies) {
            Address address = company.getAddress();
            if (address != null) {
                attachedAddressIds.add(address.getId());
            }
        }

        List<Address> allAddresses = this.ar.findAll();

        List<Address> notAttachedAddresses = allAddresses.stream()
                .filter(address -> !attachedAddressIds.contains(address.getId()))
                .collect(Collectors.toList());

        return notAttachedAddresses;
    }

}
