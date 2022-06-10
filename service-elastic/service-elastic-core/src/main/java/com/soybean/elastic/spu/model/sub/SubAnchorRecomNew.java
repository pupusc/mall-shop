package com.soybean.elastic.spu.model.sub;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Description: 主播推荐
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 5:47 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SubAnchorRecomNew {

    /**
     * 主播推荐id
     */
    @Field(type = FieldType.Integer)
    private Integer recomId;

    /**
     * 主播推荐名称  1、樊登讲书 2非凡精读 3樊登直播 4李蕾慢读
     */
    @Field(type = FieldType.Keyword)
    private String recomName;
}
