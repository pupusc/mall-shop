package com.wanmi.sbc.goods.api.response.flashsalecate;

import com.wanmi.sbc.goods.bean.vo.FlashSaleCateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）秒杀分类信息response</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleCateByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 秒杀分类信息
     */
    @ApiModelProperty(value = "秒杀分类信息")
    private FlashSaleCateVO flashSaleCateVO;
}
