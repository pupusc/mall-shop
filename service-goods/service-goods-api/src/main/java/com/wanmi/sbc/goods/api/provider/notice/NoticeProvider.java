package com.wanmi.sbc.goods.api.provider.notice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.request.notice.NoticePageProviderRequest;
import com.wanmi.sbc.goods.api.request.notice.NoticeProviderRequest;
import com.wanmi.sbc.goods.api.response.notice.NoticeProviderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 5:17 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@FeignClient(value = "${application.goods.name}", contextId = "NoticeProvider")
public interface NoticeProvider {


    /**
     * 新增公告
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/notice/add")
    BaseResponse add(@Validated(NoticeProviderRequest.Add.class) @RequestBody NoticeProviderRequest request);


    /**
     * 修改公告
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/notice/update")
    BaseResponse update(@Validated(NoticeProviderRequest.Add.class) @RequestBody NoticeProviderRequest request);


    /**
     * 获取公告列表
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/notice/list")
    BaseResponse<MicroServicePage<NoticeProviderResponse>> list(@RequestBody NoticePageProviderRequest request);

    /**
     * 获取公告列表，无分页
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/notice/listNoPage")
    BaseResponse<List<NoticeProviderResponse>> listNoPage(NoticePageProviderRequest request);

}
