package com.example.casaibm_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.casaibm_api.domain.Reserva;
import com.example.casaibm_api.dto.ReservaDTO;
import com.example.casaibm_api.repository.ReservaRepository;

@Service
public class ReservaService {
    
    @Autowired
    private ReservaRepository repository;

    public Reserva create(Reserva obj) {
        obj.setId(null);
        return repository.save(obj);
    }

    public List<Reserva> findAll() {
        return repository.findAll();
    }

    public Reserva findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Reserva update(Reserva obj) {
        Reserva data = findById(obj.getId());
        updateData(data, obj);
        return repository.save(data);
    }

     private void updateData(Reserva data, Reserva update) {
        data.setNomeHospede(update.getNomeHospede());
        data.setDataInicio(update.getDataInicio());
        data.setDataFim(update.getDataFim());
        data.setQuantidadePessoas(update.getQuantidadePessoas());
        data.setStatus(update.getStatus());
    }

    public Reserva fromDTO(ReservaDTO dto) {
        return new Reserva(dto.getId(), dto.getNomeHospede(), dto.getDataInicio(), dto.getDataFim(), dto.getQuantidadePessoas(), dto.getStatus());
    }
}
