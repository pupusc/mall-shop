package com.wanmi.sbc.account.api.request.customerdrawcash;

import com.wanmi.sbc.account.bean.enums.CheckState;
import lombok.*;

/**
 * 提现管理页面查询对象
 */
@Data
public class CustomerDrawCashSearchPageRequest extends CustomerDrawCashPageRequest{


    private String drawCashId;


    private String customerOption;


    private String customerOptionValue;


    private String drawCashOption;


    private String drawCashOptionValue;


    private CheckState checkState;



}
