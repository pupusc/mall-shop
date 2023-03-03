package com.wanmi.sbc.goods.collect;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/15 2:15 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class RedisTagsConstant {


    /**
     * 书有spu、isbn
     * 10. 大促标签
     * 20. 榜单标签
     * 30. 书本身有奖项，显示第一个奖项名称
     * 40. 图书作者有获奖，显示『奖项名称+获得者（作者）』
     * 50. 当有指定的打标媒体、名家、专业机构推荐时，显示『媒体名称/名家名称/专业机构名称推荐』
     * 60. 有图书库-推荐信息，显示『X位名家，X家媒体，X家专业机构推荐』
     * 70. 书中提到的人物，有数据则显示：人物名称
     * 80. 图书本身最小年龄段、最大年龄段有数据，显示数字，X~Y岁，当任意一项没有对应显示为空
     * 90. 适读对象：当数对像有数据，则全量显示
     * 100. 行业类类目：（本次新加字段），显示图书所在行业类目，按类目树结构显示，一级名称>二级名称>三级名称
     * 110. 图书被包含在某丛书，显示「丛书」名称
     * 120. 标签：一级分类=？？下显示3个图书库中关联优先级最高的标签
     */

    /**
     * 非书只有spu
     * 商品：平台类目≠书籍，根据SPU查询到静态标签
     * 1. 标签一级分类=营销标签，二级分类=大促标签，显示序号第一的标签
     * 2. 榜单：显示最新更新的榜单名称+第X名-点击进入榜单聚合页
     * 3. 取商品上关联的静态标签，按标签优先级依次呈现
     */

    public static final String ELASTIC_SAVE_GOODS_TAGS_SPU_NO   = "ELASTIC_SAVE:GOODS_TAGS_SPU_NO";     //spu_no

    public static final String ELASTIC_SAVE_BOOKS_DETAIL_SPU_NO = "ELASTIC_SAVE:BOOKS_DETAIL_SPU_NO";   //spu_no && isbn


}
