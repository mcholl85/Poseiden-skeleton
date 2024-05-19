package com.nnk.springboot.services;

import com.nnk.springboot.dtos.trade.CreateTradeDTO;
import com.nnk.springboot.dtos.trade.ReadTradeDTO;
import com.nnk.springboot.dtos.trade.UpdateTradeDTO;

import java.util.List;

public interface TradeService {
    List<ReadTradeDTO> getTradeListDto();
    ReadTradeDTO getTradeDtoById(Integer id);
    ReadTradeDTO addTrade(CreateTradeDTO tradeDTO);
    ReadTradeDTO updateTradeById(Integer id, UpdateTradeDTO updateTradeDTO);
    void deleteTrade(Integer id);
}
