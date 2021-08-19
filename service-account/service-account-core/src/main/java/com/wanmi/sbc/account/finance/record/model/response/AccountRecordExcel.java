package com.wanmi.sbc.account.finance.record.model.response;

import lombok.Data;

import java.util.List;

@Data
public class AccountRecordExcel {
    /**
     * 需要导出的收入账单数据
     */
    List<AccountRecord> accountRecords;

    /**
     * 需要导出的收入汇总数据
     */
    List<AccountGather> accountGathers;

    /**
     * 导出文件的主题
     */
    String theme;

    /**
     * 需要导出的退款账单数据
     */
    List<AccountRecord> returnRecords;

    /**
     * 需要导出的退款汇总数据
     */
    List<AccountGather> returnGathers;
}
