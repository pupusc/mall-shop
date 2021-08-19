package com.wanmi.sbc.goods.api.response.flashsaleactivity;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.FlashSaleActivityVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>秒杀活动列表结果</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleActivityPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 秒杀活动列表结果
     */
    @ApiModelProperty(value = "秒杀活动列表结果")
    private MicroServicePage<FlashSaleActivityVO> flashSaleActivityVOPage;
}
