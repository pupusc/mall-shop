package com.wanmi.sbc.goods.api.provider.image;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.request.image.ImagePageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImageProviderRequest;
import com.wanmi.sbc.goods.api.request.image.ImageSortProviderRequest;
import com.wanmi.sbc.goods.api.response.image.ImageProviderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    BaseResponse<MicroServicePage<ImageProviderResponse>> list(@RequestBody ImagePageProviderRequest imagePageProviderRequest);


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

    /**
     * 轮播图 开启关闭
     * @menu 后台CMS2.0
     * @param id false 关闭
     * @param isOpen true 开启
     * @return
     */
    @GetMapping("/publish/{imageId}/{isOpen}")
    BaseResponse publish(@PathVariable("imageId") Integer id, @PathVariable("isOpen") Boolean isOpen);

    /**
     * 删除图片
     * @menu 后台CMS2.0
     * @return
     */
    @GetMapping("/delete/{imageId}")
    BaseResponse delete(@PathVariable("imageId") Integer imageId);
}
