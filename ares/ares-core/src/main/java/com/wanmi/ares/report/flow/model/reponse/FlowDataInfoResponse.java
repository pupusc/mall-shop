package com.wanmi.ares.report.flow.model.reponse;

import lombok.Data;

/**
 * @ClassName FlowDataInfoResponse
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/22 11:19
 **/
@Data
public class FlowDataInfoResponse {
    /**
     * 访问人数
     */
    private Long uv;

    /**
     * 访问量
     */
    private Long pv;

    /**
     * 商品访问人数
     */
    private Long goodsUv;

    /**
     * 商品访问量
     */
    private Long goodsPv;

    /**
     * 店铺id
     */
    private String companyId;
}
