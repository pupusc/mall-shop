package com.wanmi.sbc.setting.topicconfig.model.root;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "topic_storey")
@Entity
public class TopicStorey {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "topic_id")
    private Integer topicId;

    @Column(name = "name")
    private String name;

    @Column(name = "navigation_name")
    private String navigationName;

    @Column(name = "storey_type")
    private Integer storeyType;

    @Column(name = "image_url")
    private String imageUrl;

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

    @Column(name = "background")
    private String background;

    @Column(name = "has_padding")
    private Integer hasPadding;

    @Column(name = "water_fall_type")
    private Integer waterFallType;

}
