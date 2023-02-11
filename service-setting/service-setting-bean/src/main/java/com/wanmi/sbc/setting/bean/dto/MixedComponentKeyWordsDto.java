package com.wanmi.sbc.setting.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: TODO
 * @Author zh
 * @Date 2023/2/10 11:03
 */
@Data
public class MixedComponentKeyWordsDto implements Serializable  {
    private static final long serialVersionUID = 347615824584051811L;

    private String keyWordSelectedColor;

    private String keyWordUnSelectedColor;

    private List<KeyWordDto> keyWord;
}
