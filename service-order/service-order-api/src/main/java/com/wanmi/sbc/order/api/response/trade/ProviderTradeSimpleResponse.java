package com.wanmi.sbc.order.api.response.trade;

import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/2/18 6:20 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ProviderTradeSimpleResponse implements Serializable {

    /**
     * 主单id
     */
    private String tid;

    /**
     * 子单id
     */
    private String pid;

    /**
     * 运费价格
     */
    private String deliveryPrice;


    /**
     * 订单商品列表
     */
    private List<TradeItemVO> tradeItems = new ArrayList<>();

}
