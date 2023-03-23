package com.wanmi.sbc.setting.bean.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>轮播ci</p>
 * @author chenzhen
 * @date 2023-03-16 11:40:28
 */
@ApiModel
@Data
public class PresetSearchRotationVO implements Serializable {

    private String name;

    private Integer type = 2;

    private String page_url;

    private String spu_id;

    private String sku_id;

}
