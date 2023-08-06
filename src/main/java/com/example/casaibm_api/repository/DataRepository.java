package com.example.casaibm_api.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.casaibm_api.domain.Data;

public interface DataRepository extends JpaRepository<Data, Integer>{
    
    Data findByDia(Date dia);

}
