package com.wanmi.sbc.elastic.api.request.goods;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/13 11:29 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsGoodsCustomQueryProviderRequest implements Serializable {

    private int pageNum = 1;

    private int pageSize = 20;

    /**
     * 商品列表
     */
    private Collection<String> goodIdList;

    /**
     * 不包含的商品列表
     */
    private Collection<String> unGoodIdList;

    /**
     * 知识顾问专享 0:不是 ，1：是
     */
    private Integer cpsSpecial;

    /**
     * 上架时间之后
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime afterAddedTime;

    /**
     * 主播推荐 1樊登解读,2非凡精读,3樊登直播,4热销，5上新
     */
    private String anchorPushs;
    /**
     * 是否展示无库存
     */
    private Boolean hasShowUnStock;

    /**
     * 字段排序
     */
    private List<SortCustomBuilder> sortBuilderList;

    /**
     * 开始时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;

    /**
     * 结束时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;


    /**
     * 店铺分类id
     */
    public Collection<Integer> classifyIdList;


    /**
     * 店铺分类id列表
     */
    public Collection<Integer> unClassifyIdList;

    /**
     * 特价书 市场价/定价 小于 0.5
     */
    private String scriptSpecialOffer;

    /**
     * 是否是图书，1 是 其他否
     */
    private Integer bookFlag;

    /**
     * 脚本排序
     */
    private String scriptSort;

}
