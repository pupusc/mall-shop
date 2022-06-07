package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
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
public class SearchWeightController {

    @Autowired
    private SearchWeightService searchWeightService;

    @Override
    public BaseResponse<List<SearchWeightResp>> list(String key) {
        return BaseResponse.success(searchWeightService.list(key));
    }
}
