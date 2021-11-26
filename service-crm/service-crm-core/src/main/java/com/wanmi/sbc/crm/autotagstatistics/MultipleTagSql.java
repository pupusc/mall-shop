package com.wanmi.sbc.crm.autotagstatistics;

import com.wanmi.sbc.crm.bean.enums.RelationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @program: sbc-micro-service-A
 * @description: 综合标签sql拼写
 * @create: 2020-08-28 14:22
 **/
@Component
@Slf4j
public class MultipleTagSql {

    @Autowired
    private SqlActionChoose sqlActionChoose;

    private final String[] str = {"ja", "jb", "jc", "jd", "je", "jf","jg","jh","ji","jj","jk","jl","jm","jn"};
    public String getSql(StatisticsTagInfo tagInfo){
        StringBuilder sql = new StringBuilder();
        StatisticsTagInfo statisticsTagInfo = new StatisticsTagInfo();
        statisticsTagInfo.setTagId(tagInfo.getTagId());
        statisticsTagInfo.setTagName(tagInfo.getTagName());
        statisticsTagInfo.setTagType(tagInfo.getTagType());
        statisticsTagInfo.setDayNum(tagInfo.getDayNum());
        statisticsTagInfo.setBigData(tagInfo.isBigData());
        List<StatisticsDimensionInfo> dimensionInfoList = tagInfo.getDimensionInfoList();
        RelationType relationType = tagInfo.getRelationType();
        if (RelationType.OR.equals(relationType)){
            for (int i = 0; i < dimensionInfoList.size(); i++) {
                StatisticsDimensionInfo dimensionInfo = dimensionInfoList.get(i);
                statisticsTagInfo.setDimensionInfoList(Collections.singletonList(dimensionInfo));
                SqlTool sqlTool = sqlActionChoose.choose(dimensionInfo.getDimensionName());
                sql.append(sqlTool.getSql(statisticsTagInfo));
                if (i != dimensionInfoList.size() -1){
                    sql.append("union ");
                }
            }
        } else {
            if (tagInfo.isBigData()){
                sql.append("select ja.tagId, ja.tagName, ja.tagType, ja.customer_id, ${date} from ");
            } else {
                sql.append("select ja.tagId, ja.tagName, ja.tagType, ja.customer_id from ");
            }

            for (int i = 0; i < dimensionInfoList.size(); i++) {
                if (i > 0){
                    sql.append(" join ");
                }
                StatisticsDimensionInfo dimensionInfo = dimensionInfoList.get(i);
                statisticsTagInfo.setDimensionInfoList(Collections.singletonList(dimensionInfo));
                SqlTool sqlTool = sqlActionChoose.choose(dimensionInfo.getDimensionName());
                sql.append("(").append(sqlTool.getSql(statisticsTagInfo)).append(") ").append(str[i]);
                if (i > 0){
                    sql.append(" on ").append(str[i-1]).append(".customer_id = ").append(str[i]).append(".customer_id" +
                            " ");
                }
            }
        }
        log.info("MultipleTagSql:{}", sql.toString());
        return sql.toString();
    }
}