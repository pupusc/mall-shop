package com.wanmi.sbc.topic.response;


import com.wanmi.sbc.marketing.bean.vo.CouponVO;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TopicStoreyContentReponse extends TopicStoreyContentDTO {

    @ApiModelProperty("商品信息")
    private GoodsAndAtmosphereResponse goods;

    @ApiModelProperty("优惠券信息")
    private CouponVO couponInfo;
}
