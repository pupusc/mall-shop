package com.wanmi.sbc.crm.autotagstatistics.sql;

import com.wanmi.sbc.crm.autotagstatistics.*;
import com.wanmi.sbc.crm.bean.enums.*;
import com.wanmi.sbc.crm.bean.vo.RangeParamVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: sbc-micro-service-A
 * @description: 访问
 * @create: 2020-08-27 17:37
 **/
@Service
@Slf4j
public class CustomerFollowSql extends SqlTool {

    private final Map<TagParamColumn,String> RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 1819263693130501269L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"b.terminal_source");
            put(TagParamColumn.NUM,"COUNT(*) as total");
            put(TagParamColumn.DAY_NUM,"COUNT(DISTINCT DATE(b.create_time)) as total");
            put(TagParamColumn.TO_DATE_NUM,"DATEDIFF(NOW(),b.create_time) as days");
            put(TagParamColumn.TIME,"b.create_time");
            put(TagParamColumn.DATE,"WEEKDAY(b.create_time) as days");
            put(TagParamColumn.CATE_TOP_ID,"b.cate_top_id");
            put(TagParamColumn.CATE_ID,"b.cate_id");
            put(TagParamColumn.BRAND_ID,"b.brand_id");
            put(TagParamColumn.GOODS_ID,"b.goods_id");
            put(TagParamColumn.STORE_ID,"b.company_info_id");
        }
    };

    private final Map<TagParamColumn,String> FRONT_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"y.terminal_source");
            put(TagParamColumn.NUM,"y.total");
            put(TagParamColumn.DAY_NUM,"y.total");
            put(TagParamColumn.TO_DATE_NUM,"y.days");
            put(TagParamColumn.TIME,"y.create_time");
            put(TagParamColumn.DATE,"y.days");
            put(TagParamColumn.CATE_TOP_ID,"z.cate_name");
            put(TagParamColumn.CATE_ID,"z.cate_name");
            put(TagParamColumn.BRAND_ID,"z.brand_name");
            put(TagParamColumn.GOODS_ID,"z.goods_name");
            put(TagParamColumn.STORE_ID,"z.store_name");
        }
    };

    private final Map<TagParamColumn,String> QUOTA_FRONT_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"y.terminal_source");
            put(TagParamColumn.NUM,"y.total");
            put(TagParamColumn.DAY_NUM,"y.total");
            put(TagParamColumn.TO_DATE_NUM,"y.days");
            put(TagParamColumn.TIME,"y.create_time");
            put(TagParamColumn.DATE,"y.days");
            put(TagParamColumn.CATE_TOP_ID,"y.cate_top_id");
            put(TagParamColumn.CATE_ID,"y.cate_id");
            put(TagParamColumn.BRAND_ID,"y.brand_id");
            put(TagParamColumn.GOODS_ID,"y.goods_id");
            put(TagParamColumn.STORE_ID,"y.company_info_id");
        }
    };

    private final Map<TagParamColumn,String> FRONT_FIRST_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"t.terminal_source");
            put(TagParamColumn.TO_DATE_NUM,"t.days");
            put(TagParamColumn.TIME,"t.create_time");
            put(TagParamColumn.DATE,"t.days");
            put(TagParamColumn.CATE_TOP_ID,"z.cate_name");
            put(TagParamColumn.CATE_ID,"z.cate_name");
            put(TagParamColumn.BRAND_ID,"z.brand_name");
            put(TagParamColumn.GOODS_ID,"z.goods_name");
            put(TagParamColumn.STORE_ID,"z.store_name");
        }
    };

    private final Map<TagParamColumn,String> QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"b.terminal_source");
            put(TagParamColumn.TO_DATE_NUM,"DATEDIFF(NOW(),b.create_time) as days");
            put(TagParamColumn.TIME,"b.create_time");
            put(TagParamColumn.DATE,"WEEKDAY(b.create_time) as days");
            put(TagParamColumn.CATE_TOP_ID,"b.cate_top_id");
            put(TagParamColumn.CATE_ID,"b.cate_id");
            put(TagParamColumn.BRAND_ID,"b.brand_id");
            put(TagParamColumn.GOODS_ID,"b.goods_id");
            put(TagParamColumn.STORE_ID,"b.company_info_id");
        }
    };

    private final Map<TagParamColumn,String> QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP_COMMON = new HashMap<TagParamColumn,
            String>(){
        {
            put(TagParamColumn.TERMINAL_SOURCE,"h.terminal_source");
            put(TagParamColumn.TO_DATE_NUM,"h.days");
            put(TagParamColumn.TIME,"h.create_time");
            put(TagParamColumn.DATE,"h.days");
            put(TagParamColumn.CATE_TOP_ID,"h.cate_top_id");
            put(TagParamColumn.CATE_ID,"h.cate_id");
            put(TagParamColumn.BRAND_ID,"h.brand_id");
            put(TagParamColumn.GOODS_ID,"h.goods_id");
            put(TagParamColumn.STORE_ID,"h.company_info_id");
        }
    };

    private final Map<TagParamColumn,String> GROUP_BY_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8118624560823343394L;
        {
            put(TagParamColumn.TERMINAL_SOURCE, "GROUP BY b.customer_id, b.terminal_source ");
            put(TagParamColumn.NUM, "GROUP BY b.customer_id ");
            put(TagParamColumn.DAY_NUM, "GROUP BY b.customer_id ");
            put(TagParamColumn.TIME,"GROUP BY b.customer_id, b.create_time ");
            put(TagParamColumn.DATE,"GROUP BY b.customer_id, WEEKDAY(b.create_time) ");
            put(TagParamColumn.CATE_TOP_ID,"GROUP BY b.customer_id, b.cate_top_id ");
            put(TagParamColumn.CATE_ID,"GROUP BY b.customer_id, b.cate_id ");
            put(TagParamColumn.BRAND_ID,"GROUP BY b.customer_id, b.brand_id ");
            put(TagParamColumn.GOODS_ID,"GROUP BY b.customer_id, b.goods_id ");
            put(TagParamColumn.STORE_ID,"GROUP BY b.customer_id, b.company_info_id ");
        }
    };

    private final Map<TagParamColumn,String> WHERE_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
            put(TagParamColumn.TERMINAL_SOURCE," b.terminal_source in (%s) ");
            put(TagParamColumn.TIME," (date_format(b.create_time,'HH:mm')>= '%s' AND date_format(b.create_time," +
                    "'HH:mm') < '%s') ");
            put(TagParamColumn.DATE," WEEKDAY(b.create_time) IN (%s) ");
            put(TagParamColumn.CATE_TOP_ID," b.cate_top_id in (%s) ");
            put(TagParamColumn.CATE_ID," b.cate_id in (%s) ");
            put(TagParamColumn.BRAND_ID," b.brand_id in (%s) ");
            put(TagParamColumn.GOODS_ID," b.goods_id in (%s) ");
            put(TagParamColumn.STORE_ID," z.store_id in (%s) ");
        }
    };

    private final Map<TagParamColumn,String> WHERE_NO_NULL_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
            put(TagParamColumn.TERMINAL_SOURCE," and b.terminal_source is not null ");
            put(TagParamColumn.DAY_NUM, " and b.create_time is not null ");
            put(TagParamColumn.TO_DATE_NUM," and b.create_time is not null ");
            put(TagParamColumn.TIME," and b.create_time is not null ");
            put(TagParamColumn.DATE," and b.create_time is not null ");
            put(TagParamColumn.CATE_TOP_ID," and b.cate_top_id is not null ");
            put(TagParamColumn.CATE_ID," and b.cate_id is not null ");
            put(TagParamColumn.BRAND_ID," and b.brand_id is not null ");
            put(TagParamColumn.GOODS_ID," and b.goods_id is not null ");
            put(TagParamColumn.STORE_ID," and b.company_info_id is not null ");
        }
    };

    private final Map<TagParamColumn,String> TAG_NAME_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
            put(TagParamColumn.CATE_TOP_ID," left join goods_cate z on z.cate_id = y.cate_top_id ");
            put(TagParamColumn.CATE_ID," left join goods_cate z on z.cate_id = y.cate_id ");
            put(TagParamColumn.BRAND_ID," left join goods_brand z on z.brand_id = y.brand_id ");
            put(TagParamColumn.GOODS_ID," left join goods z on z.goods_id = y.goods_id ");
            put(TagParamColumn.STORE_ID," left join store z on y.company_info_id = z.company_info_id ");
        }
    };

    private final Map<TagParamColumn,String> FIRST_TAG_NAME_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
            put(TagParamColumn.CATE_TOP_ID," left join goods_cate z on z.cate_id =  t.cate_top_id ");
            put(TagParamColumn.CATE_ID," left join goods_cate z on z.cate_id = t.cate_id ");
            put(TagParamColumn.BRAND_ID," left join goods_brand z on z.brand_id = t.brand_id ");
            put(TagParamColumn.GOODS_ID," left join goods z on z.goods_id = t.goods_id ");
            put(TagParamColumn.STORE_ID," left join store z on z.company_info_id = t.company_info_id ");
        }
    };

    // 非首（末） 指标值范围
    private final Map<TagParamColumn,String> HAVING_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = -4577280056554789138L;
        {
            put(TagParamColumn.TERMINAL_SOURCE," y.terminal_source in (%s) ");
            put(TagParamColumn.NUM," y.total %s ");
            put(TagParamColumn.DAY_NUM," y.total %s ");
            put(TagParamColumn.TIME," (date_format(y.create_time,'HH:mm') >= '%s' AND date_format(y.create_time," +
                    "'HH:mm') < '%s') ");
            put(TagParamColumn.DATE," y.days IN (%s) ");
            put(TagParamColumn.CATE_TOP_ID," y.cate_top_id in (%s) ");
            put(TagParamColumn.CATE_ID," y.cate_id in (%s) ");
            put(TagParamColumn.BRAND_ID," y.brand_id in (%s) ");
            put(TagParamColumn.GOODS_ID," y.goods_id in (%s) ");
            put(TagParamColumn.STORE_ID," z.store_id in (%s) ");
        }
    };

    // 首（末）指标值范围
    private final Map<TagParamColumn,String> FIRST_LAST_WHERE_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8769617825667430529L;
        {
            put(TagParamColumn.TERMINAL_SOURCE," b.terminal_source in (%s) ");
            put(TagParamColumn.TO_DATE_NUM,"  DATEDIFF(NOW(),b.create_time) %s ");
            put(TagParamColumn.TIME," (date_format(b.create_time,'HH:mm') >= '%s' AND date_format(b.create_time," +
                    "'HH:mm') < '%s') ");
            put(TagParamColumn.DATE," WEEKDAY(b.create_time) IN (%s) ");
            put(TagParamColumn.CATE_TOP_ID," b.cate_top_id in (%s) ");
            put(TagParamColumn.CATE_ID," b.cate_id in (%s) ");
            put(TagParamColumn.BRAND_ID," b.brand_id in (%s) ");
            put(TagParamColumn.GOODS_ID," b.goods_id in (%s) ");
            put(TagParamColumn.STORE_ID," z.store_id in (%s) ");
        }
    };

    // 首（末）指标值范围
    private final Map<TagParamColumn,String> FIRST_LAST_WHERE_NO_NULL_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
            put(TagParamColumn.CATE_TOP_ID," and b.goods_info_id is not null ");
            put(TagParamColumn.CATE_ID," and b.goods_info_id is not null ");
            put(TagParamColumn.BRAND_ID," and b.goods_info_id is not null ");
            put(TagParamColumn.GOODS_ID," and b.goods_info_id is not null ");
            put(TagParamColumn.STORE_ID," and b.store_id is not null ");
        }
    };

    // 偏好类
    private final Map<TagParamColumn,String> PREFERENCE_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 1819263693130501269L;
        {
            put(TagParamColumn.TERMINAL_SOURCE,"b.terminal_source");
            put(TagParamColumn.TIME,"b.create_time");
            put(TagParamColumn.DATE,"WEEKDAY(b.create_time)");
            put(TagParamColumn.CATE_TOP_ID,"b.cate_top_id");
            put(TagParamColumn.CATE_ID,"b.cate_id");
            put(TagParamColumn.BRAND_ID,"b.brand_id");
            put(TagParamColumn.GOODS_ID,"b.goods_id");
            put(TagParamColumn.STORE_ID,"b.store_id");
        }
    };

    private static final String FIRST_OR_LAST_JOIN_SQL_A = "GROUP BY customer_id) a JOIN customer_follow b ON " +
            "a.customer_id = b.customer_id AND a.time_str = b.create_time ";

    private String getFrontSql(boolean isFirst, StatisticsTagInfo tagInfo){
        StatisticsDimensionInfo info = tagInfo.getDimensionInfoList().get(0);
        String frontSql = "";
        String date = "";
        String queryTimeStr =
                "b.create_time >= date_format(date_sub(NOW(), interval " + tagInfo.getDayNum() + " day), '%Y-%m-%d') " +
                        "AND " +
                        "b.create_time < date_format(NOW(), '%Y-%m-%d')";
        if (tagInfo.isBigData()){
            date = ", ${date}";
            queryTimeStr =
                    "b.p_date >=  date_format(date_sub(CURRENT_TIMESTAMP, " + tagInfo.getDayNum() + "), 'yyyy-MM-dd')" +
                            " " +
                            "AND b.p_date < date_format(CURRENT_TIMESTAMP, 'yyyy-MM-dd')";
        }
        queryTimeStr = queryTimeStr.concat(" ");
        String str = "'" + tagInfo.getTagName() + "'  as tagName";

        StringBuilder join = new StringBuilder();
        info.getParamInfoList().forEach(item->{
            if (TagParamColumn.STORE_ID.equals(item.getParamName())){
                join .append(" left join store z on z.company_info_id = b.company_info_id ");
            }
        });

        if (isFirst){
            // 判断首末
            String maxOrMin = "";
            if (TagDimensionFirstLastType.FIRST.equals(info.getDimensionType())){
                maxOrMin = "MIN";
            } else if (TagDimensionFirstLastType.LAST.equals(info.getDimensionType())){
                maxOrMin = "MAX";
            }
            if (TagType.QUOTA.equals(tagInfo.getTagType())){
                String time = " ,b.create_time ";
                if (TagParamColumn.TIME.equals(info.getParamResult().getParamName())){
                    time = "";
                }
                String column = FRONT_FIRST_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName());
                str = QuotaSqlUtil.TagName(tagInfo.getTagName(), info, column);
                String resultCol = QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP_COMMON.get(info.getParamResult().getParamName());
                frontSql = "select " + tagInfo.getTagId() + " as tagId, " + str + "," + tagInfo.getTagType().toValue() + " as " +
                        "tagType, t.customer_id" + date + " from ( select h.customer_id, " + resultCol + ", row_number"+
                        "() over ( PARTITION BY h.customer_id ORDER BY last_time DESC ) AS rankNum " +
                        "from ( select h.customer_id, "+resultCol+",min(h.create_time) as last_time from ( select b" +
                        ".customer_id, "+ QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) + time +
                        " from( SELECT b.customer_id," + maxOrMin +
                        "(DISTINCT " +
                        "create_time) as time_str FROM customer_follow b " + join.toString() + " WHERE " + queryTimeStr;
            } else {
                frontSql = "select " + tagInfo.getTagId() + " as tagId, " + str + "," + tagInfo.getTagType().toValue() + " as " +
                        "tagType, b.customer_id" + date + " from ( SELECT b.customer_id," + maxOrMin +
                        "(DISTINCT " +
                        "create_time) as time_str FROM customer_follow b " + join.toString() + " WHERE " + queryTimeStr;
            }

            if (FIRST_LAST_WHERE_NO_NULL_MAP.containsKey(info.getParamResult().getParamName())){
                frontSql = frontSql.concat(FIRST_LAST_WHERE_NO_NULL_MAP.get(info.getParamResult().getParamName()));
            }
        } else {
            if (TagType.QUOTA.equals(tagInfo.getTagType())){
                String column = FRONT_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName());
                str = QuotaSqlUtil.TagName(tagInfo.getTagName(), info, column);
                frontSql =  "select " + tagInfo.getTagId() + " as tagId, " + str + "," + tagInfo.getTagType().toValue() + " as " +
                        "tagType, y.customer_id " + date + " from (" +
                        "select y.customer_id, " + QUOTA_FRONT_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) +
                        ",row_number() over ( PARTITION BY y.customer_id " +
                        " ORDER BY lastTime DESC ) AS rankNum  from (" +
                        " select b.customer_id, "
                        + RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) + ",max( b.create_time ) " +
                        "as lastTime from " +
                        "customer_follow b "+ join.toString() +" " +
                        "where " + queryTimeStr;
            } else {
                frontSql =  "select " + tagInfo.getTagId() + " as tagId, " + str + "," + tagInfo.getTagType().toValue() + " as " +
                        "tagType, y.customer_id " + date + " from ( select b.customer_id, "
                        + RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) + " from customer_follow b "+ join.toString() +" " +
                        "where " + queryTimeStr;
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
                    "'%s' as dimensionType, resAll.result as dimensionId, resAll.total as num, ${date} FROM (SELECT " +
                    "row_number" +
                    "() over " +
                    "(partition by res.customer_id order by res.total desc, res.last_time desc ) rankNum, res.* FROM ( ";
        }
        return sql;
    }

    private String preferenceSelectSqlStr(boolean isBigData, StatisticsDimensionInfo info){
        // 访问店铺要关联store表特殊处理
        String join = "";
        boolean flag = info.getParamInfoList().stream().anyMatch(x -> TagParamColumn.STORE_ID.equals(x.getParamName()));
        if (TagParamColumn.STORE_ID.equals(info.getParamResult().getParamName()) || flag){
            join = " left join store z on z.company_info_id = b.company_info_id ";
        }
        String sql = "SELECT customer_id, count(%s) as total, %s as result, max(b.create_time) as last_time FROM " +
                "customer_follow b "+ join +" where ";
        if (isBigData){
            sql = sql.concat("b.p_date >= %s and b.p_date <= %s ");
        } else {
            sql = sql.concat("b.create_time >= %s and b.create_time <= %s ");
        }
        return sql;
    }

    private static final String PREFERENCE_GROUP_SQL_STR = "GROUP BY b.customer_id, %s ORDER BY b.customer_id ";

    private static final String PREFERENCE_RANG_WHERE_STR = "AND date_format(b.create_time,'HH:mm') >= '%s' AND " +
            "date_format(b.create_time,'HH:mm') " +
            "< '%s' ";

    private static final String PREFERENCE_TIME_AND_MONEY_GROUP_SQL_STR = "GROUP BY b.customer_id ";

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
                whereColumn(paramInfo, where, "AND");
            });
        } else {
            List<StatisticsTagParamInfo> paramInfoList = info.getParamInfoList();
            if (paramInfoList.size() == 1){
                whereColumn(paramInfoList.get(0), where, "AND");
            } else {
                for (int i = 0; i < paramInfoList.size(); i++){
                    StatisticsTagParamInfo paramInfo = paramInfoList.get(i);
                    // b.create_time >= '2020-07-20' AND (b.goods_info_id IN ( 'GOODS_ID', 'GOODS_ID_2' ) or b.brand_id IN ( 1, 2 ))
                    // or条件首次要处理AND(,末次要处理)
                    if (i == 0){
                        whereColumn(paramInfo, where, "AND (");
                        continue;
                    }
                    whereColumn(paramInfo, where, "or");
                    if (i == paramInfoList.size()-1){
                        where.append(")");
                    }
                }
            }
        }

        sql =
                sql.concat(where.toString()).concat(GROUP_BY_MAP.get(info.getParamResult().getParamName())).concat(")" +
                        " y ");

        // 指标值关联表取name
        if (TagType.QUOTA.equals(tagInfo.getTagType())){
            sql = sql.concat(" ) y ");
            if (TAG_NAME_MAP.containsKey(info.getParamResult().getParamName())){
                String join = TAG_NAME_MAP.get(info.getParamResult().getParamName());
                sql = sql.concat(join);
            }
            sql = sql.concat(" where y.rankNum <= 3  ").concat(" group by y.customer_id");
        } else if (TagType.MULTIPLE.equals(tagInfo.getTagType()) || TagType.RANGE.equals(tagInfo.getTagType())){ // 指标值范围
            if (TagParamColumn.STORE_ID.equals(info.getParamResult().getParamName())){
                sql = sql.concat(" left join store z on y.company_info_id = z.company_info_id ");
            }
            sql = this.range(sql, info);
        }
        return sql;
    }

    private String firstLase(StatisticsTagInfo tagInfo){
        StatisticsDimensionInfo info = tagInfo.getDimensionInfoList().get(0);
        String sql = getFrontSql(true, tagInfo).concat(" ");
        // where 过滤条件
        String where = firstWhere(info);
        sql = sql.concat(where).concat(FIRST_OR_LAST_JOIN_SQL_A);
        boolean flag = info.getParamInfoList().stream().anyMatch(x -> TagParamColumn.STORE_ID.equals(x.getParamName()));
        if (TagParamColumn.STORE_ID.equals(info.getParamResult().getParamName()) || flag){
            sql = sql.concat(" left join store z on b.company_info_id = z.company_info_id ");
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
            } else if (TagParamColumn.TO_DATE_NUM.equals(resultParamInfo.getParamName())){
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
        } else if (TagType.QUOTA.equals(tagInfo.getTagType())){ // 指标值关联表取name
            String notNull = "";
            if (WHERE_NO_NULL_MAP.containsKey(info.getParamResult().getParamName())){
                notNull = (WHERE_NO_NULL_MAP.get(info.getParamResult().getParamName()));
            }
            sql = sql.concat(" where 1 = 1 ").concat(where).concat(notNull).concat(") h group by h.customer_id," +
                            QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP_COMMON.get(info.getParamResult().getParamName()) +") h ) t ");
            if (FIRST_TAG_NAME_MAP.containsKey(info.getParamResult().getParamName())){
                String join = FIRST_TAG_NAME_MAP.get(info.getParamResult().getParamName());
                sql = sql.concat(join);
            }
            sql = sql.concat(" where rankNum <= 3 GROUP BY t.customer_id ");
        }
        return sql;
    }

    private String firstWhere(StatisticsDimensionInfo info){
        // where 过滤条件
        StringBuilder where = new StringBuilder();
        if (RelationType.AND.equals(info.getRelationType())){
            info.getParamInfoList().forEach(paramInfo -> {
                whereColumn(paramInfo, where, "AND");
            });
        } else {
            List<StatisticsTagParamInfo> paramInfoList = info.getParamInfoList();
            if (paramInfoList.size() == 1){
                whereColumn(paramInfoList.get(0), where, "AND");
            } else {
                for (int i = 0; i < paramInfoList.size(); i++){
                    StatisticsTagParamInfo paramInfo = paramInfoList.get(i);
                    // b.create_time >= '2020-07-20' AND (b.goods_info_id IN ( 'GOODS_ID', 'GOODS_ID_2' ) or b.brand_id IN ( 1, 2 ))
                    // or条件首次要处理AND(,末次要处理)
                    if (i == 0){
                        whereColumn(paramInfo, where, "AND (");
                        continue;
                    }
                    whereColumn(paramInfo, where, "or");
                    if (i == paramInfoList.size()-1){
                        where.append(")");
                    }
                }
            }
        }
        return where.toString();
    }

    private void whereColumn(StatisticsTagParamInfo paramInfo, StringBuilder where, String andOr){
        // GOODS_ID 要处理字符串引号问题
        if (TagParamColumn.TIME.equals(paramInfo.getParamName())){
            List<String> times = Arrays.asList(paramInfo.getParamValue().split(","));
            if (times.size() == 1){
                String time = times.get(0);
                List<String> timeList = new ArrayList<>(Arrays.asList(time.split("-")));
                where.append(andOr).append(String.format(WHERE_COLUMN_MAP.get(paramInfo.getParamName()),
                        timeList.toArray()));
            } else {
                for (int i = 0; i < times.size(); i++){
                    String time = times.get(i);
                    List<String> timeList = new ArrayList<>(Arrays.asList(time.split("-")));
                    if (i == 0){
                        where.append(andOr).append(" (").append(String.format(WHERE_COLUMN_MAP.get(paramInfo.getParamName()),
                                timeList.toArray()));
                        continue;
                    }
                    where.append("OR").append(String.format(WHERE_COLUMN_MAP.get(paramInfo.getParamName()),
                            timeList.toArray()));

                    if (i == times.size() - 1){
                        where.append(")");
                    }
                }
            }
        } else if (TagParamColumn.GOODS_ID.equals(paramInfo.getParamName())){
            StringBuilder goodsInfoId = new StringBuilder();
            Arrays.asList(paramInfo.getParamValue().split(",")).forEach(str->{
                goodsInfoId.append("'").append(str).append("',");
            });
            String str = goodsInfoId.substring(0, goodsInfoId.length() -1);
            where.append(andOr).append(String.format(WHERE_COLUMN_MAP.get(paramInfo.getParamName()),
                    Collections.singletonList(str).toArray()));
        } else if (paramInfo.getParamName().equals(TagParamColumn.STORE_ID)) {
            where.append(andOr).append(" z.store_id in (").append(paramInfo.getParamValue()).append(") ");
        } else {
            where.append(andOr).append(String.format(WHERE_COLUMN_MAP.get(paramInfo.getParamName()),
                    Collections.singletonList(paramInfo.getParamValue()).toArray()));
        }
    }

    // 指标值范围
    private String range (String sql, StatisticsDimensionInfo info){
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
                TagParamColumn.DAY_NUM.equals(resultParamInfo.getParamName())){
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
        return sql.concat(having.toString());
    }

    // 偏好类
    private String preference (StatisticsTagInfo tagInfo){
        StatisticsDimensionInfo info = tagInfo.getDimensionInfoList().get(0);

        String notNull = "";
        if (WHERE_NO_NULL_MAP.containsKey(info.getParamResult().getParamName())){
            notNull = WHERE_NO_NULL_MAP.get(info.getParamResult().getParamName());
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
                    "customer_id",
                    "'" + item1 + "-" + item2 + "'",
                    // 开始时间
                    "date_format(date_sub(NOW(), interval " + tagInfo.getDayNum() + " day), '%Y-%m-%d')",
                    // 结束时间
                    "date_format(NOW(), '%Y-%m-%d')"
            };
            sql = sql.concat(String.format(preferenceSelectSqlStr(tagInfo.isBigData(), info), preferenceFirstSqlArr));
        } else if (TagParamColumn.STORE_ID.equals(info.getParamResult().getParamName())) {
            Object[] preferenceFirstSqlArr = {
                    // sql结果集
                    "z.store_id",
                    "z.store_id",
                    // 开始时间
                    "date_format(date_sub(NOW(), interval " + tagInfo.getDayNum() + " day), '%Y-%m-%d')",
                    // 结束时间
                    "date_format(NOW(), '%Y-%m-%d')"
            };
            sql = sql.concat(String.format(preferenceSelectSqlStr(tagInfo.isBigData(), info), preferenceFirstSqlArr));
        }else {
            Object[] preferenceFirstSqlArr = {
                    // sql结果集
                    PREFERENCE_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()),
                    PREFERENCE_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()),
                    // 开始时间
                    "date_format(date_sub(NOW(), interval " + tagInfo.getDayNum() + " day), '%Y-%m-%d')",
                    // 结束时间
                    "date_format(NOW(), '%Y-%m-%d')"
            };
            sql = sql.concat(String.format(preferenceSelectSqlStr(tagInfo.isBigData(), info), preferenceFirstSqlArr));
        }

        // 时间查询后紧跟非空筛选
        sql = sql.concat(notNull);

        StringBuilder where = new StringBuilder();
        if (RelationType.AND.equals(info.getRelationType())){
            info.getParamInfoList().forEach(paramInfo -> {
                whereColumn(paramInfo, where, "AND");
            });
        } else {
            List<StatisticsTagParamInfo> paramInfoList = info.getParamInfoList();
            if (paramInfoList.size() == 1){
                whereColumn(paramInfoList.get(0), where, "AND");
            } else {
                for (int i = 0; i < paramInfoList.size(); i++){
                    StatisticsTagParamInfo paramInfo = paramInfoList.get(i);
                    // b.create_time >= '2020-07-20' AND (b.goods_info_id IN ( 'GOODS_ID', 'GOODS_ID_2' ) or b.brand_id IN ( 1, 2 ))
                    // or条件首次要处理AND(,末次要处理)
                    if (i == 0){
                        whereColumn(paramInfo, where, "AND (");
                        continue;
                    }
                    whereColumn(paramInfo, where, "or");
                    if (i == paramInfoList.size() - 1){
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
                            "customer_id",
                            "'" + item1 + "-" + item2 + "'",
                            // 开始时间
                            "date_format(date_sub(NOW(), interval " + tagInfo.getDayNum() + " day), '%Y-%m-%d')",
                            // 结束时间
                            "date_format(NOW(), '%Y-%m-%d')"
                    };

                    sql = sql.concat(String.format(preferenceSelectSqlStr(tagInfo.isBigData(), info),
                            preferenceFirstSqlArr))
                            .concat(notNull)
                            .concat(where.toString())
                            .concat(rangWhere).concat(PREFERENCE_TIME_AND_MONEY_GROUP_SQL_STR);
                }
                if (i != maps.size() -1){
                    sql = sql.concat(" union ");
                }
            }
        } else if (TagParamColumn.STORE_ID.equals(info.getParamResult().getParamName())){
            sql = sqlItem.concat(String.format(PREFERENCE_GROUP_SQL_STR, "z.store_id"));
        }else  {
            sql = sqlItem.concat(String.format(PREFERENCE_GROUP_SQL_STR,
                    PREFERENCE_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName())));
        }
        sql = sql.concat(preferenceLastSqlStr(tagInfo.isBigData()));
        sql = sql.concat(info.getParamResult().getParamValue());
        return sql;
    }

    @Override
    public DimensionName[] supports() {
        return new DimensionName[] {DimensionName.ACCESS};
    }

    @Override
    public String getSql(StatisticsTagInfo tagInfo){
        String sql = "";
        if (TagDimensionFirstLastType.NO_FIRST_LAST.equals(tagInfo.getDimensionInfoList().get(0).getDimensionType())){
            sql = notFirstLast(tagInfo);
        } else {
            sql = firstLase(tagInfo);
        }
        log.info("DimensionName.ACCESS SQL:{}", sql);
        return sql;
    }

    public static void main(String[] args) {
        CustomerFollowSql sql = new CustomerFollowSql();
        StatisticsTagInfo info = new StatisticsTagInfo();
        info.setTagId(1L);
        info.setTagName("标签值标签");
        info.setTagType(TagType.PREFERENCE);
        info.setRelationType(RelationType.AND);
        info.setDayNum(30);
        info.setBigData(false);

        StatisticsDimensionInfo statisticsDimensionInfo = new StatisticsDimensionInfo();
        statisticsDimensionInfo.setDimensionName(DimensionName.ACCESS);
        statisticsDimensionInfo.setDimensionType(TagDimensionFirstLastType.FIRST);
        statisticsDimensionInfo.setDimensionType(TagDimensionFirstLastType.NO_FIRST_LAST);
        statisticsDimensionInfo.setRelationType(RelationType.OR);
        StatisticsTagParamInfo paramInfo = new StatisticsTagParamInfo();
        paramInfo.setParamName(TagParamColumn.STORE_ID);
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

        StatisticsTagParamInfo paramInfo1 = new StatisticsTagParamInfo();
        paramInfo1.setParamName(TagParamColumn.GOODS_ID);
        paramInfo1.setParamValue("1,2");
        StatisticsTagParamInfo paramInfo2 = new StatisticsTagParamInfo();
        paramInfo2.setParamName(TagParamColumn.BRAND_ID);
        paramInfo2.setParamValue("1,2");
        StatisticsTagParamInfo paramInfo3 = new StatisticsTagParamInfo();
        paramInfo3.setParamName(TagParamColumn.TIME);
        paramInfo3.setParamValue("10:00-11:00,12:00-13:00");
        StatisticsTagParamInfo paramInfo4 = new StatisticsTagParamInfo();
        paramInfo4.setParamName(TagParamColumn.STORE_ID);
        paramInfo4.setParamValue("1,2");
        List<StatisticsTagParamInfo> s = new ArrayList<>();
        s.add(paramInfo1);
        s.add(paramInfo2);
//        s.add(paramInfo3);
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