package com.wanmi.sbc.goods.api.response.brand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 签约品牌列表删除响应结构
 * Created by daiyitian on 2018/11/02.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractBrandDeleteByIdsResponse implements Serializable {

    private static final long serialVersionUID = 4515178783252329105L;

    /**
     * 平台品牌id列表
     */
    @ApiModelProperty(value = "平台品牌id列表")
    private List<Long> brandIds;

}
