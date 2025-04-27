/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Repositories;

import com.ttn.jobapp.Pojo.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Win11
 */
@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long>{
    Optional<Account> findByEmail(String email);
    
    Boolean existsByEmail(String email);
    
    @Query("SELECT a.role FROM Account a WHERE a.email LIKE :email")
    String getRoleByEmail(@Param("email") String email);
    
    @Query("SELECT a FROM Account a where a.verifyToken LIKE :verifyToken")
    Optional<Account> findByVerifyToken(@Param("verifyToken") String verifyToken);
    
    @Query("SELECT a FROM Account a WHERE a.email = :email AND a.role = 'ADMIN'")
    Optional<Account> findAdminByEmail(@Param("email") String email);
    
}
