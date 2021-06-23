package com.wanmi.sbc.marketing.api.response.grouponsetting;

import com.wanmi.sbc.marketing.bean.vo.GrouponSettingVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>拼团活动信息表修改结果</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponSettingModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的拼团活动信息表信息
     */
    private GrouponSettingVO grouponSettingVO;
}
