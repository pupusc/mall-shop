package com.wanmi.sbc.setting.api.request.topicconfig;

import com.wanmi.sbc.setting.bean.dto.HeadImageConfigDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HeadImageConfigAddRequest implements Serializable {
    private static final long serialVersionUID = -797995665161219409L;
    
    List<HeadImageConfigDTO> headImage;
}
