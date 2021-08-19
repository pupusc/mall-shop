package com.wanmi.sbc.marketing.api.response.grouponsetting;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author: gaomuwei
 * @Date: Created In 下午5:18 2019/5/27
 * @Description:
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponSettingAuditFlagResponse {

    /**
     * 拼团商品审核状态
     */
    @ApiModelProperty("拼团商品审核状态")
    private DefaultFlag goodsAuditFlag;

}
