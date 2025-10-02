package com.userdetails.userdetails.repository;


import com.userdetails.userdetails.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}