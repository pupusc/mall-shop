package com.wanmi.sbc.account.api.response.finance.record;

import com.wanmi.sbc.account.bean.vo.AccountDetailsVO;
import com.wanmi.sbc.common.base.MicroServicePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>对账明细分页返回结构</p>
 * Created by of628-wenzhi on 2018-10-13-下午2:20.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailsPageResponse implements Serializable{

    private static final long serialVersionUID = 4885328781398300086L;

    /**
     * 对账明细分页列表 {@link AccountDetailsVO}
     */
    @ApiModelProperty(value = "对账明细分页列表")
    private MicroServicePage<AccountDetailsVO> accountDetailsVOPage;
}
