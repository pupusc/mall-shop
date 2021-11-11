package com.wanmi.sbc.setting.topicconfig.model.root;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "topic")
@Entity
public class Topic {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "topic_name")
    private String topicName;
    @Column(name = "link_url")
    private String linkUrl;
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    @Column(name = "deleted")
    private Integer deleted;
    @Column(name = "topic_key")
    private String topicKey;
    @Column(name = "track_key")
    private  String trackKey;
    @Column(name = "background")
    private String background;

}
