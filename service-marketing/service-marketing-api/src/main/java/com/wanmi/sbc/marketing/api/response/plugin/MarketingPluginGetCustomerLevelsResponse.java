package com.wanmi.sbc.marketing.api.response.plugin;

import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-27
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingPluginGetCustomerLevelsResponse implements Serializable {

    private static final long serialVersionUID = -2638352485783592944L;

    @ApiModelProperty(value = "店铺会员等级map<key为店铺id，value为会员等级>")
    private HashMap<Long, CommonLevelVO> commonLevelVOMap;
}
