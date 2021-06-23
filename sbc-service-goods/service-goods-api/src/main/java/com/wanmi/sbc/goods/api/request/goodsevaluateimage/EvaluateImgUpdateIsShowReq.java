package com.wanmi.sbc.goods.api.request.goodsevaluateimage;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2019-04-24 17:01
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateImgUpdateIsShowReq extends BaseQueryRequest {

    private static final long serialVersionUID = 1612904995339645760L;

    /**
     * 商品评价ID
     */
    private String evaluateId;

    /**
     * 0：不显示 1：显示
     */
    private int isShow;
}