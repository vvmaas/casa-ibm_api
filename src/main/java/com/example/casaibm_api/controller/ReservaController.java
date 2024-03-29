package com.example.casaibm_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.casaibm_api.domain.Reserva;
import com.example.casaibm_api.dto.ReservaDTO;
import com.example.casaibm_api.service.ReservaService;

@RestController
@CrossOrigin(origins = {"https://casa-ibm-web.vercel.app", "http://localhost:4200"})
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService service;
    
    @PostMapping
    public ResponseEntity<Reserva> create(@RequestBody ReservaDTO body) {
        Reserva obj = service.fromDTO(body);
        obj = service.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> findAll() {
        List<Reserva> obj = service.findAll();
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Integer id) {
        Reserva obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> update(@RequestBody ReservaDTO body, @PathVariable Integer id) {
        Reserva obj = service.fromDTO(body);
        obj.setId(id);
        obj = service.update(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> delete(@PathVariable Integer id) {
        Reserva reserva = service.delete(id);
        return ResponseEntity.ok().body(reserva);
    }
}
