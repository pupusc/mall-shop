package com.wanmi.sbc.customer.api.response.employee;

import com.wanmi.sbc.customer.bean.vo.RoleInfoVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/11 9:26
 * @Version: 1.0
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleInfoAddResponse extends RoleInfoVO implements Serializable{
    private static final long serialVersionUID = 3085866087729790417L;
}
