package com.wanmi.sbc.goods.appointmentsalegoods.service;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.AppointmentStatus;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleGoodsVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>预约活动商品信息表分页查询请求参数</p>
 *
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentGoodsInfoSimpleCriterIaBuilder extends BaseQueryRequest {


    private static final long serialVersionUID = 1154213435297457823L;


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
     * 预约类型 0：不预约不可购买  1：不预约可购买
     */
    @ApiModelProperty(value = "预约类型 0：不预约不可购买  1：不预约可购买")
    private Integer appointmentType;


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
     * 4:预约数倒序
     */
    @ApiModelProperty(value = "排序标识")
    private Integer sortFlag;

    /**
     * 状态 0：预约进行中
     */
    @ApiModelProperty(value = "预约状态 0：预约进行中")
    private Integer appointmentStatus;


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
        sb.append("       info.market_price as `marketPrice`, ");
        sb.append("       a.price as `price`, ");
        sb.append("       a.appointment_count as `appointmentCount`, ");
        sb.append("       b.appointment_start_time as `appointmentStartTime`, ");
        sb.append("       b.appointment_end_time as `appointmentEndTime`, ");
        sb.append("       b.snap_up_start_time as `snapUpStartTime`, ");
        sb.append("       b.snap_up_end_time as `snapUpEndTime`, ");
        sb.append("       (good.goods_favorable_comment_num/good.goods_evaluate_num) as `goodsFeedbackRate`, ");
        sb.append("       (a.buyer_count + good.sham_sales_num) as `goodsSalesNum` ");
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
        sb.append("from appointment_sale_goods a ");
        sb.append("       inner join appointment_sale b on a.appointment_sale_id = b.id ");
        sb.append("       inner join goods_info info on a.goods_info_id = info.goods_info_id ");
        sb.append("       inner join goods good on a.goods_id = good.goods_id ");
        sb.append("where good.del_flag = 0 ");
        sb.append("  and info.del_flag = 0 ");
        sb.append("  and info.added_flag = 1 ");
        sb.append("  and info.audit_status = 1 ");
        sb.append("  and b.del_flag = 0 ");
        sb.append("  and info.stock > 0 ");
        sb.append("  and b.snap_up_end_time > now()");
        sb.append(" and (info.vendibility = 1 or info.vendibility is null) ");
        sb.append(" and (info.provider_status = 1 or info.provider_status is null) ");

        // 商品名称模糊查询
        if (StringUtils.isNoneBlank(goodsName)) {
            sb.append("  and good.goods_name like concat('%', :goodsName, '%')");
        }
        if (CollectionUtils.isNotEmpty(goodsInfoIds)) {
            sb.append("  and a.goods_info_id in (:goodsInfoIds)");
        }

        //预约进行中(预约中和抢购中都视为进行中)
        if(Integer.valueOf(0).equals(appointmentStatus)){
            sb.append("  and b.appointment_start_time <= now()  and  snap_up_end_time >= now()");
        }

        // 活动进行中
        if(Objects.nonNull(queryTab) && queryTab.equals(AppointmentStatus.RUNNING)){
            sb.append("  and b.pause_flag = 0 ");
        }

        return sb.toString();
    }

    /**
     * 查询结果排序
     * 0:销量倒序
     * 1:好评数倒序
     * 2:评论率倒序
     * 3:排序号倒序
     * 4:预约数倒序
     */
    public String getQuerySort() {
        if (Objects.nonNull(sortFlag)) {
            switch (sortFlag) {
                case 1:
                    return "order by good.goods_favorable_comment_num desc, b.appointment_start_time desc ";
                case 2:
                    return "order by goodsFeedbackRate desc, b.appointment_start_time desc ";
                case 3:
                    return "order by good.sort_no desc, b.appointment_start_time desc ";
                case 4:
                    return "order by a.appointment_count desc, b.appointment_start_time desc ";
                default:
                    return "order by goodsSalesNum desc, b.appointment_start_time desc ";
            }
        }
        StringBuilder sb = new StringBuilder();
//        sb.append("order by appointmentCount desc, b.appointment_start_time desc ");
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
    public static List<AppointmentSaleGoodsVO> converter(List<Map<String, Object>> sqlResult) {
        return sqlResult.stream().map(item ->
                AppointmentSaleGoodsVO.builder()
                        .appointmentSaleId(toLong(item, "id"))
                        .storeId(toLong(item, "storeId"))
                        .goodsId(toStr(item, "goodsId"))
                        .goodsInfoId(toStr(item, "goodsInfoId"))
                        .goodsName(toStr(item, "goodsName"))
                        .goodsImg(toStr(item, "goodsImg"))
                        .marketPrice(toBigDecimal(item, "marketPrice"))
                        .price(toBigDecimal(item, "price"))
                        .appointmentCount(toInteger(item, "appointmentCount"))
                        .appointmentStartTime(toLocalDateTime(item, "appointmentStartTime"))
                        .appointmentEndTime(toLocalDateTime(item, "appointmentEndTime"))
                        .snapUpStartTime(toLocalDateTime(item, "snapUpStartTime"))
                        .snapUpEndTime(toLocalDateTime(item, "snapUpEndTime"))
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