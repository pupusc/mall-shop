package com.wanmi.sbc.goods.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.IndexCmsProvider;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicAddRequest;
import com.wanmi.sbc.goods.api.request.index.CmsSpecialTopicSearchRequest;
import com.wanmi.sbc.goods.api.request.index.CmsTitleAddRequest;
import com.wanmi.sbc.goods.api.response.index.IndexFeatureDto;
import com.wanmi.sbc.goods.api.response.index.IndexModuleDto;
import com.wanmi.sbc.goods.index.model.IndexFeature;
import com.wanmi.sbc.goods.index.model.IndexModule;
import com.wanmi.sbc.goods.index.service.IndexCmsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CMS首页
 */
@RestController
public class IndexCmsController implements IndexCmsProvider {

    @Autowired
    private IndexCmsService indexCmsService;

    /**
     * 添加特色栏目
     * @param cmsSpecialTopicAddRequest
     * @return
     */
    @Override
    public BaseResponse addSpecialTopic(CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest) {
        indexCmsService.addSpecialTopic(cmsSpecialTopicAddRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新特色栏目
     * @param cmsSpecialTopicAddRequest
     * @return
     */
    @Override
    public BaseResponse updateSpecialTopic(CmsSpecialTopicAddRequest cmsSpecialTopicAddRequest) {
        indexCmsService.updateSpecialTopic(cmsSpecialTopicAddRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询特色栏目
     * @param cmsSpecialTopicSearchRequest
     * @return
     */
    @Override
    public BaseResponse<MicroServicePage<IndexFeatureDto>> searchSpecialTopic(CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest) {
        Page<IndexFeature> page = indexCmsService.searchSpecialTopicPage(cmsSpecialTopicSearchRequest);
        List<IndexFeature> content = page.getContent();
        LocalDateTime now = LocalDateTime.now();
        List<IndexFeatureDto> dtos = content.stream().map(indexFeature -> {
            IndexFeatureDto indexFeatureDto = new IndexFeatureDto();
            BeanUtils.copyProperties(indexFeature, indexFeatureDto);
            if(now.isBefore(indexFeature.getBeginTime())){
                //未开始
                indexFeatureDto.setState(0);
            }else if(now.isAfter(indexFeature.getEndTime())){
                //已结束
                indexFeatureDto.setState(2);
            }else{
                //进行中
                indexFeatureDto.setState(1);
            }
            return indexFeatureDto;
        }).collect(Collectors.toList());
        MicroServicePage<IndexFeatureDto> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(page.getTotalElements());
        microServicePage.setContent(dtos);
        return BaseResponse.success(microServicePage);
    }

    /**
     * 添加主副标题
     * @param cmsTitleAddRequest
     * @return
     */
    @Override
    public BaseResponse addTitle(CmsTitleAddRequest cmsTitleAddRequest) {
        indexCmsService.addTitle(cmsTitleAddRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除主副标题
     * @param id
     * @return
     */
    @Override
    public BaseResponse deleteTitle(Integer id) {
        indexCmsService.deleteTitle(id);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新主副标题
     * @param cmsTitleAddRequest
     * @return
     */
    @Override
    public BaseResponse updateTitle(CmsTitleAddRequest cmsTitleAddRequest) {
        indexCmsService.updateTitle(cmsTitleAddRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询主副标题
     * @return
     */
    @Override
    public BaseResponse<List<IndexModuleDto>> searchTitle() {
        List<IndexModule> indexModules = indexCmsService.searchTitle();
        List<IndexModuleDto> dtos = indexModules.stream().map(indexModule -> {
            IndexModuleDto indexModuleDto = new IndexModuleDto();
            BeanUtils.copyProperties(indexModule, indexModuleDto);
            return indexModuleDto;
        }).collect(Collectors.toList());
        return BaseResponse.success(dtos);
    }
}
