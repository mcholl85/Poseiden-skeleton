package com.nnk.poseidenskeleton.services.impl;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dtos.bidlist.CreateBidListDTO;
import com.nnk.springboot.dtos.bidlist.ReadBidListDTO;
import com.nnk.springboot.dtos.bidlist.UpdateBidListDTO;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.impl.BidListServiceImpl;
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

@ExtendWith(MockitoExtension.class)
class BidListServicesImplTest {
    @Mock
    BidListRepository bidListRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    BidListServiceImpl bidListService;

    BidList bidList;
    ReadBidListDTO readBidListDTO;
    CreateBidListDTO createBidListDTO;
    UpdateBidListDTO updateBidListDTO;

    @BeforeEach
    void setUp() {
        bidList = new BidList(1, "AccountTest", "TypeTest", 100.0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        readBidListDTO = new ReadBidListDTO(1, "AccountTest", "TypeTest", 100.0);
        createBidListDTO = new CreateBidListDTO("AccountTest", "TypeTest", 100.0);
        updateBidListDTO = new UpdateBidListDTO(1, "AccountTest", "TypeTest", 120.0);
    }

    @Test
    void getBidListDto_success() {
        when(bidListRepository.findAll()).thenReturn(Collections.singletonList(bidList));
        when(modelMapper.map(any(BidList.class), eq(ReadBidListDTO.class))).thenReturn(readBidListDTO);

        List<ReadBidListDTO> result = bidListService.getBidListDto();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(readBidListDTO, result.get(0));

        verify(bidListRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(any(BidList.class), eq(ReadBidListDTO.class));
    }

    @Test
    void getBidListDtoById_success() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList));
        when(modelMapper.map(bidList, ReadBidListDTO.class)).thenReturn(readBidListDTO);

        ReadBidListDTO result = bidListService.getBidListDtoById(1);

        assertNotNull(result);
        assertEquals(readBidListDTO, result);

        verify(bidListRepository, times(1)).findById(1);
        verify(modelMapper, times(1)).map(any(BidList.class), eq(ReadBidListDTO.class));
    }

    @Test
    void getBidListDtoById_notFound() {
        when(bidListRepository.findById(1)).thenThrow(new EntityNotFoundException("BidList not found with : 1"));

        assertThrows(EntityNotFoundException.class, () -> bidListService.getBidListDtoById(1));

        verify(bidListRepository, times(1)).findById(1);
        verify(modelMapper, times(0)).map(any(BidList.class), eq(ReadBidListDTO.class));
    }

    @Test
    void addBidList_validInput() {
        when(modelMapper.map(createBidListDTO, BidList.class)).thenReturn(bidList);
        when(bidListRepository.save(bidList)).thenReturn(bidList);
        when(modelMapper.map(bidList, ReadBidListDTO.class)).thenReturn(readBidListDTO);

        ReadBidListDTO result = bidListService.addBidList(createBidListDTO);

        assertNotNull(result);
        assertEquals(readBidListDTO, result);

        verify(modelMapper, times(1)).map(createBidListDTO, BidList.class);
        verify(bidListRepository, times(1)).save(bidList);
        verify(modelMapper, times(1)).map(any(BidList.class), eq(ReadBidListDTO.class));
    }

    @Test
    void updateBidListById_success() {
        when(bidListRepository.existsById(1)).thenReturn(true);
        when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);
        when(modelMapper.map(updateBidListDTO, BidList.class)).thenReturn(bidList);
        when(modelMapper.map(bidList, ReadBidListDTO.class)).thenReturn(readBidListDTO);

        ReadBidListDTO result = bidListService.updateBidListById(1, updateBidListDTO);

        assertNotNull(result);
        assertEquals(readBidListDTO, result);

        verify(bidListRepository, times(1)).existsById(1);
        verify(modelMapper, times(1)).map(updateBidListDTO, BidList.class);
        verify(bidListRepository, times(1)).save(any(BidList.class));
        verify(modelMapper, times(1)).map(any(BidList.class), eq(ReadBidListDTO.class));
    }

    @Test
    void updateBidListById_notFound() {
        when(bidListRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> bidListService.updateBidListById(1, updateBidListDTO));

        verify(bidListRepository, times(1)).existsById(1);
        verify(modelMapper, times(0)).map(updateBidListDTO, BidList.class);
        verify(bidListRepository, times(0)).save(any(BidList.class));
        verify(modelMapper, times(0)).map(any(BidList.class), eq(ReadBidListDTO.class));
    }

    @Test
    void deleteBidList_success() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList));
        doNothing().when(bidListRepository).delete(bidList);

        assertDoesNotThrow(() -> bidListService.deleteBidList(1));

        verify(bidListRepository, times(1)).findById(1);
        verify(bidListRepository, times(1)).delete(bidList);
    }
}
