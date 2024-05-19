package com.nnk.springboot.dtos.trade;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTradeDTO {
    @NotBlank
    private String account;
    private String type;
    private Double buyQuantity;
}
