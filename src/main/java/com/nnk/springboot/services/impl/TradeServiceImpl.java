package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dtos.trade.CreateTradeDTO;
import com.nnk.springboot.dtos.trade.ReadTradeDTO;
import com.nnk.springboot.dtos.trade.UpdateTradeDTO;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class TradeServiceImpl implements TradeService {
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public List<ReadTradeDTO> getTradeListDto() {
        List<Trade> trades = tradeRepository.findAll();

        return trades.stream().map(trade -> mapper.map(trade, ReadTradeDTO.class)).toList();
    }

    @Override
    public ReadTradeDTO getTradeDtoById(Integer id) {
        Trade trade = this.getTradeById(id);

        return mapper.map(trade, ReadTradeDTO.class);
    }

    @Override
    public ReadTradeDTO addTrade(CreateTradeDTO tradeDTO) {
        try {
            Trade tradeToSave = mapper.map(tradeDTO, Trade.class);
            Trade tradeSaved = tradeRepository.save(tradeToSave);

            return mapper.map(tradeSaved, ReadTradeDTO.class);
        } catch (Exception e) {
            log.error("AddTrade Error : {}", e.getMessage());
            return null;
        }
    }

    @Override
    public ReadTradeDTO updateTradeById(Integer id, UpdateTradeDTO updateTradeDTO) {
        if(!tradeRepository.existsById(id))
            throw new EntityNotFoundException("Trade not found with : " + id);
        Trade tradeToUpdate = mapper.map(updateTradeDTO, Trade.class);

        try {
            Trade tradeUpdated = tradeRepository.save(tradeToUpdate);

            return mapper.map(tradeUpdated, ReadTradeDTO.class);
        } catch (Exception e) {
            log.error("UpdateTrade Error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteTrade(Integer id) {
        Trade trade = this.getTradeById(id);

        try {
            tradeRepository.delete(trade);
        } catch (Exception e) {
            log.error("DeleteTrade Error : {}", e.getMessage());
        }
    }

    private Trade getTradeById(Integer id) {
        return tradeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Trade not found with : " + id));
    }
}
