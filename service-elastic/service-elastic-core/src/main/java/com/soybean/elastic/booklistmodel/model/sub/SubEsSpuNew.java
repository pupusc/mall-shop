package com.soybean.elastic.booklistmodel.model.sub;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/31 5:30 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubEsSpuNew {

    @Field(type = FieldType.Keyword)
    private String spuId;

    /**
     * 商品标题
     */
    @Field(type = FieldType.Text)
    private String spuName;
}
