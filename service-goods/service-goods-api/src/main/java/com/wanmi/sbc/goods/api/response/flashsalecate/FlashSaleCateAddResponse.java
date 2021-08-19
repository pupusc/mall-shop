package com.wanmi.sbc.goods.api.response.flashsalecate;

import com.wanmi.sbc.goods.bean.vo.FlashSaleCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>秒杀分类新增结果</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleCateAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的秒杀分类信息
     */
    @ApiModelProperty(value = "已新增的秒杀分类信息")
    private FlashSaleCateVO flashSaleCateVO;
}
