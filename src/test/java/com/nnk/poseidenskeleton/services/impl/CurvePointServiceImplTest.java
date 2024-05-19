package com.nnk.poseidenskeleton.services.impl;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dtos.curvepoint.CreateCurvePointDTO;
import com.nnk.springboot.dtos.curvepoint.ReadCurvePointDTO;
import com.nnk.springboot.dtos.curvepoint.UpdateCurvePointDTO;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.impl.CurvePointServiceImpl;
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
class CurvePointServiceImplTest {
    @Mock
    CurvePointRepository curvePointRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    CurvePointServiceImpl curvePointService;

    CurvePoint curvePoint;
    ReadCurvePointDTO readCurvePointDTO;
    CreateCurvePointDTO createCurvePointDTO;
    UpdateCurvePointDTO updateCurvePointDTO;

    @BeforeEach
    void setUp() {
        curvePoint = new CurvePoint(1, 2, null, 2.0, 2.0, null);
        readCurvePointDTO = new ReadCurvePointDTO(1, 2, 2.0, 2.0);
        createCurvePointDTO = new CreateCurvePointDTO(2, 2.0, 2.0);
        updateCurvePointDTO = new UpdateCurvePointDTO(1, 2, 2.0, 2.0);
    }

    @Test
    void getCurvePointDTO_success() {
        when(curvePointRepository.findAll()).thenReturn(Collections.singletonList(curvePoint));
        when(modelMapper.map(any(CurvePoint.class), eq(ReadCurvePointDTO.class))).thenReturn(readCurvePointDTO);

        List<ReadCurvePointDTO> result = curvePointService.getCurvePointListDto();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(readCurvePointDTO, result.get(0));

        verify(curvePointRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(any(CurvePoint.class), eq(ReadCurvePointDTO.class));
    }

    @Test
    void getCurvePointDtoById_success() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));
        when(modelMapper.map(curvePoint, ReadCurvePointDTO.class)).thenReturn(readCurvePointDTO);

        ReadCurvePointDTO result = curvePointService.getCurvePointDtoById(1);

        assertNotNull(result);
        assertEquals(readCurvePointDTO, result);

        verify(curvePointRepository, times(1)).findById(1);
        verify(modelMapper, times(1)).map(any(CurvePoint.class), eq(ReadCurvePointDTO.class));
    }

    @Test
    void getCurvePointById_notFound() {
        when(curvePointRepository.findById(1)).thenThrow(new EntityNotFoundException("CurvePoint not found with ; 1"));

        assertThrows(EntityNotFoundException.class, () -> curvePointService.getCurvePointDtoById(1));

        verify(curvePointRepository, times(1)).findById(1);
        verify(modelMapper, times(0)).map(any(CurvePoint.class), eq(ReadCurvePointDTO.class));
    }

    @Test
    void addCurvePoint_validInput() {
        when(modelMapper.map(createCurvePointDTO, CurvePoint.class)).thenReturn(curvePoint);
        when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);
        when(modelMapper.map(curvePoint, ReadCurvePointDTO.class)).thenReturn(readCurvePointDTO);

        ReadCurvePointDTO result = curvePointService.addCurvePoint(createCurvePointDTO);

        assertNotNull(result);
        assertEquals(readCurvePointDTO, result);

        verify(modelMapper, times(1)).map(createCurvePointDTO, CurvePoint.class);
        verify(curvePointRepository, times(1)).save(curvePoint);
        verify(modelMapper, times(1)).map(any(CurvePoint.class), eq(ReadCurvePointDTO.class));
    }

    @Test
    void updateCurvePointById_success() {
        when(curvePointRepository.existsById(1)).thenReturn(true);
        when(modelMapper.map(updateCurvePointDTO, CurvePoint.class)).thenReturn(curvePoint);
        when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);
        when(modelMapper.map(curvePoint, ReadCurvePointDTO.class)).thenReturn(readCurvePointDTO);

        ReadCurvePointDTO result = curvePointService.updateCurvePointById(1, updateCurvePointDTO);

        assertNotNull(result);
        assertEquals(readCurvePointDTO, result);

        verify(curvePointRepository, times(1)).existsById(1);
        verify(modelMapper, times(1)).map(updateCurvePointDTO, CurvePoint.class);
        verify(curvePointRepository, times(1)).save(any(CurvePoint.class));
        verify(modelMapper, times(1)).map(any(CurvePoint.class), eq(ReadCurvePointDTO.class));
    }

    @Test
    void updateCurvePointById_notFound() {
        when(curvePointRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> curvePointService.updateCurvePointById(1, updateCurvePointDTO));

        verify(curvePointRepository, times(1)).existsById(1);
        verify(modelMapper, times(0)).map(updateCurvePointDTO, CurvePoint.class);
        verify(curvePointRepository, times(0)).save(any(CurvePoint.class));
        verify(modelMapper, times(0)).map(any(CurvePoint.class), eq(ReadCurvePointDTO.class));
    }

    @Test
    void deleteCurvePointById_success() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));
        doNothing().when(curvePointRepository).delete(curvePoint);

        assertDoesNotThrow(() -> curvePointService.deleteCurvePointById(1));

        verify(curvePointRepository, times(1)).findById(1);
        verify(curvePointRepository, times(1)).delete(curvePoint);
    }
}
