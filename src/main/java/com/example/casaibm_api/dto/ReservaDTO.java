package com.example.casaibm_api.dto;

import com.example.casaibm_api.domain.Reserva;
import com.example.casaibm_api.domain.Status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class ReservaDTO implements Serializable{
    
    private Integer id;

    private String nomeHospede;

    private Date dataInicio;

    private Date dataFim;

    private Integer quantidadePessoas;

    private Status status;

    public ReservaDTO(Reserva obj) {
        id = obj.getId();
        nomeHospede = obj.getNomeHospede();
        dataInicio = obj.getDataInicio();
        dataFim = obj.getDataFim();
        quantidadePessoas = obj.getQuantidadePessoas();
        status = obj.getStatus();
    }
}
