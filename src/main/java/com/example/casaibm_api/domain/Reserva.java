package com.example.casaibm_api.domain;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Reserva implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nomeHospede;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dataFim;

    private Integer quantidadePessoas;

    private Status status;

    public Reserva(Reserva reserva) {
        this.id = reserva.getId();
        this.nomeHospede = reserva.getNomeHospede();
        this.dataInicio = reserva.getDataInicio();
        this.dataFim = reserva.getDataFim();
        this.quantidadePessoas = reserva.getQuantidadePessoas();
        this.status = reserva.getStatus();
    }
}
