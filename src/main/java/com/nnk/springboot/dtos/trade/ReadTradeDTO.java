package com.nnk.springboot.dtos.trade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadTradeDTO {
    private Integer id;
    private String account;
    private String type;
    private Double buyQuantity;
}
