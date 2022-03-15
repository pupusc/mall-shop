package com.wanmi.sbc.returnorder.request;

import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.order.bean.vo.TradeDistributeItemVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 分销对象【预留】
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/12/23 1:54 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class DistributionAddProviderRequest {

    /**
     * 分销渠道类型
     */
    private ChannelType channelType;

    /**
     * 邀请人分销员id
     */
    private String distributorId;

    /**
     * 分销员名称
     */
    private String distributorName;

    /**
     * 分销商品列表
     */
    private List<TradeDistributeItemVO> distributeItems;



//    /**
//     * 邀请人会员id
//     */
//    private String inviteeId;


}
