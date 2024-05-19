package com.nnk.springboot.services;

import com.nnk.springboot.dtos.rulename.CreateRuleNameDTO;
import com.nnk.springboot.dtos.rulename.ReadRuleNameDTO;
import com.nnk.springboot.dtos.rulename.UpdateRuleNameDTO;

import java.util.List;

public interface RuleNameService {
    List<ReadRuleNameDTO> getRuleNameDto();
    ReadRuleNameDTO getRuleNameDtoById(Integer id);
    ReadRuleNameDTO addRuleName(CreateRuleNameDTO ruleNameDTO);
    ReadRuleNameDTO updateRuleNameById(Integer id, UpdateRuleNameDTO updateRuleNameDTO);
    void deleteRuleName(Integer id);
}
