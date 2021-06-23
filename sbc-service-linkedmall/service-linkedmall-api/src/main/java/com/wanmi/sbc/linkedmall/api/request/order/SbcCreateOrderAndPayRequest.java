package com.wanmi.sbc.linkedmall.api.request.order;


import com.alibaba.fastjson.JSON;
import com.aliyuncs.linkedmall.model.v20180116.CreateOrderRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 下单并支付请求
 * \* User: yhy
 * \* Date: 2020-8-10
 * \* Time: 17:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class SbcCreateOrderAndPayRequest implements Serializable {

    @ApiModelProperty(value = "商品信息,itemId：spuId; quantity:购买数量; skuId: skuId")
    @NotEmpty
    private List<CreateOrderRequest.ItemList> lmGoodsItems;

    @ApiModelProperty(value = "街道上一级的divisionCode")
    @NotBlank
    private String divisionCode;

    @ApiModelProperty(value = "收货人姓名")
    @NotBlank
    private String fullName;

    @ApiModelProperty(value = "收货人电话")
    @NotBlank
    private String mobile;

    @ApiModelProperty(value = "收货人地址,详细地址需补足5个字符")
    @NotBlank
    private String addressDetail;

    @ApiModelProperty(value = "商城内部订单id,需保证唯一")
    @NotBlank
    private String outTradeId;

    @ApiModelProperty(value = "商城内部用户id")
    @NotBlank
    private String bizUid;

    /**
     * 构建收货地址信息
     * linkedMall需要 json字符串 收货人地址大于等于5个字符
     * { "divisionCode": "街道上一级的divisionCode", "fullName": "收货人姓名", "mobile": "收货人电话", "addressDetail": "收货人地址" }
     */
    public String buildDeliveryAddress() {
        Map<String, String> map = new HashMap<>();
        // 街道上一级的divisionCode
        if (StringUtils.isNotBlank(divisionCode)) {
            map.put("divisionCode", divisionCode);
        }
        // 收货人姓名
        if (StringUtils.isNotBlank(fullName)) {
            map.put("fullName", fullName);
        }
        // 收货人电话
        if (StringUtils.isNotBlank(mobile)) {
            map.put("mobile", mobile);
        }
        // 收货人地址,详细地址需补足5个字符
        if (StringUtils.isNotBlank(addressDetail)) {
            if (addressDetail.length() < 5) {
                addressDetail = String.format("%-5s", addressDetail);
            }
            map.put("addressDetail", addressDetail);
        }
        return JSON.toJSONString(map);
    }
}
