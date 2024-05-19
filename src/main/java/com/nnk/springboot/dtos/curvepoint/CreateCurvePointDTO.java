package com.nnk.springboot.dtos.curvepoint;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCurvePointDTO {
    @NotNull
    private Integer curveId;
    private Double term;
    private Double value;
}
