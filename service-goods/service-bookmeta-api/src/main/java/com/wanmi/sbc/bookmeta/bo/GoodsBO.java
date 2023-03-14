package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/14/14:51
 * @Description:
 */
@Data
public class GoodsBO implements Serializable {
    private String goodsName;
    private String goodsId;
}
