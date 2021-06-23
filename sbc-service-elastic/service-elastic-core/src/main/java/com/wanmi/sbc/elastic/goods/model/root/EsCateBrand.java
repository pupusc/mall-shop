package com.wanmi.sbc.elastic.goods.model.root;

import com.wanmi.sbc.common.util.EsConstants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 数据商品分类-品牌,只在商品ES索引时引用
 * Created by dyt on 2017/4/21.
 */
@Document(indexName = EsConstants.DOC_CATE_BRAND_TYPE)
@Data
public class EsCateBrand {

    /**
     * documentId组成成分:分类ID_品牌ID
     */
    @Id
    private String id;

    /**
     * 商品分类信息
     */
    private GoodsCateNest goodsCate;

    /**
     * 商品品牌信息
     */
    private GoodsBrandNest goodsBrand;


}
