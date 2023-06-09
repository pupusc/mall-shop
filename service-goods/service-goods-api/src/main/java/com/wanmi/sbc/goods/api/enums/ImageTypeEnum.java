package com.wanmi.sbc.goods.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/20 1:41 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum ImageTypeEnum {

    ROTATION_CHART_IMG(1, "轮播图"),
    ADVERT_IMG(2, "首页广告图"),
    SELL_IMG(3, "首页卖点图"),
    WX_SUBSCRIBE(4, "直播订阅"),
    WX_ORDER_PREPARE_AD(5, "准备支付页面广告图"),
    ;

    private final Integer code;
    private final String message;

    public static ImageTypeEnum getByCode(int code) {
        for (ImageTypeEnum p : values()) {
            if (Objects.equals(p.getCode(), code)) {
                return p;
            }
        }
        return null;
    }
}
