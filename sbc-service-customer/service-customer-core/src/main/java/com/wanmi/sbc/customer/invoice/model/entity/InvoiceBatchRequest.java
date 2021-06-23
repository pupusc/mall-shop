package com.wanmi.sbc.customer.invoice.model.entity;


import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangjin on 2017/5/4.
 */
@Data
public class InvoiceBatchRequest extends CustomerBaseRequest implements Serializable {

    private static final long serialVersionUID = 2945115959411738875L;
    /**
     * 专票ids
     */
    private List<Long> customerInvoiceIds;

    /**
     * 审核状态
     */
    private CheckState checkState;

    /**
     * 增票id
     */
    private Long customerInvoiceId;

    /**
     * 审核未通过原因
     */
    private String rejectReason;
}
