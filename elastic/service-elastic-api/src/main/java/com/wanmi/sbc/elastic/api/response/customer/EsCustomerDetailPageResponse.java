package com.wanmi.sbc.elastic.api.response.customer;

import com.wanmi.sbc.elastic.bean.vo.customer.EsCustomerDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 会员详情查询参数
 * Created by CHENLI on 2017/4/19.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsCustomerDetailPageResponse implements Serializable {

    private static final long serialVersionUID = -1281379836937760934L;

    /**
     * 会员分页
     */
    @ApiModelProperty(value = "会员分页")
    private List<EsCustomerDetailVO> detailResponseList;

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
