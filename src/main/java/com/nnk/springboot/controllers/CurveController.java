package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dtos.curvepoint.CreateCurvePointDTO;
import com.nnk.springboot.dtos.curvepoint.ReadCurvePointDTO;
import com.nnk.springboot.dtos.curvepoint.UpdateCurvePointDTO;
import com.nnk.springboot.services.CurvePointService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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
public class CurveController {
    @Autowired
    private CurvePointService curvePointService;

    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        List<ReadCurvePointDTO> curvePointDTOList = curvePointService.getCurvePointListDto();
        model.addAttribute("curvePoints", curvePointDTOList);

        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addBidForm(@ModelAttribute("curvePoint") ReadCurvePointDTO curvePoint) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CreateCurvePointDTO curvePoint, BindingResult result) {
        if(!result.hasErrors()) {
            curvePointService.addCurvePoint(curvePoint);

            return "redirect:/curvePoint/list";
        }
        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@ModelAttribute("id") @PathVariable("id") Integer id, Model model) {
        try {
            ReadCurvePointDTO curvePointDTO = curvePointService.getCurvePointDtoById(id);
            model.addAttribute("curvePoint", curvePointDTO);

            return "curvePoint/update";
        } catch(EntityNotFoundException e) {
            log.info(e.getMessage());
            return "redirect:/curvePoint/list";
        }
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@ModelAttribute("id") @PathVariable("id") Integer id, @Valid @ModelAttribute("curvePoint") UpdateCurvePointDTO curvePoint,
                             BindingResult result) {
        if(result.hasErrors()) {
            return "curvePoint/update";
        }

        ReadCurvePointDTO curvePointDTO = curvePointService.updateCurvePointById(id, curvePoint);

        if(curvePointDTO == null) {
            result.rejectValue("term", "error.term", "Updating Error...");
            return "curvePoint/update";
        }
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        curvePointService.deleteCurvePointById(id);

        return "redirect:/curvePoint/list";
    }
}
