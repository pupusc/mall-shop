package com.wanmi.sbc.setting.topicconfig.model.root;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "topic_rank_relation")
@Entity
public class TopicRankRelation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "p_rank_colum_id")
    private Integer PRankColumId;

    @Column(name = "p_rank_id")
    private Integer PRankId;

    @Column(name = "c_rank_id")
    private Integer CRankId;

    @Column(name = "c_rank_name")
    private String CRankName;

    @Column(name = "c_rank_sorting")
    private Integer CRankSorting;

    @Column(name = "topic_rank_sorting")
    private Integer topicRankSorting;

    @Column(name = "start_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endTime;

    @Column(name = "del_flag")
    private Integer delFlag;
}
