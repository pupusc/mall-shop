package com.wanmi.sbc.goods.api.request.brand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 签约品牌查询请求结构
 * Created by daiyitian on 2018/11/02.
 */
@ApiModel
@Data
public class ContractBrandListRequest implements Serializable {

    private static final long serialVersionUID = 4515178783252329105L;

    /**
     * 签约品牌分类
     */
    @ApiModelProperty(value = "签约品牌分类")
    private Long contractBrandId;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;

    /**
     * 平台品牌id
     */
    @ApiModelProperty(value = "平台品牌id")
    private List<Long> goodsBrandIds;

    /**
     * 自定义品牌名称
     */
    @ApiModelProperty(value = "自定义品牌名称")
    private String checkBrandName;

}
