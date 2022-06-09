package com.soybean.mall.goods.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Description: 搜索结果首页
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/10 2:53 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SearchHomeResp<S> {


    /**
     * 商品
     */
    private SubSearchHomeResp spus;
    /**
     * 图书
     */
    private SubSearchHomeResp books;

    /**
     * 书单
     */
    private SubSearchHomeResp bookLists;
    /**
     * 榜单
     */
    private SubSearchHomeResp rankingLists;


    @Data
    @AllArgsConstructor
    public static class SubSearchHomeResp<S> {
        private String navName;

        private S content;
    }
}
