package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class LinkedMallItemDelDTO implements Serializable {

    private static final long serialVersionUID = -1223946824787025853L;
    //        时间戳
    private long gmt_create;
    private Long itemId;
    private String lmItemId;
    private List<Long> skuIdList;
}
