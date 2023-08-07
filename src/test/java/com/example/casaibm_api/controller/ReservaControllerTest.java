package com.example.casaibm_api.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.casaibm_api.domain.Reserva;
import com.example.casaibm_api.domain.Status;
import com.example.casaibm_api.dto.ReservaDTO;
import com.example.casaibm_api.service.ReservaService;

@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReservaService ReservaService;

    @InjectMocks
    private ReservaController ReservaController;

    //2023/08/06
    private Long dataInicio = Long.parseLong("1691280000000");
    //2023/08/08
    private Long dataFim = Long.parseLong("1691452800000");

    @Test
    public void testInsertReserva() throws Exception{
        Reserva ReservaFromDTO = new Reserva(null, "joao", new Date(dataInicio), new Date(dataFim), 4, null);
        Reserva ReservaInserida = new Reserva(1, "joao", new Date(dataInicio), new Date(dataFim), 4, Status.CONFIRMADA);

        when(ReservaService.fromDTO(any(ReservaDTO.class))).thenReturn(ReservaFromDTO);
        when(ReservaService.create(any(Reserva.class))).thenReturn(ReservaInserida);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomeHospede\":\"joao\",\"dataInicio\":\"2023-08-06\",\"dataFim\":\"2023-08-08\",\"quantidadePessoas\":4}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/reservas/1"));
    }

    @Test
    public void testUpdateReserva() throws Exception{
        Reserva ReservaFromDTO = new Reserva(null, "jonas", new Date(dataInicio), new Date(dataFim), 3, Status.PENDENTE);
        Reserva ReservaAtualizada = new Reserva(1, "jonas", new Date(dataInicio), new Date(dataFim), 3, Status.PENDENTE);

        when(ReservaService.fromDTO(any(ReservaDTO.class))).thenReturn(ReservaFromDTO);
        when(ReservaService.update(any(Reserva.class))).thenReturn(ReservaAtualizada);

        mockMvc.perform(MockMvcRequestBuilders.put("/reservas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nomeHospede\":\"jonas\",\"dataInicio\":\"2023-08-06\",\"dataFim\":\"2023-08-08\",\"quantidadePessoas\":3,\"status\":\"PENDENTE\"}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/reservas/1"));
    }

    @Test
    public void testDeleteReserva() throws Exception{
        Integer idReserva = 1;
        Reserva ReservaCancelada = new Reserva(idReserva, "joao", new Date(dataInicio), new Date(dataFim), 4, Status.CANCELADA);

        when(ReservaService.delete(idReserva)).thenReturn(ReservaCancelada);

        mockMvc.perform(MockMvcRequestBuilders.delete("/reservas/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomeHospede").value("joao"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CANCELADA"));
                
    }

    @Test
    public void testFindAllReserva() throws Exception{

        Reserva reserva1 = new Reserva();
        Reserva reserva2 = new Reserva();

        when(ReservaService.findAll()).thenReturn(Arrays.asList(reserva1, reserva2));

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray()) 
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    public void testFindReservaByIdExistente() throws Exception{
        Integer idReserva = 1;
        Reserva ReservaEncontrada = new Reserva(idReserva, "joao",new Date(dataInicio), new Date(dataFim), 4, Status.CONFIRMADA);

        when(ReservaService.findById(idReserva)).thenReturn(ReservaEncontrada);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomeHospede").value("joao"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("CONFIRMADA"));
    }

}
