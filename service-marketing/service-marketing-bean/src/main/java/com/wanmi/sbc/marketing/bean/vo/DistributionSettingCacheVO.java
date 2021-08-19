package com.wanmi.sbc.marketing.bean.vo;

import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>分销设置缓存VO</p>
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionSettingCacheVO implements Serializable {

    private static final long serialVersionUID = -377128448597230486L;

    /**
     * 分销配置
     */
    @ApiModelProperty(value = "分销配置")
    private DistributionSettingVO distributionSetting;

    /**
     * 分销员等级列表
     */
    @ApiModelProperty(value = "分类员等级列表")
    private List<DistributorLevelVO> distributorLevels = new ArrayList<>();

}
