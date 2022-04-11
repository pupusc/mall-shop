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
    private CouponInfo couponInfo;

    @Data
    public static class CouponInfo{

        @ApiModelProperty(value = "优惠券是否已领取")
        private boolean hasFetched;


        @ApiModelProperty(value = "优惠券是否有剩余")
        private boolean leftFlag;

        @ApiModelProperty(value = "优惠券Id",hidden = true)
        private String couponId;

        @ApiModelProperty(value = "优惠券活动Id",hidden = true)
        private String activityId;

        /**
         * 一天限领一次的券今日是否已领取
         */
        private Boolean canFetchMore;

    }
}
