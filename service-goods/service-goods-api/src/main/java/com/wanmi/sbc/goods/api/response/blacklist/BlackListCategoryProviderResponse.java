package com.wanmi.sbc.goods.api.response.blacklist;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/21 1:43 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BlackListCategoryProviderResponse implements Serializable {

    /**
     * 二级分类列表
     */
    private List<String> secondClassifyIdList;

    /**
     * 商品id列表
     */
    private List<String> goodsIdList;


    /**
     * 不进行分类的值列表
     */
    private List<String> normalList;
}
