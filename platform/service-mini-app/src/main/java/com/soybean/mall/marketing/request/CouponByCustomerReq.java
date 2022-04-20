package com.soybean.mall.marketing.request;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/20 10:49 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CouponByCustomerReq {

    /**
     * 商品id和数量
     */
    private List<TradeItemSimpleRequest> tradeItemSimpleList;

}
