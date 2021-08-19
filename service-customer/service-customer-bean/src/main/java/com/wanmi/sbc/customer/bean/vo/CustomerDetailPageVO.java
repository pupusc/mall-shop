package com.wanmi.sbc.customer.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 会员信息响应
 * Created by CHENLI on 2017/4/19.
 */
@ApiModel
@Data
public class CustomerDetailPageVO implements Serializable {
    private static final long serialVersionUID = 2269702455839540265L;
    /**
     * 会员分页
     */
    @ApiModelProperty(value = "会员分页")
    private List<CustomerDetailForPageVO> detailResponseList;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private Long total;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer currentPage;
}
