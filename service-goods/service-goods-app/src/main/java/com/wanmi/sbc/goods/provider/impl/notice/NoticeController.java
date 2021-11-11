package com.wanmi.sbc.goods.provider.impl.notice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.notice.NoticeProvider;
import com.wanmi.sbc.goods.api.request.notice.NoticePageProviderRequest;
import com.wanmi.sbc.goods.api.request.notice.NoticeProviderRequest;
import com.wanmi.sbc.goods.api.response.notice.NoticeProviderResponse;
import com.wanmi.sbc.goods.notice.model.root.NoticeDTO;
import com.wanmi.sbc.goods.notice.service.NoticeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 3:46 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class NoticeController implements NoticeProvider {

    @Autowired
    private NoticeService noticeService;

    /**
     * 新增公告
     * @param request
     * @return
     */
    @Override
    public BaseResponse add(NoticeProviderRequest request) {
        noticeService.add(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改公告
     * @param request
     * @return
     */
    @Override
    public BaseResponse update(NoticeProviderRequest request) {
        noticeService.update(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 获取公告列表
     * @param request
     * @return
     */
    @Override
    public BaseResponse<MicroServicePage<NoticeProviderResponse>> list(NoticePageProviderRequest request) {
        List<NoticeProviderResponse> result = new ArrayList<>();
        Page<NoticeDTO> noticeDTOPage = noticeService.list(request);
        for (NoticeDTO noticeParam : noticeDTOPage.getContent()) {
            NoticeProviderResponse noticeProviderResponse = new NoticeProviderResponse();
            noticeProviderResponse.setId(noticeParam.getId());
            noticeProviderResponse.setContent(noticeParam.getContent());
            noticeProviderResponse.setBeginTime(noticeParam.getBeginTime());
            noticeProviderResponse.setEndTime(noticeParam.getEndTime());
            noticeProviderResponse.setPublishState(noticeParam.getPublishState());
            result.add(noticeProviderResponse);
        }

        MicroServicePage<NoticeProviderResponse> resultMicroService = new MicroServicePage<>();
        resultMicroService.setPageable(noticeDTOPage.getPageable());
        resultMicroService.setTotal(noticeDTOPage.getTotalElements());
        resultMicroService.setContent(result);
        return BaseResponse.success(resultMicroService);
    }


    /**
     * 获取无分页公告列表
     * @param request
     * @return
     */
    @Override
    public BaseResponse<List<NoticeProviderResponse>> listNoPage(NoticePageProviderRequest request) {
        List<NoticeProviderResponse> result = new ArrayList<>();
        List<NoticeDTO> noticeDTOList = noticeService.listNoPage(request);
        for (NoticeDTO noticeParam : noticeDTOList) {
            NoticeProviderResponse noticeProviderResponse = new NoticeProviderResponse();
            BeanUtils.copyProperties(noticeParam, noticeProviderResponse);
            result.add(noticeProviderResponse);
        }
        return BaseResponse.success(result);
    }
}
