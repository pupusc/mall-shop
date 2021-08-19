package com.wanmi.sbc.account.api.response.offline;

import com.wanmi.sbc.account.bean.vo.OfflineAccountVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 线下账户获取响应
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class OfflineAccountGetByIdResponse extends OfflineAccountVO {
    private static final long serialVersionUID = -2646176070274251796L;
}
