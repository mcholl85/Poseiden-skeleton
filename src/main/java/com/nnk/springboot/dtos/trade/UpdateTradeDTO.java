package com.nnk.springboot.dtos.trade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTradeDTO {
    @NotNull
    private Integer id;
    @NotBlank
    private String account;
    private String type;
    private Double buyQuantity;
}
