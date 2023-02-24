package com.wanmi.sbc.setting.api.response.mixedcomponentV2;

import com.wanmi.sbc.setting.bean.dto.MixedComponentDto;
import lombok.Data;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class TopicStoreyMixedComponentResponse implements Serializable {

    private static final long serialVersionUID = -6397511642982350302L;

    private List<MixedComponentDto>  mixedComponentTabs;
}
