package com.wanmi.sbc.goods.api.request.booklistgoodspublish;

import lombok.Data;

import java.util.Collection;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/9 3:29 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class CountBookListModelGroupProviderRequest {


    /**
     * 书单id列表
     */
    private Collection<Integer> bookListIdCollection;

    /**
     * 业务类型
     */
    private Collection<Integer> businessTypeColl;

    /**
     * 分类
     */
    private Integer categoryId;

}
