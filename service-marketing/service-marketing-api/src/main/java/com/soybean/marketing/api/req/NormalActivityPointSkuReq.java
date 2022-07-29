package com.soybean.marketing.api.req;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/12 2:36 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class NormalActivityPointSkuReq extends NormalActivityReq {

    @NotNull
    @Valid
    private List<NormalActivitySkuReq> normalActivitySkus;
}
