package com.wanmi.sbc.marketing.api.request.grouponactivity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * <p>批量删除拼团活动信息表请求参数</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityDelByIdListRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量删除-活动IDList
     */
    @NotEmpty
    private List<String> grouponActivityIdList;
}