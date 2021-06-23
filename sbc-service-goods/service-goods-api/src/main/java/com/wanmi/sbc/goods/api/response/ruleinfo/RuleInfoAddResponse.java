package com.wanmi.sbc.goods.api.response.ruleinfo;

import com.wanmi.sbc.goods.bean.vo.RuleInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>规则说明新增结果</p>
 * @author zxd
 * @date 2020-05-25 18:55:56
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleInfoAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的规则说明信息
     */
    @ApiModelProperty(value = "已新增的规则说明信息")
    private RuleInfoVO ruleInfoVO;
}
