package com.wanmi.sbc.order.returnorder.model.value;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品规格
 * Created by jinwei on 19/4/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecDetail {

    /**
     * 规格id
     */
    private Long specId;

    /**
     * 规格名称
     */
    private String specName;

    /**
     * 规格值id
     */
    private Long detailId;

    /**
     * 规格值
     */
    private String detailName;

}
