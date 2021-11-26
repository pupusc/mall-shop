package com.wanmi.sbc.crm.api.response.rfmgroupstatistics;

import com.wanmi.sbc.crm.bean.vo.RfmGroupDataVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName RfmgroupstatisticsListResponse
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/10/15 16:59
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfmGroupListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统人群分页查询结果
     */
    @ApiModelProperty(value = "系统人群分页查询结果")
    private List<RfmGroupDataVo> groupDataList;

}
