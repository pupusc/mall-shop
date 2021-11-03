package com.wanmi.sbc.classify;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.classify.response.ClassifyResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/7 7:10 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/classify")
@RestController
public class ClassifyController {

    @Autowired
    private ClassifyProvider classifyProvider;

    /**
     *
     * 类目列表
     * @menu 商城书单和类目
     * @status undone
     * @return
     */
    @GetMapping("/listClassify")
    public BaseResponse<List<ClassifyResponse>> listClassify() {
        BaseResponse<List<ClassifyProviderResponse>> listClassifyResponse = classifyProvider.listClassify();
        String listClassifyJsonStr = JSON.toJSONString(listClassifyResponse.getContext());
        List<ClassifyResponse> result = JSON.parseArray(listClassifyJsonStr, ClassifyResponse.class);
        return BaseResponse.success(result);
    }
}