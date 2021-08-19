package com.wanmi.sbc.goods.api.request.virtualcoupon;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;

import java.time.LocalDateTime;
import java.util.List;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponCodeVO;
import lombok.*;

import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>券码新增参数</p>
 *
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponCodeAddRequest {

    @ApiModelProperty("导入列表")
    private List<VirtualCouponCodeVO> virtualCouponCodes;

    @ApiModelProperty("店铺ID")
    private Long storeId;

    @ApiModelProperty("卡券ID")
    private Long couponId;

}