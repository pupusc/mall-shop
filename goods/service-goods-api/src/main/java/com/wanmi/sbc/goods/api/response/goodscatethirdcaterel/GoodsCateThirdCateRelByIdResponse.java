package com.wanmi.sbc.goods.api.response.goodscatethirdcaterel;

import com.wanmi.sbc.goods.bean.vo.GoodsCateThirdCateRelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）平台类目和第三方平台类目映射信息response</p>
 * @author 
 * @date 2020-08-18 19:51:55
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCateThirdCateRelByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 平台类目和第三方平台类目映射信息
     */
    @ApiModelProperty(value = "平台类目和第三方平台类目映射信息")
    private GoodsCateThirdCateRelVO goodsCateThirdCateRelVO;
}
