package com.nnk.springboot.dtos.bidlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadBidListDTO {
    private Integer id;
    private String account;
    private String type;
    private Double bidQuantity;
}
