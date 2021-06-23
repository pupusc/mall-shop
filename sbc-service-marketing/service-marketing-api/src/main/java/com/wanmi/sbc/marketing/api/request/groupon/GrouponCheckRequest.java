package com.wanmi.sbc.marketing.api.request.groupon;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * <p>拼团信息</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
public class GrouponCheckRequest extends BaseRequest {


    private static final long serialVersionUID = 5989615145427177342L;





    /**
     * 是否拼团购买
     */
    private Boolean grouponFlag;

    /**
     * 是否团长
     */
    private Boolean leader;

    /**
     * 会员Id
     */
    @Length(max=32)
    private String customerId;

    /**
     * 活动ID
     */
    private String grouponActivityId;


}