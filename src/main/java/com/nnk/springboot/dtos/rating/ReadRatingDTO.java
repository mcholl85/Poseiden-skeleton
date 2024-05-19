package com.nnk.springboot.dtos.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadRatingDTO {
    private Integer id;
    private String moodysRating;
    private String sandpRating;
    private String fitchRating;
    private Integer orderNumber;
}
