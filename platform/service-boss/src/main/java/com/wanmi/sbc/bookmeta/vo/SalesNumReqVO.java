package com.wanmi.sbc.bookmeta.vo;

import com.wanmi.sbc.common.base.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/11/11:25
 * @Description:
 */
@Data
public class SalesNumReqVO implements Serializable {
    private String spuId;
    private String spuName;
    private String skuId;
    private String skuName;
    private int salesNum;
    private double fixPrice;
    private Page page = new Page(1, 10);
}
