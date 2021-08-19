package com.wanmi.ares.request.mq;

import com.wanmi.ares.base.BaseMqRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 商家基础信息
 * Created by sunkun on 2017/9/21.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class CompanyInfoRequest extends BaseMqRequest {

    private static final long serialVersionUID = -1125512098500345955L;

    /**
     * 名称
     */
    private String name;

    /**
     * 省
     */
    private Long provinceId;

    /**
     * 市
     */
    private Long cityId;

    /**
     * 区
     */
    private Long areaId;

    /**
     * 详细地址
     */
    private String detailAddress;

}
