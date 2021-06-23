package com.wanmi.sbc.marketing.api.response.plugin;

import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * <p>获取营销返回视图</p>
 * author: sunkun
 * Date: 2018-11-19
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingPluginByGoodsInfoListAndCustomerLevelResponse implements Serializable {

    private static final long serialVersionUID = -3979973320013189578L;

    @ApiModelProperty(value = "单品营销活动map<key为单品id，value为营销活动列表>")
    private HashMap<String, List<MarketingViewVO>> marketingMap;
}
