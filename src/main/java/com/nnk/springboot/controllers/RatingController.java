package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dtos.rating.CreateRatingDTO;
import com.nnk.springboot.dtos.rating.ReadRatingDTO;
import com.nnk.springboot.dtos.rating.UpdateRatingDTO;
import com.nnk.springboot.services.RatingService;
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
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        List<ReadRatingDTO> ratingDTOList = ratingService.getRatingListDto();
        model.addAttribute("ratings", ratingDTOList);

        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(@ModelAttribute("rating") CreateRatingDTO rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid @ModelAttribute("rating") CreateRatingDTO rating, BindingResult result) {
        if(!result.hasErrors()) {
            ratingService.addRating(rating);

            return "redirect:/rating/list";
        }
        return "rating/add";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            ReadRatingDTO ratingDTO = ratingService.getRatingDtoById(id);
            model.addAttribute("rating", ratingDTO);

            return "rating/update";
        } catch(EntityNotFoundException e) {
            log.info(e.getMessage());
            return "redirect:/rating/list";
        }
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id,@ModelAttribute("rating") @Valid UpdateRatingDTO rating,
                             BindingResult result) {
        if(result.hasErrors()) {
            return "rating/update";
        }

        ReadRatingDTO ratingDTO = ratingService.updateRatingById(id, rating);

        if(ratingDTO == null) {
            result.rejectValue("moodysRating", "error.moodysRating", "Updating Error...");
            return "rating/update";
        }

        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.deleteRatingById(id);

        return "redirect:/rating/list";
    }
}
