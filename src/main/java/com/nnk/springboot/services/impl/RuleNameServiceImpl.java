package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dtos.rulename.CreateRuleNameDTO;
import com.nnk.springboot.dtos.rulename.ReadRuleNameDTO;
import com.nnk.springboot.dtos.rulename.UpdateRuleNameDTO;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class RuleNameServiceImpl implements RuleNameService {
    @Autowired
    private RuleNameRepository ruleNameRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public List<ReadRuleNameDTO> getRuleNameDto() {
        List<RuleName> ruleNames = ruleNameRepository.findAll();

        return ruleNames.stream().map(ruleName -> mapper.map(ruleName, ReadRuleNameDTO.class)).toList();
    }

    @Override
    public ReadRuleNameDTO getRuleNameDtoById(Integer id) {
        RuleName ruleName = this.getRuleNameById(id);

        return mapper.map(ruleName, ReadRuleNameDTO.class);
    }

    @Override
    public ReadRuleNameDTO addRuleName(CreateRuleNameDTO ruleNameDTO) {
        try {
            RuleName ruleNameToSave = mapper.map(ruleNameDTO, RuleName.class);
            RuleName ruleNameSaved = ruleNameRepository.save(ruleNameToSave);

            return mapper.map(ruleNameSaved, ReadRuleNameDTO.class);
        } catch (Exception e) {
            log.error("AddRuleName Error : {}", e.getMessage());

            return null;
        }
    }

    @Override
    public ReadRuleNameDTO updateRuleNameById(Integer id, UpdateRuleNameDTO updateRuleNameDTO) {
        if(!ruleNameRepository.existsById(id))
            throw new EntityNotFoundException("RuleName not found with : " + id);
        RuleName ruleNameToUpdate = mapper.map(updateRuleNameDTO, RuleName.class);

        try {
            RuleName ruleNameUpdated = ruleNameRepository.save(ruleNameToUpdate);

            return mapper.map(ruleNameUpdated, ReadRuleNameDTO.class);
        } catch (Exception e) {
            log.error("UpdateRuleName Error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteRuleName(Integer id) {
        RuleName ruleName = this.getRuleNameById(id);

        try {
            ruleNameRepository.delete(ruleName);
        } catch (Exception e) {
            log.error("DeleteRuleName Error: {}", e.getMessage());
        }
    }

    private RuleName getRuleNameById(Integer id) {
        return ruleNameRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("RuleName not found with :" + id));
    }
}
