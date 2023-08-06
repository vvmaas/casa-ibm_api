package com.example.casaibm_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.casaibm_api.domain.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
    
}
