package com.wanmi.sbc.goods.bean.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class LinkedMallItemModificationDTO implements Serializable {
    private static final long serialVersionUID = 6269509039796344871L;

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
