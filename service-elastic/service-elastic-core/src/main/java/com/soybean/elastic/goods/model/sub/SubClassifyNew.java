package com.soybean.elastic.goods.model.sub;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Description: 店铺分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/19 2:53 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class SubClassifyNew {

    @Field(type = FieldType.Long)
    private Long id;

    /**
     * 店铺分类名称
     */
    @Field(type = FieldType.Keyword)
    private String classifyName;
}
