package com.wanmi.sbc.marketing.api.request.grouponsetting;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>单个删除拼团活动信息表请求参数</p>
 *
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponSettingDelByIdRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @NotNull
    private String id;
}