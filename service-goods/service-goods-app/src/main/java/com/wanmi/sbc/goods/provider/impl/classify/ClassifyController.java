package com.wanmi.sbc.goods.provider.impl.classify;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.goods.classify.service.ClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/7 7:02 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class ClassifyController implements ClassifyProvider {

    @Autowired
    private ClassifyService classifyService;

    /**
     * 获取类目列表
     * @return
     */
    @Override
    public BaseResponse<List<ClassifyProviderResponse>> listClassify() {
        return BaseResponse.success(classifyService.listClassify());
    }
}
