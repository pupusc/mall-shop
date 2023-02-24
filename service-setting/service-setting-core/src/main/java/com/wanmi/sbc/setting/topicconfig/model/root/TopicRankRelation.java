package com.wanmi.sbc.setting.topicconfig.model.root;


import lombok.Data;

import javax.persistence.*;

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
}
