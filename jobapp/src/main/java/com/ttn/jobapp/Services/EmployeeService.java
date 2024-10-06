/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.Employee;
import java.util.List;

/*
 *
 * @author Win11
 */


public interface EmployeeService {
    
    Employee save(Employee employee);
    
    List<Employee> getEmployees();
    
    void delete(Long id);
    
    Employee getEmployeeById(Long id);
    
}
