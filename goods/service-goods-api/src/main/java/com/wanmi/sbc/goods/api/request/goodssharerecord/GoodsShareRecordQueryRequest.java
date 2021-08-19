package com.wanmi.sbc.goods.api.request.goodssharerecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.ShareChannel;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>商品分享通用查询请求参数</p>
 *
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsShareRecordQueryRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量查询-shareIdList
     */
    @ApiModelProperty(value = "批量查询-shareIdList")
    private List<Long> shareIdList;

    /**
     * shareId
     */
    @ApiModelProperty(value = "shareId")
    private Long shareId;

    /**
     * 会员Id
     */
    @ApiModelProperty(value = "会员Id")
    private String customerId;

    /**
     * SPU id
     */
    @ApiModelProperty(value = "SPU id")
    private String goodsId;

    /**
     * SKU id
     */
    @ApiModelProperty(value = "SKU id")
    private String goodsInfoId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 终端：1 H5，2pc，3APP，4小程序
     */
    @ApiModelProperty(value = "终端：1 H5，2pc，3APP，4小程序")
    private TerminalSource terminalSource;

    /**
     * 分享渠道：0微信，1朋友圈，2QQ，3QQ空间，4微博，5复制链接，6保存图片
     */
    @ApiModelProperty(value = "分享渠道：0微信，1朋友圈，2QQ，3QQ空间，4微博，5复制链接，6保存图片")
    private ShareChannel shareChannel;

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

}