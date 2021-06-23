package com.wanmi.sbc.setting.api.response.companyinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by feitingting on 2019/11/15.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoRopResponse implements Serializable {
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
    private Long provinceId;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private Long cityId;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private Long areaId;

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
