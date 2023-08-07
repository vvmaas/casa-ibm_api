package com.example.casaibm_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.casaibm_api.domain.Reserva;
import com.example.casaibm_api.domain.Status;
import com.example.casaibm_api.dto.ReservaDTO;
import com.example.casaibm_api.repository.ReservaRepository;
import com.example.casaibm_api.service.exceptions.BadRequestException;
import com.example.casaibm_api.service.exceptions.ObjectNotFoundException;

@Service
public class ReservaService {

    @Autowired
    private DataService dataService;
    
    @Autowired
    private ReservaRepository repository;

    public Reserva create(Reserva obj) {
        obj.setId(null);
        obj.setStatus(Status.CONFIRMADA);
        dataService.saveDates(obj.getDataInicio().getTime(), obj.getDataFim().getTime());
        return repository.save(obj);
    }

    public List<Reserva> findAll() {
        return repository.findAll();
    }

    public Reserva findById(Integer id) {
        return repository.findById(id).orElseThrow(() -> 
        new ObjectNotFoundException(
                "Reserva não encontrada. \n O código de registro " + id + " não existe."
            ));
    }

    public Reserva delete(Integer id) {
        Reserva reserva = findById(id);
        checkCancelledReserva(reserva);
        reserva.setStatus(Status.CANCELADA);
        dataService.deleteDates(reserva.getDataInicio().getTime(), reserva.getDataFim().getTime());
        return repository.save(reserva);
    }

    public Reserva update(Reserva obj) {  
        Reserva reserva = findById(obj.getId());
        checkCancelledReserva(reserva);
        if(obj.getStatus() == Status.CANCELADA) {
            throw new BadRequestException(
                "Dados inválidos.\n Para cancelar a reserva, envie uma requisição DELETE para /reservas/" + obj.getId() + "/cancelar"
                );
        }
        
        if(obj.getDataInicio() == null) obj.setDataInicio(reserva.getDataInicio());
        if(obj.getDataFim() == null) obj.setDataFim(reserva.getDataFim());

        dataService.updateDates(reserva.getDataInicio().getTime(), reserva.getDataFim().getTime(), obj.getDataInicio().getTime(), obj.getDataFim().getTime());
        updateReserva(reserva, obj);
        return repository.save(reserva);
    } 

     private void updateReserva(Reserva data, Reserva update) {
        if(update.getNomeHospede() != null) data.setNomeHospede(update.getNomeHospede());
        data.setDataInicio(update.getDataInicio());
        data.setDataFim(update.getDataFim());
        if(update.getQuantidadePessoas() != null) data.setQuantidadePessoas(update.getQuantidadePessoas());
        if(update.getStatus() != null) data.setStatus(update.getStatus());
    }

    private void checkCancelledReserva(Reserva obj) {
        if(obj.getStatus() == Status.CANCELADA){
            throw new BadRequestException(
                "A reserva de código " + obj.getId() + " foi cancelada. \n Faça uma nova reserva."
                );
        } 
    }

    public Reserva fromDTO(ReservaDTO dto) {
        return new Reserva(dto.getId(), dto.getNomeHospede(), dto.getDataInicio(), dto.getDataFim(), dto.getQuantidadePessoas(), dto.getStatus());
    }
}
