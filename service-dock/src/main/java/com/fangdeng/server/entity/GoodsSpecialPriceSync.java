package com.fangdeng.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsSpecialPriceSync {

    private Long id;

    private String goodsNo;


    private Byte status;


    private Byte deleted;

    private BigDecimal specialPrice;


    private Date createTime;


    private Date updateTime;

    private LocalDateTime startTime;


    private LocalDateTime endTime;


}