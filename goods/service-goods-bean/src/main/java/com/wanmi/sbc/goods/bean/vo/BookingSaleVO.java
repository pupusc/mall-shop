package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.PresellSaleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>预售信息VO</p>
 *
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@ApiModel
@Data
public class BookingSaleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * 商户id
     */
    @ApiModelProperty(value = "商户id")
    private Long storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 预售类型 0：全款预售  1：定金预售
     */
    @ApiModelProperty(value = "预售类型 0：全款预售  1：定金预售")
    private Integer bookingType;

    /**
     * 定金支付开始时间
     */
    @ApiModelProperty(value = "定金支付开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime handSelStartTime;

    /**
     * 定金支付结束时间
     */
    @ApiModelProperty(value = "定金支付结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime handSelEndTime;

    /**
     * 尾款支付开始时间
     */
    @ApiModelProperty(value = "尾款支付开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tailStartTime;

    /**
     * 尾款支付结束时间
     */
    @ApiModelProperty(value = "尾款支付结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime tailEndTime;

    /**
     * 预售开始时间
     */
    @ApiModelProperty(value = "预售开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime bookingStartTime;

    /**
     * 预售结束时间
     */
    @ApiModelProperty(value = "预售结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime bookingEndTime;

    /**
     * 发货日期 2020-01-10
     */
    @ApiModelProperty(value = "发货日期 2020-01-10")
    private String deliverTime;

    /**
     * 参加会员  -3:企业会员 -2：付费会员 -1:平台全部客户 0:店铺全部等级 other:店铺其他等级
     */
    @ApiModelProperty(value = "参加会员  -3:企业会员 -2：付费会员 -1:平台全部客户 0:店铺全部等级 other:店铺其他等级")
    private String joinLevel;

    /**
     * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）没有用到
     */
    @ApiModelProperty(value = "是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）")
    private DefaultFlag joinLevelType;

    /**
     * 是否暂停 0:否 1:是
     */
    @ApiModelProperty(value = "是否暂停 0:否 1:是")
    private Integer pauseFlag;


    @ApiModelProperty(value = "活动商品相关信息列表")
    private List<BookingSaleGoodsVO> bookingSaleGoodsList;


    @ApiModelProperty(value = "活动商品相关信息")
    private BookingSaleGoodsVO bookingSaleGoods;

    /**
     * 定金支付数量
     */
    @ApiModelProperty(value = "定金支付数量")
    private Integer handSelCount;

    /**
     * 尾款支付数量
     */
    @ApiModelProperty(value = "尾款支付数量")
    private Integer tailCount;

    /**
     * 全款支付数量
     */
    @ApiModelProperty(value = "全款支付数量")
    private Integer payCount;

    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String levelName;

    /**
     * 查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束
     */
    @ApiModelProperty(value = "查询类型，0：全部，1：进行中，2：暂停中，3：未开始，4：已结束")
    private PresellSaleStatus status;

    public void buildStatus() {
        if (this.getBookingType().equals(NumberUtils.INTEGER_ZERO)) {
            if (this.getBookingStartTime().isAfter(LocalDateTime.now())) {
                this.status = PresellSaleStatus.NOT_START;
            }
            if (this.getBookingStartTime().isBefore(LocalDateTime.now()) && this.getBookingEndTime().isAfter(LocalDateTime.now())) {
                this.status = PresellSaleStatus.STARTED;
            }
            if (this.getBookingEndTime().isBefore(LocalDateTime.now())) {
                this.status = PresellSaleStatus.ENDED;
            }
            if (Objects.nonNull(this.pauseFlag) && this.pauseFlag == 1) {
                this.status = PresellSaleStatus.PAUSED;
            }
        }
        if (this.getBookingType().equals(NumberUtils.INTEGER_ONE)) {
            if (this.getHandSelStartTime().isAfter(LocalDateTime.now())) {
                this.status = PresellSaleStatus.NOT_START;
            }
            if (this.getHandSelStartTime().isBefore(LocalDateTime.now()) && this.getTailEndTime().isAfter(LocalDateTime.now())) {
                this.status = PresellSaleStatus.STARTED;
            }
            if (this.getTailEndTime().isBefore(LocalDateTime.now())) {
                this.status = PresellSaleStatus.ENDED;
            }
            if (Objects.nonNull(this.pauseFlag) && this.pauseFlag == 1) {
                this.status = PresellSaleStatus.PAUSED;
            }
        }
    }
}