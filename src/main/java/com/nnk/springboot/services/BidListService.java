package com.nnk.springboot.services;

import com.nnk.springboot.dtos.bidlist.CreateBidListDTO;
import com.nnk.springboot.dtos.bidlist.ReadBidListDTO;
import com.nnk.springboot.dtos.bidlist.UpdateBidListDTO;

import java.util.List;

public interface BidListService {
    List<ReadBidListDTO> getBidListDto();
    ReadBidListDTO getBidListDtoById(Integer id);
    ReadBidListDTO addBidList(CreateBidListDTO bidListDTO);
    ReadBidListDTO updateBidListById(Integer id, UpdateBidListDTO updateBidListDTO);
    void deleteBidList(Integer id);
}
