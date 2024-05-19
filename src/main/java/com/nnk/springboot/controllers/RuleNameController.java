package com.nnk.springboot.controllers;

import com.nnk.springboot.dtos.rulename.CreateRuleNameDTO;
import com.nnk.springboot.dtos.rulename.ReadRuleNameDTO;
import com.nnk.springboot.dtos.rulename.UpdateRuleNameDTO;
import com.nnk.springboot.services.RuleNameService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
public class RuleNameController {
    @Autowired
    private RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        List<ReadRuleNameDTO> ruleNameDTOList = ruleNameService.getRuleNameDto();
        model.addAttribute("ruleNames", ruleNameDTOList);

        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(@ModelAttribute("ruleName") CreateRuleNameDTO ruleName) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid @ModelAttribute("ruleName") CreateRuleNameDTO ruleName, BindingResult result) {
        if(!result.hasErrors()) {
            ruleNameService.addRuleName(ruleName);

            return "redirect:/ruleName/list";
        }
        return "ruleName/add";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@ModelAttribute("id") @PathVariable("id") Integer id, Model model) {
        try {
            ReadRuleNameDTO ruleNameDTO = ruleNameService.getRuleNameDtoById(id);
            model.addAttribute("ruleName", ruleNameDTO);

            return "ruleName/update";
        } catch(EntityNotFoundException e) {
            log.info(e.getMessage());
            return "redirect:/ruleName/list";
        }
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@ModelAttribute("id") @PathVariable("id") Integer id, @ModelAttribute("ruleName") @Valid UpdateRuleNameDTO ruleName,
                             BindingResult result) {
        if(result.hasErrors())
            return "ruleName/update";

        ReadRuleNameDTO ruleNameDTO = ruleNameService.updateRuleNameById(id, ruleName);

        if(ruleNameDTO == null) {
            result.rejectValue("name", "error.name", "Updating Error...");
            return "ruleName/update";
        }

        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        ruleNameService.deleteRuleName(id);

        return "redirect:/ruleName/list";
    }
}
