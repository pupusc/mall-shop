package com.wanmi.sbc.topic.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ThreeGoodBookResponse {

    //"专栏id"
    private Integer id;

    //专栏名称
    private String Name;

    //排序
    private Integer orderNum;

    private List<ThreeGoodBookGoods>  goodBookGoods;

}
