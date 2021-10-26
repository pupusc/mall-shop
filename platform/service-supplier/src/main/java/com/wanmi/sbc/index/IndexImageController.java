package com.wanmi.sbc.index;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.enums.ImageTypeEnum;
import com.wanmi.sbc.goods.api.provider.image.ImageProvider;
import com.wanmi.sbc.goods.api.request.image.ImagePageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImageSortProviderRequest;
import com.wanmi.sbc.goods.api.response.image.ImageProviderResponse;
import com.wanmi.sbc.index.request.ImagePageRequest;
import com.wanmi.sbc.index.request.ImageRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/27 1:18 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/index/image")
@RestController
public class IndexImageController {

    @Resource
    private ImageProvider imageProvider;

    /**
     * 轮播图 新增
     * @menu 后台CMS2.0
     * @param imageRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@Validated(ImageRequest.Add.class) @RequestBody ImageRequest imageRequest) {
        ImageProviderRequest imageProviderRequest = new ImageProviderRequest();
        imageProviderRequest.setId(null);
        BeanUtils.copyProperties(imageRequest, imageProviderRequest);
        imageProviderRequest.setImageType(ImageTypeEnum.ROTATION_CHART_IMG.getCode());
        imageProvider.add(imageProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 轮播图 修改
     * @menu 后台CMS2.0
     * @param imageRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse update(@Validated(ImageRequest.Update.class) @RequestBody ImageRequest imageRequest) {
        ImageProviderRequest imageProviderRequest = new ImageProviderRequest();
        imageProviderRequest.setId(null);
        BeanUtils.copyProperties(imageRequest, imageProviderRequest);
        imageProviderRequest.setImageType(ImageTypeEnum.ROTATION_CHART_IMG.getCode());
        imageProvider.update(imageProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 轮播图 修改
     * @menu 后台CMS2.0
     * @param imageSortProviderRequestList
     * @return
     */
    @PostMapping("/sort")
    public BaseResponse sort(@RequestBody List<ImageSortProviderRequest> imageSortProviderRequestList) {
        imageProvider.sort(imageSortProviderRequestList);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 轮播图 列表
     * @menu 后台CMS2.0
     * @param imagePageRequest
     * @return
     */
    @PostMapping("/list")
    public BaseResponse<MicroServicePage<ImageProviderResponse>> list(@RequestBody ImagePageRequest imagePageRequest) {

        ImagePageProviderRequest imagePageProviderRequest = new ImagePageProviderRequest();
        BeanUtils.copyProperties(imagePageRequest, imagePageProviderRequest);
        return imageProvider.list(imagePageProviderRequest);
    }


}
