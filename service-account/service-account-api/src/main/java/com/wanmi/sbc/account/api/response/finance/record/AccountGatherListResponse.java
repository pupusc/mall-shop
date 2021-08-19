package com.wanmi.sbc.account.api.response.finance.record;

import com.wanmi.sbc.account.bean.vo.AccountGatherVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>对账列表汇总response</p>
 * Created by of628-wenzhi on 2018-10-13-下午2:10.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountGatherListResponse implements Serializable{

    private static final long serialVersionUID = -80743640133272767L;

    /**
     * 对账汇总列表 {@link AccountGatherVO}
     */
    @ApiModelProperty(value = "对账汇总列表")
    private List<AccountGatherVO> accountGatherVOList;
}
