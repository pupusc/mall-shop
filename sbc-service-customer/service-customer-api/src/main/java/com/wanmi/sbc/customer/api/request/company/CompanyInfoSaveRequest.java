package com.wanmi.sbc.customer.api.request.company;

import com.wanmi.sbc.common.annotation.CanEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公司信息参数
 * Created by CHENLI on 2017/5/12.
 */
@ApiModel
@Data
public class CompanyInfoSaveRequest implements Serializable {

    private static final long serialVersionUID = 2786039114279422918L;
    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private Long companyInfoId;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    @CanEmpty
    private Long provinceId;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    @CanEmpty
    private Long cityId;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    @CanEmpty
    private Long areaId;

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private Long streetId;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String detailAddress;

    /**
     * 联系人名字
     */
    @ApiModelProperty(value = "联系人名字")
    private String contactName;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactPhone;

    /**
     * 版权信息
     */
    @ApiModelProperty(value = "版权信息")
    private String copyright;

    /**
     * 公司简介
     */
    @ApiModelProperty(value = "公司简介")
    private String companyDescript;
}
