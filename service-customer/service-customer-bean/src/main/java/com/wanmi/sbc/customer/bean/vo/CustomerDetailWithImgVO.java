package com.wanmi.sbc.customer.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 客户详细信息
 * Created by CHENLI on 2017/4/13.
 */
@ApiModel
@Data
public class CustomerDetailWithImgVO implements Serializable {

    private static final long serialVersionUID = -4567833252052862839L;


    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;


    @ApiModelProperty(value = "头像路径")
    private String headimgurl;

    /**
     * 考虑到后面可能会有很多类似“企业会员”的标签，用List存放标签内容
     */
    @ApiModelProperty(value = "会员标签")
    private List<String> customerLabelList = new ArrayList<>();
}
