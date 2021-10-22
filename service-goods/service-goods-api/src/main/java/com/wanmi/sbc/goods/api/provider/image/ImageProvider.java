package com.wanmi.sbc.goods.api.provider.image;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.image.ImagePageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImageSortProviderRequest;
import com.wanmi.sbc.goods.api.response.image.ImageProviderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "${application.goods.name}", contextId = "ImageProvider")
public interface ImageProvider {

    /**
     * 新增图片
     * @param imageProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/image/add")
    BaseResponse add(@RequestBody @Validated(ImageProviderRequest.Add.class) ImageProviderRequest imageProviderRequest);


    /**
     * 修改图片
     * @param imageProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/image/update")
    BaseResponse update(@RequestBody @Validated(ImageProviderRequest.Update.class) ImageProviderRequest imageProviderRequest);


    /**
     * 获取图片列表
     * @param imagePageProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/image/list")
    BaseResponse list(@RequestBody ImagePageProviderRequest imagePageProviderRequest);


    /**
     * 获取图片列表 无分页
     * @param imagePageProviderRequest
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/image/listNoPage")
    BaseResponse<List<ImageProviderResponse>> listNoPage(@RequestBody ImagePageProviderRequest imagePageProviderRequest);

    /**
     * 排序图片
     * @param imageSortProviderRequestList
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/image/sort")
    BaseResponse sort(@RequestBody List<ImageSortProviderRequest> imageSortProviderRequestList);

}
