package com.nnk.springboot.services;

import com.nnk.springboot.dtos.curvepoint.CreateCurvePointDTO;
import com.nnk.springboot.dtos.curvepoint.ReadCurvePointDTO;
import com.nnk.springboot.dtos.curvepoint.UpdateCurvePointDTO;

import java.util.List;

public interface CurvePointService {
    List<ReadCurvePointDTO> getCurvePointListDto();
    ReadCurvePointDTO getCurvePointDtoById(Integer id);
    ReadCurvePointDTO addCurvePoint(CreateCurvePointDTO curvePointDTO);
    ReadCurvePointDTO updateCurvePointById(Integer id, UpdateCurvePointDTO updateCurvePointDTO);
    void deleteCurvePointById(Integer id);
}
