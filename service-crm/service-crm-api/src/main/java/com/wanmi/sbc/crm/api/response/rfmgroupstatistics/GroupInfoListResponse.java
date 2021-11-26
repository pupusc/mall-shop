package com.wanmi.sbc.crm.api.response.rfmgroupstatistics;

import com.wanmi.sbc.crm.bean.vo.GroupInfoVo;
import com.wanmi.sbc.crm.bean.vo.RfmgroupstatisticsDataVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName GroupInfoListResponse
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/1/17 13:48
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfoListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 人群信息查询结果
     */
    @ApiModelProperty(value = "人群信息查询结果")
    private List<GroupInfoVo> groupInfoVoList;
}
