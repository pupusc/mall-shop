package com.wanmi.sbc.goods.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.IndexCmsProvider;
import com.wanmi.sbc.goods.api.request.index.*;
import com.wanmi.sbc.goods.api.response.index.IndexFeatureVo;
import com.wanmi.sbc.goods.api.response.index.IndexModuleVo;
import com.wanmi.sbc.goods.index.model.IndexFeature;
import com.wanmi.sbc.goods.index.model.IndexModule;
import com.wanmi.sbc.goods.index.service.IndexCmsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    private List<IndexFeatureVo> changeIndexFeature2Vo(List<IndexFeature> content) {
        LocalDateTime now = LocalDateTime.now();
         return content.stream().map(indexFeature -> {
            IndexFeatureVo indexFeatureVo = new IndexFeatureVo();
            BeanUtils.copyProperties(indexFeature, indexFeatureVo);
            if(now.isBefore(indexFeature.getBeginTime())){
                //未开始
                indexFeatureVo.setState(0);
            }else if(now.isAfter(indexFeature.getEndTime())){
                //已结束
                indexFeatureVo.setState(2);
            }else{
                //进行中
                indexFeatureVo.setState(1);
            }
            return indexFeatureVo;
        }).collect(Collectors.toList());
    }

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
     * @param cmsSpecialTopicUpdateRequest
     * @return
     */
    @Override
    public BaseResponse updateSpecialTopic(CmsSpecialTopicUpdateRequest cmsSpecialTopicUpdateRequest) {
        indexCmsService.updateSpecialTopic(cmsSpecialTopicUpdateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询特色栏目
     * @param cmsSpecialTopicSearchRequest
     * @return
     */
    @Override
    public BaseResponse<MicroServicePage<IndexFeatureVo>> searchSpecialTopic(CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest) {
        Page<IndexFeature> page = indexCmsService.searchSpecialTopicPage(cmsSpecialTopicSearchRequest);
        List<IndexFeature> content = page.getContent();
        LocalDateTime now = LocalDateTime.now();
        List<IndexFeatureVo> dtos = content.stream().map(indexFeature -> {
            IndexFeatureVo indexFeatureVo = new IndexFeatureVo();
            BeanUtils.copyProperties(indexFeature, indexFeatureVo);
            if(now.isBefore(indexFeature.getBeginTime())){
                //未开始
                indexFeatureVo.setState(0);
            }else if(now.isAfter(indexFeature.getEndTime())){
                //已结束
                indexFeatureVo.setState(2);
            }else{
                //进行中
                indexFeatureVo.setState(1);
            }
            indexFeatureVo.setPublishState(indexFeature.getPublishState().toValue());
            return indexFeatureVo;
        }).collect(Collectors.toList());
        MicroServicePage<IndexFeatureVo> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(page.getTotalElements());
        microServicePage.setContent(this.changeIndexFeature2Vo(content));
        return BaseResponse.success(microServicePage);
    }


    /**
     * 查询特色列表
     * @param cmsSpecialTopicSearchRequest
     * @return
     */
    public BaseResponse<List<IndexFeatureVo>> listNoPageSpecialTopic(CmsSpecialTopicSearchRequest cmsSpecialTopicSearchRequest){
        return BaseResponse.success(this.changeIndexFeature2Vo(indexCmsService.listNoPageSpecialTopic(cmsSpecialTopicSearchRequest)));
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
     * @param 
     * @return
     */
    @Override
    public BaseResponse updateTitle(CmsTitleUpdateRequest cmsTitleUpdateRequest) {
        indexCmsService.updateTitle(cmsTitleUpdateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询主副标题,前端请求 0-未启用 1-启用
     * @return
     */
    @Override
    public BaseResponse<List<IndexModuleVo>> searchTitle(@RequestParam Integer publishState) {
        CmsTitleSearchRequest cmsTitleSearchRequest = new CmsTitleSearchRequest();
        cmsTitleSearchRequest.setPublishState(publishState);
        List<IndexModule> indexModules = indexCmsService.searchTitle(cmsTitleSearchRequest, true);
        List<IndexModuleVo> dtos = indexModules.stream().map(indexModule -> {
            IndexModuleVo indexModuleVo = new IndexModuleVo();
            BeanUtils.copyProperties(indexModule, indexModuleVo);
            indexModuleVo.setPublishState(indexModule.getPublishState().toValue());
            return indexModuleVo;
        }).collect(Collectors.toList());
        return BaseResponse.success(dtos);
    }

    /**
     * 查询主副标题，后台请求
     * @return
     */
    @Override
    public BaseResponse<List<IndexModuleVo>> searchTitle(@RequestBody CmsTitleSearchRequest cmsTitleSearchRequest) {
        List<IndexModule> indexModules = indexCmsService.searchTitle(cmsTitleSearchRequest, false);
        List<IndexModuleVo> dtos = indexModules.stream().map(indexModule -> {
            IndexModuleVo indexModuleVo = new IndexModuleVo();
            BeanUtils.copyProperties(indexModule, indexModuleVo);
            indexModuleVo.setPublishState(indexModule.getPublishState().toValue());
            return indexModuleVo;
        }).collect(Collectors.toList());
        return BaseResponse.success(dtos);
    }
}
