package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: songhanlin
 * @Date: Created In 下午1:56 2017/11/2
 * @Description: 设置结算日期Request
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountDateModifyRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = 8999052734390625443L;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    @NotNull
    private Long storeId;

    /**
     * 结算日期
     */
    @ApiModelProperty(value = "结算日期")
    @Size(min = 1, max = 5)
    @NotEmpty
    private List<Long> days = new ArrayList<>();

    /**
     * 结算日期字符串
     */
    @ApiModelProperty(value = "结算日期字符串")
    private String accountDay;

}
