package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dtos.trade.CreateTradeDTO;
import com.nnk.springboot.dtos.trade.ReadTradeDTO;
import com.nnk.springboot.dtos.trade.UpdateTradeDTO;
import com.nnk.springboot.services.TradeService;
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
public class TradeController {
    @Autowired
    private TradeService tradeService;
    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        List<ReadTradeDTO> tradeDTOList = tradeService.getTradeListDto();
        model.addAttribute("trades", tradeDTOList);

        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(@ModelAttribute("trade") ReadTradeDTO bid) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid @ModelAttribute("trade") CreateTradeDTO trade, BindingResult result) {
        if(!result.hasErrors()) {
            tradeService.addTrade(trade);

            return "redirect:/trade/list";
        }
        return "trade/add";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@ModelAttribute("id") @PathVariable("id") Integer id, Model model) {
        try {
            ReadTradeDTO tradeDTO = tradeService.getTradeDtoById(id);
            model.addAttribute("trade", tradeDTO);

            return "trade/update";
        } catch(EntityNotFoundException e) {
            log.info(e.getMessage());
            return "redirect:/trade/list";
        }
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@ModelAttribute("id") @PathVariable("id") Integer id, @ModelAttribute("trade") @Valid UpdateTradeDTO trade,
                             BindingResult result) {
        if(result.hasErrors())
            return "trade/update";

        ReadTradeDTO tradeDTO = tradeService.updateTradeById(id, trade);

        if(tradeDTO == null) {
            result.rejectValue("account", "error.account", "Updating Error");
            return "trade/update";
        }

        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) {
        tradeService.deleteTrade(id);

        return "redirect:/trade/list";
    }
}
