package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.marketing.bean.dto.TradeMarketingDTO;
import com.wanmi.sbc.order.bean.dto.StoreCommitInfoDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.enums.OrderSource;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>客户端提交订单参数结构，包含除商品信息外的其他必要参数</p>
 * Created by of628-wenzhi on 2017-07-18-下午3:40.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class TradeCommitRequest extends BaseRequest {

    private static final long serialVersionUID = -1555919128448507297L;

    /**
     * 订单收货地址id，必传
     */
    @ApiModelProperty(value = "订单收货地址id")
    @NotBlank
    private String consigneeId;

    /**
     * 收货地址详细信息(包含省市区)，必传
     */
    @ApiModelProperty(value = "收货地址详细信息(包含省市区)")
    @NotBlank
    private String consigneeAddress;

    /**
     * 收货地址修改时间，可空
     */
    @ApiModelProperty(value = "收货地址修改时间")
    private String consigneeUpdateTime;

    @Valid
    @NotEmpty
    @NotNull
    @ApiModelProperty(value = "订单信息")
    private List<StoreCommitInfoDTO> storeCommitInfoList;

    /**
     * 选择的平台优惠券(通用券)id
     */
    @ApiModelProperty(value = "选择的平台优惠券(通用券)id")
    private String commonCodeId;

    /**
     * 是否强制提交，用于营销活动有效性校验，true: 无效依然提交， false: 无效做异常返回
     */
    @ApiModelProperty(value = "是否强制提交", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    public boolean forceCommit;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;

    /**
     * 下单用户
     */
    @ApiModelProperty(value = "下单用户")
    private CustomerSimplifyOrderCommitVO customer;

    /**
     * 下单用户是否分销员
     */
    private DefaultFlag isDistributor = DefaultFlag.NO;

    @Override
    public void checkParam() {
        storeCommitInfoList.forEach(StoreCommitInfoDTO::checkParam);
    }

    /**
     * 订单来源--区分h5,pc,app,小程序,代客下单
     */
    @ApiModelProperty(value = "订单来源")
    private OrderSource orderSource;

    /**
     * 分销渠道
     */
    @ApiModelProperty(value = "分销渠道")
    private DistributeChannel distributeChannel;

    /**
     * 小店名称
     */
    @ApiModelProperty(value = "小店名称")
    private String shopName;

    /**
     * 平台分销设置开关
     */
    private DefaultFlag openFlag = DefaultFlag.NO;

    /**
     * 使用积分
     */
    @ApiModelProperty(value = "使用积分")
    private Long points;

    /**
     * 使用知豆
     */
    @ApiModelProperty(value = "使用知豆")
    private Long knowledge;

    /**
     * 分享人id
     */
    @ApiModelProperty(value = "分享人id")
    private String shareUserId;

    /**
     * 尾款通知手机号
     */
    @ApiModelProperty(value = "尾款通知手机号")
    private String tailNoticeMobile;
    /**
     * 虚拟商品直冲手机号
     */
    @ApiModelProperty(value = "虚拟商品直冲手机号")
    private String directChargeMobile;

    /**
     * 是否是秒杀抢购商品订单
     */
    private Boolean isFlashSaleGoods;

    /**
     * 用户终端token用于区分同一用户存在多端登陆的情况
     */
    private String terminalToken;

    /**
     * 是否是预售商品定金
     */
    @ApiModelProperty(value = "是否是预售商品定金")
    private Boolean isBookingSaleGoods = Boolean.FALSE;


    /**
     * 是否是尾款支付
     */
    @ApiModelProperty(value = "是否是尾款支付")
    private Boolean isBookingSaleTailGoods = Boolean.FALSE;

    /**
     * 尾款支付订单号
     */
    @ApiModelProperty(value = "尾款支付订单号")
    private String tid;

    /**
     * 推广人用户id
     */
    private Object promoteUserId;

    /**
     * 推广人用户id
     */
    private Object source;

    /**
     * 订单营销信息快照
     */
    @ApiModelProperty(value = "订单营销信息快照")
    private List<TradeMarketingDTO> tradeMarketingList;


    /**
     * 是否参加积分换购活动
     * 0-否 1-是
     */
    private Integer joinPointMarketing;

    /**
     * 埋点
     */
    private String emallSessionId;

    /**
     * 商品信息
     */
    private List<TradeItemDTO> tradeItems;

    /**
     * openId
     */
    private String openId;

    /**
     * 小程序订单场景1小程序2视频号
     */
    private Integer miniProgramScene;

    /**
     * 提交方式：小程序购物车下单
     */
    private boolean miniProgramCart;
}
