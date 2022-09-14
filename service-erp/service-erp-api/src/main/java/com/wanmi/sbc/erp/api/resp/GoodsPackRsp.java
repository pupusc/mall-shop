package com.wanmi.sbc.erp.api.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GoodsPackRsp implements Serializable {
    private static final long serialVersionUID = -266329842590889812L;
    /**
     * 销售码CODE
     */
    private String goodsCode;
    /**
     * 销售码id
     */
    private Integer tid;

    /**
     * 商销售码名称
     */
    private String name;
    /**
     * 是否有效
     */
    private Boolean validFlag;
    /**
     * 是否允许拆仓发货
     */
    private Boolean splitWbFlag;
    /**
     * 是否是打包类型
     */
    private Boolean packType;


    /**
     * 限购 -1代表不限
     */
    private Integer limitBuy = -1;


    /**
     * 会期类型 0 体验，1 正式
     */
    private Integer rightsCategory;

    /**
     * 打包-关联货品信息
     */
    List<PackSkuListRsp> goodsLists;

}
