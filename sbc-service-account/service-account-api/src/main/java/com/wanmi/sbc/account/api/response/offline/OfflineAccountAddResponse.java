package com.wanmi.sbc.account.api.response.offline;

import com.wanmi.sbc.account.bean.vo.OfflineAccountVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 线下账户新增响应
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class OfflineAccountAddResponse extends OfflineAccountVO {
    private static final long serialVersionUID = -4623469727342576285L;
}
