package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dtos.curvepoint.CreateCurvePointDTO;
import com.nnk.springboot.dtos.curvepoint.ReadCurvePointDTO;
import com.nnk.springboot.dtos.curvepoint.UpdateCurvePointDTO;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class CurvePointServiceImpl  implements CurvePointService {
    @Autowired
    private CurvePointRepository curvePointRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public List<ReadCurvePointDTO> getCurvePointListDto() {
        List<CurvePoint> curvePoints = curvePointRepository.findAll();

        return curvePoints.stream().map(curvePoint -> mapper.map(curvePoint, ReadCurvePointDTO.class)).toList();
    }

    @Override
    public ReadCurvePointDTO getCurvePointDtoById(Integer id) {
        CurvePoint curvePoint = this.getCurvePointById(id);

        return mapper.map(curvePoint, ReadCurvePointDTO.class);
    }

    @Override
    public ReadCurvePointDTO addCurvePoint(CreateCurvePointDTO curvePointDTO) {
        try {
            CurvePoint curvePointToSave = mapper.map(curvePointDTO, CurvePoint.class);
            CurvePoint curvePointSaved = curvePointRepository.save(curvePointToSave);

            return mapper.map(curvePointSaved, ReadCurvePointDTO.class);
        } catch(Exception e) {
            log.error("AddCurvePoint Error : {}", e.getMessage());

            return null;
        }
    }

    @Override
    public ReadCurvePointDTO updateCurvePointById(Integer id, UpdateCurvePointDTO updateCurvePointDTO) {
        if(!curvePointRepository.existsById(id))
            throw new EntityNotFoundException("CurvePoint not found with : " + id);
        CurvePoint curvePointToUpdate = mapper.map(updateCurvePointDTO, CurvePoint.class);

        try {
            CurvePoint curvePointUpdated = curvePointRepository.save(curvePointToUpdate);

            return mapper.map(curvePointUpdated, ReadCurvePointDTO.class);
        } catch(Exception e) {
            log.error("UpdateCurvePoint Error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteCurvePointById(Integer id) {
        CurvePoint curvePoint = this.getCurvePointById(id);

        try {
            curvePointRepository.delete(curvePoint);
        } catch (Exception e) {
            log.error("DeleteCurvePoint Error with: " +id);
        }
    }

    private CurvePoint getCurvePointById(Integer id) {
        return curvePointRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CurvePoint not found with : " + id));
    }
}
