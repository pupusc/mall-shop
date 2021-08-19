package com.wanmi.sbc.order.bean.vo;

import com.wanmi.sbc.customer.bean.vo.CustomerDetailWithImgVO;
import com.wanmi.sbc.marketing.bean.enums.GrouponDetailOptStatus;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import com.wanmi.sbc.marketing.bean.vo.TradeGrouponVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: lq
 * @CreateTime:2019-05-18 14:32
 * @Description:todo
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrouponDetailWithGoodsVO {

    /**
     * 团活动
     */
    private GrouponActivityVO grouponActivity;

    /**
     * 开团-非同团用户的正在进行中的同活动订单信息(返回团信息)
     */
    private TradeGrouponVO tradeGroupon;

    /**
     * 验证信息
     */
    private GrouponDetailOptStatus grouponDetailOptStatus;

    /**
     * 参团人员信息
     */
    private List<CustomerDetailWithImgVO> customerVOList;


    /**
     * 团长个skuid
     */
    private String goodInfoId;

    /**
     * 团长的customerId
     */
    private String groupCustomerId;

}
