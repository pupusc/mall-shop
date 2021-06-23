package com.wanmi.sbc.account.api.response.company;

import com.wanmi.sbc.account.bean.vo.CompanyAccountVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商家收款新增账户响应
 * Created by daiyitian on 2017/11/30.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyAccountAddResponse extends CompanyAccountVO implements Serializable {

    private static final long serialVersionUID = 7309000250063986312L;
}
