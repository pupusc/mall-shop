package com.wanmi.sbc.setting.topicconfig.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @Description 楼层专栏
 * @Author zh
 * @Date  2023/2/7 14:47
 * @param: null
 * @return: null
 */
@Data
@Table(name = "topic_storey_Search")
@Entity
public class TopicStoreySearch {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "topic_storey_id")
    private Integer topicStoreyId;

    @Column(name = "name")
    private String name;

    @Column(name = "sub_name")
    private String subName;

    @Column(name = "cate_id")
    private String cateId;

    @Column(name = "brand_id")
    private String brandId;

    @Column(name = "label_id")
    private String labelId;

    @Column(name = "num")
    private Integer num;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "show_type")
    private Integer showType;

    @Column(name = "book_type")
    private Integer bookType;

    @Column(name = "p_id")
    private Integer pId;

    @Column(name = "level")
    private Integer level;

    @Column(name = "color")
    private String color;

    @Column(name = "image")
    private String image;

    @Column(name = "relation_store_id")
    private Integer relationStroeId;

}
