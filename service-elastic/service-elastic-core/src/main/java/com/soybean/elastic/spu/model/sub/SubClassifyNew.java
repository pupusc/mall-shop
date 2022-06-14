package com.soybean.elastic.spu.model.sub;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Description: 店铺分类
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/19 2:53 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubClassifyNew {


    @Field(type = FieldType.Integer)
    private Integer fClassifyId;

    /**
     * 店铺分类名称
     */
    @Field(type = FieldType.Keyword)
    private String fClassifyName;


    @Field(type = FieldType.Integer)
    private Integer classifyId;

    /**
     * 店铺分类名称
     */
    @Field(type = FieldType.Keyword)
    private String classifyName;
}
