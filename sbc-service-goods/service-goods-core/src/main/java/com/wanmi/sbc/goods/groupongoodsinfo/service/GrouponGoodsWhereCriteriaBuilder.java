package com.wanmi.sbc.goods.groupongoodsinfo.service;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsVO;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class GrouponGoodsWhereCriteriaBuilder extends BaseQueryRequest {

    /**
     * 拼团分类ID
     */
    private String grouponCateId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 是否精选，0：否，1：是
     */
    private Boolean sticky;

    /**
     * 店铺Id
     */
    private Long storeId;

    /**
     * 查询拼团活动商品信息
     *
     * @return
     */
    public String getQuerySql() {
        StringBuilder sb = new StringBuilder();
        sb.append("select groupon.groupon_activity_id as `grouponActivityId`, ");
        sb.append("       groupon.goods_id as `goodsId`, ");
        sb.append("       groupon.goods_info_id as `goodsInfoId`, ");
        sb.append("       good.goods_name as `goodsName`, ");
        sb.append("       good.goods_img as `goodsImg`, ");
        sb.append("       info.market_price as `marketPrice`, ");
        sb.append("       groupon.groupon_price as `grouponPrice`, ");
        sb.append("       sum(groupon.already_groupon_num) as `alreadyGrouponNum` ");

        return sb.toString();
    }

    /**
     * 分页查询时查询拼团活动商品信息总数
     *
     * @return
     */
    public String getQueryTotalCountSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(1) AS `totalCount` from (SELECT groupon.goods_id ");

        return sb.toString();
    }

    /**
     * 拼接查询条件
     *
     * @return
     */
    public String getQueryConditionSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("from groupon_goods_info groupon ");
        sb.append("       left join goods_info info on groupon.goods_info_id = info.goods_info_id ");
        sb.append("       left join goods good on info.goods_id = good.goods_id ");
        sb.append("where good.del_flag = 0 ");
        sb.append("  and info.del_flag = 0 ");
        sb.append("  and info.distribution_goods_audit != 2 ");
        sb.append("  and info.added_flag = 1 ");
        sb.append("  and info.audit_status = 1 ");
//        sb.append("  and info.stock > 0 ");
        sb.append("  and groupon.audit_status = 1 ");
        sb.append("  and now() >= groupon.start_time and now() <= groupon.end_time ");
        sb.append(" and (info.vendibility = 1 or info.vendibility is null) ");
        sb.append(" and (info.provider_status = 1 or info.provider_status is null) ");

        // 店铺Id
        if(Objects.nonNull(storeId)) {
            sb.append("  and good.store_id = '" + storeId + "' ");
            sb.append("  and groupon.store_id = '" + storeId + "' ");
        }

        // 商品名称模糊查询
        if (StringUtils.isNoneBlank(goodsName)) {
            sb.append("  and good.goods_name like concat('%', :goodsName,'%') ");
        }
        // 拼团分类
        if (StringUtils.isNoneBlank(grouponCateId)) {
            sb.append("  and groupon.groupon_cate_id = :grouponCateId ");
        }
        // 是否默认精选分类
        if (sticky) {
            sb.append("  and groupon.sticky = 1 ");
        }
        sb.append("group by groupon.goods_id ");

        return sb.toString();
    }

    /**
     * 查询结果排序
     *
     * @return
     */
    public String getQuerySort() {
        StringBuilder sb = new StringBuilder();
        sb.append("order by alreadyGrouponNum desc, groupon.start_time desc ");

        return sb.toString();
    }

    /**
     * 查询列表总数子查询
     *
     * @return
     */
    public String getQueryTotalTemp() {
        StringBuilder sb = new StringBuilder();
        sb.append(") as temp ");

        return sb.toString();
    }


    /**
     * 查询对象转换
     *
     * @param sqlResult
     * @return
     */
    public static List<GrouponGoodsVO> converter(List<Map<String, Object>> sqlResult) {
        return sqlResult.stream().map(item ->
                GrouponGoodsVO.builder()
                        .grouponActivityId(toStr(item, "grouponActivityId"))
                        .goodsId(toStr(item, "goodsId"))
                        .goodsInfoId(toStr(item, "goodsInfoId"))
                        .goodsName(toStr(item, "goodsName"))
                        .goodsImg(toStr(item, "goodsImg"))
                        .marketPrice(toBigDecimal(item, "marketPrice"))
                        .grouponPrice(toBigDecimal(item, "grouponPrice"))
                        .alreadyGrouponNum(toInteger(item, "alreadyGrouponNum"))
                        .build()
        ).collect(Collectors.toList());
    }

    private static String toStr(Map<String, Object> map, String key) {
        return map.get(key) != null ? map.get(key).toString() : null;
    }

    private static Integer toInteger(Map<String, Object> map, String key) {
        return map.get(key) != null ? Integer.parseInt(map.get(key).toString()) : null;
    }

    private static BigDecimal toBigDecimal(Map<String, Object> map, String key) {
        return map.get(key) != null ? new BigDecimal(map.get(key).toString()) : null;
    }
}
