package com.wanmi.sbc.index.request;

import com.wanmi.sbc.goods.api.request.image.ImageSortProviderRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/2 2:42 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SortRequest {

    @NotNull
    List<ImageSortProviderRequest> sortList;
}
