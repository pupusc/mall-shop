package com.wanmi.sbc.goods.api.request.appointmentsale;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;
import java.util.Map;


@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleMergeInProgressRequest extends BaseQueryRequest {


    private static final long serialVersionUID = -7552208113924726472L;

    /**
     * 预售
     */
    @ApiModelProperty(value = "skuId")
    private List<String>  bookingSaleGoodsInfoIds;

    /**
     * 预售
     */
    private Map<String,Long> skuIdAndBookSaleIdMap;

    /**
     * 限售信息
     */
    private List<GoodsRestrictedValidateVO> goodsRestrictedValidateVOS;

    /**
     * 会员信息
     */
    private CustomerSimplifyVO customerVO;

    /**
     * 预约
     */
    private  List<String> appointSaleGoodsInfoIds;

    /**
     *
     */
    private  List<String> needValidSkuIds;

}