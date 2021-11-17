package com.wanmi.sbc.goods.api.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseSortProviderRequest implements Serializable {


    private Integer id;

    /**
     * 排序
     */
    private Integer orderNum;
}
