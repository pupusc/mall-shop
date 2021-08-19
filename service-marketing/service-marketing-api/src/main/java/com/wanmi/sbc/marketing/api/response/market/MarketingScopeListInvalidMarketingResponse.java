package com.wanmi.sbc.marketing.api.response.market;

import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingScopeListInvalidMarketingResponse implements Serializable {

    private static final long serialVersionUID = 1506933929495607020L;
    /**
     * 失效活动集合
     */
    @ApiModelProperty(value = "失效活动集合")
    private List<MarketingVO> marketingList;

}
