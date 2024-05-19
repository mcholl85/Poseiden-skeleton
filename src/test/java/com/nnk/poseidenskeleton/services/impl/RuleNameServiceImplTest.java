package com.nnk.poseidenskeleton.services.impl;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dtos.rulename.CreateRuleNameDTO;
import com.nnk.springboot.dtos.rulename.ReadRuleNameDTO;
import com.nnk.springboot.dtos.rulename.UpdateRuleNameDTO;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.impl.RuleNameServiceImpl;
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
class RuleNameServiceImplTest {
    @Mock
    RuleNameRepository ruleNameRepository;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    RuleNameServiceImpl ruleNameService;

    RuleName ruleName;
    ReadRuleNameDTO readRuleNameDTO;
    CreateRuleNameDTO createRuleNameDTO;
    UpdateRuleNameDTO updateRuleNameDTO;

    @BeforeEach
    void setUp() {
        ruleName = new RuleName(1, "nameTest", "descriptionTest", null, null, null, null);
        readRuleNameDTO = new ReadRuleNameDTO(1, "nameTest", "descriptionTest", null, null, null, null);
        createRuleNameDTO = new CreateRuleNameDTO("nameTest", "descriptionTest", null, null, null, null);
        updateRuleNameDTO = new UpdateRuleNameDTO(1, "nameTest", "descriptionTest", null, null, null, null);
    }

    @Test
    void getRuleNameDto_success() {
        when(ruleNameRepository.findAll()).thenReturn(Collections.singletonList(ruleName));
        when(modelMapper.map(any(RuleName.class), eq(ReadRuleNameDTO.class))).thenReturn(readRuleNameDTO);

        List<ReadRuleNameDTO> result = ruleNameService.getRuleNameDto();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(readRuleNameDTO, result.get(0));

        verify(ruleNameRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(any(RuleName.class), eq(ReadRuleNameDTO.class));
    }

    @Test
    void getRuleNameDtoById_success() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));
        when(modelMapper.map(ruleName, ReadRuleNameDTO.class)).thenReturn(readRuleNameDTO);

        ReadRuleNameDTO result = ruleNameService.getRuleNameDtoById(1);

        assertNotNull(result);
        assertEquals(readRuleNameDTO, result);

        verify(ruleNameRepository, times(1)).findById(1);
        verify(modelMapper, times(1)).map(any(RuleName.class), eq(ReadRuleNameDTO.class));
    }

    @Test
    void getRuleNameDtoById_notFound() {
        when(ruleNameRepository.findById(1)).thenThrow(new EntityNotFoundException("BidList not found with : 1"));

        assertThrows(EntityNotFoundException.class, () -> ruleNameService.getRuleNameDtoById(1));

        verify(ruleNameRepository, times(1)).findById(1);
        verify(modelMapper, times(0)).map(any(RuleName.class), eq(ReadRuleNameDTO.class));
    }

    @Test
    void addRuleName_validInput() {
        when(modelMapper.map(createRuleNameDTO, RuleName.class)).thenReturn(ruleName);
        when(ruleNameRepository.save(ruleName)).thenReturn(ruleName);
        when(modelMapper.map(ruleName, ReadRuleNameDTO.class)).thenReturn(readRuleNameDTO);

        ReadRuleNameDTO result = ruleNameService.addRuleName(createRuleNameDTO);

        assertNotNull(result);
        assertEquals(readRuleNameDTO, result);

        verify(modelMapper, times(1)).map(createRuleNameDTO, RuleName.class);
        verify(ruleNameRepository, times(1)).save(ruleName);
        verify(modelMapper, times(1)).map(any(RuleName.class), eq(ReadRuleNameDTO.class));
    }

    @Test
    void updateRuleNameById_success() {
        when(ruleNameRepository.existsById(1)).thenReturn(true);
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);
        when(modelMapper.map(updateRuleNameDTO, RuleName.class)).thenReturn(ruleName);
        when(modelMapper.map(ruleName, ReadRuleNameDTO.class)).thenReturn(readRuleNameDTO);

        ReadRuleNameDTO result = ruleNameService.updateRuleNameById(1, updateRuleNameDTO);

        assertNotNull(result);
        assertEquals(readRuleNameDTO, result);

        verify(ruleNameRepository, times(1)).existsById(1);
        verify(modelMapper, times(1)).map(updateRuleNameDTO, RuleName.class);
        verify(ruleNameRepository, times(1)).save(any(RuleName.class));
        verify(modelMapper, times(1)).map(any(RuleName.class), eq(ReadRuleNameDTO.class));
    }

    @Test
    void updateRuleNameById_notFound() {
        when(ruleNameRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> ruleNameService.updateRuleNameById(1, updateRuleNameDTO));

        verify(ruleNameRepository, times(1)).existsById(1);
        verify(modelMapper, times(0)).map(updateRuleNameDTO, RuleName.class);
        verify(ruleNameRepository, times(0)).save(any(RuleName.class));
        verify(modelMapper, times(0)).map(any(RuleName.class), eq(ReadRuleNameDTO.class));
    }

    @Test
    void deleteRuleName_success() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));
        doNothing().when(ruleNameRepository).delete(ruleName);

        assertDoesNotThrow(() -> ruleNameService.deleteRuleName(1));

        verify(ruleNameRepository, times(1)).findById(1);
        verify(ruleNameRepository, times(1)).delete(ruleName);
    }
}
