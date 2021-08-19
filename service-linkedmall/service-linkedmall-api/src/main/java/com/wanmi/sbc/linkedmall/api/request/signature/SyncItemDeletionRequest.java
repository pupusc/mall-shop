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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class SyncItemDeletionRequest implements Serializable {

    private static final long serialVersionUID = 1984542753941724926L;

    @ApiModelProperty("子渠道编码")
    private String subBizId;
    @ApiModelProperty("签名")
    private String signature;
    @ApiModelProperty("请求标识")
    private String requestId;
    @ApiModelProperty("客户业务 ID")
    private String bizId;
    @ApiModelProperty("签名方法")
    private String signatureMethod;
    @ApiModelProperty("商品列表")
    private String itemIdList;

    public List<LinkedMallDeletionItem> getItemIdListEntity(){
        return JSONObject.parseArray(itemIdList, LinkedMallDeletionItem.class);
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LinkedMallDeletionItem implements Serializable {
        private static final long serialVersionUID = -6318804231489695400L;
//        时间戳
        private long gmt_create;
        private Long itemId;
        private String lmItemId;
        private List<Long> skuIdList;
    }

}
