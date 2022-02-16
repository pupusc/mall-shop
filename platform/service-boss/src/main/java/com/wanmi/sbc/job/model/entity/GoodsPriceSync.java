package com.wanmi.sbc.job.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GoodsPriceSync implements Serializable {
    private static final long serialVersionUID = -2309480374062408521L;
    /**
     * 是否全部同步，1全部同步
     */
    private Integer isAllSync = 1 ;

    /**
     * 按照sku编码同步
     */
    private List<String> goodsInfoNo;
}
