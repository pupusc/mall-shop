package com.soybean.mall.image;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.enums.ImageTypeEnum;
import com.wanmi.sbc.goods.api.enums.StateEnum;
import com.wanmi.sbc.goods.api.enums.UsingStateEnum;
import com.wanmi.sbc.goods.api.provider.image.ImageProvider;
import com.wanmi.sbc.goods.api.request.image.ImagePageProviderRequest;
import com.wanmi.sbc.goods.api.response.image.ImageProviderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/20 2:09 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/wx/image")
@RestController
public class ImageController {

    @Autowired
    private ImageProvider imageProvider;

    /**
     * app - 小程序订阅图片
     * @menu 小程序订阅
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<ImageProviderResponse>> listImageSubscribe() {
        ImagePageProviderRequest imagePageProviderRequest = new ImagePageProviderRequest();
        imagePageProviderRequest.setPublishState(UsingStateEnum.USING.getCode());
        imagePageProviderRequest.setStatus(StateEnum.RUNNING.getCode());
        imagePageProviderRequest.setImageTypeList(Collections.singletonList(ImageTypeEnum.WX_SUBSCRIBE.getCode()));
        BaseResponse<List<ImageProviderResponse>> listBaseResponse = imageProvider.listNoPage(imagePageProviderRequest);
        return BaseResponse.success(listBaseResponse.getContext());
    }
}
