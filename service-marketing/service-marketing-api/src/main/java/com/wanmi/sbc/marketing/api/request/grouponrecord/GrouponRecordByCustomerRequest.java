package com.wanmi.sbc.marketing.api.request.grouponrecord;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * <p>拼团活动参团信息表新增参数</p>
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrouponRecordByCustomerRequest {

    /**
     * 拼团活动ID
     */
    @NotNull
    private String grouponActivityId;

    /**
     * 会员ID
     */
    @NotNull
    private String customerId;

    /**
     * sku编号
     */
    private String goodsInfoId;


}