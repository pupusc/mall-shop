package com.wanmi.sbc.vas.api.response.iepsetting;

import com.wanmi.sbc.vas.bean.vo.IepSettingVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>企业购设置列表结果</p>
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IepSettingListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 企业购设置列表结果
     */
    @ApiModelProperty(value = "企业购设置列表结果")
    private List<IepSettingVO> iepSettingVOList;
}
