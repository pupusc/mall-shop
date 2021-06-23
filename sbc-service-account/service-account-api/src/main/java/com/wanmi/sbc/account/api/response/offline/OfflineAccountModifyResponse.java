package com.wanmi.sbc.account.api.response.offline;

import com.wanmi.sbc.account.bean.vo.OfflineAccountVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 线下账户修改响应
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class OfflineAccountModifyResponse extends OfflineAccountVO {
    private static final long serialVersionUID = -8471402980714657134L;
}
