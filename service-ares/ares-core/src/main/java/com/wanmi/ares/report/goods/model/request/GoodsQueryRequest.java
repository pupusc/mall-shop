package com.wanmi.ares.report.goods.model.request;

import com.wanmi.ares.base.BaseRequest;
import com.wanmi.ares.enums.QueryDateCycle;
import com.wanmi.ares.enums.SortOrder;
import com.wanmi.ares.utils.DateUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;

/**
 * Es 商品查询 返回结果
 *
 * @author liangck
 * @version 1.0
 * @since 16/6/28 17:29
 */
@Data
public class GoodsQueryRequest extends BaseRequest {

    /**
     * 选择类型
     */
    private QueryDateCycle selectType;

    /**
     * 当选择类型为其他时
     * 选择时间
     */
    private String dateStr;

    /**
     * 模糊关键字
     */
    private String keyword;

    /**
     * 分类ID或品牌id
     */
    private String id;

    /**
     * 页码
     */
    private Long pageNum = 0L;

    /**
     * 页面大小
     */
    private Long pageSize = 10L;

    /**
     * 0:下单笔数
     *
     * 1:下单金额
     *
     * 2:下单件数
     *
     * 3:付款商品件数
     *
     * 4:退货笔数
     *
     * 5:退货金额
     *
     * 6:退货件数
     *
     * 7: 转化率
     */
    private Integer sortCol;

    /**
     * 排序
     */
    private SortOrder sortType;

    /**
     * 商户编号
     */
    private String companyId;

    /**
     * 如果是其他时间，则获取传递月份
     * @return
     */
    public LocalDate getTime(){
        if(StringUtils.isNotBlank(dateStr)){
            return DateUtil.parse2Date(dateStr.concat("-01"), DateUtil.FMT_DATE_1);
        }
        return null;
    }

    /**
     * 如果是其他时间，则获取传递月份
     * @return
     */
    public Integer getMonth(){
        LocalDate date = getTime();
        if(date != null){
            return NumberUtils.toInt(DateUtil.format(date, DateUtil.FMT_MONTH_1));
        }
        return null;
    }

    /**
     * 单日查询
     * 获取分页参数对象
     * @return
     */
    public PageRequest getPageable(){
        return  PageRequest.of(pageNum.intValue(),pageSize.intValue());
    }

    /**
     * 单日查询
     * 根据类型获取相应排序参数
     * @return
     */
    public String[] getSort(){
        if(sortCol == null){
            sortCol = 0;
        }
        String type = SortOrder.DESC.toString();
        if(sortType != null && sortType == SortOrder.ASC){
            type = SortOrder.ASC.toString();
        }
        String col = "orderCount";
        switch (sortCol){
            case 0:col = "orderCount";break;
            case 1:col = "orderAmt";break;
            case 2:col = "orderNum";break;
            case 3:col = "payNum";break;
            case 4:col = "returnOrderCount";break;
            case 5:col = "returnOrderAmt";break;
            case 6:col = "returnOrderNum";break;
            case 7:col = "orderConversion";break;
        }
        return new String[]{col,type};
    }
}
