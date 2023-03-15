package com.wanmi.sbc.setting.bean.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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

    private static final long serialVersionUID = 5071840004503566922L;

    private String spuId;

    private String skuId;

    private String referrer;

    private String referrerTitle;

    private String goodsName;

    private String image;

    private String score;

    private String recommend;

    private String recommendName;

    private BigDecimal retailPrice;

    private BigDecimal paidCardPrice;

    private String discount;

    private String listMessage;

    private List<String> tags;

    private List<TagsDto> labelId;

    private String isbn;
}
