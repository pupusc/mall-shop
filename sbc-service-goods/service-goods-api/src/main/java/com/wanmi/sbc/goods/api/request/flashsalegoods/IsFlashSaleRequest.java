package com.wanmi.sbc.goods.api.request.flashsalegoods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author yang
 * @since 2019-07-23
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IsFlashSaleRequest extends BaseQueryRequest {

   private static final long serialVersionUID = -3555934764803043987L;

   /**
    * 活动时间：13:00
    */
   @ApiModelProperty(value = "活动时间：13:00")
   private String activityTime;
}
