package com.nnk.poseidenskeleton.services.impl;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dtos.rating.CreateRatingDTO;
import com.nnk.springboot.dtos.rating.ReadRatingDTO;
import com.nnk.springboot.dtos.rating.UpdateRatingDTO;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.impl.RatingServiceImpl;
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
class RatingServiceImplTest {
    @Mock
    RatingRepository ratingRepository;
    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    RatingServiceImpl ratingService;

    Rating rating;
    ReadRatingDTO readRatingDTO;
    CreateRatingDTO createRatingDTO;
    UpdateRatingDTO updateRatingDTO;

    @BeforeEach
    void setUp() {
        rating = new Rating(1, "2", "2", "2", 3);
        readRatingDTO = new ReadRatingDTO(1, "2", "2", "2", 3);
        createRatingDTO = new CreateRatingDTO( "2", "2", "2", 3);
        updateRatingDTO = new UpdateRatingDTO(1, "2", "2", "2", 3);
    }

    @Test
    void getRatingDTO_success() {
        when(ratingRepository.findAll()).thenReturn(Collections.singletonList(rating));
        when(modelMapper.map(any(Rating.class), eq(ReadRatingDTO.class))).thenReturn(readRatingDTO);

        List<ReadRatingDTO> result = ratingService.getRatingListDto();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(readRatingDTO, result.get(0));

        verify(ratingRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(any(Rating.class), eq(ReadRatingDTO.class));
    }

    @Test
    void getRatingDtoById_success() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));
        when(modelMapper.map(rating, ReadRatingDTO.class)).thenReturn(readRatingDTO);

        ReadRatingDTO result = ratingService.getRatingDtoById(1);

        assertNotNull(result);
        assertEquals(readRatingDTO, result);

        verify(ratingRepository, times(1)).findById(1);
        verify(modelMapper, times(1)).map(any(Rating.class), eq(ReadRatingDTO.class));
    }

    @Test
    void getRatingById_notFound() {
        when(ratingRepository.findById(1)).thenThrow(new EntityNotFoundException("Rating not found with ; 1"));

        assertThrows(EntityNotFoundException.class, () -> ratingService.getRatingDtoById(1));

        verify(ratingRepository, times(1)).findById(1);
        verify(modelMapper, times(0)).map(any(Rating.class), eq(ReadRatingDTO.class));
    }

    @Test
    void addRating_validInput() {
        when(modelMapper.map(createRatingDTO, Rating.class)).thenReturn(rating);
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(modelMapper.map(rating, ReadRatingDTO.class)).thenReturn(readRatingDTO);

        ReadRatingDTO result = ratingService.addRating(createRatingDTO);

        assertNotNull(result);
        assertEquals(readRatingDTO, result);

        verify(modelMapper, times(1)).map(createRatingDTO, Rating.class);
        verify(ratingRepository, times(1)).save(rating);
        verify(modelMapper, times(1)).map(any(Rating.class), eq(ReadRatingDTO.class));
    }

    @Test
    void updateRatingById_success() {
        when(ratingRepository.existsById(1)).thenReturn(true);
        when(modelMapper.map(updateRatingDTO, Rating.class)).thenReturn(rating);
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(modelMapper.map(rating, ReadRatingDTO.class)).thenReturn(readRatingDTO);

        ReadRatingDTO result = ratingService.updateRatingById(1, updateRatingDTO);

        assertNotNull(result);
        assertEquals(readRatingDTO, result);

        verify(ratingRepository, times(1)).existsById(1);
        verify(modelMapper, times(1)).map(updateRatingDTO, Rating.class);
        verify(ratingRepository, times(1)).save(any(Rating.class));
        verify(modelMapper, times(1)).map(any(Rating.class), eq(ReadRatingDTO.class));
    }

    @Test
    void updateRatingById_notFound() {
        when(ratingRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> ratingService.updateRatingById(1, updateRatingDTO));

        verify(ratingRepository, times(1)).existsById(1);
        verify(modelMapper, times(0)).map(updateRatingDTO, Rating.class);
        verify(ratingRepository, times(0)).save(any(Rating.class));
        verify(modelMapper, times(0)).map(any(Rating.class), eq(ReadRatingDTO.class));
    }

    @Test
    void deleteRatingById_success() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));
        doNothing().when(ratingRepository).delete(rating);

        assertDoesNotThrow(() -> ratingService.deleteRatingById(1));

        verify(ratingRepository, times(1)).findById(1);
        verify(ratingRepository, times(1)).delete(rating);
    }
}
