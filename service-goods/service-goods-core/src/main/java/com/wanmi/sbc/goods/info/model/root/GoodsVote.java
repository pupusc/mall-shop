package com.wanmi.sbc.goods.info.model.root;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "goods_vote")
public class GoodsVote {

    //spu id
    @Id
    @Column(name = "goods_id")
    private String goodsId;

    //投票次数
    @Column(name = "vote_number")
    private Long voteNumber;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    //是否删除
    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;
}
