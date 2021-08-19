package com.wanmi.sbc.crm.autotagstatistics;

import com.wanmi.sbc.crm.bean.enums.TagParamColumn;
import com.wanmi.sbc.crm.bean.enums.TagParamColumnType;
import com.wanmi.sbc.crm.bean.vo.RangeParamVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName StatisticsTagParamInfo
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/3/17 13:40
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsTagParamInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 指标条件名称
     */
    private TagParamColumn paramName;

    /**
     * 指标条件值
     */
    private String paramValue;

    /**
     * 偏好类标签范围属性
     */
    private List<RangeParamVo> dataRange;

    /**
     * 指标条件类型
     */
    private TagParamColumnType paramColumnType;
}
