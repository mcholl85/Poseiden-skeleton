package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dtos.bidlist.CreateBidListDTO;
import com.nnk.springboot.dtos.bidlist.ReadBidListDTO;
import com.nnk.springboot.dtos.bidlist.UpdateBidListDTO;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class BidListServiceImpl implements BidListService {
    @Autowired
    private BidListRepository bidListRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public List<ReadBidListDTO> getBidListDto() {
        List<BidList> bidLists = bidListRepository.findAll();

        return bidLists.stream().map(bidList -> mapper.map(bidList, ReadBidListDTO.class)).toList();
    }

    @Override
    public ReadBidListDTO getBidListDtoById(Integer id) {
        BidList bidList = this.getBidListById(id);

        return mapper.map(bidList, ReadBidListDTO.class);
    }

    @Override
    public ReadBidListDTO addBidList(CreateBidListDTO bidListDTO) {
        try {
            BidList bidListToSave = mapper.map(bidListDTO, BidList.class);
            BidList bidListSaved = bidListRepository.save(bidListToSave);

            return mapper.map(bidListSaved, ReadBidListDTO.class);
        }
        catch (Exception e) {
            log.error("AddBidList Error : {}", e.getMessage());

            return null;
        }
    }
    @Override
    public ReadBidListDTO updateBidListById(Integer id, UpdateBidListDTO updateBidListDTO) {
        if(!bidListRepository.existsById(id))
            throw new EntityNotFoundException("BidList not found with : " + id);
        BidList bidListToUpdate = mapper.map(updateBidListDTO, BidList.class);

        try {
            BidList bidListUpdated = bidListRepository.save(bidListToUpdate);

            return mapper.map(bidListUpdated, ReadBidListDTO.class);
        } catch (Exception e) {
            log.error("UpdateBidList Error: {}", e.getMessage());
            return null;
        }
    }
    @Override
    public void deleteBidList(Integer id) {
        BidList bidList = this.getBidListById(id);

        try {
            bidListRepository.delete(bidList);
        } catch (Exception e) {
            log.error("DeleteBidList Error: {}", e.getMessage());
        }
    }

    private BidList getBidListById(Integer id) {
        return bidListRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("BidList not found with : " + id));
    }
}
