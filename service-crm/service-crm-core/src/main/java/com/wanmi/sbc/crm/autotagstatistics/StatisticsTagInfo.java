package com.wanmi.sbc.crm.autotagstatistics;

import com.wanmi.sbc.crm.bean.enums.RelationType;
import com.wanmi.sbc.crm.bean.enums.TagType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @ClassName AutoTagStatisticsInfo
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2020/3/17 10:12
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsTagInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签类型，0：偏好标签组，1：指标值标签，2：指标值范围标签，3、综合类标签
     */
    private TagType tagType;

    /**
     * 一级维度且或关系，0：且，1：或
     */
    private RelationType relationType;

    /**
     * 规则天数
     */
    private Integer dayNum;

    /**
     * 将规则天数转换成日期区间--结束时间
     */
    private LocalDate endStaDate;

    /**
     * 将规则天数转换成日期区间--开始时间
     */
    private LocalDate startStaDate;

    /**
     * 标签维度list
     */
    private List<StatisticsDimensionInfo> dimensionInfoList;

    public LocalDate getStartStaDate(){
        return LocalDate.now().minusDays(dayNum);
    }

    public LocalDate getEndStaDate(){
        return LocalDate.now();
    }

    /**
     * crm大数据平台是否开启
     */
    private boolean bigData;

}
