package com.wanmi.sbc.crm.autotagstatistics.sql;

import com.wanmi.sbc.crm.autotagstatistics.*;
import com.wanmi.sbc.crm.bean.enums.*;
import com.wanmi.sbc.crm.bean.vo.RangeParamVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @program: sbc-micro-service-A
 * @description: 分享赚
 * @create: 2020-08-20 16:20
 **/
@Service
@Slf4j
public class CommissionSql extends SqlTool {
    private final Map<TagParamColumn,String> RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
//            put(TagParamColumn.TERMINAL_SOURCE,"a.terminal_source");
            put(TagParamColumn.NUM,"COUNT(*) as total");
            put(TagParamColumn.DAY_NUM,"COUNT(DISTINCT DATE(a.pay_time)) as total");
            put(TagParamColumn.TIME,"a.pay_time");
            put(TagParamColumn.DATE,"WEEKDAY(a.pay_time) as days");
            put(TagParamColumn.CATE_TOP_ID,"b.cate_top_id");
            put(TagParamColumn.CATE_ID,"b.cate_id");
            put(TagParamColumn.BRAND_ID,"b.brand_id");
            put(TagParamColumn.GOODS_ID,"b.goods_id");
            put(TagParamColumn.STORE_ID,"a.store_id");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM,"SUM(a.order_goods_price) as order_goods_price");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM,"SUM(a.commission_goods) as commission_goods");
        }
    };

    private final Map<TagParamColumn,String> FRONT_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
//            put(TagParamColumn.TERMINAL_SOURCE,"y.terminal_source");
            put(TagParamColumn.NUM,"y.total");
            put(TagParamColumn.DAY_NUM,"y.total");
            put(TagParamColumn.TIME,"y.pay_time");
            put(TagParamColumn.DATE,"y.days");
            put(TagParamColumn.CATE_TOP_ID,"z.cate_name");
            put(TagParamColumn.CATE_ID,"z.cate_name");
            put(TagParamColumn.BRAND_ID,"z.brand_name");
            put(TagParamColumn.GOODS_ID,"z.goods_name");
            put(TagParamColumn.STORE_ID,"z.store_name");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM,"y.order_goods_price");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM,"y.commission_goods");
        }
    };

    private final Map<TagParamColumn,String> QUOTA_FRONT_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
            put(TagParamColumn.NUM,"y.total");
            put(TagParamColumn.DAY_NUM,"y.total");
            put(TagParamColumn.TIME,"y.pay_time");
            put(TagParamColumn.DATE,"y.days");
            put(TagParamColumn.CATE_TOP_ID,"y.cate_top_id");
            put(TagParamColumn.CATE_ID,"y.cate_id");
            put(TagParamColumn.BRAND_ID,"y.brand_id");
            put(TagParamColumn.GOODS_ID,"y.goods_id");
            put(TagParamColumn.STORE_ID,"y.store_id");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM,"y.order_goods_price");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM,"y.commission_goods");
        }
    };

    private final Map<TagParamColumn,String> FRONT_FIRST_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
            put(TagParamColumn.TO_DATE_NUM,"t.days");
            put(TagParamColumn.TIME,"t.pay_time");
            put(TagParamColumn.DATE,"t.days");
            put(TagParamColumn.CATE_TOP_ID,"z.cate_name");
            put(TagParamColumn.CATE_ID,"z.cate_name");
            put(TagParamColumn.BRAND_ID,"z.brand_name");
            put(TagParamColumn.GOODS_ID,"z.goods_name");
            put(TagParamColumn.STORE_ID,"z.store_name");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM,"t.order_goods_price");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM,"t.commission_goods");
        }
    };

    private final Map<TagParamColumn,String> QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8663638743504508858L;
        {
            put(TagParamColumn.TO_DATE_NUM,"DATEDIFF(NOW(),a.pay_time) as days");
            put(TagParamColumn.TIME,"a.pay_time");
            put(TagParamColumn.DATE,"WEEKDAY(a.pay_time) as days");
            put(TagParamColumn.CATE_TOP_ID,"b.cate_top_id");
            put(TagParamColumn.CATE_ID,"b.cate_id");
            put(TagParamColumn.BRAND_ID,"b.brand_id");
            put(TagParamColumn.GOODS_ID,"b.goods_id");
            put(TagParamColumn.STORE_ID,"a.store_id");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM,"a.order_goods_price");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM,"a.commission_goods");
        }
    };

    private final Map<TagParamColumn,String> QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP_COMMON = new HashMap<TagParamColumn,
            String>(){
        {
            put(TagParamColumn.TO_DATE_NUM,"h.days");
            put(TagParamColumn.TIME,"h.pay_time");
            put(TagParamColumn.DATE,"h.days");
            put(TagParamColumn.CATE_TOP_ID,"h.cate_top_id");
            put(TagParamColumn.CATE_ID,"h.cate_id");
            put(TagParamColumn.BRAND_ID,"h.brand_id");
            put(TagParamColumn.GOODS_ID,"h.goods_id");
            put(TagParamColumn.STORE_ID,"h.store_id");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM,"h.order_goods_price");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM,"h.commission_goods");
        }
    };

    private final Map<TagParamColumn,String> GROUP_BY_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8076938296165644926L;
        {
//            put(TagParamColumn.TERMINAL_SOURCE, "GROUP BY a.customer_id, a.terminal_source ");
            put(TagParamColumn.NUM, "GROUP BY a.distributor_customer_id ");
            put(TagParamColumn.DAY_NUM, "GROUP BY a.distributor_customer_id ");
            put(TagParamColumn.TIME,"GROUP BY a.distributor_customer_id, a.pay_time ");
            put(TagParamColumn.DATE,"GROUP BY a.distributor_customer_id, WEEKDAY(a.pay_time) ");
            put(TagParamColumn.CATE_TOP_ID,"GROUP BY a.distributor_customer_id, b.cate_top_id ");
            put(TagParamColumn.CATE_ID,"GROUP BY a.distributor_customer_id, b.cate_id ");
            put(TagParamColumn.BRAND_ID,"GROUP BY a.distributor_customer_id, b.brand_id ");
            put(TagParamColumn.GOODS_ID,"GROUP BY a.distributor_customer_id, b.goods_id ");
            put(TagParamColumn.STORE_ID,"GROUP BY a.distributor_customer_id, a.store_id ");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM,"GROUP BY a.distributor_customer_id ");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM,"GROUP BY a.distributor_customer_id ");
        }
    };

    private final Map<TagParamColumn,String> WHERE_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 4509761025924962579L;
        {
//            put(TagParamColumn.TERMINAL_SOURCE, " a.terminal_source in (%s) ");
            put(TagParamColumn.TIME," (date_format(a.pay_time,'HH:mm')>= '%s' AND date_format(a.pay_time,'HH:mm') < '%s') ");
            put(TagParamColumn.DATE," WEEKDAY(a.pay_time) IN (%s) ");
            put(TagParamColumn.CATE_TOP_ID," b.cate_top_id in (%s) ");
            put(TagParamColumn.CATE_ID," b.cate_id in (%s) ");
            put(TagParamColumn.BRAND_ID," b.brand_id in (%s) ");
            put(TagParamColumn.GOODS_ID," b.goods_id in (%s) ");
            put(TagParamColumn.STORE_ID," a.store_id in (%s) ");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM," a.order_goods_price %s ");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM," (a.commission_goods %s and a.commission_state = 1) ");
        }
    };

    private final Map<TagParamColumn,String> WHERE_NO_NULL_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
//            put(TagParamColumn.TERMINAL_SOURCE," and a.terminal_source is not null ");
            put(TagParamColumn.DAY_NUM, " and a.pay_time is not null ");
            put(TagParamColumn.TO_DATE_NUM," and a.pay_time is not null ");
            put(TagParamColumn.TIME," and a.pay_time is not null ");
            put(TagParamColumn.DATE," and a.pay_time is not null ");
            put(TagParamColumn.CATE_TOP_ID," and b.cate_top_id is not null ");
            put(TagParamColumn.CATE_ID," and b.cate_id is not null ");
            put(TagParamColumn.BRAND_ID," and b.brand_id is not null ");
            put(TagParamColumn.GOODS_ID," and b.goods_id is not null ");
            put(TagParamColumn.STORE_ID," and a.store_id is not null ");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM," and a.order_goods_price is not null ");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM," and a.commission_goods is not null ");
        }
    };

    private final Map<TagParamColumn,String> TAG_NAME_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
            put(TagParamColumn.CATE_TOP_ID," left join goods_cate z on z.cate_id = y.cate_top_id ");
            put(TagParamColumn.CATE_ID," left join goods_cate z on z.cate_id = y.cate_id ");
            put(TagParamColumn.BRAND_ID," left join goods_brand z on z.brand_id = y.brand_id ");
            put(TagParamColumn.GOODS_ID," left join goods z on z.goods_id = y.goods_id ");
            put(TagParamColumn.STORE_ID," left join store z on z.store_id = y.store_id ");
        }
    };

    private final Map<TagParamColumn,String> FIRST_TAG_NAME_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 7092314267873635659L;
        {
            put(TagParamColumn.CATE_TOP_ID," left join goods_cate z on z.cate_id =  t.cate_top_id ");
            put(TagParamColumn.CATE_ID," left join goods_cate z on z.cate_id = t.cate_id ");
            put(TagParamColumn.BRAND_ID," left join goods_brand z on z.brand_id = t.brand_id ");
            put(TagParamColumn.GOODS_ID," left join goods z on z.goods_id = t.goods_id ");
            put(TagParamColumn.STORE_ID," left join store z on z.store_id = t.store_id ");
        }
    };

    // 非首（末） 指标值范围
    private final Map<TagParamColumn,String> HAVING_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = -5256974995591967200L;
        {
//            put(TagParamColumn.TERMINAL_SOURCE," y.terminal_source in (%s) ");
            put(TagParamColumn.NUM," y.total %s ");
            put(TagParamColumn.DAY_NUM," y.total %s ");
            put(TagParamColumn.TIME," (date_format(y.pay_time,'HH:mm') >= '%s' AND date_format(y" +
                    ".pay_time,'HH:mm') < " +
                    "'%s') ");
            put(TagParamColumn.DATE," y.days IN (%s) ");
            put(TagParamColumn.CATE_TOP_ID," y.cate_top_id in (%s) ");
            put(TagParamColumn.CATE_ID," y.cate_id in (%s) ");
            put(TagParamColumn.BRAND_ID," y.brand_id in (%s) ");
            put(TagParamColumn.GOODS_ID," y.goods_id in (%s) ");
            put(TagParamColumn.STORE_ID," y.store_id in (%s) ");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM, " y.order_goods_price %s ");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM, " y.commission_goods %s ");
        }
    };

    // 首（末）指标值范围
    private final Map<TagParamColumn,String> FIRST_LAST_WHERE_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 8769617825667430529L;
        {
//            put(TagParamColumn.TERMINAL_SOURCE," a.terminal_source in (%s) ");
            put(TagParamColumn.TO_DATE_NUM,"  DATEDIFF(NOW(),a.pay_time) %s ");
            put(TagParamColumn.TIME," (date_format(a.pay_time,'HH:mm') >= '%s' AND date_format(a" +
                    ".pay_time,'HH:mm') < '%s') ");
            put(TagParamColumn.DATE," WEEKDAY(a.pay_time) IN (%s) ");
            put(TagParamColumn.CATE_TOP_ID," b.cate_top_id in (%s) ");
            put(TagParamColumn.CATE_ID," b.cate_id in (%s) ");
            put(TagParamColumn.BRAND_ID," b.brand_id in (%s) ");
            put(TagParamColumn.GOODS_ID," b.goods_id in (%s) ");
            put(TagParamColumn.STORE_ID," a.store_id in (%s) ");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM, " a.order_goods_price %s ");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM, " a.commission_goods %s ");
        }
    };

    // 偏好类
    private final Map<TagParamColumn,String> PREFERENCE_RESULT_COLUMN_MAP = new HashMap<TagParamColumn,String>(){
        private static final long serialVersionUID = 1819263693130501269L;
        {
//            put(TagParamColumn.TERMINAL_SOURCE,"a.terminal_source");
            put(TagParamColumn.TIME,"a.pay_time");
            put(TagParamColumn.DATE,"WEEKDAY(a.pay_time)");
            put(TagParamColumn.CATE_TOP_ID,"b.cate_top_id");
            put(TagParamColumn.CATE_ID,"b.cate_id");
            put(TagParamColumn.BRAND_ID,"b.brand_id");
            put(TagParamColumn.GOODS_ID,"b.goods_id");
            put(TagParamColumn.STORE_ID,"a.store_id");
            put(TagParamColumn.SHARE_GOODS_SALE_NUM, "a.order_goods_price");
            put(TagParamColumn.SHARE_GOODS_COMMISSION_NUM, "a.commission_goods");
        }
    };


    private String getFrontSql(boolean isFirst, StatisticsTagInfo tagInfo){
        StatisticsDimensionInfo info = tagInfo.getDimensionInfoList().get(0);
        String frontSql = "";
        String date = "";
        String queryTimeStr =
                "a.pay_time >= date_format(date_sub(NOW(), interval " + tagInfo.getDayNum() + " day), '%Y-%m-%d') " +
                        "AND " +
                        "a.pay_time < date_format(NOW(), '%Y-%m-%d')";
        if (tagInfo.isBigData()){
            date = ", ${date}";
            queryTimeStr =
                    "a.p_date >=  date_format(date_sub(CURRENT_TIMESTAMP, " + tagInfo.getDayNum() + "), 'yyyy-MM-dd')" +
                            " " +
                            "AND a.p_date < date_format(CURRENT_TIMESTAMP, 'yyyy-MM-dd')";
        }
        queryTimeStr = queryTimeStr.concat(" ");
        String str = "'" + tagInfo.getTagName() + "'  as tagName";

        if (TagParamColumn.SHARE_GOODS_COMMISSION_NUM.equals(info.getParamResult().getParamName())){
            queryTimeStr = queryTimeStr.concat("and a.commission_state = 1 ");
        }
        if (isFirst){
            // 判断首末
            String maxOrMin = "";
            if (TagDimensionFirstLastType.FIRST.equals(info.getDimensionType())){
                maxOrMin = "MIN";
            } else if (TagDimensionFirstLastType.LAST.equals(info.getDimensionType())){
                maxOrMin = "MAX";
            }
            if (TagType.QUOTA.equals(tagInfo.getTagType())){
                String time = " ,a.pay_time ";
                if (TagParamColumn.TIME.equals(info.getParamResult().getParamName())){
                    time = "";
                }
                String column = FRONT_FIRST_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName());
                str = QuotaSqlUtil.TagName(tagInfo.getTagName(), info, column);
                String resultCol = QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP_COMMON.get(info.getParamResult().getParamName());
                frontSql = "select " + tagInfo.getTagId() + " as tagId, " + str + ", " + tagInfo.getTagType().toValue() + " as " +
                        "tagType, t.distributor_customer_id as customer_id " + date + " FROM (select h" +
                        ".distributor_customer_id, " + resultCol +
                        ", row_number () over ( PARTITION BY h.distributor_customer_id ORDER BY last_time DESC ) AS rankNum " +
                        "from ( select h.distributor_customer_id, "+resultCol+",min(h.pay_time) as last_time from ( " +
                        "select a.distributor_customer_id, "+ QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) + time +
                        " from(SELECT a.distributor_customer_id," + maxOrMin + "(DISTINCT a" +
                        ".pay_time) as " +
                        "time_str FROM distribution_record a LEFT JOIN goods_info b on a.goods_info_id = b.goods_info_id WHERE " + queryTimeStr;
            } else {
                frontSql = "select " + tagInfo.getTagId() + " as tagId, " + str + ", " + tagInfo.getTagType().toValue() + " as " +
                        "tagType, a.distributor_customer_id as customer_id " + date + " FROM (SELECT a" +
                        ".distributor_customer_id," + maxOrMin + "(DISTINCT a" +
                        ".pay_time) as " +
                        "time_str FROM distribution_record a LEFT JOIN goods_info b on a.goods_info_id = b.goods_info_id WHERE " + queryTimeStr;
            }
        } else {
            if (TagType.QUOTA.equals(tagInfo.getTagType())){
                String column = FRONT_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName());
                str = QuotaSqlUtil.TagName(tagInfo.getTagName(), info, column);
                frontSql = "select " + tagInfo.getTagId() + " as tagId, " + str + "," + tagInfo.getTagType().toValue() + " as " +
                        "tagType, y.distributor_customer_id as customer_id " + date + " from (" +
                        "select y.distributor_customer_id, " + QUOTA_FRONT_RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) +
                        ",row_number() over ( PARTITION BY y.distributor_customer_id " +
                        " ORDER BY lastTime DESC ) AS rankNum  from (" +
                        " select a.distributor_customer_id, "
                        + RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) + " ,max( a.pay_time ) as " +
                        "lastTime from " +
                        "distribution_record a LEFT JOIN " +
                        "goods_info b on a.goods_info_id = b.goods_info_id WHERE " + queryTimeStr;
            } else {
                frontSql = "select " + tagInfo.getTagId() + " as tagId, " + str + "," + tagInfo.getTagType().toValue() + " as " +
                        "tagType, y.distributor_customer_id as customer_id " + date + " from ( select a" +
                        ".distributor_customer_id, "
                        + RESULT_COLUMN_MAP.get(info.getParamResult().getParamName()) + " from distribution_record a LEFT JOIN " +
                        "goods_info b on a.goods_info_id = b.goods_info_id WHERE " + queryTimeStr;
            }

        }

        // 指标值标签过滤指为空的数据
        if (TagType.QUOTA.equals(tagInfo.getTagType()) && WHERE_NO_NULL_MAP.containsKey(info.getParamResult().getParamName())){
            frontSql = frontSql.concat(WHERE_NO_NULL_MAP.get(info.getParamResult().getParamName()));
        }

        return frontSql;
    }



    private static final String FIRST_OR_LAST_JOIN_SQL = "GROUP BY a.distributor_customer_id ) d JOIN distribution_record a ON d.distributor_customer_id = a" +
            ".distributor_customer_id AND d.time_str = a.pay_time ";

    private static final String FIRST_OR_LAST_JOIN_SQL_1 = "JOIN goods_info b on b.goods_info_id = a.goods_info_id ";

    // ------------------------------------------------ 偏好类 start-----------------------------------------------------
    private String preferenceFirstSqlStr(boolean isBigData){
        String sql = "SELECT %d AS tagId, '%s' as tagName, '%s' as tagType, %d as type, resAll.distributor_customer_id, " +
                "'%s' as dimensionType, resAll.result as dimensionId, resAll.total as num FROM (SELECT @rankNum := IF( " +
                "@customerId = res.distributor_customer_id, @rankNum + 1, 1 ) as rankNum, @customerId := res.distributor_customer_id as " +
                "distributor_customer_id, res.total, res.result FROM ( ";
        if (isBigData){
            sql = "SELECT %d AS tagId, '%s' as tagName, '%s' as tagType, %d as type, resAll.distributor_customer_id, " +
                    "'%s' as dimensionType, resAll.result as dimensionId, resAll.total as num, ${date} FROM (SELECT row_number" +
                    "() over " +
                    "(partition by res.distributor_customer_id order by res.total desc, res.last_time desc ) rankNum, res.* FROM ( ";
        }
        return sql;
    }

    private String preferenceSelectSqlStr(boolean isBigData){
        String sql = "SELECT a.distributor_customer_id, count(%s) as total, %s as result,  max(a.pay_time) as last_time " +
                "FROM distribution_record a LEFT JOIN goods_info b on a.goods_info_id = b.goods_info_id where ";
        if (isBigData){
            sql = sql.concat("a.p_date >= %s and a.p_date <= %s ");
        } else {
            sql = sql.concat("a.pay_time >= %s and a.pay_time <= %s ");
        }
        return sql;
    }

    private static final String PREFERENCE_GROUP_SQL_STR = "GROUP BY a.distributor_customer_id, %s ORDER BY a.distributor_customer_id ";

    private static final String PREFERENCE_RANG_WHERE_STR = "AND date_format(a.pay_time,'HH:mm') >= '%s' AND " +
            "date_format(a.pay_time ,'HH:mm') < '%s' ";

    private static final String PREFERENCE_TIME_AND_MONEY_GROUP_SQL_STR = "GROUP BY distributor_customer_id ";

    private String preferenceLastSqlStr(boolean isBigData){
        String sql = ") res,( SELECT @customerId := '', @rankNum := 0 ) rank " +
                "ORDER BY res.distributor_customer_id, res.total DESC ) resAll WHERE resAll.rankNum <= ";
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
        String sql = getFrontSql(false, tagInfo);

        StringBuilder where = new StringBuilder();
        if (RelationType.AND.equals(info.getRelationType())){
            info.getParamInfoList().forEach(paramInfo -> {
                whereColumn(paramInfo, where, "AND");
            });
        } else {
            List<StatisticsTagParamInfo> paramInfoList = info.getParamInfoList();
            if (paramInfoList.size() == 1) {
                whereColumn(paramInfoList.get(0), where, "AND");
            } else {
                for (int i = 0; i < paramInfoList.size(); i++) {
                    StatisticsTagParamInfo paramInfo = paramInfoList.get(i);
                    // b.create_time >= '2020-07-20' AND (b.goods_info_id IN ( 'GOODS_ID', 'GOODS_ID_2' ) or b.brand_id IN ( 1, 2 ))
                    // or条件首次要处理AND(,末次要处理)
                    if (i == 0) {
                        whereColumn(paramInfo, where, "AND (");
                        continue;
                    }
                    whereColumn(paramInfo, where, "or");
                    if (i == paramInfoList.size() - 1) {
                        where.append(")");
                    }

                }
            }
        }

        sql = sql.concat(where.toString()).concat(GROUP_BY_MAP.get(info.getParamResult().getParamName())).concat(") y" +
                " ");

        // 指标值关联表取name
        if (TagType.QUOTA.equals(tagInfo.getTagType())){
            sql = sql.concat(") y ");
            if (TAG_NAME_MAP.containsKey(info.getParamResult().getParamName())){
                String join = TAG_NAME_MAP.get(info.getParamResult().getParamName());
                sql = sql.concat(join);
            }
            sql = sql.concat(" where y.rankNum <= 3  ").concat(" group by y.distributor_customer_id");
        } else if (TagType.MULTIPLE.equals(tagInfo.getTagType()) || TagType.RANGE.equals(tagInfo.getTagType())){ // 指标值范围
            sql = sql.concat(this.range(info));
        }
        return sql;
    }

    // 指标值范围
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
                TagParamColumn.DAY_NUM.equals(resultParamInfo.getParamName()) ||
                TagParamColumn.SHARE_GOODS_SALE_NUM.equals(resultParamInfo.getParamName())
                || TagParamColumn.SHARE_GOODS_COMMISSION_NUM.equals(resultParamInfo.getParamName())){
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
                    "a.distributor_customer_id",
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
            if (TagParamColumn.SHARE_GOODS_COMMISSION_NUM.equals(info.getParamResult().getParamName())){
                sql = sql.concat(" and a.commission_state = 1 ");
            }
        }
        if (DimensionName.PAY_ORDER.equals(info.getDimensionName())){
            sql = sql.concat("and a.pay_state = 2 ");
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
            if (paramInfoList.size() == 1) {
                whereColumn(paramInfoList.get(0), where, "AND");
            } else {
                for (int i = 0; i < paramInfoList.size(); i++) {
                    StatisticsTagParamInfo paramInfo = paramInfoList.get(i);
                    // b.create_time >= '2020-07-20' AND (b.goods_info_id IN ( 'GOODS_ID', 'GOODS_ID_2' ) or b.brand_id IN ( 1, 2 ))
                    // or条件首次要处理AND(,末次要处理)
                    if (i == 0) {
                        whereColumn(paramInfo, where, "AND (");
                        continue;
                    }
                    whereColumn(paramInfo, where, "or");
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
                            "a.distributor_customer_id",
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
        String sql = getFrontSql(true, tagInfo);
        // where 过滤条件
        String where = firstWhere(info);
        sql = sql.concat(where).concat(FIRST_OR_LAST_JOIN_SQL);
        List<TagParamColumn> tagParamColumns = Arrays.asList(TagParamColumn.CATE_ID,
                TagParamColumn.CATE_TOP_ID, TagParamColumn.BRAND_ID,TagParamColumn.GOODS_ID);
        boolean flag = info.getParamInfoList().stream().anyMatch(x -> tagParamColumns.contains(x.getParamName()));
        if (tagParamColumns.contains(info.getParamResult().getParamName()) || flag) {
            sql = sql.concat(FIRST_OR_LAST_JOIN_SQL_1);
        }
        if (TagParamColumn.SHARE_GOODS_COMMISSION_NUM.equals(info.getParamResult().getParamName())){
            where = where.concat(" and a.commission_state = 1 ");
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
                    || TagParamColumn.SHARE_GOODS_SALE_NUM.equals(resultParamInfo.getParamName())
                    || TagParamColumn.SHARE_GOODS_COMMISSION_NUM.equals(resultParamInfo.getParamName())){
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
            } else {
                having.append(String.format(FIRST_LAST_WHERE_COLUMN_MAP.get(resultParamInfo.getParamName()),
                        Collections.singletonList(resultParamInfo.getParamValue()).toArray()));
            }
            sql = sql.concat(having.toString()).concat(where);
        } else if (TagType.QUOTA.equals(tagInfo.getTagType())){ // 指标值关联表取name
            String notNull = "";
            if (WHERE_NO_NULL_MAP.containsKey(info.getParamResult().getParamName())){
                notNull = (WHERE_NO_NULL_MAP.get(info.getParamResult().getParamName()));
            }
            sql = sql.concat(" where 1 = 1 ").concat(where).concat(notNull)
                    .concat(") h group by h.distributor_customer_id," +
                            QUOTA_FRONT_FIRST_RESULT_COLUMN_MAP_COMMON.get(info.getParamResult().getParamName()) +") h ) t ");
            if (FIRST_TAG_NAME_MAP.containsKey(info.getParamResult().getParamName())){
                String join = FIRST_TAG_NAME_MAP.get(info.getParamResult().getParamName());
                sql = sql.concat(join);
            }
            sql = sql.concat(" where rankNum <= 3 GROUP BY t.distributor_customer_id ");
        }
        return sql;
    }

    private String firstWhere(StatisticsDimensionInfo info){
        StringBuilder where = new StringBuilder();
        if (RelationType.AND.equals(info.getRelationType())){
            info.getParamInfoList().forEach(paramInfo -> {
                whereColumn(paramInfo, where, "AND");
            });
        } else {
            List<StatisticsTagParamInfo> paramInfoList = info.getParamInfoList();
            if (paramInfoList.size() == 1) {
                whereColumn(paramInfoList.get(0), where, "AND");
            } else {
                for (int i = 0; i < paramInfoList.size(); i++) {
                    StatisticsTagParamInfo paramInfo = paramInfoList.get(i);
                    // b.create_time >= '2020-07-20' AND (b.goods_info_id IN ( 'GOODS_ID', 'GOODS_ID_2' ) or b.brand_id IN ( 1, 2 ))
                    // or条件首次要处理AND(,末次要处理)
                    if (i == 0) {
                        whereColumn(paramInfo, where, "AND (");
                        continue;
                    }
                    whereColumn(paramInfo, where, "or");
                    if (i == paramInfoList.size() - 1) {
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
        } else if (TagParamColumn.SHARE_GOODS_COMMISSION_NUM.equals(paramInfo.getParamName())
                    || TagParamColumn.SHARE_GOODS_SALE_NUM.equals(paramInfo.getParamName())) {
            List<String> item = Arrays.asList(paramInfo.getParamValue().split(","));
            if (item.size() == 1){
                where.append(andOr).append(String.format(WHERE_COLUMN_MAP.get(paramInfo.getParamName()),
                        Collections.singletonList(item.get(0)).toArray()));
            } else {
                for (int i = 0; i< item.size(); i++){
                    if (i == 0){
                        where.append(andOr).append(" (").append(String.format(WHERE_COLUMN_MAP.get(paramInfo.getParamName()),
                                Collections.singletonList(item.get(i)).toArray()));
                        continue;
                    }

                    where.append("AND").append(String.format(WHERE_COLUMN_MAP.get(paramInfo.getParamName()),
                            Collections.singletonList(item.get(i)).toArray()));

                    if (i == item.size() - 1){
                        where.append(")");
                    }
                }
            }
        } else {
            where.append(andOr).append(String.format(WHERE_COLUMN_MAP.get(paramInfo.getParamName()),
                    Collections.singletonList(paramInfo.getParamValue()).toArray()));
        }
    }

    @Override
    public DimensionName[] supports() {
        return new DimensionName[] {DimensionName.COMMISSION};
    }

    @Override
    public String getSql(StatisticsTagInfo tagInfo){
        String sql = "";
        if (TagDimensionFirstLastType.NO_FIRST_LAST.equals(tagInfo.getDimensionInfoList().get(0).getDimensionType())){
            sql = notFirstLast(tagInfo);
        } else {
            sql = firstLase(tagInfo);
        }
        log.info("DimensionName.COMMISSION SQL:{}", sql);
        return sql;
    }


    public static void main(String[] args) {
        CommissionSql sql = new CommissionSql();
        StatisticsTagInfo info = new StatisticsTagInfo();
        info.setTagId(1L);
        info.setTagName("分享商品标签值标签");
        info.setTagType(TagType.QUOTA);
        info.setRelationType(RelationType.AND);
        info.setDayNum(30);
        info.setBigData(false);

        StatisticsDimensionInfo statisticsDimensionInfo = new StatisticsDimensionInfo();
        statisticsDimensionInfo.setDimensionName(DimensionName.COMMISSION);
        statisticsDimensionInfo.setDimensionType(TagDimensionFirstLastType.NO_FIRST_LAST);
        statisticsDimensionInfo.setDimensionType(TagDimensionFirstLastType.FIRST);
        statisticsDimensionInfo.setRelationType(RelationType.OR);
        StatisticsTagParamInfo paramInfo = new StatisticsTagParamInfo();
        paramInfo.setParamName(TagParamColumn.CATE_TOP_ID);
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
        paramInfo1.setParamName(TagParamColumn.GOODS_ID);
        paramInfo1.setParamValue("1,2");
        s.add(paramInfo1);
        StatisticsTagParamInfo paramInfo2 = new StatisticsTagParamInfo();
        paramInfo2.setParamName(TagParamColumn.BRAND_ID);
        paramInfo2.setParamValue("1,2");
        s.add(paramInfo2);
        StatisticsTagParamInfo paramInfo3 = new StatisticsTagParamInfo();
        paramInfo3.setParamName(TagParamColumn.TIME);
        paramInfo3.setParamValue("10:00-11:00,12:00-13:00");
        s.add(paramInfo3);
        StatisticsTagParamInfo paramInfo4 = new StatisticsTagParamInfo();
        paramInfo4.setParamName(TagParamColumn.CATE_TOP_ID);
        paramInfo4.setParamValue("1,2");
        s.add(paramInfo4);
        StatisticsTagParamInfo paramInfo5 = new StatisticsTagParamInfo();
        paramInfo5.setParamName(TagParamColumn.DATE);
        paramInfo5.setParamValue("1,2,3,4");
        s.add(paramInfo5);
        StatisticsTagParamInfo paramInfo6 = new StatisticsTagParamInfo();
        paramInfo6.setParamName(TagParamColumn.SHARE_GOODS_COMMISSION_NUM);
        paramInfo6.setParamValue("<10,<20");
        s.add(paramInfo6);

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