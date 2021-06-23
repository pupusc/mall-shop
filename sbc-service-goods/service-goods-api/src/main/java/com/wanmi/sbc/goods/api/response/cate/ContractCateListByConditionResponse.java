package com.wanmi.sbc.goods.api.response.cate;

import com.wanmi.sbc.goods.bean.vo.ContractCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>根据动态条件查询响应类</p>
 * @author daiyitian
 * @dateTime 2018-11-15
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractCateListByConditionResponse implements Serializable {

    private static final long serialVersionUID = 7069224277188218856L;

    /**
     * 签约分类列表
     */
    @ApiModelProperty(value = "签约分类列表")
    private List<ContractCateVO> contractCateList;
}
