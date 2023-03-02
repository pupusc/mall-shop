package com.wanmi.sbc.setting.bean.dto;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    private Integer sorting;

    private List<TagsDto> labelId;

    private List<GoodsDto> goods;
    public MixedComponentContentDto() {
    }

    public MixedComponentContentDto(MixedComponentTabDto mixedComponentTabDto, List<GoodsDto> goods) {
        if (mixedComponentTabDto.getBookType() == null) {return;}
        this.name = mixedComponentTabDto.getName();
        this.type = mixedComponentTabDto.getBookType();
        this.recommend = mixedComponentTabDto.getRecommend();
        this.goods = goods;
        switch (mixedComponentTabDto.getBookType()) {
            case 3: //视频
                this.image = JSON.parseObject(mixedComponentTabDto.getAttributeInfo()).get("image").toString();
                this.video = JSON.parseObject(mixedComponentTabDto.getAttributeInfo()).get("video").toString();
                break;
            case 5: //指定内容
                this.titleImage = JSON.parseObject(mixedComponentTabDto.getAttributeInfo()).get("titleImage").toString();
                this.image = JSON.parseObject(mixedComponentTabDto.getAttributeInfo()).get("image").toString();
                break;
            default:
                break;
        }
    }
}
