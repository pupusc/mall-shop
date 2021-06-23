package com.wanmi.sbc.marketing.api.request.grouponsetting;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * <p>批量删除拼团活动信息表请求参数</p>
 *
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponSettingDelByIdListRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量删除-主键List
     */
    @NotEmpty
    private List<String> idList;
}