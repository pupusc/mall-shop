package com.wanmi.sbc.goods.api.request.standard;

import com.wanmi.sbc.common.enums.BoolFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * com.wanmi.sbc.goods.api.request.standard.StandardImportGoodsRequest
 * 商品库导入商品请求对象
 *
 * @author lipeng
 * @dateTime 2018/11/9 下午2:47
 */
@ApiModel
@Data
public class StandardImportGoodsRequest implements Serializable {

    private static final long serialVersionUID = 382332593693958309L;

    /**
     * 商品库编号
     */
    @ApiModelProperty(value = "商品库编号")
    private List<String> goodsIds;

    /**
     * 公司信息编号
     */
    @ApiModelProperty(value = "公司信息编号")
    private Long companyInfoId;

    /**
     * 商户类型
     */
    @ApiModelProperty(value = "商户类型", notes = "0:平台自营, 1: 第三方商家")
    private BoolFlag companyType;

    /**
     * 店铺编号
     */
    @ApiModelProperty(value = "店铺编号")
    private Long storeId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;
}
