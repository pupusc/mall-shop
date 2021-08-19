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
 * 线下账户获取响应
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineAccountListByConditionResponse implements Serializable {

    private static final long serialVersionUID = -5487132605041121900L;

    /**
     * 线下账户VO列表 {@link OfflineAccountVO}
     */
    @ApiModelProperty(value = "线下账户VO列表")
    private List<OfflineAccountVO> voList;
}
