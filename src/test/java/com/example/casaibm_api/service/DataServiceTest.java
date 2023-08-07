package com.example.casaibm_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.casaibm_api.domain.Data;
import com.example.casaibm_api.repository.DataRepository;

@ExtendWith(SpringExtension.class)
public class DataServiceTest {
    
    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private DataService dataService;

    //2023/08/06
    private Long dataInicio = Long.parseLong("1691280000000");
    //2023/08/08
    private Long dataFim = Long.parseLong("1691452800000");

    @Test
    public void testFindAllDatas(){
        Data Data1 = new Data(1, new Date(dataInicio));
        Data Data2 = new Data(2, new Date(dataFim));

        when(dataRepository.findAll()).thenReturn(Arrays.asList(Data1, Data2));

        List<Data> Datas = dataService.findAll();

        assertNotNull(Datas);
        assertEquals(Datas.get(0).getId(), Data1.getId());
        assertEquals(Datas.get(1).getId(), Data2.getId());

        verify(dataRepository, times(1)).findAll();
    }
}
