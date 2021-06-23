package com.wanmi.sbc.account.api.response.company;

import com.wanmi.sbc.account.bean.vo.CompanyAccountVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 根据accountID查询商家收款账户信息返回结果
 *
 * @author chenli
 * @date 2018/12/13
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyAccountFindResponse extends CompanyAccountVO implements Serializable {

    private static final long serialVersionUID = 1369268585909596726L;
}
