package com.wanmi.sbc.goods.booklistmodel.request;

import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 5:02 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListModelPageRequest {

    private Integer id;

    /**
     * 书单列表
     */
    private Collection<Integer> idCollection;

    /**
     * 名字
     */
    private String name;

    /**
     * 发布状态 0 草稿 1 已编辑未发布 2 已发布
     */
//    private Integer publishState;

    private List<Integer> publishStateList;

    /**
     *  书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题
     */
//    private Integer businessType;

    /**
     * 获取多个
     */
    private List<Integer> businessTypeList;

    /**
     * 是否置顶 0 否 1 是
     */
    private Integer hasTop;

}
