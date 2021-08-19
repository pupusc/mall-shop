package com.wanmi.sbc.account.api.response.customerdrawcash;

import com.wanmi.sbc.account.bean.vo.CustomerDrawCashVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>会员提现管理修改结果</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDrawCashModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的会员提现管理信息
     */
    @ApiModelProperty(value = "已修改的会员提现管理信息")
    private CustomerDrawCashVO customerDrawCashVO;
}
