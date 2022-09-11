package com.sbc.wanmi.erp.bean.vo;

import lombok.Data;

import java.util.List;

@Data
public class MetaSockVO {

	private int total;

    private List<NewGoodsInfoVO> stocks;
}
