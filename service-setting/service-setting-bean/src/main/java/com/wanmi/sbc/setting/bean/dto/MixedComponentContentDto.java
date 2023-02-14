package com.wanmi.sbc.setting.bean.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: TODO
 * @Author zh
 * @Date 2023/2/10 10:29
 */
@Data
@ApiModel
public class MixedComponentContentDto implements Serializable {

    private static final long serialVersionUID = 2588838972751435906L;

    private String name;

    private String recommend;

    private Integer type;

    private String image;

    private String titleImage;

    private String video;

    private String url;

    private List<GoodsDto> goods;

}
