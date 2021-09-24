package com.wanmi.sbc.goods.provider.impl.chooserule;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.chooserule.ChooseRuleProvider;
import com.wanmi.sbc.goods.api.request.chooserule.ChooseRuleProviderRequest;
import com.wanmi.sbc.goods.api.response.chooserulegoodslist.ChooseRuleProviderResponse;
import com.wanmi.sbc.goods.chooserule.model.root.ChooseRuleDTO;
import com.wanmi.sbc.goods.chooserule.request.ChooseRuleRequest;
import com.wanmi.sbc.goods.chooserule.service.ChooseRuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/22 6:19 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class ChooseRuleController implements ChooseRuleProvider {

    @Resource
    private ChooseRuleService chooseRuleService;

    /**
     * 获取控件信息
     * @param chooseRuleProviderRequest
     * @return
     */
    @Override
    public BaseResponse<ChooseRuleProviderResponse> findChooseRuleNoGoodsByCondition(ChooseRuleProviderRequest chooseRuleProviderRequest) {
        ChooseRuleRequest chooseRuleRequest = new ChooseRuleRequest();
        chooseRuleRequest.setBookListId(chooseRuleProviderRequest.getBookListModelId());
        chooseRuleRequest.setCategory(chooseRuleProviderRequest.getCategoryId());
        ChooseRuleDTO chooseRuleDTO = chooseRuleService.findByCondition(chooseRuleRequest);
        if (chooseRuleDTO != null) {
            ChooseRuleProviderResponse chooseRuleProviderResponse = new ChooseRuleProviderResponse();
            BeanUtils.copyProperties(chooseRuleDTO, chooseRuleProviderResponse);
            return BaseResponse.success(chooseRuleProviderResponse);
        }
        return BaseResponse.SUCCESSFUL();
    }
}
