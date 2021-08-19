package com.wanmi.sbc.elastic.api.response.goods;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author houshuai
 * @date 2020/12/10 10:24
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class EsGoodsBrandPageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌分页列表
     */
    @ApiModelProperty(value = "品牌分页列表")
    private MicroServicePage<GoodsBrandVO> goodsBrandPage;
}
