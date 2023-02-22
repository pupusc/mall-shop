package com.wanmi.sbc.setting.topicconfig.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description 楼层专栏商品
 * @Author zh
 * @Date  2023/2/7 14:47
 * @param: null
 * @return: null
 */
@Data
@Table(name = "topic_storey_column_content")
@Entity
public class TopicStoreyColumnContent {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "topic_storey_column_id")
    private Integer topicStoreySearchId;

    @Column(name = "topic_storey_id")
    private Integer topicStoreyId;

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

    @Column(name = "title")
    private String title;

    @Column(name = "recommend")
    private String recommend;

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "name")
    private String name;

    @Column(name = "publisher_id")
    private Integer publisherId;

    @Column(name = "publisher_name")
    private Integer publisherName;

    @Column(name = "num_txt")
    private String numTxt;

    @Column(name = "show_label_txt")
    private String showLabeTxt;

    @Column(name = "market_price")
    private BigDecimal marketPrice;

    @Column(name = "goods_status")
    private Integer goodsStatus;

    @Column(name = "image_id")
    private Long imageId;

}
