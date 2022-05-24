package com.wanmi.sbc.goods.provider.impl.image;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.image.ImageProvider;
import com.wanmi.sbc.goods.api.request.image.ImagePageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImageSortProviderRequest;
import com.wanmi.sbc.goods.api.response.image.ImageProviderResponse;
import com.wanmi.sbc.goods.image.model.root.ImageDTO;
import com.wanmi.sbc.goods.image.service.ImageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 图片信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/21 2:41 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
public class ImageController implements ImageProvider {


    @Autowired
    private ImageService imageService;

    @Override
    public BaseResponse add(ImageProviderRequest imageProviderRequest) {
        imageService.add(imageProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse update(ImageProviderRequest imageProviderRequest) {
        imageService.update(imageProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse<MicroServicePage<ImageProviderResponse>> list(ImagePageProviderRequest imagePageProviderRequest) {

        Page<ImageDTO> list = imageService.list(imagePageProviderRequest);
        List<ImageProviderResponse> result = this.packageImageProviderResponse(list.getContent());
        MicroServicePage<ImageProviderResponse> microServicePage = new MicroServicePage<>();
        microServicePage.setPageable(list.getPageable());
        microServicePage.setNumber(list.getNumber());
        microServicePage.setSize(list.getSize());
        microServicePage.setTotal(list.getTotalElements());
        microServicePage.setContent(result);
        return BaseResponse.success(microServicePage);
    }


    @Override
    public BaseResponse<List<ImageProviderResponse>> listNoPage(ImagePageProviderRequest imagePageProviderRequest){
        List<ImageDTO> listImageDTO = imageService.listNoPage(imagePageProviderRequest);
        List<ImageProviderResponse> result = this.packageImageProviderResponse(listImageDTO);
        return BaseResponse.success(result);
    }

    @Override
    public BaseResponse sort(List<ImageSortProviderRequest> imageSortProviderRequestList) {
        imageService.sort(imageSortProviderRequestList);
        return BaseResponse.SUCCESSFUL();
    }


    @Override
    public BaseResponse publish(Integer id, Boolean isOpen) {
        imageService.publish(id, isOpen);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 封装对象
     * @param imageDTOList
     * @return
     */
    private List<ImageProviderResponse> packageImageProviderResponse(List<ImageDTO> imageDTOList) {
        LocalDateTime now = LocalDateTime.now();
        List<ImageProviderResponse> result = new ArrayList<>();
        for (ImageDTO imageDTOParam : imageDTOList) {
            ImageProviderResponse imageProviderResponse = new ImageProviderResponse();
            BeanUtils.copyProperties(imageDTOParam, imageProviderResponse);
            imageProviderResponse.setPublishState(imageDTOParam.getPublishState());
            if (imageProviderResponse.getBeginTime() != null && imageProviderResponse.getEndTime() != null) {
                //未开始
                if (imageProviderResponse.getBeginTime().isAfter(now)) {
                    imageProviderResponse.setStatus(0);
                } else if (imageProviderResponse.getBeginTime().isBefore(now) && imageProviderResponse.getEndTime().isAfter(now)) {
                    //进行中
                    imageProviderResponse.setStatus(1);
                } else {
                    //已结束
                    imageProviderResponse.setStatus(2);
                }
            }
            result.add(imageProviderResponse);
        }
        return result;
    }

    @Override
    public BaseResponse delete(Integer imageId) {
        imageService.delete(imageId);
        return BaseResponse.SUCCESSFUL();
    }
}
