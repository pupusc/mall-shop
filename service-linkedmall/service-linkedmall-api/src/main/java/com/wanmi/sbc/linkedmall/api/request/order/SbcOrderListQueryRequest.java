package com.wanmi.sbc.linkedmall.api.request.order;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * \* User: yhy
 * \* Date: 2020-8-10
 * \* Time: 17:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class SbcOrderListQueryRequest implements Serializable {

    @ApiModelProperty(value = "商城内部用户id")
    @NotBlank
    private String bizUid;

    @ApiModelProperty(value = "主订单号列表，最多支持20个")
    private List<String> lmOrderList;

    @ApiModelProperty(value = "订单支付状态，12=待支付，2=已支付，4=已退款关闭，6=交易成功，8=被淘宝关闭")
    private Long orderStatus;

    /*
    linkedMall商城 物流状态
        1=未发货 -> 等待卖家发货
        2=已发货 -> 等待买家确认收货
        3=已收货 -> 交易成功
        4=已经退货 -> 交易失败
        5=部分收货 -> 交易成功
        6=部分发货中
        8=还未创建物流订单
     */
    @ApiModelProperty(value = "订单物流状态，1=未发货，2=已发货，3=已收货， 4=已经退货， 5=部分收货， 6=部分发货中，8=还未创建物流订单")
    private Integer logisticsStatus;

    @ApiModelProperty(value = "创单开始时间戳")
    private Long createBeginTime;

    @ApiModelProperty(value = "创单结束时间戳")
    private Long createEndTime;

    @ApiModelProperty(value = "订单状态，0：生效，1：无效, -1：都查，不填默认为-1")
    private Integer enableStatus;

    @ApiModelProperty(value = "当前页码，从1开始")
    private Long pageNum;

    @ApiModelProperty(value = "每页多少条记录,最大20条")
    private Long pageSize;

    @ApiModelProperty(value = "是否全量查询")
    private Boolean allFlag;


    // 构建过滤条件
    public String buildFilterOption() {
        Map<String, Object> map = new HashMap<>();
        // 主订单号，最多支持20个
        if (CollectionUtils.isNotEmpty(lmOrderList)) {
            map.put("lmOrderList", lmOrderList);
        }
        // 订单支付状态
        if (Objects.nonNull(orderStatus)) {
            map.put("orderStatus", orderStatus);
        }
        // 订单物流状态
        if (Objects.nonNull(logisticsStatus)) {
            map.put("logisticsStatus", logisticsStatus);
        }
        // 订单状态
        if (Objects.nonNull(enableStatus)) {
            map.put("enableStatus", enableStatus);
        } else {
            map.put("enableStatus", -1);
        }
        // 创单时间 时间戳
        StringBuilder sb = new StringBuilder();
        if (Objects.nonNull(createBeginTime)) {
            sb.append(" createTime >= ").append(createBeginTime);
        }
        if (Objects.nonNull(createBeginTime) && Objects.nonNull(createEndTime)) {
            sb.append(" And ");
        }
        if (Objects.nonNull(createEndTime)) {
            sb.append(" createTime <= ").append(createEndTime);
        }
        if (StringUtils.isNotBlank(sb.toString())) {
            map.put("filter", sb.toString());
        }

        return JSON.toJSONString(map);
    }

    public Long getPageNum() {
        return Objects.nonNull(pageNum) && pageNum.compareTo(0L) > 0 ? pageNum : 1L;
    }

    public Long getPageSize() {
        return Objects.nonNull(pageSize) && pageSize.compareTo(21L) < 0 ? pageSize : 20L;
    }

}
