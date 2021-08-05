package com.wanmi.sbc.crm.autotagstatistics.sql;

import com.wanmi.sbc.crm.autotagstatistics.*;
import com.wanmi.sbc.crm.bean.enums.*;
import com.wanmi.sbc.crm.bean.vo.RangeParamVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: sbc-micro-service-A
 * @description: 邀请好友
 * @create: 2020-08-20 15:26
 **/
@Service
@Slf4j
public class InviteSql extends SqlTool {

    private final Map<TagParamColumn,String> RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 1819263693130501269L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"b.terminal_source");
            put(TagParamColumn.NUM,"COUNT(distinct b.invited_customer_id) as total");
            put(TagParamColumn.DAY_NUM,"COUNT(DISTINCT DATE(b.reward_cash_recorded_time)) as total");
            put(TagParamColumn.TO_DATE_NUM,"DATEDIFF(NOW(),b.reward_cash_recorded_time) as days");
            put(TagParamColumn.TIME,"b.reward_cash_recorded_time");
            put(TagParamColumn.DATE,"WEEKDAY(b.reward_cash_recorded_time) as days");
            put(TagParamColumn.INVITE_NEW_NUM,"COUNT(distinct b.invited_customer_id) as total");
            put(TagParamColumn.EFFECTIVE_INVITE_NEW_NUM,"COUNT(distinct b.invited_customer_id) as total");
            put(TagParamColumn.INVITE_NEW_REWARD,"SUM(b.reward_cash) as total");
        }
    };

    private final Map<TagParamColumn,String> FRONT_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"y.terminal_source");
            put(TagParamColumn.NUM,"y.total");
            put(TagParamColumn.DAY_NUM,"y.total");
            put(TagParamColumn.TO_DATE_NUM,"y.days");
            put(TagParamColumn.TIME,"y.reward_cash_recorded_time");
            put(TagParamColumn.DATE,"y.days");
            put(TagParamColumn.INVITE_NEW_NUM,"y.total");
            put(TagParamColumn.EFFECTIVE_INVITE_NEW_NUM,"y.total");
            put(TagParamColumn.INVITE_NEW_REWARD,"y.total");
        }
    };

    private final Map<TagParamColumn,String> QUOTA_FRONT_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"y.terminal_source");
            put(TagParamColumn.NUM,"y.total");
            put(TagParamColumn.DAY_NUM,"y.total");
            put(TagParamColumn.TO_DATE_NUM,"y.days");
            put(TagParamColumn.TIME,"y.reward_cash_recorded_time");
            put(TagParamColumn.DATE,"y.days");
            put(TagParamColumn.INVITE_NEW_NUM,"y.total");
            put(TagParamColumn.EFFECTIVE_INVITE_NEW_NUM,"y.total");
            put(TagParamColumn.INVITE_NEW_REWARD,"y.total");
        }
    };

    private final Map<TagParamColumn,String> FRONT_FIRST_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"t.terminal_source");
            put(TagParamColumn.TO_DATE_NUM,"t.days");
            put(TagParamColumn.TIME,"t.reward_cash_recorded_time");
            put(TagParamColumn.DATE,"t.days");
            put(TagParamColumn.INVITE_NEW_REWARD,"t.reward_cash");
        }
    };

    private final Map<TagParamColumn,String> QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"b.terminal_source");
            put(TagParamColumn.TO_DATE_NUM,"DATEDIFF(NOW(),b.reward_cash_recorded_time) as days");
            put(TagParamColumn.TIME,"b.reward_cash_recorded_time");
            put(TagParamColumn.DATE,"WEEKDAY(b.reward_cash_recorded_time) as days");
            put(TagParamColumn.INVITE_NEW_REWARD,"b.reward_cash");
        }
    };

    private final Map<TagParamColumn,String> QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP_COMMON = new HashMap<TagParamColumn,
            String>(){
        {
            put(TagParamColumn.TERMINAL_SOURCE,"h.terminal_source");
            put(TagParamColumn.TO_DATE_NUM,"h.days");
            put(TagParamColumn.TIME,"h.reward_cash_recorded_time");
            put(TagParamColumn.DATE,"h.days");
            put(TagParamColumn.INVITE_NEW_REWARD,"h.reward_cash");
        }
    };

    private final Map<TagParamColumn,String> GROUP_BY_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8118624560823343394L;
        {
            put(TagParamColumn.TERMINAL_SOURCE, "GROUP BY b.request_customer_id, b.terminal_source ");
            put(TagParamColumn.NUM, "GROUP BY b.request_customer_id ");
            put(TagParamColumn.DAY_NUM, "GROUP BY b.request_customer_id ");
            put(TagParamColumn.TIME,"GROUP BY b.request_customer_id, b.reward_cash_recorded_time ");
            put(TagParamColumn.DATE,"GROUP BY b.request_customer_id, WEEKDAY(b.reward_cash_recorded_time) ");
            put(TagParamColumn.INVITE_NEW_NUM,"GROUP BY b.request_customer_id ");
            put(TagParamColumn.EFFECTIVE_INVITE_NEW_NUM,"GROUP BY b.request_customer_id ");
            put(TagParamColumn.INVITE_NEW_REWARD,"GROUP BY b.request_customer_id ");
        }
    };

    private final Map<TagParamColumn,String> WHERE_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
            put(TagParamColumn.TERMINAL_SOURCE," b.terminal_source in (%s) ");
            put(TagParamColumn.TIME," (date_format(b.reward_cash_recorded_time,'HH:mm') >= '%s' AND date_format" +
                    "(b.reward_cash_recorded_time,'HH:mm') <" +
                    " '%s') ");
            put(TagParamColumn.DATE," WEEKDAY(b.reward_cash_recorded_time) IN (%s) ");
            put(TagParamColumn.INVITE_NEW_REWARD," (b.reward_cash %s and b.reward_flag = 0 and reward_recorded = 1 ) ");
        }
    };

    private final Map<TagParamColumn,String> WHERE_NO_NULL_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
            put(TagParamColumn.TERMINAL_SOURCE," and b.terminal_source is not null ");
            put(TagParamColumn.DAY_NUM, " and b.reward_cash_recorded_time is not null ");
            put(TagParamColumn.TO_DATE_NUM," and b.reward_cash_recorded_time is not null ");
            put(TagParamColumn.TIME," and b.reward_cash_recorded_time is not null ");
            put(TagParamColumn.DATE," and b.reward_cash_recorded_time is not null ");
            put(TagParamColumn.INVITE_NEW_REWARD," and b.reward_cash is not null ");
        }
    };

    private final Map<TagParamColumn,String> FIRST_WHERE_NO_NULL_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
            put(TagParamColumn.TERMINAL_SOURCE," and a.terminal_source is not null ");
            put(TagParamColumn.DAY_NUM, " and a.reward_cash_recorded_time is not null ");
            put(TagParamColumn.TO_DATE_NUM," and a.reward_cash_recorded_time is not null ");
            put(TagParamColumn.TIME," and a.reward_cash_recorded_time is not null ");
            put(TagParamColumn.DATE," and a.reward_cash_recorded_time is not null ");
            put(TagParamColumn.INVITE_NEW_REWARD," and a.reward_cash is not null ");
        }
    };

    // 非首（末） 指标值范围
    private final Map<TagParamColumn,String> HAVING_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = -4577280056554789138L;
        {
            put(TagParamColumn.TERMINAL_SOURCE," y.terminal_source in (%s) ");
            put(TagParamColumn.NUM," y.total %s ");
            put(TagParamColumn.DAY_NUM," y.total %s ");
            put(TagParamColumn.TIME," (date_format(y.reward_cash_recorded_time,'HH:mm') >= '%s' AND date_format" +
                    "(y.reward_cash_recorded_time,'HH:mm') < '%s') ");
            put(TagParamColumn.DATE," y.days IN (%s) ");
            put(TagParamColumn.INVITE_NEW_NUM," y.total %s ");
            put(TagParamColumn.EFFECTIVE_INVITE_NEW_NUM," y.total %s ");
            put(TagParamColumn.INVITE_NEW_REWARD," y.total %s ");
        }
    };

    // 首（末）指标值范围
    private final Map<TagParamColumn,String> FIRST_LAST_WHERE_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8769617825667430529L;
        {
            put(TagParamColumn.TERMINAL_SOURCE," b.terminal_source in (%s) ");
            put(TagParamColumn.TO_DATE_NUM,"  DATEDIFF(NOW(),b.reward_cash_recorded_time) %s ");
            put(TagParamColumn.TIME," (date_format(b.reward_cash_recorded_time,'HH:mm') >= '%s' AND date_format" +
                    "(b.reward_cash_recorded_time,'HH:mm') < '%s') ");
            put(TagParamColumn.DATE," WEEKDAY(b.reward_cash_recorded_time) IN (%s) ");
            put(TagParamColumn.INVITE_NEW_REWARD," (b.reward_cash %s and b.reward_flag = 0) ");
        }
    };

    // 偏好类
    private final Map<TagParamColumn,String> PREFERENCE_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 1819263693130501269L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"a.terminal_source");
            put(TagParamColumn.TIME,"a.reward_cash_recorded_time");
            put(TagParamColumn.DATE,"WEEKDAY(a.reward_cash_recorded_time)");
        }
    };

    private final Map<TagParamColumn,String> PREFERENCE_WHERE_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
            put(TagParamColumn.TERMINAL_SOURCE," a.terminal_source in (%s) ");
            put(TagParamColumn.NUM," b.total %s ");
            put(TagParamColumn.DAY_NUM," b.day_total %s ");
            put(TagParamColumn.TIME," (date_format(a.reward_cash_recorded_time,'HH:mm') >= '%s' AND date_format" +
                    "(a.reward_cash_recorded_time,'HH:mm') < '%s') ");
            put(TagParamColumn.DATE," WEEKDAY(a.reward_cash_recorded_time) IN (%s) ");
            put(TagParamColumn.INVITE_NEW_REWARD," (a.reward_cash %s and a.reward_flag = 0) ");
            put(TagParamColumn.INVITE_NEW_NUM," b.total %s ");
            put(TagParamColumn.EFFECTIVE_INVITE_NEW_NUM," c.success_total %s ");
        }
    };

    private static final String FIRST_OR_LAST_JOIN_SQL = "GROUP BY request_customer_id) a JOIN invite_new_record b ON " +
            "a.customer_id = b.request_customer_id AND a.time_str = b.reward_cash_recorded_time ";

    private String getFrontSql(boolean isFirst, StatisticsTagInfo tagInfo){
        StatisticsDimensionInfo info = tagInfo.getDimensionInfoList().get(0);
        String frontSql = "";
        String date = "";
        String queryTimeStr =
                "b.reward_cash_recorded_time >= date_format(date_sub(NOW(), interval " + tagInfo.getDayNum() + " day)" +
                        ", '%Y-%m-%d') " +
                        "AND " +
                        "b.reward_cash_recorded_time < date_format(NOW(), '%Y-%m-%d')";
        if (tagInfo.isBigData()){
            date = ", ${date}";
            queryTimeStr =
                    "b.p_date >=  date_format(date_sub(CURRENT_TIMESTAMP, " + tagInfo.getDayNum() + "), 'yyyy-MM-dd')" +
                            " " +
                            "AND b.p_date < date_format(CURRENT_TIMESTAMP, 'yyyy-MM-dd')";
        }
        queryTimeStr = queryTimeStr.concat(" ");
        if (TagParamColumn.EFFECTIVE_INVITE_NEW_NUM.equals(info.getParamResult().getParamName())){
            queryTimeStr = queryTimeStr.concat(" and b.available_distribution = 1 ");
        }
        if (TagParamColumn.INVITE_NEW_REWARD.equals(info.getParamResult().getParamName())){ // 邀新奖励只统计有效的且是现金的且入账的
            queryTimeStr = queryTimeStr.concat("and b.reward_flag = 0 ").concat(" and b.reward_recorded = 1 ");
        }
        String str = "'" + tagInfo.getTagName() + "'  as tagName";

        if (isFirst){
            // 判断首末
            String maxOrMin = "";
            if (TagDimensionFirstLastType.FIRST.equals(info.getDimensionType())){
                maxOrMin = "MIN";
            } else if (TagDimensionFirstLastType.LAST.equals(info.getDimensionType())){
                maxOrMin = "MAX";
            }
            if (TagType.QUOTA.equals(tagInfo.getTagType())){
                String time = " ,b.reward_cash_recorded_time ";
                if (TagParamColumn.TIME.equals(info.getParamResult().getParamName())){
                    time = "";
                }
                String column = FRONT_FIRST_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName());
                str = QuotaSqlUtil.TagName(tagInfo.getTagName(), info, column);
                String resultCol = QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP_COMMON.get(info.getParamResult().getParamName());
                frontSql = "select " + tagInfo.getTagId() + " as tagId, " + str + "," + tagInfo.getTagType().toValue() + " as " +
                        "tagType, t.request_customer_id as customer_id" + date + " from ( select h" +
                        ".request_customer_id," + resultCol + ", row_number" +
                        "() over ( PARTITION BY h.request_customer_id ORDER BY last_time DESC ) AS rankNum " +
                        "from ( select h.request_customer_id, "+resultCol+",min(h.reward_cash_recorded_time) as " +
                        "last_time from ( select b" +
                        ".request_customer_id, "+ QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) + time +
                        " from(SELECT request_customer_id as customer_id," + maxOrMin + "(DISTINCT " +
                        "reward_cash_recorded_time) as time_str FROM invite_new_record b WHERE " + queryTimeStr;
            } else {
                frontSql = "select " + tagInfo.getTagId() + " as tagId, " + str + "," + tagInfo.getTagType().toValue() + " as " +
                        "tagType, b.request_customer_id as customer_id" + date + " from ( SELECT request_customer_id as customer_id," + maxOrMin + "(DISTINCT " +
                        "reward_cash_recorded_time) as time_str FROM invite_new_record b WHERE " + queryTimeStr;
            }
        } else {
            if (TagType.QUOTA.equals(tagInfo.getTagType())){
                String column = FRONT_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName());
                str = QuotaSqlUtil.TagName(tagInfo.getTagName(), info, column);
                frontSql =  "select " + tagInfo.getTagId() + " as tagId, " + str + "," + tagInfo.getTagType().toValue() + " as " +
                        "tagType, y.customer_id" + date + " from (" +
                        "select y.customer_id, " + QUOTA_FRONT_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) +
                        ",row_number() over ( PARTITION BY y.customer_id " +
                        " ORDER BY lastTime DESC ) AS rankNum  from (" +
                        " select request_customer_id as customer_id, "
                        + RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) + " ,max( b" +
                        ".reward_cash_recorded_time ) as lastTime from" +
                        " invite_new_record b " +
                        "where " + queryTimeStr;
            } else {
                frontSql =  "select " + tagInfo.getTagId() + " as tagId, " + str + "," + tagInfo.getTagType().toValue() + " as " +
                        "tagType, y.customer_id" + date + " from ( select request_customer_id as customer_id, "
                        + RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) + " from invite_new_record b where " + queryTimeStr;
            }

        }

        // 指标值标签过滤指为空的数据
        if (TagType.QUOTA.equals(tagInfo.getTagType()) && WHERE_NO_NULL_MAP.containsKey(info.getParamResult().getParamName())){
            frontSql = frontSql.concat(WHERE_NO_NULL_MAP.get(info.getParamResult().getParamName()));
        }

        return frontSql;
    }

    // ------------------------------------------------ 偏好类 start-----------------------------------------------------

    private String preferenceFirstSqlStr(boolean isBigData){
        String sql = "SELECT %d AS tagId, '%s' as tagName, '%s' as tagType, %d as type, resAll.customer_id, " +
                "'%s' as dimensionType, resAll.result as dimensionId, resAll.total as num FROM (SELECT @rankNum := IF( " +
                "@customerId = res.customer_id, @rankNum + 1, 1 ) as rankNum, @customerId := res.customer_id as " +
                "customer_id, res.total, res.result FROM ( ";
        if (isBigData){
            sql = "SELECT %d AS tagId, '%s' as tagName, '%s' as tagType, %d as type, resAll.customer_id, " +
                    "'%s' as dimensionType, resAll.result as dimensionId, resAll.total as num, ${date} FROM (SELECT row_number" +
                    "() over (partition by res.customer_id order by res.total desc, res.last_time desc ) rankNum, res.* FROM ( ";
        }
        return sql;
    }

    private String preferenceSelectSqlStr(boolean isBigData){
        String sql = "SELECT a.request_customer_id as customer_id, count(%s) " +
                "as total, %s as result, max(a.reward_cash_recorded_time) as last_time FROM invite_new_record a left " +
                "JOIN ( SELECT " +
                "request_customer_id, count(*) " +
                "as " +
                "total, COUNT(DISTINCT DATE(reward_cash_recorded_time)) as day_total FROM invite_new_record GROUP BY " +
                "request_customer_id ) b on a.request_customer_id = b.request_customer_id LEFT JOIN ( SELECT " +
                "request_customer_id, count(*) as success_total  FROM invite_new_record " +
                "GROUP BY request_customer_id ) c on a.request_customer_id = c.request_customer_id where ";
        if (isBigData){
            sql = sql.concat("p_date >= %s and p_date <= %s ");
        } else {
            sql = sql.concat("a.reward_cash_recorded_time >= %s and a.reward_cash_recorded_time <= %s ");
        }
        return sql;
    }

    private static final String PREFERENCE_GROUP_SQL_STR = "GROUP BY a.request_customer_id, %s ORDER BY " +
            "a.request_customer_id ";

    private static final String PREFERENCE_RANG_WHERE_STR = "AND date_format(a.reward_cash_recorded_time,'HH:mm') >= " +
            "'%s' " +
            "AND date_format(a.reward_cash_recorded_time,'HH:mm') < '%s' ";

    private static final String PREFERENCE_TIME_AND_MONEY_GROUP_SQL_STR = "GROUP BY a.request_customer_id ";

    private String preferenceLastSqlStr(boolean isBigData){
        String sql = ") res,( SELECT @customerId := '', @rankNum := 0 ) rank " +
                "ORDER BY res.customer_id, res.total DESC ) resAll WHERE resAll.rankNum <= ";
        if (isBigData){
            sql = ") res ) resAll WHERE resAll.rankNum <= ";
        }
        return sql;
    }
    // ------------------------------------------------ 偏好类 end-------------------------------------------------------


    private String notFirstLast(StatisticsTagInfo tagInfo){
        // 偏好类
        if (TagType.PREFERENCE.equals(tagInfo.getTagType())){
            return this.preference(tagInfo);
        }

        StatisticsDimensionInfo info = tagInfo.getDimensionInfoList().get(0);
        String sql = getFrontSql(false, tagInfo).concat(" ");

        StringBuilder where = new StringBuilder();
        if (RelationType.AND.equals(info.getRelationType())){
            info.getParamInfoList().forEach(paramInfo -> {
                whereColumn(paramInfo, where, "AND", WHERE_COLUMN_MAP);
            });
        } else {
            List<StatisticsTagParamInfo> paramInfoList = info.getParamInfoList();
            if (paramInfoList.size() == 1) {
                whereColumn(paramInfoList.get(0), where, "AND", WHERE_COLUMN_MAP);
            } else {
                for (int i = 0; i < paramInfoList.size(); i++) {
                    StatisticsTagParamInfo paramInfo = paramInfoList.get(i);
                    // b.create_time >= '2020-07-20' AND (b.goods_info_id IN ( 'GOODS_ID', 'GOODS_ID_2' ) or b.brand_id IN ( 1, 2 ))
                    // or条件首次要处理AND(,末次要处理)
                    if (i == 0) {
                        whereColumn(paramInfo, where, "AND (", WHERE_COLUMN_MAP);
                        continue;
                    }
                    whereColumn(paramInfo, where, "or", WHERE_COLUMN_MAP);
                    if (i == paramInfoList.size() - 1) {
                        where.append(")");
                    }
                }
            }
        }

        sql = sql.concat(where.toString()).concat(GROUP_BY_MAP.get(info.getParamResult().getParamName())).concat(") y" +
                " ");
        // 指标值范围
        if (TagType.MULTIPLE.equals(tagInfo.getTagType()) || TagType.RANGE.equals(tagInfo.getTagType())){
            sql = sql.concat(this.range(info));
        } else if (TagType.QUOTA.equals(tagInfo.getTagType())){
            sql = sql.concat(") y ");
            sql = sql.concat(" where y.rankNum <= 3  ").concat(" group by y.customer_id");
        }
        return sql;
    }

    private String range(StatisticsDimensionInfo info){
        StatisticsTagParamInfo resultParamInfo = info.getParamResult();
        // GOODS_ID 要处理字符串引号问题
        StringBuilder having = new StringBuilder("where ");
        if (TagParamColumn.TIME.equals(resultParamInfo.getParamName())){
            List<String> times = Arrays.asList(resultParamInfo.getParamValue().split(","));
            for (int i = 0; i < times.size(); i++){
                String time = times.get(i);
                List<String> timeList = new ArrayList<>(Arrays.asList(time.split("-")));
                if (i == 0){
                    having.append(String.format(HAVING_MAP.get(resultParamInfo.getParamName()),
                            timeList.toArray()));
                    continue;
                }
                having.append("OR").append(String.format(HAVING_MAP.get(resultParamInfo.getParamName()),
                        timeList.toArray()));
            }
        } else if (TagParamColumn.GOODS_ID.equals(resultParamInfo.getParamName())){
            StringBuilder goodsInfoId = new StringBuilder();
            Arrays.asList(resultParamInfo.getParamValue().split(",")).forEach(str->{
                goodsInfoId.append("'").append(str).append("',");
            });
            String str = goodsInfoId.substring(0, goodsInfoId.length() -1);
            having.append(String.format(HAVING_MAP.get(resultParamInfo.getParamName()),
                    Collections.singletonList(str).toArray()));
        } else if (TagParamColumn.NUM.equals(resultParamInfo.getParamName()) ||
                TagParamColumn.DAY_NUM.equals(resultParamInfo.getParamName())
                || TagParamColumn.INVITE_NEW_NUM.equals(resultParamInfo.getParamName())
                || TagParamColumn.EFFECTIVE_INVITE_NEW_NUM.equals(resultParamInfo.getParamName())
                || TagParamColumn.INVITE_NEW_REWARD.equals(resultParamInfo.getParamName())){
            List<String> item = Arrays.asList(resultParamInfo.getParamValue().split(","));
            for (int i = 0; i< item.size(); i++){
                if (i != 0){
                    having.append(" AND ").append(String.format(HAVING_MAP.get(resultParamInfo.getParamName()),
                            Collections.singletonList(item.get(i)).toArray()));
                    continue;
                }
                having.append(String.format(HAVING_MAP.get(resultParamInfo.getParamName()),
                        Collections.singletonList(item.get(i)).toArray()));
            }
        } else {
            having.append(String.format(HAVING_MAP.get(resultParamInfo.getParamName()),
                    Collections.singletonList(resultParamInfo.getParamValue()).toArray()));
        }
        return having.toString();
    }

    // 偏好类
    private String preference (StatisticsTagInfo tagInfo){
        StatisticsDimensionInfo info = tagInfo.getDimensionInfoList().get(0);

        String notNull = "";
        if (FIRST_WHERE_NO_NULL_MAP.containsKey(info.getParamResult().getParamName())){
            notNull = FIRST_WHERE_NO_NULL_MAP.get(info.getParamResult().getParamName());
        }

        Object[] publicResultArr = {
                // 标签id
                tagInfo.getTagId(),
                // 标签名称
                tagInfo.getTagName(),
                // 标签类型
                tagInfo.getTagType().toValue(),
                // 业务纬度类型 如：下单、付款、加购...
                info.getDimensionName().toValue(),
                // 统计纬度
                info.getParamResult().getParamName()
        };
        String sql = String.format(preferenceFirstSqlStr(tagInfo.isBigData()), publicResultArr);

        if (TagParamColumn.TIME.equals(info.getParamResult().getParamName())) {
            List<RangeParamVo> rangeParamVos = info.getParamResult().getDataRange();
            List<Map<String, String>> maps = rangeParamVos.get(0).getDataValue();
            String item1 = maps.get(0).get("0");
            String item2 = maps.get(0).get("1");
            Object[] preferenceFirstSqlArr = {
                    // sql结果集
                    "a.request_customer_id",
                    "'" + item1 + "-" + item2 + "'",
                    // 开始时间
                    "date_format(date_sub(NOW(), interval " + tagInfo.getDayNum() + " day), '%Y-%m-%d')",
                    // 结束时间
                    "date_format(NOW(), '%Y-%m-%d')"
            };
            sql = sql.concat(String.format(preferenceSelectSqlStr(tagInfo.isBigData()), preferenceFirstSqlArr));
        } else {
            Object[] preferenceFirstSqlArr = {
                    // sql结果集
                    PREFERENCE_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()),
                    PREFERENCE_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()),
                    // 开始时间
                    "date_format(date_sub(NOW(), interval " + tagInfo.getDayNum() + " day), '%Y-%m-%d')",
                    // 结束时间
                    "date_format(NOW(), '%Y-%m-%d')"
            };
            sql = sql.concat(String.format(preferenceSelectSqlStr(tagInfo.isBigData()), preferenceFirstSqlArr));
        }

        // 时间查询后紧跟非空筛选
        sql = sql.concat(notNull);

        StringBuilder where = new StringBuilder();
        if (RelationType.AND.equals(info.getRelationType())){
            info.getParamInfoList().forEach(paramInfo -> {
                whereColumn(paramInfo, where, "AND", PREFERENCE_WHERE_COLUMN_MAP);
            });
        } else {
            List<StatisticsTagParamInfo> paramInfoList = info.getParamInfoList();
            if (paramInfoList.size() == 1) {
                whereColumn(paramInfoList.get(0), where, "AND", PREFERENCE_WHERE_COLUMN_MAP);
            } else {
                for (int i = 0; i < paramInfoList.size(); i++) {
                    StatisticsTagParamInfo paramInfo = paramInfoList.get(i);
                    // b.create_time >= '2020-07-20' AND (b.goods_info_id IN ( 'GOODS_ID', 'GOODS_ID_2' ) or b.brand_id IN ( 1, 2 ))
                    // or条件首次要处理AND(,末次要处理)
                    if (i == 0) {
                        whereColumn(paramInfo, where, "AND (", PREFERENCE_WHERE_COLUMN_MAP);
                        continue;
                    }
                    whereColumn(paramInfo, where, "or", PREFERENCE_WHERE_COLUMN_MAP);
                    if (i == paramInfoList.size() - 1) {
                        where.append(")");
                    }
                }
            }
        }

        String sqlItem = sql.concat(where.toString());
        if (TagParamColumn.TIME.equals(info.getParamResult().getParamName())){
            // 时间范围和金额范围特殊处理
            List<RangeParamVo> rangeParamVos = info.getParamResult().getDataRange();
            List<Map<String, String>> maps = rangeParamVos.get(0).getDataValue();
            for (int i = 0; i < maps.size(); i++){
                Map<String, String> item = maps.get(i);
                String item1 = item.get("0");
                String item2 = item.get("1");
                String rangWhere = String.format(PREFERENCE_RANG_WHERE_STR, item1,
                        item2);
                if (i == 0){
                    sql = sqlItem.concat(rangWhere).concat(PREFERENCE_TIME_AND_MONEY_GROUP_SQL_STR);
                } else {
                    Object[] preferenceFirstSqlArr = {
                            // sql结果集
                            "a.request_customer_id",
                            "'" + item1 + "-" + item2 + "'",
                            // 开始时间
                            "date_format(date_sub(NOW(), interval " + tagInfo.getDayNum() + " day), '%Y-%m-%d')",
                            // 结束时间
                            "date_format(NOW(), '%Y-%m-%d')"
                    };

                    sql = sql.concat(String.format(preferenceSelectSqlStr(tagInfo.isBigData()), preferenceFirstSqlArr))
                            .concat(notNull)
                            .concat(where.toString())
                            .concat(rangWhere).concat(PREFERENCE_TIME_AND_MONEY_GROUP_SQL_STR);
                }
                if (i != maps.size() -1){
                    sql = sql.concat(" union ");
                }
            }
        } else {
            sql = sqlItem.concat(String.format(PREFERENCE_GROUP_SQL_STR,
                    PREFERENCE_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName())));
        }
        sql = sql.concat(preferenceLastSqlStr(tagInfo.isBigData()));
        sql = sql.concat(info.getParamResult().getParamValue());
        return sql;
    }

    private String firstLase(StatisticsTagInfo tagInfo){
        StatisticsDimensionInfo info = tagInfo.getDimensionInfoList().get(0);
        String sql = getFrontSql(true, tagInfo).concat(" ");

        // where 过滤条件
        String where = firstWhere(info);
        sql = sql.concat(where).concat(FIRST_OR_LAST_JOIN_SQL);
        if (TagParamColumn.EFFECTIVE_INVITE_NEW_NUM.equals(info.getParamResult().getParamName())){
            where = where.concat(" and b.available_distribution = 1 ");
        }
        if (TagParamColumn.INVITE_NEW_REWARD.equals(info.getParamResult().getParamName())){ // 邀新奖励只统计是现金的且入账的
            where = where.concat(" and b.reward_flag = 0 and b.reward_recorded = 1");
        }
        // 指标值范围
        if (TagType.MULTIPLE.equals(tagInfo.getTagType()) || TagType.RANGE.equals(tagInfo.getTagType())){
            StatisticsTagParamInfo resultParamInfo = info.getParamResult();
            // GOODS_ID 要处理字符串引号问题
            StringBuilder having = new StringBuilder("WHERE ");
            if (TagParamColumn.TIME.equals(resultParamInfo.getParamName())){
                List<String> times = Arrays.asList(resultParamInfo.getParamValue().split(","));
                for (int i = 0; i < times.size(); i++){
                    String time = times.get(i);
                    List<String> timeList = new ArrayList<>(Arrays.asList(time.split("-")));
                    if (i == 0){
                        having.append(String.format(FIRST_LAST_WHERE_COLUMN_MAP.get(resultParamInfo.getParamName()),
                                timeList.toArray()));
                        continue;
                    }
                    having.append("OR").append(String.format(FIRST_LAST_WHERE_COLUMN_MAP.get(resultParamInfo.getParamName()),
                            timeList.toArray()));
                }
            } else if (TagParamColumn.GOODS_ID.equals(resultParamInfo.getParamName())){
                StringBuilder goodsInfoId = new StringBuilder();
                Arrays.asList(resultParamInfo.getParamValue().split(",")).forEach(str->{
                    goodsInfoId.append("'").append(str).append("',");
                });
                String str = goodsInfoId.substring(0, goodsInfoId.length() -1);
                having.append(String.format(FIRST_LAST_WHERE_COLUMN_MAP.get(resultParamInfo.getParamName()),
                        Collections.singletonList(str).toArray()));
            } else if (TagParamColumn.TO_DATE_NUM.equals(resultParamInfo.getParamName())
            || TagParamColumn.INVITE_NEW_REWARD.equals(resultParamInfo.getParamName())){
                List<String> item = Arrays.asList(resultParamInfo.getParamValue().split(","));
                for (int i = 0; i< item.size(); i++){
                    if (i != 0){
                        having.append(" AND ").append(String.format(FIRST_LAST_WHERE_COLUMN_MAP.get(resultParamInfo.getParamName()),
                                Collections.singletonList(item.get(i)).toArray()));
                        continue;
                    }
                    having.append(String.format(FIRST_LAST_WHERE_COLUMN_MAP.get(resultParamInfo.getParamName()),
                            Collections.singletonList(item.get(i)).toArray()));
                }
            }
            else {
                having.append(String.format(FIRST_LAST_WHERE_COLUMN_MAP.get(resultParamInfo.getParamName()),
                        Collections.singletonList(resultParamInfo.getParamValue()).toArray()));
            }
            sql = sql.concat(having.toString()).concat(where);
        } else if (TagType.QUOTA.equals(tagInfo.getTagType())){
            String notNull = "";
            if (WHERE_NO_NULL_MAP.containsKey(info.getParamResult().getParamName())){
                notNull = (WHERE_NO_NULL_MAP.get(info.getParamResult().getParamName()));
            }
            sql = sql.concat(" where 1 = 1 ").concat(where).concat(notNull)
                    .concat(") h group by h.request_customer_id," +
                            QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP_COMMON.get(info.getParamResult().getParamName()) +") h ) t ");
            sql = sql.concat(" where rankNum <= 3 GROUP BY t.request_customer_id ");
        }
        return sql;
    }

    private String firstWhere(StatisticsDimensionInfo info){
        StringBuilder where = new StringBuilder();
        if (RelationType.AND.equals(info.getRelationType())){
            info.getParamInfoList().forEach(paramInfo -> {
                whereColumn(paramInfo, where, "AND", WHERE_COLUMN_MAP);
            });
        } else {
            List<StatisticsTagParamInfo> paramInfoList = info.getParamInfoList();
            if (paramInfoList.size() == 1) {
                whereColumn(paramInfoList.get(0), where, "AND", WHERE_COLUMN_MAP);
            } else {
                for (int i = 0; i < paramInfoList.size(); i++) {
                    StatisticsTagParamInfo paramInfo = paramInfoList.get(i);
                    // b.create_time >= '2020-07-20' AND (b.goods_info_id IN ( 'GOODS_ID', 'GOODS_ID_2' ) or b.brand_id IN ( 1, 2 ))
                    // or条件首次要处理AND(,末次要处理)
                    if (i == 0) {
                        whereColumn(paramInfo, where, "AND (", WHERE_COLUMN_MAP);
                        continue;
                    }
                    whereColumn(paramInfo, where, "or", WHERE_COLUMN_MAP);
                    if (i == paramInfoList.size() - 1) {
                        where.append(")");
                    }
                }
            }
        }
        return where.toString();
    }

    private void whereColumn(StatisticsTagParamInfo paramInfo, StringBuilder where, String andOr, Map<TagParamColumn,
            String> columnMap){
        // GOODS_ID 要处理字符串引号问题
        if (TagParamColumn.TIME.equals(paramInfo.getParamName())){
            List<String> times = Arrays.asList(paramInfo.getParamValue().split(","));
            if (times.size() == 1){
                String time = times.get(0);
                List<String> timeList = new ArrayList<>(Arrays.asList(time.split("-")));
                where.append(andOr).append(String.format(WHERE_COLUMN_MAP.get(paramInfo.getParamName()),
                        timeList.toArray()));
            } else {
                for (int i = 0; i < times.size(); i++) {
                    String time = times.get(i);
                    List<String> timeList = new ArrayList<>(Arrays.asList(time.split("-")));
                    if (i == 0) {
                        where.append(andOr).append(" (").append(String.format(columnMap.get(paramInfo.getParamName()),
                                timeList.toArray()));
                        continue;
                    }
                    where.append("OR").append(String.format(columnMap.get(paramInfo.getParamName()),
                            timeList.toArray()));

                    if (i == times.size() - 1) {
                        where.append(")");
                    }
                }
            }
        } else if (TagParamColumn.INVITE_NEW_REWARD.equals(paramInfo.getParamName())
                || TagParamColumn.NUM.equals(paramInfo.getParamName())
                || TagParamColumn.DAY_NUM.equals(paramInfo.getParamName())
                || TagParamColumn.EFFECTIVE_INVITE_NEW_NUM.equals(paramInfo.getParamName())){
            List<String> item = Arrays.asList(paramInfo.getParamValue().split(","));
            if (item.size() == 1){
                where.append(andOr).append(String.format(columnMap.get(paramInfo.getParamName()),
                        Collections.singletonList(item.get(0)).toArray()));
            } else {
                for (int i = 0; i< item.size(); i++){
                    if (i == 0){
                        where.append(andOr).append(" (").append(String.format(columnMap.get(paramInfo.getParamName()),
                                Collections.singletonList(item.get(i)).toArray()));
                        continue;
                    }

                    where.append("AND").append(String.format(columnMap.get(paramInfo.getParamName()),
                            Collections.singletonList(item.get(i)).toArray()));

                    if (i == item.size() - 1){
                        where.append(")");
                    }
                }
            }
        } else {
            where.append(andOr).append(String.format(columnMap.get(paramInfo.getParamName()),
                    Collections.singletonList(paramInfo.getParamValue()).toArray()));
        }
    }

    @Override
    public DimensionName[] supports() {
        return new DimensionName[] {DimensionName.INVITE};
    }

    @Override
    public String getSql(StatisticsTagInfo tagInfo){
        String sql = "";
        if (TagDimensionFirstLastType.NO_FIRST_LAST.equals(tagInfo.getDimensionInfoList().get(0).getDimensionType())){
            sql = notFirstLast(tagInfo);
        } else {
            sql = firstLase(tagInfo);
        }
        log.info("DimensionName.INVITE SQL:{}", sql);
        return sql;
    }

    public static void main(String[] args) {
        InviteSql sql = new InviteSql();
        StatisticsTagInfo info = new StatisticsTagInfo();
        info.setTagId(1L);
        info.setTagName("邀请好友标签值标签");
        info.setTagType(TagType.QUOTA);
        info.setRelationType(RelationType.AND);
        info.setDayNum(30);

        StatisticsDimensionInfo statisticsDimensionInfo = new StatisticsDimensionInfo();
        statisticsDimensionInfo.setDimensionName(DimensionName.INVITE);
        statisticsDimensionInfo.setDimensionType(TagDimensionFirstLastType.FIRST);
//        statisticsDimensionInfo.setDimensionType(TagDimensionFirstLastType.NO_FIRST_LAST);
        statisticsDimensionInfo.setRelationType(RelationType.AND);
        StatisticsTagParamInfo paramInfo = new StatisticsTagParamInfo();
        paramInfo.setParamName(TagParamColumn.TERMINAL_SOURCE);
        paramInfo.setParamValue("3");
        List<Map<String, String>> rangList = new ArrayList<>();
        Map<String, String> rangMap1 = new HashMap<>();
        rangMap1.put("0", "00:00");
        rangMap1.put("1", "01:00");
        rangList.add(rangMap1);

        Map<String, String> rangMap2 = new HashMap<>();
        rangMap2.put("0", "02:00");
        rangMap2.put("1", "03:00");
        rangList.add(rangMap2);

        Map<String, String> rangMap3 = new HashMap<>();
        rangMap3.put("0", "03:00");
        rangMap3.put("1", "04:00");
        rangList.add(rangMap3);

        Map<String, String> rangMap4 = new HashMap<>();
        rangMap4.put("0", "07:00");
        rangMap4.put("1", "08:00");
        rangList.add(rangMap4);

        RangeParamVo vo = new RangeParamVo();
        vo.setType(0);
        vo.setDataValue(rangList);

//        paramInfo.setDataRange(Collections.singletonList(vo));
        statisticsDimensionInfo.setParamResult(paramInfo);

        List<StatisticsTagParamInfo> s = new ArrayList<>();
        StatisticsTagParamInfo paramInfo1 = new StatisticsTagParamInfo();
        paramInfo1.setParamName(TagParamColumn.DATE);
        paramInfo1.setParamValue("1,2");
        s.add(paramInfo1);
        StatisticsTagParamInfo paramInfo2 = new StatisticsTagParamInfo();
        paramInfo2.setParamName(TagParamColumn.TERMINAL_SOURCE);
        paramInfo2.setParamValue("1,2");
        s.add(paramInfo2);
        StatisticsTagParamInfo paramInfo3 = new StatisticsTagParamInfo();
        paramInfo3.setParamName(TagParamColumn.TIME);
        paramInfo3.setParamValue("10:00-11:00,12:00-13:00");
        s.add(paramInfo3);
        StatisticsTagParamInfo paramInfo4 = new StatisticsTagParamInfo();
        paramInfo4.setParamName(TagParamColumn.INVITE_NEW_REWARD);
        paramInfo4.setParamValue(">1,<10");
        s.add(paramInfo4);
        statisticsDimensionInfo.setParamInfoList(s);

        List<StatisticsDimensionInfo> r = new ArrayList<>();
        r.add(statisticsDimensionInfo);
        info.setDimensionInfoList(r);
        sql.getSql(info);
        System.out.println("====================================================================================");
        info.setBigData(true);
        String bigsql = sql.getSql(info);
        System.out.println(QuotaSqlUtil.sql(bigsql));
    }
}