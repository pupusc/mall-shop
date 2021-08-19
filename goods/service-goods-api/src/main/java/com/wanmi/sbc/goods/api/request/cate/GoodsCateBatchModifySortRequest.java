package com.wanmi.sbc.goods.api.request.cate;

import com.wanmi.sbc.goods.bean.dto.GoodsCateSortDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 修改商品分类排序信息请求对象
 * @author daiyitian
 * @dateTime 2018/11/1 下午4:54
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateBatchModifySortRequest implements Serializable {

    private static final long serialVersionUID = 4362250574081024617L;

    /**
     * 批量修改商品分类排序 {@link GoodsCateSortDTO}
     */
    @ApiModelProperty(value = "批量修改商品分类排序")
    @NotEmpty
    private List<GoodsCateSortDTO> goodsCateSortDTOList;
}
