package com.wanmi.sbc.goods.bean.wx.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WxGoodsVo {

    private Long id;

    private String goodsId;

    //状态
    private Integer status;

    //审核状态
    private Integer auditStatus;

    //审核通过次数
    private Integer auditTimes;

    //微信类目id
    private Integer wxCategory;

    //提审时间
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime uploadTime;

}
