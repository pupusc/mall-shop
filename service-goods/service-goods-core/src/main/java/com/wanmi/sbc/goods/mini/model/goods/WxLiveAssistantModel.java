package com.wanmi.sbc.goods.mini.model.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.HasAssistantGoodsValidEnum;
import com.wanmi.sbc.goods.bean.wx.request.assistant.WxLiveAssistantCreateRequest;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "t_wx_live_assistant")
public class WxLiveAssistantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "theme")
    private String theme;

    @Column(name = "goods_count")
    private Integer goodsCount;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 是否直播计划商品有效0 不同步 1同步
     */
    @Column(name = "has_assistant_goods_valid")
    private Integer hasAssistantGoodsValid;

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

    public static WxLiveAssistantModel create(WxLiveAssistantCreateRequest wxLiveAssistantCreateRequest){
        WxLiveAssistantModel wxLiveAssistantModel = new WxLiveAssistantModel();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.parse(wxLiveAssistantCreateRequest.getStartTime(), df);
        LocalDateTime endTime = LocalDateTime.parse(wxLiveAssistantCreateRequest.getEndTime(), df);
        if(endTime.isBefore(startTime)) throw new SbcRuntimeException("开始时间不能大于结束时间");
        if(endTime.isBefore(now)) throw new SbcRuntimeException("结束时间不能小于现在");
        wxLiveAssistantModel.setTheme(wxLiveAssistantCreateRequest.getTheme());
        wxLiveAssistantModel.setStartTime(startTime);
        wxLiveAssistantModel.setEndTime(endTime);
        wxLiveAssistantModel.setGoodsCount(0);
        wxLiveAssistantModel.setHasAssistantGoodsValid(HasAssistantGoodsValidEnum.NO_SYNC.getCode());

        wxLiveAssistantModel.setCreateTime(now);
        wxLiveAssistantModel.setUpdateTime(now);
        wxLiveAssistantModel.setDelFlag(DeleteFlag.NO);
        return wxLiveAssistantModel;
    }
}
