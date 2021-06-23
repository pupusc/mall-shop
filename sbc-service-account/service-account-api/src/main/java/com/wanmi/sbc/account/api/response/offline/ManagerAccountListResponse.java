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
 * 账号管理列表响应
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAccountListResponse implements Serializable {
    private static final long serialVersionUID = -7687909151681798388L;

    /**
     * 账号管理VO列表 {@link OfflineAccountVO}
     */
    @ApiModelProperty(value = "账号管理VO列表")
    private List<OfflineAccountVO> voList;
}
