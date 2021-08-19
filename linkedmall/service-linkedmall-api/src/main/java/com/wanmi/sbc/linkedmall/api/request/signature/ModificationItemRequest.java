package com.wanmi.sbc.linkedmall.api.request.signature;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ModificationItemRequest implements Serializable {

    private static final long serialVersionUID = -2456148279347178129L;
    @ApiModelProperty("子渠道ID")
    private String subBizId;
    @ApiModelProperty("请求标识")
    private String requestId;
    @ApiModelProperty("客户业务 ID")
    private String bizId;
    @ApiModelProperty("商品列表")
    private String itemList;
    @ApiModelProperty("签名")
    private String signature;

    private String signatureMethod;

    public List<LinkedMallItemModification> getItemListEntity(){
        return JSONObject.parseArray(itemList, LinkedMallItemModification.class);
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LinkedMallItemModification {
        private Boolean canSell;
//        扩展字段
        private String extJson;
//        时间戳
        private Long gmtModified;
        private Long itemId;
        private String lmItemId;
        private List<LinkedMallSku> skuList;
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class LinkedMallSku {
             private Boolean canSell;
             private long points;
             private long pointsAmount;
//             "销售价格（分）
             private Long priceCent;
             private Long skuId;

         }
    }

}
