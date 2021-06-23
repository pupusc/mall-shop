package com.wanmi.sbc.goods.bookingsalegoods.service;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.AppointmentStatus;
import com.wanmi.sbc.goods.bean.enums.BookingSaleTimeStatus;
import com.wanmi.sbc.goods.bean.vo.BookingSaleGoodsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>预售活动商品信息表分页查询请求参数</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingGoodsInfoSimpleCriterIaBuilder extends BaseQueryRequest {


    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 预售类型 0：全款预售  1：定金预售
     */
    @ApiModelProperty(value = "预售类型 0：全款预售  1：定金预售")
    private Integer bookingType;


    /**
     * 状态  0:全部 1:进行中，2 已暂停 3 未开始 4. 已结束
     */
    @ApiModelProperty(value = "状态  0:全部 1:进行中，2 已暂停 3 未开始 4. 已结束")
    private AppointmentStatus queryTab;


    /**
     * 商品skuId
     */
    @ApiModelProperty(value = "批量商品skuId")
    private List<String> goodsInfoIds;

    /**
     * 店铺storeId
     */
    @ApiModelProperty(value = "店铺storeId")
    private List<Long> storeIds;

    /**
     * 排序标识
     * 0:销量倒序
     * 1:好评数倒序
     * 2:评论率倒序
     * 3:排序号倒序
     * 4:按定金支付数量倒序
     */
    @ApiModelProperty(value = "排序标识")
    private Integer sortFlag;

    /**
     * 预售时间筛选枚举，每个枚举之间是以or关系
     */
    @ApiModelProperty(value = "预售时间筛选枚举")
    private List<BookingSaleTimeStatus> bookingSaleTimeStatuses;


    /**
     * 查询拼团活动商品信息
     *
     * @return
     */
    public String getQuerySql() {
        StringBuilder sb = new StringBuilder();
        sb.append("select b.id as `id`, ");
        sb.append("       a.goods_id as `goodsId`, ");
        sb.append("       a.store_id as `storeId`, ");
        sb.append("       a.goods_info_id as `goodsInfoId`, ");
        sb.append("       good.goods_name as `goodsName`, ");
        sb.append("       good.goods_img as `goodsImg`, ");
        sb.append("       info.goods_info_img as `goodsInfoImg`, ");
        sb.append("       good.line_price as `linePrice`, ");
        sb.append("       info.market_price as `marketPrice`, ");
        sb.append("       a.hand_sel_price as `handSelPrice`, ");
        sb.append("       a.inflation_price as `inflationPrice`, ");
        sb.append("       a.booking_price as `bookingPrice`, ");
        sb.append("       b.booking_type as `bookingType`, ");
        sb.append("       b.hand_sel_start_time as `handSelStartTime`, ");
        sb.append("       b.hand_sel_end_time as `handSelEndTime`, ");
        sb.append("       b.tail_start_time as `tailStartTime`, ");
        sb.append("       b.tail_end_time as `tailEndTime`, ");
        sb.append("       b.booking_start_time as `bookingStartTime`, ");
        sb.append("       b.booking_end_time as `bookingEndTime`, ");
        sb.append("       (good.goods_favorable_comment_num/good.goods_evaluate_num) as `goodsFeedbackRate`, ");
        sb.append("       ((case when b.booking_type=0 then a.hand_sel_count else a.pay_count end) + good.sham_sales_num) as `goodsSalesNum` ");
        return sb.toString();
    }

    /**
     * 分页查询时查询拼团活动商品信息总数
     *
     * @return
     */
    public String getQueryTotalCountSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT count(1) AS `totalCount` from (SELECT a.goods_id ");

        return sb.toString();
    }

    /**
     * 拼接查询条件
     *
     * @return
     */
    public String getQueryConditionSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("from booking_sale_goods a ");
        sb.append("       inner join (select t.*,(case t.booking_type when 1 then t.hand_sel_end_time when 0 then t.booking_end_time end) selfEndTime from booking_sale t where t.end_time > now()" +
                "  and t.del_flag = 0 and t.pause_flag = 0" +
                ") b on a.booking_sale_id = b.id ");
        sb.append("       inner join goods_info info on a.goods_info_id = info.goods_info_id ");
        sb.append("       inner join goods good on a.goods_id = good.goods_id ");
        sb.append("where good.del_flag = 0 ");
        sb.append("  and info.del_flag = 0 ");
        sb.append("  and info.added_flag = 1 ");
        sb.append("  and info.audit_status = 1 ");
        sb.append("  and info.stock > 0 ");
        sb.append("  and b.selfEndTime > now() ");
        sb.append(" and (info.vendibility = 1 or info.vendibility is null) ");
        sb.append(" and (info.provider_status = 1 or info.provider_status is null) ");

        // 商品名称模糊查询
        if (StringUtils.isNoneBlank(goodsName)) {
            sb.append("  and good.goods_name like concat('%', :goodsName ,'%')");
        }
        if (CollectionUtils.isNotEmpty(goodsInfoIds)) {
            sb.append("  and a.goods_info_id in (:goodsInfoIds)");
        }

        if(CollectionUtils.isNotEmpty(bookingSaleTimeStatuses)){
            List<String> timeWhere = new ArrayList<>();
            bookingSaleTimeStatuses.forEach(s -> {
                switch (s) {
                    case BOOKING_RUNNING:
                        timeWhere.add(" b.booking_start_time <= now() and b.booking_end_time >= now() ");
                        break;
                    case HAND_SEL_RUNNING:
                        timeWhere.add(" b.hand_sel_start_time <= now() and b.hand_sel_end_time >= now() ");
                        break;
                }
            });
            if(CollectionUtils.isNotEmpty(timeWhere)){
                sb.append(" and ( ").append(StringUtils.join(timeWhere, " or ")).append(" ) ");
            }
        }
        return sb.toString();
    }

    /**
     * 查询结果排序
     * 0:销量倒序
     * 1:好评数倒序
     * 2:评论率倒序
     * 3:排序号倒序
     * 4:定金支付数量倒序
     */
    public String getQuerySort() {
        if (Objects.nonNull(sortFlag)) {
            switch (sortFlag) {
                case 1:
                    return "order by good.goods_favorable_comment_num desc, start_time desc ";
                case 2:
                    return "order by goodsFeedbackRate desc, start_time desc ";
                case 3:
                    return "order by good.sort_no desc, start_time desc ";
                case 4:
                    return "order by a.hand_sel_count desc, start_time desc ";
                default:
                    return "order by goodsSalesNum desc, start_time desc ";
            }
        }
        StringBuilder sb = new StringBuilder();
//        sb.append("order by appointmentCount desc, b.start_time desc ");
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
    public static List<BookingSaleGoodsVO> converter(List<Map<String, Object>> sqlResult) {
        return sqlResult.stream().map(item ->
                BookingSaleGoodsVO.builder()
                        .bookingSaleId(toLong(item, "id"))
                        .storeId(toLong(item, "storeId"))
                        .goodsId(toStr(item, "goodsId"))
                        .goodsInfoId(toStr(item, "goodsInfoId"))
                        .goodsName(toStr(item, "goodsName"))
                        .goodsImg(toStr(item, "goodsImg"))
                        .marketPrice(toBigDecimal(item, "marketPrice"))
                        .linePrice(toBigDecimal(item, "linePrice"))
                        .handSelPrice(toBigDecimal(item, "handSelPrice"))
                        .inflationPrice(toBigDecimal(item, "inflationPrice"))
                        .bookingPrice(toBigDecimal(item, "bookingPrice"))
                        .handSelStartTime(toLocalDateTime(item, "handSelStartTime"))
                        .handSelEndTime(toLocalDateTime(item, "handSelEndTime"))
                        .tailStartTime(toLocalDateTime(item, "tailStartTime"))
                        .tailEndTime(toLocalDateTime(item, "tailEndTime"))
                        .bookingStartTime(toLocalDateTime(item, "bookingStartTime"))
                        .bookingEndTime(toLocalDateTime(item, "bookingEndTime"))
                        .bookingType(toInteger(item, "bookingType"))
                        .build()
        ).collect(Collectors.toList());
    }

    private static LocalDateTime toLocalDateTime(Map<String, Object> map, String key) {
        return map.get(key) != null ? Timestamp.valueOf(map.get(key).toString()).toLocalDateTime() : null;
    }

    private static String toStr(Map<String, Object> map, String key) {
        return map.get(key) != null ? map.get(key).toString() : null;
    }

    private static Integer toInteger(Map<String, Object> map, String key) {
        return map.get(key) != null ? Integer.parseInt(map.get(key).toString()) : null;
    }

    private static Long toLong(Map<String, Object> map, String key) {
        return map.get(key) != null ? Long.parseLong(map.get(key).toString()) : null;
    }

    private static BigDecimal toBigDecimal(Map<String, Object> map, String key) {
        return map.get(key) != null ? new BigDecimal(map.get(key).toString()) : null;
    }

}