package com.wanmi.sbc.goods.api.response.booklistmodel;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Table(name = "topic_storey_Search")
@Entity
public class RankGoodsPublishTempResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 控件id
     */
    @Column(name = "choose_rule_id")
    private Integer chooseRuleId;

    /**
     * 书单模板id或者书单类目id
     */
    @Column(name = "book_list_id")
    private Integer bookListId;

    /**
     * 分类 1书单模板 2类目
     */
    @Column(name = "category")
    private Integer category;

    /**
     *
     */
    @Column(name = "spu_id")
    private String spuId;

    @Column(name = "spu_no")
    private String spuNo;

    @Column(name = "name")
    private String name;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "sku_no")
    private String skuNo;

    @Column(name = "erp_goods_no")
    private String erpGoodsNo;

    @Column(name = "erp_goods_info_no")
    private String erpGoodsInfoNo;

    @Column(name = "order_num")
    private Integer orderNum;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "del_flag")
    private Integer delFlag;

    @Column(name = "sale_num")
    private Integer saleNum;

    @Column(name = "rank_text")
    private String rankText;
}
