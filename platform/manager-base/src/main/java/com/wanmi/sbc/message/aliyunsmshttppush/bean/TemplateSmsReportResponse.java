package com.wanmi.sbc.message.aliyunsmshttppush.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName TemplateSmsReportResponse
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/12/9 15:05
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateSmsReportResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 模板类型
     */
    private String template_type;

    /**
     * 审核未通过原因
     */
    private String reason;

    /**
     * 模板名称
     */
    private String template_name;

    /**
     * 短信模板审核工单号
     */
    private String order_id;

    /**
     * 模板内容
     */
    private String template_content;

    /**
     * 模板审核状态：
     * approving：审核中
     * approved：审核通过
     * rejected：审核未通过
     */
    private String template_status;

    /**
     * 申请说明
     */
    private String remark;

    /**
     * 短信模板CODE
     */
    private String template_code;

    /**
     * 短信模板创建日期和时间
     */
    private String create_date;
}
