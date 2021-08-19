package com.wanmi.ares.request.flow;

import com.wanmi.ares.enums.SortOrder;
import com.wanmi.ares.enums.StatisticsDataType;
import com.wanmi.ares.enums.StatisticsWeekType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @ClassName FlowDataRequest
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/26 15:21
 **/
@Data
public class FlowDataListRequest implements Serializable {

    private static final long serialVersionUID = -1692902738583119836L;

    /**
     * 店铺id
     */
    private String companyId;

    /**
     * 统计开始时间
     */
    private LocalDate beginDate;

    /**
     * 统计结束时间
     */
    private LocalDate endDate;

    /**
     * 是否是按周统计
     */
    private boolean isWeek;

    /**
     * 排序字段
     */
    private String sortName;

    /**
     * 排序类型
     */
    private SortOrder sortOrder;

    /**
     * 统计时间周期类型：0：今天，1：昨天，2：最近七天；3：最近30天；4：按月统计，5：最近30天按周统计，6：最近6个月按周统计
     */
    private StatisticsDataType statisticsDataType;

    /**
     * 按周统计：0:最近30天按周统计，1：最近6个月按周统计
     */
    private StatisticsWeekType statisticsWeekType;

    /**
     * 统计月份
     */
    private String month;

    /**
     * 按月统计：激励当前月份几个月
     */
    private Integer monthNum;

    /**
     * 页数
     */
    private int pageNum;

    /**
     * 条数
     */
    private int pageSize;

    /**
     * 按周类型，0 30天  1 月
     */
    private int type;

    public String getSortName() {
        switch (this.sortName){
            case "date":this.sortName = "date"; break;
            case "orderCount":this.sortName = "order_num"; break;
            case "orderNum":this.sortName = "order_user_num"; break;
            case "orderAmt":this.sortName = "order_money"; break;
            case "PayOrderCount":this.sortName = "pay_num"; break;
            case "PayOrderNum":this.sortName = "pay_user_num"; break;
            case "payOrderAmt":this.sortName = "pay_money"; break;
            case "orderConversionRate":this.sortName = "orderConversionRate"; break;
            case "payOrderConversionRate":this.sortName = "pay_conversion"; break;
            case "wholeStoreConversionRate":this.sortName = "all_conversion"; break;
            case "customerUnitPrice":this.sortName = "order_per_price"; break;
            case "returnOrderCount":this.sortName = "refund_num"; break;
            case "returnOrderNum":this.sortName = "refund_user_num"; break;
            case "returnOrderAmt":this.sortName = "refund_money"; break;
            default:  break;
        }
        return sortName;
    }
}
