package com.example.casaibm_api.service;

import com.example.casaibm_api.domain.Reserva;
import com.example.casaibm_api.domain.Status;
import com.example.casaibm_api.repository.ReservaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    private DataService dataService = mock(DataService.class);

    @InjectMocks
    private ReservaService reservaService;

    //2023/08/06
    private Long dataInicio = Long.parseLong("1691280000000");
    //2023/08/08
    private Long dataFim = Long.parseLong("1691452800000");

    @Test
    public void testInsertReserva(){
        Reserva Reserva = new Reserva(1, "joao", new Date(dataInicio), new Date(dataFim), 4, Status.CONFIRMADA);

        when(reservaRepository.save(any(Reserva.class))).thenReturn(Reserva);
        doNothing().when(dataService).saveDates(anyLong(), anyLong());

        Reserva ReservaInserido = reservaService.create(Reserva);

        assertNotNull(ReservaInserido);
        assertEquals(Reserva.getNomeHospede(), ReservaInserido.getNomeHospede());
        assertEquals(Reserva.getDataInicio(), ReservaInserido.getDataInicio());
        assertEquals(Reserva.getDataInicio(), ReservaInserido.getDataInicio());
        assertNull(ReservaInserido.getId());

        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    public void testFindAllReserva(){
        Reserva Reserva1 = new Reserva(1, "joao", new Date(dataInicio), new Date(dataFim), 4, Status.CONFIRMADA);
        Reserva Reserva2 = new Reserva(2, "jonas", new Date(dataInicio), new Date(dataFim), 3, Status.CONFIRMADA);

        when(reservaRepository.findAll()).thenReturn(Arrays.asList(Reserva1, Reserva2));

        List<Reserva> reservas = reservaService.findAll();

        assertNotNull(reservas);
        assertEquals(reservas.get(0).getNomeHospede(), Reserva1.getNomeHospede());
        assertEquals(reservas.get(1).getNomeHospede(), Reserva2.getNomeHospede());

        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    public void testFindByIdReserva(){
        Reserva Reserva1 = new Reserva(1, "joao", new Date(dataInicio), new Date(dataFim), 4, Status.CONFIRMADA);

        when(reservaRepository.findById(1)).thenReturn(Optional.of(Reserva1));

        Reserva reserva = reservaService.findById(1);

        assertNotNull(reserva);
        assertEquals(reserva.getNomeHospede(), Reserva1.getNomeHospede());
        assertEquals(reserva.getId(), Reserva1.getId());

        verify(reservaRepository, times(1)).findById(1);
    }

    @Test
    public void deleteReserva(){
        Reserva Reserva = new Reserva(1, "joao", new Date(dataInicio), new Date(dataFim), 4, Status.CONFIRMADA);
        Reserva ReservaCancelada = new Reserva(1, "joao", new Date(dataInicio), new Date(dataFim), 4, Status.CANCELADA);

        when(reservaRepository.findById(1)).thenReturn(Optional.of(Reserva));
        doNothing().when(dataService).deleteDates(anyLong(), anyLong());
        when(reservaRepository.save(any(Reserva.class))).thenReturn(ReservaCancelada);

        Reserva reserva = reservaService.delete(1);

        assertNotNull(reserva);
        assertEquals(reserva.getStatus(), ReservaCancelada.getStatus());
        assertEquals(reserva.getId(), ReservaCancelada.getId());

        verify(reservaRepository, times(1)).findById(1);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    public void updateReserva(){
        Reserva ReservaExistente = new Reserva(1, "joao", new Date(dataInicio), new Date(dataFim), 4, Status.CONFIRMADA);
        Reserva ReservaAtualizada = new Reserva(1, "joao", new Date(dataInicio), new Date(dataFim), 4, Status.PENDENTE);

        when(reservaRepository.findById(1)).thenReturn(Optional.of(ReservaExistente));
        doNothing().when(dataService).updateDates(anyLong(), anyLong(), anyLong(), anyLong());
        when(reservaRepository.save(any(Reserva.class))).thenReturn(ReservaAtualizada);

        Reserva reserva = reservaService.update(ReservaAtualizada);

        assertNotNull(reserva);
        assertEquals(reserva.getStatus(), ReservaAtualizada.getStatus());
        assertEquals(reserva.getId(), ReservaAtualizada.getId());

        verify(reservaRepository, times(1)).findById(1);
        verify(reservaRepository, times(1)).save(any(Reserva.class));
    }


}