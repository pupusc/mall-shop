package com.wanmi.sbc.goods.mini.model.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wanmi.sbc.common.enums.DeleteFlag;
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
        wxLiveAssistantModel.setTheme(wxLiveAssistantCreateRequest.getTheme());
        wxLiveAssistantModel.setStartTime(LocalDateTime.parse(wxLiveAssistantCreateRequest.getStartTime(), df));
        wxLiveAssistantModel.setEndTime(LocalDateTime.parse(wxLiveAssistantCreateRequest.getEndTime(), df));
        wxLiveAssistantModel.setGoodsCount(0);
        LocalDateTime now = LocalDateTime.now();
        wxLiveAssistantModel.setCreateTime(now);
        wxLiveAssistantModel.setUpdateTime(now);
        wxLiveAssistantModel.setDelFlag(DeleteFlag.NO);
        return wxLiveAssistantModel;
    }
}
