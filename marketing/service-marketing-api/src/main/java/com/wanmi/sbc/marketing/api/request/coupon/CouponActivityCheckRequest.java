package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.bean.dto.CouponActivityConfigDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
public class CouponActivityCheckRequest implements Serializable {

    private static final long serialVersionUID = 4333371385683393019L;

    private LocalDateTime activityEndTime;

    private List<CouponActivityConfigDTO> couponActivityConfigs;
}
