package com.wanmi.sbc.goods.provider.impl.chooserule;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.chooserule.ChooseRuleProvider;
import com.wanmi.sbc.goods.api.request.chooserule.ChooseRuleProviderRequest;
import com.wanmi.sbc.goods.chooserule.model.root.ChooseRuleDTO;
import com.wanmi.sbc.goods.chooserule.service.ChooseRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/1 7:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class ChooseRuleController implements ChooseRuleProvider {

    @Autowired
    private ChooseRuleService chooseRuleService;

    /**
     * 新增
     * @param chooseRuleProviderRequest
     * @return
     */
    @Override
    public BaseResponse add(@Validated @RequestBody ChooseRuleProviderRequest chooseRuleProviderRequest) {
        chooseRuleService.add(chooseRuleProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 修改
     * @param chooseRuleProviderRequest
     * @return
     */
    @Override
    public BaseResponse update(@Validated @RequestBody ChooseRuleProviderRequest chooseRuleProviderRequest) {
        chooseRuleService.update(chooseRuleProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取信息
     * @param chooseRuleProviderRequest
     * @return
     */
    @Override
    public BaseResponse findByCondition(@Validated @RequestBody ChooseRuleProviderRequest chooseRuleProviderRequest) {
        ChooseRuleDTO chooseRuleDTO = chooseRuleService.findByCondition(chooseRuleProviderRequest);
        return BaseResponse.success(chooseRuleDTO);
    }
}
