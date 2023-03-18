package com.wanmi.sbc.bookmeta.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/08/17:00
 * @Description:
 */
@Data
public class GoodsOtherDetail implements Serializable {
    private String goodsId;
    private String goodsName;
    private String goodsShowType;
    private String goodsDetail;
    private String orderShowType;
    private String orderDetail;


}
