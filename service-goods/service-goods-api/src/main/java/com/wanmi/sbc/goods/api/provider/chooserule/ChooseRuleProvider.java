package com.wanmi.sbc.goods.api.provider.chooserule;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.chooserule.ChooseRuleProviderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/22 6:16 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@FeignClient(value = "${application.goods.name}", contextId = "ChooseRuleProvider")
public interface ChooseRuleProvider {

    @PostMapping("/goods/${application.goods.version}/choose-rule/find-choose-rule-no-goods-by-condition")
    BaseResponse findChooseRuleNoGoodsByCondition(@RequestBody ChooseRuleProviderRequest chooseRuleProviderRequest);
}
