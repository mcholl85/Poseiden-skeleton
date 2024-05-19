package com.nnk.springboot.dtos.curvepoint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadCurvePointDTO {
    private Integer id;
    private Integer curveId;
    private Double term;
    private Double value;
}
