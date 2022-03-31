package com.soybean.mall.order.miniapp.model.root;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.soybean.mall.order.api.request.order.WxMiniProgramCallbackRequest;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_mini_program_callback")
public class WxMiniProgramCallbackModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "param")
    private String param;

    @Column(name = "content")
    private String content;

    /**
     * 状态 0-处理中 1-异常 2-已完成
     */
    @Column(name = "status")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "create_time")
    private LocalDateTime createTime;

    public static WxMiniProgramCallbackModel fromCreateRequest(WxMiniProgramCallbackRequest wxMiniProgramCallbackRequest){
        WxMiniProgramCallbackModel wxMiniProgramCallbackModel = new WxMiniProgramCallbackModel();
        LocalDateTime now = LocalDateTime.now();
        wxMiniProgramCallbackModel.setParam(wxMiniProgramCallbackRequest.getParam());
        wxMiniProgramCallbackModel.setContent(wxMiniProgramCallbackRequest.getContent());
        wxMiniProgramCallbackModel.setCreateTime(now);
        wxMiniProgramCallbackModel.setUpdateTime(now);
        wxMiniProgramCallbackModel.setStatus(wxMiniProgramCallbackRequest.getStatus());
        return wxMiniProgramCallbackModel;
    }
}
