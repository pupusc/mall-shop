package com.wanmi.sbc.goods.mini.model.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantCreateRequest;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantGoodsCreateRequest;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "t_wx_live_assistant_goods")
public class WxLiveAssistantGoodsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "assist_id")
    private Long assistId;

    @Column(name = "goods_id")
    private String goodsId;

//    @Column(name = "price")
//    private BigDecimal price;
//
//    @Column(name = "stock")
//    private Integer stock;

    /**
     * 添加助手前的商品价格，便于在直播结束后恢复原先的值，以json形式存储
     */
    @Column(name = "old_goods_info")
    private String oldGoodsInfo;

    /**
     * 添加助手前的库存同步标识，直播结束后恢复原值
     */
    @Column(name = "olf_sync_stock_flag")
    private String olfSyncStockFlag;

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

    public static List<WxLiveAssistantGoodsModel> create(WxLiveAssistantGoodsCreateRequest wxLiveAssistantGoodsCreateRequest) {
        List<WxLiveAssistantGoodsModel> assistantGoodsModels = new ArrayList<>();
        List<String> goodsIds = wxLiveAssistantGoodsCreateRequest.getGoods();
        for (String goodId : goodsIds) {
            WxLiveAssistantGoodsModel wxLiveAssistantGoodsModel = new WxLiveAssistantGoodsModel();
            wxLiveAssistantGoodsModel.setAssistId(wxLiveAssistantGoodsCreateRequest.getAssistantId());
            wxLiveAssistantGoodsModel.setGoodsId(goodId);
            LocalDateTime now = LocalDateTime.now();
            wxLiveAssistantGoodsModel.setCreateTime(now);
            wxLiveAssistantGoodsModel.setUpdateTime(now);
            wxLiveAssistantGoodsModel.setDelFlag(DeleteFlag.NO);
            assistantGoodsModels.add(wxLiveAssistantGoodsModel);
        }
        return assistantGoodsModels;
    }
}
