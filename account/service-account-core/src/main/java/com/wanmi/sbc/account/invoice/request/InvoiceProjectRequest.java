package com.wanmi.sbc.account.invoice.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 开票项目请求参数
 * Created by yuanlinling on 2017/4/25.
 */
@Data
public class InvoiceProjectRequest implements Serializable {

    /**
     * 开票项目id
     */
    private String projectId;

}
