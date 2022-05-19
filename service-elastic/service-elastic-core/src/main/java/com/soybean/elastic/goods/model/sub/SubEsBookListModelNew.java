package com.soybean.elastic.goods.model.sub;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Description: 书单对象
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/19 2:36 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubEsBookListModelNew {

    @Field(type = FieldType.Long)
    private Long id;

    /**
     * ik_max_word 会对文本做最细 力度的拆分
     * ik_smart：会对文本做最粗粒度的拆分
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String bookListModelName;
}
