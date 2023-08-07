package com.example.casaibm_api.service;

import java.util.ArrayList;
import java.util.Date;
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
    //É utilizada em chamadas de new Date() para compensar o comportamento da função.
    private Integer day = 86400000;

    private void checkDatesCreate(Long start, Long end) {
        validateDates(start, end);
        List<Date> invalidos = new ArrayList<Date>();

        while(!start.equals(end + day)){
            Data dia = repository.findByDia(newDate(start));
            if(dia != null) invalidos.add(dia.getDia());
            start += day;
        }

        if(!invalidos.isEmpty()){
                throw new ConflictException(
                    "Dados inválidos.\n Os seguintes dias: " + invalidos + " não estão disponíveis. Selecione uma nova data."
                );
            }
    }

    private void checkDatesUpdate(Long oldStart, Long oldEnd, Long newStart, Long newEnd) {
        validateDates(newStart, newEnd);
        List<Date> invalidos = new ArrayList<Date>();

        while(!newStart.equals(newEnd + day)){
            Data dia = repository.findByDia(newDate(newStart));
            if(dia != null) invalidos.add(dia.getDia());
            newStart += day;
        }

        if(!invalidos.isEmpty()){
                saveDates(oldStart, oldEnd);
                throw new ConflictException(
                    "Não foi possível atualizar os dados.\n Os seguintes dias: " + invalidos + " não estão disponíveis. Selecione uma nova data."
                );
            }
    }

    public void saveDates(Long start, Long end) {
        checkDatesCreate(start, end);
        insertDates(start, end);
    }

    public void updateDates(Long oldStart, Long oldEnd, Long newStart, Long newEnd) {
        deleteDates(oldStart, oldEnd);
        checkDatesUpdate(oldStart, oldEnd, newStart, newEnd);
        insertDates(newStart, newEnd);
    }

    public void deleteDates(Long start, Long end) {
        while(!start.equals(end + day)){
            Data dia = repository.findByDia(newDate(start));
            repository.deleteById(dia.getId());
            start += day;
        }
    }

    private void insertDates(Long start, Long end) {
        while(!start.equals(end + day)){
            Data dia = new Data(newDate(start));
            repository.save(dia);
            start += day;
        }
    }

    private void validateDates(Long start, Long end) {
        if(start > end) {
            throw new BadRequestException(
                    "Dados inválidos.\n O dia de início da hospedagem deve anteceder o dia do fim.\n Dados diaInicio = " + newDate(start) + " diaFim = " + newDate(end) + " são inválidos."
                );
        }
    }

    private Date newDate(Long data){
        return new Date(data + day/2);
    }
}
