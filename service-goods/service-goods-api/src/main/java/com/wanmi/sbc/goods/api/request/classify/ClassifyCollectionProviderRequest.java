package com.wanmi.sbc.goods.api.request.classify;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/21 2:33 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ClassifyCollectionProviderRequest implements Serializable {

    private Collection<Integer> parentIdColl;
}
