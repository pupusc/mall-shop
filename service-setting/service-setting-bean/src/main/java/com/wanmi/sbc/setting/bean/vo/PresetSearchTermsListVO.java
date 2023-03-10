package com.wanmi.sbc.setting.bean.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PresetSearchTermsListVO implements Serializable {
    private List<PresetSearchTermsVO> presetSearchTermsVOList;
}
