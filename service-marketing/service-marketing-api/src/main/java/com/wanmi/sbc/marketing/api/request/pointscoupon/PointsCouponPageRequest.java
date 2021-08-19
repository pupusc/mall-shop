package com.wanmi.sbc.marketing.api.request.pointscoupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>积分兑换券表分页查询请求参数</p>
 *
 * @author yang
 * @date 2019-06-11 10:07:09
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsCouponPageRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量查询-积分兑换券idList
     */
    @ApiModelProperty(value = "批量查询-积分兑换券idList")
    private List<Long> pointsCouponIdList;

    /**
     * 积分兑换券id
     */
    @ApiModelProperty(value = "积分兑换券id")
    private Long pointsCouponId;

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private String couponId;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    /**
     * 兑换总数
     */
    @ApiModelProperty(value = "兑换总数")
    private Long totalCount;

    /**
     * 已兑换数量
     */
    @ApiModelProperty(value = "已兑换数量")
    private Long exchangeCount;

    /**
     * 兑换积分
     */
    @ApiModelProperty(value = "兑换积分")
    private Long points;

    /**
     * 兑换积分区间开始
     */
    @ApiModelProperty(value = "兑换积分区间开始")
    private Long pointsSectionStart;

    /**
     * 兑换积分区间结尾
     */
    @ApiModelProperty(value = "兑换积分区间结尾")
    private Long pointsSectionEnd;

    /**
     * 是否售罄
     */
    @ApiModelProperty(value = "是否售罄")
    private BoolFlag sellOutFlag;

    /**
     * 是否启用 0：停用，1：启用
     */
    @ApiModelProperty(value = "是否启用 0：停用，1：启用")
    private EnableStatus status;

    /**
     * 搜索条件:兑换开始时间开始
     */
    @ApiModelProperty(value = "搜索条件:兑换开始时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTimeBegin;
    /**
     * 搜索条件:兑换开始时间截止
     */
    @ApiModelProperty(value = "搜索条件:兑换开始时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime beginTimeEnd;

    /**
     * 搜索条件:兑换结束时间开始
     */
    @ApiModelProperty(value = "搜索条件:兑换结束时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTimeBegin;
    /**
     * 搜索条件:兑换结束时间截止
     */
    @ApiModelProperty(value = "搜索条件:兑换结束时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime endTimeEnd;

    /**
     * 搜索条件:创建时间开始
     */
    @ApiModelProperty(value = "搜索条件:创建时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;
    /**
     * 搜索条件:创建时间截止
     */
    @ApiModelProperty(value = "搜索条件:创建时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createPerson;

    /**
     * 搜索条件:修改时间开始
     */
    @ApiModelProperty(value = "搜索条件:修改时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeBegin;
    /**
     * 搜索条件:修改时间截止
     */
    @ApiModelProperty(value = "搜索条件:修改时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTimeEnd;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String updatePerson;

    /**
     * 删除标识,0: 未删除 1: 已删除
     */
    @ApiModelProperty(value = "删除标识,0: 未删除 1: 已删除")
    private DeleteFlag delFlag;

    /**
     * 排序标识
     * 0:我能兑换
     * 1:积分价格数升序
     * 2:积分价格数倒序
     * 3:市场价升序
     * 4:市场价倒序
     */
    @ApiModelProperty(value = "排序标识")
    private Integer sortFlag;

}