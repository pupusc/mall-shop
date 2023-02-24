package com.wanmi.sbc.setting.topicconfig.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "topic_storey_column")
@Entity
public class TopicStoreyContent {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "topic_id")
    private Integer topicId;

    @Column(name = "storey_id")
    private Integer storeyId;

    @Column(name = "sku_no")
    private String skuNo;

    @Column(name = "spu_no")
    private String spuNo;

    @Column(name = "sku_id")
    private String skuId;

    @Column(name = "type")
    private Integer type;

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
