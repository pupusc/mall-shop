package com.wanmi.sbc.goods.api.response.storecate;

import com.wanmi.sbc.goods.bean.vo.StoreCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据分类id批量查询店铺分类列表响应结构
 * Author: daiyitian
 * Time: 2018/11/19.10:25
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateListByIdsResponse implements Serializable {

    private static final long serialVersionUID = 3585371863867140208L;

    /**
     * 店铺分类列表
     */
    @ApiModelProperty(value = "店铺分类列表")
    private List<StoreCateVO> storeCateVOList;
}
