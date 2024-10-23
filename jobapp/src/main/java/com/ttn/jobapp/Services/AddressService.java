/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.Address;
import java.util.List;

/**
 *
 * @author Win11
 */
public interface AddressService {
    
    Address save(Address address);

    List<Address> getAddress();

    void delete(Long id);
    
    List<Address> getAddressNotAttach();
}
