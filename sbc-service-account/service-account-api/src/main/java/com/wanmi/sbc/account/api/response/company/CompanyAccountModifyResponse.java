package com.wanmi.sbc.account.api.response.company;

import com.wanmi.sbc.account.bean.vo.CompanyAccountVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商家收款修改账户响应
 * Created by daiyitian on 2017/11/30.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyAccountModifyResponse extends CompanyAccountVO implements Serializable {

    private static final long serialVersionUID = -2629974480473851524L;
}
