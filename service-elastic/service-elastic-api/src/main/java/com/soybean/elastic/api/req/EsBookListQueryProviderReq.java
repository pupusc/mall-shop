package com.soybean.elastic.api.req;

import com.soybean.common.req.CommonPageQueryReq;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/9 1:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class EsBookListQueryProviderReq extends EsSortQueryProviderReq {

    /**
     * 商品id信息
     */
    private List<String> spuIds;


}
