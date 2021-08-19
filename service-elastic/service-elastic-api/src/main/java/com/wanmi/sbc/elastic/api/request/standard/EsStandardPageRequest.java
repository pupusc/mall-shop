package com.wanmi.sbc.elastic.api.request.standard;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品库查询请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EsStandardPageRequest extends BaseQueryRequest {

    /**
     * 批量SPU编号
     */
    private List<String> goodsIds;

    /**
     * 模糊条件-商品名称
     */
    private String likeGoodsName;

    /**
     * 模糊条件-供应商名称
     */
    private String likeProviderName;

    private String likeGoodsInfoNo;

    private String likeGoodsNo;

    /**
     * 商品来源，0供应商，1商家
     */
    private Integer goodsSource;

    /**
     *
     */
    private List<Integer> goodsSourceList;

    /**
     * 商品分类
     */
    private Long cateId;

    /**
     * 批量商品分类
     */
    private List<Long> cateIds;

    /**
     * 品牌编号
     */
    private Long brandId;

    /**
     * 批量品牌分类
     */
    private List<Long> brandIds;

    /**
     * 批量品牌分类，可与NULL以or方式查询
     */
    private List<Long> orNullBrandIds;

    /**
     * 删除标记
     */
    private Integer delFlag;

    /**
     * 非GoodsId
     */
    private String notGoodsId;
    /**
     * 店铺主键
     */
    private Long storeId;
    /**
     * 导入状态  -1 全部 1 已导入 2未导入
     */
    private Integer toLeadType;
    /**
     * 上下架状态,0:下架1:上架2:部分上架
     */
    private Integer addedFlag;

    private ThirdPlatformType thirdPlatformType;

    /**
     * 创建开始时间
     */
    private LocalDateTime createTimeBegin;

    /**
     * 创建结束时间
     */
    private LocalDateTime createTimeEnd;



}
