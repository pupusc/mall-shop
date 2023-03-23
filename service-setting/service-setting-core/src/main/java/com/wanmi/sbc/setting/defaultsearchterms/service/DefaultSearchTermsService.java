package com.wanmi.sbc.setting.defaultsearchterms.service;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.response.defaultsearchterms.DefaultSearchTermsListResponse;
import com.wanmi.sbc.setting.api.response.defaultsearchterms.SearchTermBo;

import com.wanmi.sbc.setting.bean.vo.DefaultSearchTermsVO;

import com.wanmi.sbc.setting.defaultsearchterms.model.DefaultSearchTerms;
import com.wanmi.sbc.setting.defaultsearchterms.repository.DefaultSearchTermsRespository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 默认搜索词
 * @Author zh
 * @Date 2023/2/4 11:14
 */
@Service
public class DefaultSearchTermsService {


    @Autowired
    DefaultSearchTermsRespository defaultSearchTermsRespository;

    /**
     * @Description 查询默认搜索词
     * @Author zh
     * @Date 2023/2/4 14:13
     */
    public BaseResponse<List<DefaultSearchTermsListResponse>> listDefaultSearchTerms(Integer defaultChannel) {
        //查询主标题 主标题为true 标签为false
        List<DefaultSearchTerms> defaultParentSearchTermsList = defaultSearchTermsRespository
                .findByDelFlagAndDefaultChannelAndIsParentOrderBySortNumberAscCreateTimeDesc(DeleteFlag.NO, defaultChannel, true);
        List<DefaultSearchTermsListResponse> defaultSearchTermsListResponse = defaultParentSearchTermsList.stream().map(defaultSearch -> {
            DefaultSearchTermsVO defaultParentSearchTermsVO = new DefaultSearchTermsVO();
            KsBeanUtil.copyPropertiesThird(defaultSearch, defaultParentSearchTermsVO);
            //查询子标题
            List<DefaultSearchTerms> defaultSonSearchTermsList = defaultSearchTermsRespository
                    .findByDelFlagAndDefaultChannelAndParentIdAndIsParentOrderBySortNumberAscCreateTimeDesc(DeleteFlag.NO, defaultChannel, defaultSearch.getId(), false);
            List<DefaultSearchTermsVO> defaultSonSearchTermsVOList = defaultSonSearchTermsList.stream().map(defaultSearchTerm -> {
                DefaultSearchTermsVO defaultSearchTermsVO = new DefaultSearchTermsVO();
                KsBeanUtil.copyPropertiesThird(defaultSearchTerm, defaultSearchTermsVO);
                return defaultSearchTermsVO;
            }).collect(Collectors.toList());
            return new DefaultSearchTermsListResponse(defaultParentSearchTermsVO, defaultSonSearchTermsVOList);
        }).collect(Collectors.toList());
        return BaseResponse.success(defaultSearchTermsListResponse);
    }

    public BaseResponse<MicroServicePage<SearchTermBo>> DefaultSearchTermsByPage(SearchTermBo bo) {
        Page<DefaultSearchTerms> pageSearchTerms = defaultSearchTermsRespository.findAll(defaultSearchTermsRespository.deFaultSearchTermsSearchContent(bo), PageRequest.of(bo.getPageNum(),
                bo.getPageSize(), Sort.by(Sort.Direction.ASC, "sorting")));
        List<DefaultSearchTerms> listSearchTerms = pageSearchTerms.getContent();
        MicroServicePage<SearchTermBo> microServicePage = new MicroServicePage<>();
        microServicePage.setTotal(pageSearchTerms.getTotalElements());
        microServicePage.setContent(KsBeanUtil.convertList(listSearchTerms, SearchTermBo.class));
        return BaseResponse.success(microServicePage);
    }

    public BaseResponse<List<SearchTermBo>> listDefaultSearchTermsByPage(SearchTermBo bo) {
        Sort sort = Sort.by(Sort.Direction.ASC, "sortNumber");
        List<DefaultSearchTerms> SearchTermsParent = defaultSearchTermsRespository.findAll(defaultSearchTermsRespository.deFaultSearchTermsSearchContent(bo),sort);
        List<SearchTermBo> searchTermBos = KsBeanUtil.convertList(SearchTermsParent, SearchTermBo.class);
        for (SearchTermBo term : searchTermBos) {
            List<SearchTermBo> children = KsBeanUtil.convertList(defaultSearchTermsRespository.findAll(defaultSearchTermsRespository.deFaultSearchTermsSearchContent(term),sort), SearchTermBo.class);
            term.setChildrenList(children);
        }
        return BaseResponse.success(searchTermBos);
    }


    public BaseResponse<Long> delete(SearchTermBo bo) {
        DefaultSearchTerms one = defaultSearchTermsRespository.getOne(bo.getId());
        List<DefaultSearchTerms> SearchTermsParent = defaultSearchTermsRespository.findAll(defaultSearchTermsRespository.deFaultSearchTermsSearchContent(bo));
        if (SearchTermsParent.size() > 0) {
            for (DefaultSearchTerms SearchTerms : SearchTermsParent) {
                SearchTerms.setDelFlag(DeleteFlag.YES);
                defaultSearchTermsRespository.save(SearchTerms);
            }
        }
        one.setDelFlag(DeleteFlag.YES);
        defaultSearchTermsRespository.save(one);
        return BaseResponse.success(bo.getId());
    }

    public BaseResponse<Integer> add(SearchTermBo bo) {
        DefaultSearchTerms convert = KsBeanUtil.convert(bo, DefaultSearchTerms.class);
        convert.setDelFlag(DeleteFlag.NO);
        convert.setCreateTime(LocalDateTime.now());
        defaultSearchTermsRespository.save(convert);
        return BaseResponse.success(1);
    }

    public BaseResponse<Long> update(SearchTermBo bo) {
        DefaultSearchTerms convert = KsBeanUtil.convert(bo, DefaultSearchTerms.class);
        convert.setDelFlag(DeleteFlag.NO);
        convert.setUpdateTime(LocalDateTime.now());
        DefaultSearchTerms save = defaultSearchTermsRespository.save(convert);
        return BaseResponse.success(save.getId());
    }

    public boolean existName(SearchTermBo bo) {
        int count = defaultSearchTermsRespository.countByDefaultSearchKeywordAndDelFlag(bo.getDefaultSearchKeyword());
        return count > 0;
    }

    public boolean existId(SearchTermBo bo) {
        int count = defaultSearchTermsRespository.countById(bo.getId());
        return count > 0;
    }
}
