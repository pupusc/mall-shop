package com.soybean.mall.image.req;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/7 7:09 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CommonImageReq {

    /**
     * 图片类型 1首页轮播 2 广告图片、3 首页卖点图 4 直播订阅图 5 订单准备支付页面图
     */
    private Integer imageType;
}
