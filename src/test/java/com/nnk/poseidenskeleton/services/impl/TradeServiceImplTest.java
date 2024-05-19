package com.nnk.poseidenskeleton.services.impl;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dtos.trade.CreateTradeDTO;
import com.nnk.springboot.dtos.trade.ReadTradeDTO;
import com.nnk.springboot.dtos.trade.UpdateTradeDTO;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.impl.TradeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TradeServiceImplTest {
    @Mock
    TradeRepository tradeRepository;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    TradeServiceImpl tradeService;

    Trade trade;
    ReadTradeDTO readTradeDTO;
    CreateTradeDTO createTradeDTO;
    UpdateTradeDTO updateTradeDTO;

    @BeforeEach
    void setUp() {
        trade = new Trade(1, "AccoutTest", "TypeTest", 2.0, null, null,null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        readTradeDTO = new ReadTradeDTO(1, "AccoutTest", "TypeTest", 2.0);
        createTradeDTO = new CreateTradeDTO("AccoutTest", "TypeTest", 2.0);
        updateTradeDTO = new UpdateTradeDTO(1, "AccoutTest", "TypeTest", 2.0);
    }
    @Test
    void getTradeDto_success() {
        when(tradeRepository.findAll()).thenReturn(Collections.singletonList(trade));
        when(modelMapper.map(any(Trade.class), eq(ReadTradeDTO.class))).thenReturn(readTradeDTO);

        List<ReadTradeDTO> result = tradeService.getTradeListDto();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(readTradeDTO, result.get(0));

        verify(tradeRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(any(Trade.class), eq(ReadTradeDTO.class));
    }

    @Test
    void getTradeDtoById_success() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));
        when(modelMapper.map(trade, ReadTradeDTO.class)).thenReturn(readTradeDTO);

        ReadTradeDTO result = tradeService.getTradeDtoById(1);

        assertNotNull(result);
        assertEquals(readTradeDTO, result);

        verify(tradeRepository, times(1)).findById(1);
        verify(modelMapper, times(1)).map(any(Trade.class), eq(ReadTradeDTO.class));
    }

    @Test
    void getTradeDtoById_notFound() {
        when(tradeRepository.findById(1)).thenThrow(new EntityNotFoundException("Trade not found with : 1"));

        assertThrows(EntityNotFoundException.class, () -> tradeService.getTradeDtoById(1));

        verify(tradeRepository, times(1)).findById(1);
        verify(modelMapper, times(0)).map(any(Trade.class), eq(ReadTradeDTO.class));
    }

    @Test
    void addTrade_validInput() {
        when(modelMapper.map(createTradeDTO, Trade.class)).thenReturn(trade);
        when(tradeRepository.save(trade)).thenReturn(trade);
        when(modelMapper.map(trade, ReadTradeDTO.class)).thenReturn(readTradeDTO);

        ReadTradeDTO result = tradeService.addTrade(createTradeDTO);

        assertNotNull(result);
        assertEquals(readTradeDTO, result);

        verify(modelMapper, times(1)).map(createTradeDTO, Trade.class);
        verify(tradeRepository, times(1)).save(trade);
        verify(modelMapper, times(1)).map(any(Trade.class), eq(ReadTradeDTO.class));
    }

    @Test
    void updateTradeById_success() {
        when(tradeRepository.existsById(1)).thenReturn(true);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
        when(modelMapper.map(updateTradeDTO, Trade.class)).thenReturn(trade);
        when(modelMapper.map(trade, ReadTradeDTO.class)).thenReturn(readTradeDTO);

        ReadTradeDTO result = tradeService.updateTradeById(1, updateTradeDTO);

        assertNotNull(result);
        assertEquals(readTradeDTO, result);

        verify(tradeRepository, times(1)).existsById(1);
        verify(modelMapper, times(1)).map(updateTradeDTO, Trade.class);
        verify(tradeRepository, times(1)).save(any(Trade.class));
        verify(modelMapper, times(1)).map(any(Trade.class), eq(ReadTradeDTO.class));
    }

    @Test
    void updateTradeById_notFound() {
        when(tradeRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> tradeService.updateTradeById(1, updateTradeDTO));

        verify(tradeRepository, times(1)).existsById(1);
        verify(modelMapper, times(0)).map(updateTradeDTO, Trade.class);
        verify(tradeRepository, times(0)).save(any(Trade.class));
        verify(modelMapper, times(0)).map(any(Trade.class), eq(ReadTradeDTO.class));
    }

    @Test
    void deleteTrade_success() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));
        doNothing().when(tradeRepository).delete(trade);

        assertDoesNotThrow(() -> tradeService.deleteTrade(1));

        verify(tradeRepository, times(1)).findById(1);
        verify(tradeRepository, times(1)).delete(trade);
    }
    
}
