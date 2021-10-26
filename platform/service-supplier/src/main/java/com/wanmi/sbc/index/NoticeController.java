package com.wanmi.sbc.index;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.notice.NoticeProvider;
import com.wanmi.sbc.goods.api.request.notice.NoticePageProviderRequest;
import com.wanmi.sbc.goods.api.request.notice.NoticeProviderRequest;
import com.wanmi.sbc.goods.api.response.notice.NoticeProviderResponse;
import com.wanmi.sbc.index.request.NoticePageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/27 2:00 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/notice")
@RestController
public class NoticeController {

    @Autowired
    private NoticeProvider noticeProvider;

    /**
     * 公告 新增公告
     * @menu 后台CMS2.0
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@Validated(NoticeProviderRequest.Add.class) @RequestBody NoticeProviderRequest request) {
        return noticeProvider.add(request);
    }


    /**
     * 公告 修改公告
     * @menu 后台CMS2.0
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@Validated(NoticeProviderRequest.Update.class) @RequestBody NoticeProviderRequest request) {
        return noticeProvider.update(request);
    }


    /**
     * 公告 列表公告
     * @menu 后台CMS2.0
     * @param request
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<MicroServicePage<NoticeProviderResponse>> list(@RequestBody NoticePageRequest request) {
        NoticePageProviderRequest requestParam = new NoticePageProviderRequest();
        BeanUtils.copyProperties(request, requestParam);
        return noticeProvider.list(requestParam);
    }
}
