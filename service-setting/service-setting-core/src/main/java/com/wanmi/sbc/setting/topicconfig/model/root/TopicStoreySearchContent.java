package com.wanmi.sbc.setting.topicconfig.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
    private Integer type;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "sorting")
    private Integer sorting;

    @Column(name = "update_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;

    @Column(name = "create_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "goods_name")
    private String goodsName;

    @Column(name = "spu_id")
    private String spuId;

    @Column(name = "start_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endTime;

    @Column(name = "attribute_info")
    private String attributeInfo;

    @Column(name = "num")
    private Integer num;

    @Column(name = "p_id")
    private Integer pId;

    @Column(name = "level")
    private Integer level;

    @Column(name = "title")
    private String title;

    @Column(name = "recommend")
    private String recommend;
}
