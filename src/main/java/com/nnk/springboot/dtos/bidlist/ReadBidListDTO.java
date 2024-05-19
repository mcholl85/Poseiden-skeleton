package com.nnk.springboot.dtos.bidlist;

import lombok.Data;

@Data
public class BidListDto {
    private Integer bidListId;
    private String account;
    private String type;
    private Double bidQuantity;
}
