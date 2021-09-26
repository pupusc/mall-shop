package com.wanmi.sbc.goods.api.request.booklistgoodspublish;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/15 7:53 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class BookListGoodsPublishProviderRequest implements Serializable {

    /**
     * bookListId
     */
    @NotNull
    private Collection<Integer> bookListIdColl;

    /**
     * 分类 {@link com.wanmi.sbc.goods.api.enums.CategoryEnum}
     */
    @NotNull
    private Integer categoryId;

    /**
     * 操作者
     */
    @NotBlank
    private String operator;
}
