package com.wanmi.sbc.setting.topicconfig.model.root;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "topic_storey_content")
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

    @Column(name = "sku_d")
    private String skuId;

    @Column(name = "type")
    private Integer type;


    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link_url")
    private String linkUrl;


    @Column(name = "status")
    private Integer status;

    @Column(name = "sorting")
    private Integer sorting;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "deleted")
    private Integer deleted;
}
