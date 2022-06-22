package com.soybean.elastic.spu.model.sub;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Description: 图书标签信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/31 5:01 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubBookLabelNew {

//    /**
//     * 1级标签
//     */
//    @Field(type = FieldType.Integer)
//    private Integer fTagId;
//
//    @Field(type = FieldType.Keyword)
//    private String fTagName;

    /**
     * 2级标签
     */
    @Field(type = FieldType.Integer)
    private Integer stagId;

    @Field(type = FieldType.Keyword)
    private String stagName;

    /**
     * 3级标签
     */
    @Field(type = FieldType.Integer)
    private Integer tagId;

    @Field(type = FieldType.Keyword)
    private String tagName;
}
