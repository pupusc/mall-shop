package com.wanmi.sbc.account.api.response.finance.record;

import com.wanmi.sbc.account.bean.vo.AccountRecordVO;
import com.wanmi.sbc.common.base.MicroServicePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>分页查询对账记录返回结构</p>
 * Created by of628-wenzhi on 2018-10-12-下午5:48.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRecordPageResponse implements Serializable {

    private static final long serialVersionUID = 756819778795328410L;

    /**
     * 分页对账记录数据结构
     */
    @ApiModelProperty(value = "分页对账记录数据结构")
    private MicroServicePage<AccountRecordVO> accountRecordVOPage;
}
