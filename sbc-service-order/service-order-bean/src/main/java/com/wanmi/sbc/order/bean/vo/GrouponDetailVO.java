package com.wanmi.sbc.order.bean.vo;

import com.wanmi.sbc.customer.bean.vo.CustomerDetailWithImgVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
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
public class GrouponDetailVO {

    /**
     * 商品id
     */
    private String goodsInfoId;

    /**
     * 团活动
     */
    private GrouponActivityVO grouponActivity;

    /**
     * 拼团商品信息
     */
    private List<GrouponGoodsInfoVO> grouponGoodsInfos;
    /**
     * 参团-同团订单
     */
    private List<TradeVO> tradeList;


    /**
     * 参团-同团团长订单信息
     */
    private TradeVO tradeInGroupon;


    /**
     * 开团-非同团用户的正在进行中的同活动订单信息(返回团信息)
     */
    private TradeGrouponVO tradeGroupon;

//    /**
//     * 用户团商品参团情况
//     */
//    private GrouponRecordVO grouponRecord;

    /**
     * 验证信息
     */
    private GrouponDetailOptStatus grouponDetailOptStatus;

    /**
     * 进行中团活动
     */
    private List<GrouponInstanceWithCustomerInfoVO> grouponInstanceList;


    /**
     * 参团人员信息
     */
    private List<CustomerDetailWithImgVO> customerVOList;
}
