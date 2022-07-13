package com.soybean.marketing.api.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Description: 根据sku纬度查询
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 2:14 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SpuNormalActivityReq {

    private List<String> spuIds;

    private List<String> skuIds;

    @NotNull
    private List<Integer> channelTypes;
}
