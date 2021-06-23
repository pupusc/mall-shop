package com.wanmi.sbc.order.trade.request;


import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.Invoice;
import com.wanmi.sbc.order.trade.model.entity.value.TradePrice;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TradeRemedyRequest extends BaseRequest {

    private static final long serialVersionUID = 7232157652419534899L;

    private String tradeId;

    private PayType payType;

    private String consigneeId;

    private String consigneeUpdateTime;

    private String consigneeAddress;

    @NotEmpty
    @Valid
    private List<TradeItem> tradeItems;

    private List<String> newSkuIds;

    private TradePrice tradePrice;

    private Invoice invoice;

    private String buyerRemark;

    private String sellerRemark;

    private String encloses;

    @NotNull
    private DeliverWay deliverWay;

    private Consignee consignee;

    private Consignee invoiceConsignee;

    /**
     * 订单营销信息快照
     */
    @NotNull
    private List<TradeMarketingDTO> tradeMarketingList;

    /**
     * 是否强制提交，用于营销活动有效性校验，true: 无效依然提交， false: 无效做异常返回
     */
    public boolean forceCommit;
    /**
     * 虚拟商品直冲手机号
     */
    private String directChargeMobile;
}
