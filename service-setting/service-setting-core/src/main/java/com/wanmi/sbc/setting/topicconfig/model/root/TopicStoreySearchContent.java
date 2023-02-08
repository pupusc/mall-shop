package com.wanmi.sbc.setting.topicconfig.model.root;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @Description 楼层专栏商品
 * @Author zh
 * @Date  2023/2/7 14:47
 * @param: null
 * @return: null
 */
@Data
@Table(name = "topic_storey_search_content")
@Entity
public class TopicStoreySearchContent {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "topic_storey_search_id")
    private Integer topicStoreySearchId;

    @Column(name = "topic_storey_search_table")
    private String topicStoreySearchTable;

    @Column(name = "spu_no")
    private String spuNo;

    @Column(name = "sku_no")
    private String skuNo;

    @Column(name = "type")
    private Boolean type;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "sorting")
    private Integer sorting;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "goods_name")
    private String goodsName;

    @Column(name = "spu_id")
    private String spuId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "attribute_info")
    private String attributeInfo;
}
