package com.wanmi.sbc.customer.api.response.detail;

import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailBaseVO;
import com.wanmi.sbc.customer.bean.vo.CustomerStatusBaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;


/**
 * @Author yangzhen
 * @Description //根据customerid查询会员是否被禁用以及禁用原因
 * @Date 11:33 2020/11/27
 * @Param
 * @return
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailStatusResponse implements Serializable {

    private static final long serialVersionUID = -7395969401233896552L;

    /**
     * 会员详情基础数据集合
     */
    @ApiModelProperty(value = "会员详情基础数据集合")
    private List<CustomerStatusBaseVO> list;


}
