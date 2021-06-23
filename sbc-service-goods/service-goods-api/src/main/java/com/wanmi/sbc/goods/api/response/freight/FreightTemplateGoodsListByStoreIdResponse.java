package com.wanmi.sbc.goods.api.response.freight;

import com.wanmi.sbc.goods.bean.vo.FreightTemplateGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 查询单品运费模板列表响应
 * Created by daiyitian on 2018/10/31.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateGoodsListByStoreIdResponse implements Serializable {

    private static final long serialVersionUID = 6826514907980533216L;

    /**
     * 单品运费模板列表
     */
    @ApiModelProperty(value = "单品运费模板列表")
    private List<FreightTemplateGoodsVO> freightTemplateGoodsVOList;

}
