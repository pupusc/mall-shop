package com.wanmi.sbc.goods.api.response.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 签约信息保存响应结构
 * Created by daiyitian on 2018/5/3.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractSaveResponse implements Serializable {

    private static final long serialVersionUID = 8246352610810060194L;

    /**
     * 删除后的品牌id
     */
    @ApiModelProperty(value = "删除后的品牌id")
    private List<Long> brandIds;


}
