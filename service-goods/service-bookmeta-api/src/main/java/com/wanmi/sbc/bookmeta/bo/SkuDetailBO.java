package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/07/15:11
 * @Description:
 */
@Data
public class SkuDetailBO implements Serializable {
    private String spuId;
    private String skuId;
    private String img;
    private String score;
    private String Isbn;
    private String saleNum;
    private String SkuName;
    private BigDecimal price;
    private String specNum; //规格数量
}
