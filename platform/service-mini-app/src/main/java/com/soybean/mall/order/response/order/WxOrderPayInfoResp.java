package com.soybean.mall.order.response.order;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/7 5:27 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class WxOrderPayInfoResp {

    /**
     * 支付类型 0：在线支付 1：线下支付
     */
    private String payTypeId;

    /**
     * 支付类型名称
     */
    private  String payTypeName;
}
