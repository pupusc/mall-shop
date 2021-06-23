package com.wanmi.sbc.customer.api.response.level;

import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 客户等级分页
 * @Author: daiyitian
 * @Date: Created In 上午11:38 2017/11/14
 * @Description: 公司信息Response
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerLevelDefaultResponse extends CustomerLevelVO implements Serializable {

    private static final long serialVersionUID = 6492765528117007884L;

}
