package com.soybean.mall.miniapp.image;

import com.soybean.mall.miniapp.image.request.WxImagePageRequest;
import com.soybean.mall.miniapp.image.request.WxImageRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.ImageTypeEnum;
import com.wanmi.sbc.goods.api.provider.image.ImageProvider;
import com.wanmi.sbc.goods.api.request.image.ImagePageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImageProviderRequest;
import com.wanmi.sbc.goods.api.response.image.ImageProviderResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * Description: 订阅图片
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/20 1:27 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/wx/image")
@RestController
public class WxImageController {

    @Resource
    private ImageProvider imageProvider;

    /**
     * 小程序图片 cms-新增
     * @menu 小程序订阅
     * @param imageRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@Validated(WxImageRequest.Add.class) @RequestBody WxImageRequest imageRequest) {
        if (StringUtils.isEmpty(imageRequest.getBusinessId())) {
            throw new SbcRuntimeException("K-000009");
        }
        ImageProviderRequest imageProviderRequest = new ImageProviderRequest();
        imageProviderRequest.setId(null);
        BeanUtils.copyProperties(imageRequest, imageProviderRequest);
        imageProviderRequest.setImageType(ImageTypeEnum.WX_SUBSCRIBE.getCode());
        imageProvider.add(imageProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 小程序图片 cms-修改
     * @menu 小程序订阅
     * @param imageRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@Validated(WxImageRequest.Update.class) @RequestBody WxImageRequest imageRequest) {
        ImageProviderRequest imageProviderRequest = new ImageProviderRequest();
        BeanUtils.copyProperties(imageRequest, imageProviderRequest);
        imageProviderRequest.setImageType(ImageTypeEnum.WX_SUBSCRIBE.getCode());
        imageProvider.update(imageProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 小程序图片 cms-开启关闭
     * @menu 小程序订阅
     * @param id false 关闭
     * @param isOpen true 开启
     * @return
     */
    @GetMapping("/publish/{imageId}/{isOpen}")
    public BaseResponse publish(@PathVariable("imageId") Integer id, @PathVariable("isOpen") Boolean isOpen) {
        imageProvider.publish(id, isOpen);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 小程序图片 cms-列表
     * @menu 小程序订阅
     * @param imagePageRequest
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<MicroServicePage<ImageProviderResponse>> list(@RequestBody WxImagePageRequest imagePageRequest) {

        ImagePageProviderRequest imagePageProviderRequest = new ImagePageProviderRequest();
        BeanUtils.copyProperties(imagePageRequest, imagePageProviderRequest);
        imagePageProviderRequest.setImageTypeList(Collections.singletonList(ImageTypeEnum.WX_SUBSCRIBE.getCode()));
        return imageProvider.list(imagePageProviderRequest);
    }
}
