package com.wanmi.sbc.account.api.response.finance.record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对账数据导出Excel 返回对象
 * @author CHENLI
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRecordToExcelResponse {
    /**
     * base64位字符串形式的文件流
     */
    @ApiModelProperty(value = "base64位字符串形式的文件流")
    private String file;
}
