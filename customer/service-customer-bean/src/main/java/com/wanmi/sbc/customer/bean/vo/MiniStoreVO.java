package com.wanmi.sbc.customer.bean.vo;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.StoreType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 店铺信息
 * Created by CHENLI on 2017/11/2.
 */
@ApiModel
@Data
public class MiniStoreVO implements Serializable {

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;


    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;


    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型(0、平台自营 1、第三方商家)")
    private BoolFlag companyType;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 一对多关系，多个SPU编号
     */
    @ApiModelProperty(value = "多个SPU编号")
    private List<String> goodsIds = new ArrayList<>();

    /**
     * 商家类型0品牌商城，1商家
     */
    @ApiModelProperty(value = "商家类型0品牌商城，1商家")
    private StoreType storeType;
}
