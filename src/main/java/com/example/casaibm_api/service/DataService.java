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

    //Essa variável indica o valor de 1 dia em milissegundos 
    //É adicionada em chamadas de new Date() pois, sem essa adição, o retorno viria sempre 1 dia antes do que o desejado.
    private Integer day = 86400000;

    public void checkDatesCreate(Long start, Long end) {
        if(start > end) {
            throw new BadRequestException(
                    "Dados inválidos.\n O dia de início da hospedagem deve anteceder o dia do fim.\n Dados diaInicio = " + new Date(start + day) + " diaFim = " + new Date(end + day) + " são inválidos."
                );
        }

        while(!start.equals(end + day)){
            Data dia = repository.findByDia(new Date(start + day));
            if(dia != null){
                throw new ConflictException(
                    "Dados inválidos.\n O dia " + new Date(dia.getDia().getTime()) + " não está disponível. Selecione uma nova data."
                );
            }
            start += day;
        }
    }

    public void checkDatesUpdate(Long oldStart, Long oldEnd, Long newStart, Long newEnd) {
        if(newStart > newEnd) {
            throw new BadRequestException(
                    "Dados inválidos.\n O dia de início da hospedagem deve anteceder o dia do fim.\n Dados diaInicio = " + new Date(newStart + day) + " diaFim = " + new Date(newEnd + day) + " são inválidos."
                );
        }

        while(!newStart.equals(newEnd + day)){
            Data dia = repository.findByDia(new Date(newStart + day));
            if(dia != null){
                saveDates(oldStart, oldEnd);
                throw new ConflictException(
                    "Não foi possível atualizar os dados.\n O dia " + new Date(dia.getDia().getTime()) + " não está disponível. Selecione uma nova data."
                );
            }
            newStart += day;
        }
    }

    public void saveDates(Long start, Long end) {
        checkDatesCreate(start, end);
        while(!start.equals(end + day)){
            Data dia = new Data(new Date(start + day));
            repository.save(dia);
            start += day;
        }
    }

    public void updateDates(Long oldStart, Long oldEnd, Long newStart, Long newEnd) {
        deleteDates(oldStart, oldEnd);
        checkDatesUpdate(oldStart, oldEnd, newStart, newEnd);
        while(!newStart.equals(newEnd + day)){
            Data dia = new Data(new Date(newStart + day));
            repository.save(dia);
            newStart += day;
        }
    }

    public void deleteDates(Long start, Long end) {
        while(!start.equals(end + day)){
            Data dia = repository.findByDia(new Date(start + day));
            repository.deleteById(dia.getId());
            start += day;
        }
    }
}
