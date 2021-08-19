package com.wanmi.sbc.account.bean.vo;

import lombok.Data;

import java.util.List;

@Data
public class AccountRecordExcelVO {
    /**
     * 需要导出的收入账单数据
     */
    List<AccountRecordVO> accountRecords;

    /**
     * 需要导出的收入汇总数据
     */
    List<AccountGatherVO> accountGathers;

    /**
     * 导出文件的主题
     */
    String theme;

    /**
     * 需要导出的退款账单数据
     */
    List<AccountRecordVO> returnRecords;

    /**
     * 需要导出的退款汇总数据
     */
    List<AccountGatherVO> returnGathers;
}
