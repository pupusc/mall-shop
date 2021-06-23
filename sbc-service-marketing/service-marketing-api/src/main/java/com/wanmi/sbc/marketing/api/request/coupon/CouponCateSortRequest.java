package com.wanmi.sbc.marketing.api.request.coupon;

import com.wanmi.sbc.marketing.bean.dto.CouponCateSortDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponCateSortRequest implements Serializable {

    private static final long serialVersionUID = 2893877487411495819L;

    @ApiModelProperty(value = "优惠券分类排序顺序列表")
    @Valid
    @NotNull
    @Size(min = 1)
    private List<CouponCateSortDTO> list;
}
