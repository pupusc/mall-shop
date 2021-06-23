package com.wanmi.sbc.account.api.response.offline;

import com.wanmi.sbc.account.api.request.AccountBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 线下账号删除响应
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineAccountDeleteByIdResponse extends AccountBaseRequest {
    private static final long serialVersionUID = -2079386078029744910L;

    /**
     * 影响行数
     */
    @ApiModelProperty(value = "影响行数")
    private int count;
}
