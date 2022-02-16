package com.wanmi.sbc.goods.mini.model.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wanmi.sbc.goods.mini.enums.review.WxReviewResult;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_wx_review_log")
public class WxReviewLogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //审核结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "reviewed_time")
    private LocalDateTime reviewedTime;

    //审核结果
    @Column(name = "review_result", columnDefinition = "tinyint")
    @Enumerated
    private WxReviewResult reviewResult;

    //审核说明
    @Column(name = "review_reason")
    private String reviewReason;

    //关联的实体的id
    @Column(name = "relate_id")
    private Long relateId;
}
