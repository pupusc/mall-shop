package com.wanmi.sbc.setting.api.response.mixedcomponentV2;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.dto.MixedComponentContentDto;
import com.wanmi.sbc.setting.bean.dto.MixedComponentKeyWordsDto;
import com.wanmi.sbc.setting.bean.dto.MixedComponentTabDto;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyContentDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class TopicStoreyMixedComponentResponse implements Serializable {

    private static final long serialVersionUID = -6397511642982350302L;

    private List<MixedComponentTabDto>  mixedComponentTabs;
}
