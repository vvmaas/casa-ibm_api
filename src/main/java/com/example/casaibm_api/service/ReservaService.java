package com.example.casaibm_api.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.casaibm_api.domain.Data;
import com.example.casaibm_api.domain.Reserva;
import com.example.casaibm_api.domain.Status;
import com.example.casaibm_api.dto.ReservaDTO;
import com.example.casaibm_api.repository.DataRepository;
import com.example.casaibm_api.repository.ReservaRepository;
import com.example.casaibm_api.service.exceptions.BadRequestException;
import com.example.casaibm_api.service.exceptions.ConflictException;
import com.example.casaibm_api.service.exceptions.ObjectNotFoundException;

@Service
public class ReservaService {

    @Autowired
    private DataRepository dataRepository;
    
    @Autowired
    private ReservaRepository repository;

    public Reserva create(Reserva obj) {
        checkCreateBody(obj);
        obj.setStatus(Status.CONFIRMADA);
        checkDates(obj.getDataInicio().getTime(), obj.getDataFim().getTime());
        saveDates(obj.getDataInicio().getTime(), obj.getDataFim().getTime());
        return repository.save(obj);
    }

    public List<Reserva> findAll() {
        return repository.findAll();
    }

    public Reserva findById(Integer id) {
        Reserva reserva = repository.findById(id).orElseThrow(() -> 
        new ObjectNotFoundException(
                "Reserva não encontrada. \n O código de registro " + id + " não existe."
            ));
        checkCancelledReserva(reserva);
        return reserva;
    }

    public Reserva delete(Integer id) {
        Reserva reserva = findById(id);
        reserva.setStatus(Status.CANCELADA);
        return repository.save(reserva);
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

    private void checkCancelledReserva(Reserva obj) {
        if(obj.getStatus() == Status.CANCELADA){
            throw new BadRequestException(
                "A reserva de código " + obj.getId() + " foi cancelada. \n Faça uma nova reserva."
                );
        } 
    }

    private void checkCreateBody(Reserva obj) {
        if(obj.getStatus() != null || obj.getId() != null){
            throw new BadRequestException(
                "Dados inválidos para criação da reserva. Campos ID e STATUS não devem ser enviados"
                );
        }
    }

    private void checkDates(Long start, Long end) {
        start += 86400000;
        end += 86400000;

        while(!start.equals(end + 86400000)){
            Data dia = dataRepository.findByDia(new Date(start));
            if(dia != null){
                throw new ConflictException(
                    "Dados inválidos.\n O dia " + dia + " não está disponível. Selecione uma nova data."
                );
            }
            System.out.println(dia);
            start += 86400000;
        }
    }

    private void saveDates(Long start, Long end) {
        start += 86400000;
        end += 86400000;

        while(!start.equals(end + 86400000)){
            Data dia = new Data(new Date(start));
            dataRepository.save(dia);
            start += 86400000;
        }
    }

    public Reserva fromDTO(ReservaDTO dto) {
        return new Reserva(dto.getId(), dto.getNomeHospede(), dto.getDataInicio(), dto.getDataFim(), dto.getQuantidadePessoas(), dto.getStatus());
    }
}
