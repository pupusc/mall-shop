package com.wanmi.sbc.setting.provider.impl.weight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.search.SearchAggsProvider;
import com.wanmi.sbc.setting.api.response.search.SearchAggsResp;
import com.wanmi.sbc.setting.search.service.SearchAggsService;
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
public class SearchAggsController implements SearchAggsProvider {

    @Autowired
    private SearchAggsService searchAggsService;

    @Override
    public BaseResponse<List<SearchAggsResp>> list(String key) {
        return BaseResponse.success(searchAggsService.list(key));
    }

    @Override
    public BaseResponse delete(String key) {
        searchAggsService.delete(key);
        return BaseResponse.SUCCESSFUL();
    }
}
