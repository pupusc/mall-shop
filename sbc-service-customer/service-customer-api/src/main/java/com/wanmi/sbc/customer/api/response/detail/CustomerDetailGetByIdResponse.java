package com.wanmi.sbc.customer.api.response.detail;

import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 根据Id查询明细信息响应
 * @Author: daiyitian
 * @Date: Created In 上午11:38 2017/11/14
 * @Description: 公司信息Response
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerDetailGetByIdResponse extends CustomerDetailVO implements Serializable {

    private static final long serialVersionUID = -244242267773354170L;
}
