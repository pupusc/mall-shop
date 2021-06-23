package com.wanmi.sbc.setting.api.response.searchterms;

import com.wanmi.sbc.setting.bean.vo.AssociationLongTailWordVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>搜索词结果</p>
 * @author weiwenhao
 * @date 2020-04-16
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociationLongTailWordResponse implements Serializable {


    private static final long serialVersionUID = 1763431479791211242L;

    /**
     * 联想词信息
     */
    @ApiModelProperty(value = "已保存联想词信息")
    private AssociationLongTailWordVO associationLongTailWordVO;
}
