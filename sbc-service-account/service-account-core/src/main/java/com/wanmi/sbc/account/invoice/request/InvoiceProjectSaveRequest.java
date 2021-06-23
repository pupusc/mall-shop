package com.wanmi.sbc.account.invoice.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 开票项目保存实体
 * Created by yuanlinling on 2017/4/25.
 */
@Data
public class InvoiceProjectSaveRequest implements Serializable {

    /**
     * 开票项目id
     */
    private String projectId;

    /**
     * 开票项目名称
     */
    private String projectName;

    /**
     * 公司信息ID
     */
    private Long companyInfoId;

    /**
     * 操作人
     */
    private String operatePerson;

}
