package com.wanmi.sbc.goods.api.response.cate;

import com.wanmi.sbc.goods.bean.vo.ContractCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>签约分类相应类</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractCateListResponse implements Serializable {

    private static final long serialVersionUID = 7069224277188218856L;

    @ApiModelProperty(value = "签约分类")
    private List<ContractCateVO> contractCateList;
}
