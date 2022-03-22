package com.wanmi.sbc.goods.mini.model.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsEditStatus;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_wx_goods")
public class WxGoodsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "goods_id")
    private String goodsId;

    @Column(name = "platform_product_id")
    private Long platformProductId;

    //状态
    @Column(name = "status", columnDefinition = "tinyint")
    @Enumerated
    private WxGoodsStatus status;

    //审核状态
    @Column(name = "audit_status", columnDefinition = "tinyint")
    @Enumerated
    private WxGoodsEditStatus auditStatus;

    //微信类目id
    @Column(name = "wx_category")
    private String wxCategory;

    //是否需要审核 0-不需要审核 1-需要审核 2-需要免审 3-审核通过后需要再审(审核中的时候商品有编辑)
    @Column(name = "need_to_audit")
    private Integer needToAudit;

    //审核失败原因
    @Column(name = "reject_reason")
    private String rejectReason;

    //提审时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * isbn图片
     */
    @Column(name = "isbn_img")
    private String isbnImg;

    /**
     * 出版社图片
     */
    @Column(name = "publisher_img")
    private String publisherImg;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;
}
