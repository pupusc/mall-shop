package com.wanmi.sbc.setting.provider.impl.weight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.weight.SearchWeightProvider;
import com.wanmi.sbc.setting.api.response.weight.SearchWeightResp;
import com.wanmi.sbc.setting.weight.service.SearchWeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/7 12:16 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class SearchWeightController implements SearchWeightProvider {

    @Autowired
    private SearchWeightService searchWeightService;

    @Override
    public BaseResponse<List<SearchWeightResp>> list(String key) {
        return BaseResponse.success(searchWeightService.list(key));
    }


    @Override
    public BaseResponse delete(String key) {
        searchWeightService.delete(key);
        return BaseResponse.SUCCESSFUL();
    }

}
