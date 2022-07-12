package com.soybean.mall.miniapp.activity.resp;

import com.soybean.marketing.api.resp.NormalActivityResp;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 12:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class NormalActivityDetailResp extends NormalActivityResp {

    private List<NormalActivitySkuDetailResp> normalActivitySkus;
}
