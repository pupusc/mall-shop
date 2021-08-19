package com.wanmi.sbc.goods.api.response.freight;

import com.wanmi.sbc.goods.bean.vo.FreightTemplateStoreVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 查询店铺运费模板列表响应
 * Created by daiyitian on 2018/11/1.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreightTemplateStoreListByStoreIdAndDeleteFlagResponse implements Serializable {

    private static final long serialVersionUID = 4834479641868315143L;

    /**
     * 店铺运费模板列表
     */
    @ApiModelProperty(value = "店铺运费模板列表")
    private List<FreightTemplateStoreVO> freightTemplateStoreVOList;

}
