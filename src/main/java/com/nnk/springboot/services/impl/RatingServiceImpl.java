package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dtos.rating.CreateRatingDTO;
import com.nnk.springboot.dtos.rating.ReadRatingDTO;
import com.nnk.springboot.dtos.rating.UpdateRatingDTO;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public List<ReadRatingDTO> getRatingListDto() {
        List<Rating> ratings = ratingRepository.findAll();

        return ratings.stream().map(rating -> mapper.map(rating, ReadRatingDTO.class)).toList();
    }

    @Override
    public ReadRatingDTO getRatingDtoById(Integer id) {
        Rating rating = this.getRatingById(id);

        return mapper.map(rating, ReadRatingDTO.class);
    }

    @Override
    public ReadRatingDTO addRating(CreateRatingDTO ratingDTO) {
        try {
            Rating ratingToSave = mapper.map(ratingDTO, Rating.class);
            Rating ratingSaved = ratingRepository.save(ratingToSave);

            return mapper.map(ratingSaved, ReadRatingDTO.class);
        } catch (Exception e) {
            log.error("AddRating Error : {}", e.getMessage());

            return null;
        }
    }

    @Override
    public ReadRatingDTO updateRatingById(Integer id, UpdateRatingDTO updateRatingDTO) {
        if(!ratingRepository.existsById(id))
            throw new EntityNotFoundException("Rating not found with : "+ id);
        Rating ratingToUpdate = mapper.map(updateRatingDTO, Rating.class);

        try {
            Rating ratingUpdated = ratingRepository.save(ratingToUpdate);

            return mapper.map(ratingUpdated, ReadRatingDTO.class);
        } catch (Exception e) {
            log.error("UpdateRating Error : {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteRatingById(Integer id) {
        Rating rating = this.getRatingById(id);

        try {
            ratingRepository.delete(rating);
        } catch (Exception e) {
            log.error("DeleteRating Error: {}", e.getMessage());
        }
    }

    private Rating getRatingById(Integer id) {
        return ratingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Rating not found with: "+ id));
    }
}
