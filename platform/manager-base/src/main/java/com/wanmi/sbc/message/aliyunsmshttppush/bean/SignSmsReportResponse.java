package com.wanmi.sbc.message.aliyunsmshttppush.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName SignSmsReportResponse
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/12/9 14:55
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignSmsReportResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 签名来源
     */
    private String sign_source;

    /**
     * 审核未通过原因
     */
    private String reason;

    /**
     * 签名名称
     */
    private String sign_name;

    /**
     * 短信签名审核工单号
     */
    private String orderId;

    /**
     * 签名审核状态，包括：
     * approving：审核中
     * approved：审核通过
     * rejected：审核未通过
     */
    private String sign_status;

    /**
     * 申请说明
     */
    private String remark;

    /**
     * 适用场景
     */
    private String sign_scene;

    /**
     * 短信签名创建日期和时间
     */
    private String create_date;

}
