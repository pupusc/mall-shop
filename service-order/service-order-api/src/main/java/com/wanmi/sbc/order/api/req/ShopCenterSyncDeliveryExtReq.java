package com.wanmi.sbc.order.api.req;

import lombok.Data;

import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/22 8:31 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ShopCenterSyncDeliveryExtReq extends ShopCenterSyncDeliveryReq{

    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 价格信息
     */
    private Integer price;

    /**
     * 订单id
     */
    private Long orderId;



    /**
     * 发货订单id
     */
    private Long devDeliveryId;

    /**
     * 商品码
     */
    private String metaGoodsCode;

    /**
     * sku
     */
    private String metaSkuCode;

    /**
     * 回传回来的id
     */
    private String thirdOrderId;

    /**
     * 回传回来的 编码
     */
    private String thirdOrderNo;

    /**
     * 物流编号
     */
    private String expressCode;

    /**
     * 物流号
     */
    private String expressNo;


    /**
     * 状态 发货状态 0 未发货 1 部分发货 2全部发货
     */
    private Integer status;

    /**
     * 物流状态 1 物流 2不需要物流
     */
    private Integer logisticStatus;

    /**
     * 拦截状态 1未拦截 2拦截
     */
    private Integer interceptStatus;

    /**
     * 真实发货数量
     */
    private Integer actualNum;

    /**
     * 权益开始时间
     */
    private Date rightsBeginTime;


    /**
     * 权益结束时间
     */
    private Date rightsEndTime;
}
