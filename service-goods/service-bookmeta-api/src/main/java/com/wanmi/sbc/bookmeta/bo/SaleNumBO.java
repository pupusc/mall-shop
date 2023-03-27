package com.wanmi.sbc.bookmeta.bo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/10/14:36
 * @Description:
 */
@Data
public class SaleNumBO implements Serializable {
    private String spuId;
    private String spuName;
    private String skuId;
    private String skuName;
    private int salesNum;
//    private String salesNum;
    private double fixPrice;
    private Page page = new Page(1,10);
    private int limitIndex;
    private int limitSize;
}
