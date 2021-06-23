package com.wanmi.sbc.marketing.api.response.markup;

import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>加价购活动新增结果</p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarkupAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的加价购活动信息
     */
    @ApiModelProperty(value = "已新增的加价购活动信息")
    private MarketingVO marketingVO;
}
