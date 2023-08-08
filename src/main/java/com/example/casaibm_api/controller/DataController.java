package com.example.casaibm_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.casaibm_api.domain.Data;
import com.example.casaibm_api.service.DataService;

@RestController
@CrossOrigin(origins = {"https://casa-ibm-web.vercel.app", "http://localhost:4200"})
@RequestMapping("/dias-reservados")
public class DataController {

    @Autowired
    private DataService service;
    
    @GetMapping
    public ResponseEntity<List<Data>> findAll() {
        List<Data> obj = service.findAll();
        return ResponseEntity.ok().body(obj);
    }
}
