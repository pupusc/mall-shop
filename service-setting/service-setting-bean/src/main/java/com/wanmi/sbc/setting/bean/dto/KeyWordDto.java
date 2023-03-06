package com.wanmi.sbc.setting.bean.dto;

import com.wanmi.sbc.common.base.MicroServicePage;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: TODO
 * @Author zh
 * @Date 2023/2/11 15:21
 */
@Data
public class KeyWordDto implements Serializable {
    private static final long serialVersionUID = -6910200208944439012L;

    private String id;
    private String name;

    private MicroServicePage<MixedComponentContentDto> mixedComponentContentPage;

    public KeyWordDto(String id ,String name) {
        this.id = id;
        this.name = name;
    }

    public KeyWordDto() {
    }
}
