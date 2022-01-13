package com.wanmi.sbc.goods.bean.vo;

import lombok.Data;

@Data
public class GoodsVoteVo {

    private String goodsId;

    private String goodsName;

    private Long voteNumber;

    private String image;

    private Boolean detailPage = false;
}
