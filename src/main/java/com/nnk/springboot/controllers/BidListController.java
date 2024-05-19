package com.nnk.springboot.controllers;

import com.nnk.springboot.dtos.bidlist.CreateBidListDTO;
import com.nnk.springboot.dtos.bidlist.ReadBidListDTO;
import com.nnk.springboot.dtos.bidlist.UpdateBidListDTO;
import com.nnk.springboot.services.BidListService;
import io.micrometer.common.util.StringUtils;
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
public class BidListController {
    @Autowired
    private BidListService bidListService;

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        List<ReadBidListDTO> readBidListDTO = bidListService.getBidListDto();
        model.addAttribute("bidLists", readBidListDTO);

        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(@ModelAttribute("bidList") CreateBidListDTO bidList) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid @ModelAttribute("bidList") CreateBidListDTO bid, BindingResult result) {
        if(!result.hasErrors()) {
            bidListService.addBidList(bid);

            return "redirect:/bidList/list";
        }
        return "bidList/add";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@ModelAttribute("id") @PathVariable("id") Integer id, Model model) {
        try {
            ReadBidListDTO bidListDTO = bidListService.getBidListDtoById(id);
            model.addAttribute("bidList", bidListDTO);

            return "bidList/update";
        } catch (EntityNotFoundException e) {
            log.info(e.getMessage());
            return "redirect:/bidList/list";
        }
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@ModelAttribute("id") @PathVariable("id") Integer id, @ModelAttribute("bidList") @Valid UpdateBidListDTO bidList,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/update";
        }

        ReadBidListDTO bidListDTO = bidListService.updateBidListById(id, bidList);

        if (bidListDTO == null) {
            result.rejectValue("account", "error.account", "Updating Error...");
            return "bidList/update";
        }

        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        bidListService.deleteBidList(id);

        return "redirect:/bidList/list";
    }
}
