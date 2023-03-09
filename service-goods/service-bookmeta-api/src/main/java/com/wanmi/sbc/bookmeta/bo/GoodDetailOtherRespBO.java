package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/08/16:59
 * @Description:
 */
@Data
public class GoodDetailOtherRespBO implements Serializable {
    private String goodsId;
    private String goodsShowType;
    private String goodsDetail;
    private String orderShowType;
    private String orderDetail;

}
