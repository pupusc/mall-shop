package com.wanmi.sbc.goods.mini.model.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsEditStatus;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_wx_goods")
public class WxGoodsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "goods_id")
    private String goodsId;

    @Column(name = "goods_info_id")
    private String goodsInfoId;

    //状态
    @Column(name = "status", columnDefinition = "tinyint")
    @Enumerated
    private WxGoodsStatus status;

    //审核状态
    @Column(name = "edit_status", columnDefinition = "tinyint")
    @Enumerated
    private WxGoodsEditStatus editStatus;

    //微信类目id
    @Column(name = "wx_category")
    private Integer wxCategory;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "del_flag")
    @Enumerated
    private DeleteFlag delFlag;
}
