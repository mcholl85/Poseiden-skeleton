package com.nnk.springboot.services;

import com.nnk.springboot.dtos.rating.CreateRatingDTO;
import com.nnk.springboot.dtos.rating.ReadRatingDTO;
import com.nnk.springboot.dtos.rating.UpdateRatingDTO;

import java.util.List;

public interface RatingService {
    List<ReadRatingDTO> getRatingListDto();
    ReadRatingDTO getRatingDtoById(Integer id);
    ReadRatingDTO addRating(CreateRatingDTO ratingDTO);
    ReadRatingDTO updateRatingById(Integer id, UpdateRatingDTO updateRatingDTO);
    void deleteRatingById(Integer id);
}
