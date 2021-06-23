package com.wanmi.sbc.crm.api.response.rfmgroupstatistics;

import com.github.pagehelper.PageInfo;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.crm.bean.vo.RfmgroupstatisticsDataVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;

/**
 * @ClassName RfmgroupstatisticsPageResponse
 * @Description rfm系统人群分页查询结果Response
 * @Author lvzhenwei
 * @Date 2019/10/15 16:59
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfmGroupStatisticsPageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统人群分页查询结果
     */
    @ApiModelProperty(value = "系统人群分页查询结果")
    private MicroServicePage<RfmgroupstatisticsDataVo> rfmGroupStatisticsPageResponse;
}
