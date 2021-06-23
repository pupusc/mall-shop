package com.wanmi.sbc.goods.api.request.storecate;

import com.wanmi.sbc.goods.bean.dto.StoreCateSortDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 修改店铺分类排序信息请求对象
 * Author: bail
 * Time: 2017/11/13.10:22
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateBatchModifySortRequest implements Serializable {

    private static final long serialVersionUID = 6791664312330840765L;

    /**
     * 批量修改分类排序 {@link StoreCateSortDTO}
     */
    @ApiModelProperty(value = "批量修改分类排序")
    @NotEmpty
    private List<StoreCateSortDTO> storeCateSortDTOList;

}
