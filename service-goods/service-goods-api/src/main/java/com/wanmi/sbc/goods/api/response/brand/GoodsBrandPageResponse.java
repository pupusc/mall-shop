package com.wanmi.sbc.goods.api.response.brand;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 品牌分页查询响应
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsBrandPageResponse implements Serializable {

    private static final long serialVersionUID = -6942228033110682924L;

    /**
     * 品牌分页列表
     */
    @ApiModelProperty(value = "品牌分页列表")
    private MicroServicePage<GoodsBrandVO> goodsBrandPage;
}
