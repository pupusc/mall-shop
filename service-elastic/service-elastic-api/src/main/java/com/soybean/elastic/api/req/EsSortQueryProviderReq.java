package com.soybean.elastic.api.req;

import com.soybean.common.req.CommonPageQueryReq;
import com.soybean.elastic.api.enums.BookListSortType;
import com.soybean.elastic.api.enums.SpuSortType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 1:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsSortQueryProviderReq extends CommonPageQueryReq {

    /**
     * 排序类型 0 默认
     */
    private SpuSortType spuSortType;

    /**
     * 书单排序 0默认
     */
    private BookListSortType booklistSortType;
}
