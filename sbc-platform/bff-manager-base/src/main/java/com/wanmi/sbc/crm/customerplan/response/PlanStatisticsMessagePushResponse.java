package com.wanmi.sbc.crm.customerplan.response;

import com.wanmi.sbc.crm.bean.vo.PlanStatisticsMessageVO;
import com.wanmi.sbc.crm.customerplan.vo.PlanStatisticsMessagePushVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PlanStatisticsMessagePushResponse
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/2/6 10:26
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanStatisticsMessagePushResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 运营计划效果统计通知人次统计数据信息
     */
    @ApiModelProperty(value = "运营计划效果统计通知人次统计数据信息")
    private PlanStatisticsMessagePushVo planStatisticsMessagePushVo;

}
