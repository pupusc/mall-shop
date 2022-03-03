package com.wanmi.sbc.goods.bean.wx.vo;

import lombok.Data;

import java.util.Map;

@Data
public class WxGoodsVo {

    private Long id;

    private String goodsId;

    /**
     * 状态 0-等待上传 1-已上传 2-上架 3-下架
     */
    private Integer status;

    /**
     * 审核状态 0-未审核 1-审核中 2-审核失败 3-审核成功 4-取消审核
     */
    private Integer auditStatus;

    /**
     * 销售状态 0-不可售 1-可售
     */
    private Integer saleStatus;

    /**
     * 是否需要提交审核 0-不需要 1-需要审核 2-需要免审 3-审核通过后需要再审(审核中的时候商品有编辑)
     */
    private Integer needToAudit;

    /**
     * 审核失败原因
     */
    private String rejectReason;

    /**
     * 微信类目id
     */
    private Map<String, String> wxCategory;

    /**
     * spu名字
     */
    private String goodsName;

    /**
     * spu图片
     */
    private String goodsImg;

    /**
     * sku最低价格
     */
    private String marketPrice;

    /**
     * 提审时间
     */
    private String uploadTime;

    /**
     * 创建时间
     */
    private String createTime;

}
