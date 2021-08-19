package com.wanmi.sbc.crm.api.response.planstatisticsmessage;

import com.wanmi.sbc.crm.bean.vo.PlanStatisticsMessageVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）运营计划效果统计站内信收到人/次统计数据信息response</p>
 * @author lvzhenwei
 * @date 2020-02-05 15:08:00
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanStatisticsMessageByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 运营计划效果统计站内信收到人/次统计数据信息
     */
    @ApiModelProperty(value = "运营计划效果统计站内信收到人/次统计数据信息")
    private PlanStatisticsMessageVO planStatisticsMessageVO;
}
