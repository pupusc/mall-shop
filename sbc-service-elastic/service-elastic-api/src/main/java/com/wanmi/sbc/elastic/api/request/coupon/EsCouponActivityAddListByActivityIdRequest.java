package com.wanmi.sbc.elastic.api.request.coupon;


import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据优惠券活动ID集合新增ES数据
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsCouponActivityAddListByActivityIdRequest implements Serializable {

    private static final long serialVersionUID = -9162622109556746710L;

    private List<String> activityIdList;
}
