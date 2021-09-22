package com.wanmi.sbc.elastic.api.request.goods;

import lombok.Data;
import org.elasticsearch.search.sort.SortBuilder;

import java.io.Serializable;
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
     * 是否展示无库存
     */
    private Boolean hasShowUnStock;

    /**
     * 字段排序
     */
    private List<SortBuilder> sortBuilderList;

}
