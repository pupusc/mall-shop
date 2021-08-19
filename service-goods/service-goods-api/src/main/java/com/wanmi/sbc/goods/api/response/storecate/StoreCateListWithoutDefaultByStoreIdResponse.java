package com.wanmi.sbc.goods.api.response.storecate;

import com.wanmi.sbc.goods.bean.vo.StoreCateResponseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Author: bail
 * Time: 2017/11/13.10:25
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateListWithoutDefaultByStoreIdResponse implements Serializable {

    private static final long serialVersionUID = -2670052172976994919L;

    @ApiModelProperty(value = "店铺分类")
    private List<StoreCateResponseVO> storeCateResponseVOList;
}
