package com.wanmi.sbc.customer.api.request.employee;

import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

import static com.wanmi.sbc.common.util.ValidateUtil.BLANK_EX_MESSAGE;
import static com.wanmi.sbc.common.util.ValidateUtil.NULL_EX_MESSAGE;

/**
 * @Author: songhanlin
 * @Date: Created In 下午5:30 2017/11/3
 * @Description: 启用/禁用 账号
 */

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDisableByIdRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 1486055218070650462L;

    /**
     * 商家Id
     */
    @ApiModelProperty(value = "商家Id")
    private String employeeId;

    /**
     * 账号状态 0:启用 1:禁用
     */
    @ApiModelProperty(value = "账号状态")
    private AccountState accountState;

    /**
     * 禁用原因
     */
    @ApiModelProperty(value = "禁用原因")
    @CanEmpty
    private String accountDisableReason;


    @Override
    public void checkParam() {
        //商家Id不能为空
        if (Objects.isNull(employeeId)) {
            Validate.notNull(employeeId, NULL_EX_MESSAGE, "companyInfoId");
        }

        //账号状态状态不能为空
        if (Objects.isNull(accountState)) {
            Validate.notNull(accountState, NULL_EX_MESSAGE, "accountState");
        }
        //如果是关店状态
        else if (accountState.toValue() == AccountState.DISABLE.toValue()) {
            //关店原因不能为空
            if (StringUtils.isBlank(accountDisableReason)) {
                Validate.notBlank(accountDisableReason, BLANK_EX_MESSAGE, "accountDisableReason");
            }
            //原因长度为1-100以内
            else if (!ValidateUtil.isBetweenLen(accountDisableReason, 1, 100)) {
                Validate.validState(Boolean.FALSE);
            }
        }
    }

}
