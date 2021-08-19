package com.wanmi.sbc.goods.api.response.flashsaleactivity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * <p>秒杀活动信息</p>
 * @author yxz
 * @date 2019-06-11 10:11:15
 */
@ApiModel
@Data
public class FlashSaleActivityResponse {
    /**
     * 活动日期
     */
    @ApiModelProperty(value = "活动日期")
    private String activityDate;

    /**
     * 活动时间
     */
    @ApiModelProperty(value = "活动时间")
    private String activityTime;

    /**
     * 活动时间
     */
    @ApiModelProperty(value = "活动时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime activityFullTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime activityEndTime;

    /**
     * 参与商家数
     */
    private Integer storeNum;

    /**
     * 抢购商品数量
     */
    private Integer goodsNum;

    /**
     * 从数据库实体转换为返回前端的用户信息
     * (字段顺序不可变)
     */
    public FlashSaleActivityResponse convertFromNativeSQLResult(Object result) {
        Object[] results = StringUtil.cast(result, Object[].class);
        if (results == null || results.length < 1) {
            return this;
        }

        this.setActivityDate(StringUtil.cast(results, 0, String.class));
        this.setActivityTime(StringUtil.cast(results, 1, String.class));
        this.setActivityFullTime(StringUtil.cast(results, 2, Timestamp.class).toLocalDateTime());
        this.setActivityEndTime(activityFullTime.plusHours(Constants.FLASH_SALE_LAST_HOUR));
        this.setGoodsNum(StringUtil.cast(results, 3, BigInteger.class).intValue());
        this.setStoreNum(StringUtil.cast(results, 4, BigInteger.class).intValue());

        return this;
    }
}
