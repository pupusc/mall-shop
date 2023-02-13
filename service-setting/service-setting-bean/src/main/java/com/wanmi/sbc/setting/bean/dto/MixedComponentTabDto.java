package com.wanmi.sbc.setting.bean.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author zh
 * @Date 2023/2/10 10:29
 */
@Data
@ApiModel
public class MixedComponentTabDto implements Serializable {
    private static final long serialVersionUID = 1817873869725056066L;

    private Integer id;

    private String name;

    private String subName;

    private String selectedColor;

    private String unSelectedColor;

    private String selectedImage;

    private String unSelectedImage;

    private MixedComponentKeyWordsDto mixedComponentKeyWord;
}
