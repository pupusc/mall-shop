package com.wanmi.sbc.account.api.response.offline;

import com.wanmi.sbc.account.bean.vo.OfflineAccountVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 有效线下账户列表响应
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidAccountListResponse implements Serializable {
    private static final long serialVersionUID = -4325578191051200128L;

    /**
     * 线下账户列表 {@link OfflineAccountVO}
     */
    @ApiModelProperty(value = "线下账户列表")
    private List<OfflineAccountVO> voList;
}
