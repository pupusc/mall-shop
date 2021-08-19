package com.wanmi.sbc.goods.api.response.goodscatethirdcaterel;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GoodsCateThirdCateRelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>平台类目和第三方平台类目映射分页结果</p>
 * @author 
 * @date 2020-08-18 19:51:55
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateThirdCateRelPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 平台类目和第三方平台类目映射分页结果
     */
    @ApiModelProperty(value = "平台类目和第三方平台类目映射分页结果")
    private MicroServicePage<GoodsCateThirdCateRelVO> goodsCateThirdCateRelVOPage;
}
