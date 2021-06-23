package com.wanmi.sbc.marketing.api.response.grouponsetting;
import com.wanmi.sbc.marketing.bean.vo.GrouponSettingGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>拼团活动信息表新增结果</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponSettingGoodsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<GrouponSettingGoodsVO> grouponSettingGoodsVOList;
    /**
     * 页码
     */
    @ApiModelProperty(value = "页码")
    private int number;

    /**
     * 总数据大小
     */
    @ApiModelProperty(value = "总数据大小")
    private long total;
}
