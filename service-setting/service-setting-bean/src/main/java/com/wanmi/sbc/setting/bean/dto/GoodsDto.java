package com.wanmi.sbc.setting.bean.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author zh
 * @Date 2023/2/14 10:45
 */
@Data
@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDto implements Serializable {

    private String spuId;

    private String goodsName;

    private static final long serialVersionUID = 5071840004503566922L;
}
