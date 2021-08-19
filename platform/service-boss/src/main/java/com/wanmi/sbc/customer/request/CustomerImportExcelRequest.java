package com.wanmi.sbc.customer.request;

import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author minchen
 */
@Data
public class CustomerImportExcelRequest implements Serializable {

    private static final long serialVersionUID = -9118397028139700890L;

    @NotBlank
    private String ext;

    /**
     * 是否发送短信
     */
    private Boolean sendMsgFlag;

    /**
     * 操作员id
     */
    private String userId;
}
