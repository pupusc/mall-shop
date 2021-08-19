package com.wanmi.sbc.customer.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author minchen
 */
@Data
public class CustomerImportExcelResponse implements Serializable {


    private static final long serialVersionUID = -5138285440007506347L;

    /**
     * 短信发送成功条数
     */
    private Integer sendMsgSuccessCount;

    /**
     * 短信发送失败条数
     */
    private Integer sendMsgFailedCount;


}
