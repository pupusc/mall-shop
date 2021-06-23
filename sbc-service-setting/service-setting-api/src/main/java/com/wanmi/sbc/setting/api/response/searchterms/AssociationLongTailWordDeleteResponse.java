package com.wanmi.sbc.setting.api.response.searchterms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>删除结果</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociationLongTailWordDeleteResponse implements Serializable {


    private static final long serialVersionUID = 4525463186949409894L;

    /**
     * 数据操作结果
     */
    @ApiModelProperty(value = "数据操作结果")
    private Integer resultNum;

}
