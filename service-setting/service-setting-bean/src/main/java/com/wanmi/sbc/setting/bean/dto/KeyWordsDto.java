package com.wanmi.sbc.setting.bean.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author zh
 * @Date 2023/2/11 15:21
 */
@Data
public class KeyWordsDto implements Serializable {
    private static final long serialVersionUID = -6910200208944439012L;

    private String id;

    private Integer sort;

    private String name;

}
