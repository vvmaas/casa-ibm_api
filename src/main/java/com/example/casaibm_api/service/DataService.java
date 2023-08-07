package com.example.casaibm_api.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.casaibm_api.domain.Data;
import com.example.casaibm_api.repository.DataRepository;
import com.example.casaibm_api.service.exceptions.BadRequestException;
import com.example.casaibm_api.service.exceptions.ConflictException;

@Service
public class DataService {
    
    @Autowired
    private DataRepository repository;

    public List<Data> findAll(){
        return repository.findAll();
    }

    public void checkDatesCreate(Long start, Long end) {
        if(start > end) {
            throw new BadRequestException(
                    "Dados inválidos.\n O dia de início da hospedagem deve anteceder o dia do fim.\n Dados diaInicio = " + new Date(start + 86400000) + " diaFim = " + new Date(end + 86400000) + " são inválidos."
                );
        }

        while(!start.equals(end + 86400000)){
            Data dia = repository.findByDia(new Date(start + 86400000));
            if(dia != null){
                throw new ConflictException(
                    "Dados inválidos.\n O dia " + new Date(dia.getDia().getTime() + 86400000) + " não está disponível. Selecione uma nova data."
                );
            }
            start += 86400000;
        }
    }

    public void checkDatesUpdate(Long oldStart, Long oldEnd, Long newStart, Long newEnd) {
        if(newStart > newEnd) {
            throw new BadRequestException(
                    "Dados inválidos.\n O dia de início da hospedagem deve anteceder o dia do fim.\n Dados diaInicio = " + new Date(newStart + 86400000) + " diaFim = " + new Date(newEnd + 86400000) + " são inválidos."
                );
        }

        while(!newStart.equals(newEnd + 86400000)){
            Data dia = repository.findByDia(new Date(newStart + 86400000));
            if(dia != null){
                saveDates(oldStart, oldEnd);
                throw new ConflictException(
                    "Não foi possível atualizar os dados.\n O dia " + new Date(dia.getDia().getTime()) + " não está disponível. Selecione uma nova data."
                );
            }
            newStart += 86400000;
        }
    }

    public void saveDates(Long start, Long end) {
        checkDatesCreate(start, end);
        while(!start.equals(end + 86400000)){
            Data dia = new Data(new Date(start + 86400000));
            repository.save(dia);
            start += 86400000;
        }
    }

    public void updateDates(Long oldStart, Long oldEnd, Long newStart, Long newEnd) {
        deleteDates(oldStart, oldEnd);
        checkDatesUpdate(oldStart, oldEnd, newStart, newEnd);
        while(!newStart.equals(newEnd + 86400000)){
            Data dia = new Data(new Date(newStart + 86400000));
            repository.save(dia);
            newStart += 86400000;
        }
    }

    public void deleteDates(Long start, Long end) {
        while(!start.equals(end + 86400000)){
            Data dia = repository.findByDia(new Date(start + 86400000));
            repository.deleteById(dia.getId());
            start += 86400000;
        }
    }
}
