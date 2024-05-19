package com.nnk.springboot.dtos.rating;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRatingDTO {
    @Pattern(regexp = "\\d")
    private String moodysRating;
    @Pattern(regexp = "\\d")
    private String sandpRating;
    @Pattern(regexp = "\\d")
    private String fitchRating;
    private Integer orderNumber;
}
