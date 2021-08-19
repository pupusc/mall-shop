package com.wanmi.sbc.setting.popularsearchterms.service;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsDeleteResponse;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsListResponse;
import com.wanmi.sbc.setting.bean.vo.PopularSearchTermsVO;
import com.wanmi.sbc.setting.popularsearchterms.model.PopularSearchTerms;
import com.wanmi.sbc.setting.popularsearchterms.repository.PopularSearchTermsRespository;
import com.wanmi.sbc.setting.util.error.SearchTermsErrorCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 热门搜索词
 */
@Service
public class PopularSearchTermsService {


    @Autowired
    PopularSearchTermsRespository popularSearchTermsRespository;

    /**
     * 新增热门搜索词
     *
     * @param popularSearchTerms
     * @return
     */
    public PopularSearchTermsVO add(PopularSearchTerms popularSearchTerms) {
        List<PopularSearchTerms> searchTerms =
                popularSearchTermsRespository.findByDelFlagOrderBySortNumberAscCreateTimeDesc(DeleteFlag.NO);
        if (CollectionUtils.isNotEmpty(searchTerms)){
            // 最多可设置20个热搜词
            if (searchTerms.size() >= 20){
                throw new SbcRuntimeException(SearchTermsErrorCode.POPULAR_SEARCH_TERMS_QUANTITATIVE_RESTRICTION);
            }
            // 热搜词去重
            if (searchTerms.stream().anyMatch(term -> StringUtils.equals(popularSearchTerms.getPopularSearchKeyword()
                    , term.getPopularSearchKeyword()))) {
                throw new SbcRuntimeException(SearchTermsErrorCode.SEARCH_TERMS_NAME_EXIST);
            }
        }

        popularSearchTerms.setCreateTime(LocalDateTime.now());
        popularSearchTerms.setDelFlag(DeleteFlag.NO);

        PopularSearchTermsVO popularSearchTermsVO = new PopularSearchTermsVO();
        popularSearchTermsRespository.save(popularSearchTerms);
        KsBeanUtil.copyPropertiesThird(popularSearchTerms, popularSearchTermsVO);
        return popularSearchTermsVO;
    }

    /**
     * 修改热门搜索词
     * @param popularSearchTerms
     * @return
     */
    public BaseResponse modify(PopularSearchTerms popularSearchTerms) {
        PopularSearchTerms terms =
                popularSearchTermsRespository.findById(popularSearchTerms.getId()).orElseThrow(() -> new SbcRuntimeException(SearchTermsErrorCode.NOT_EXIST));
        // 除自己外，根据分类名称查询其他拼团分类是否存在
        Integer repeatName = popularSearchTermsRespository.findByKeyWordExceptSelf(popularSearchTerms.getId(),
                popularSearchTerms.getPopularSearchKeyword());
        if (Objects.nonNull(repeatName) && repeatName > 0) {
            throw new SbcRuntimeException(SearchTermsErrorCode.SEARCH_TERMS_NAME_EXIST);
        }

        if (popularSearchTerms.getRelatedLandingPage() != null) {
            terms.setRelatedLandingPage(popularSearchTerms.getRelatedLandingPage());
        }else{
            terms.setRelatedLandingPage(null);
        }
        if (null != popularSearchTerms.getPcLandingPage()) {
            terms.setPcLandingPage(popularSearchTerms.getPcLandingPage());
        }else{
            terms.setPcLandingPage(null);
        }
        if (popularSearchTerms.getPopularSearchKeyword() != null) {
            terms.setPopularSearchKeyword(popularSearchTerms.getPopularSearchKeyword());
        }
        if (popularSearchTerms.getSortNumber() != null) {
            terms.setSortNumber(popularSearchTerms.getSortNumber());
        }
        if (popularSearchTerms.getUpdatePerson() != null) {
            terms.setUpdatePerson(popularSearchTerms.getUpdatePerson());
        }
        terms.setUpdateTime(LocalDateTime.now());
        popularSearchTermsRespository.save(terms);
        return BaseResponse.success(terms);
    }

    /**
     * 删除热门搜索词
     *
     * @param popularSearchTerms
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deleteById(PopularSearchTerms popularSearchTerms) {
        popularSearchTerms.setDeleteTime(LocalDateTime.now());
        popularSearchTerms.setDelFlag(DeleteFlag.YES);
        PopularSearchTermsDeleteResponse popularSearchTermsDeleteResponse = new PopularSearchTermsDeleteResponse();
        popularSearchTermsDeleteResponse.setResultNum(popularSearchTermsRespository.deletePopularSearchTerms(popularSearchTerms.getId()));
        return BaseResponse.success(popularSearchTermsDeleteResponse);
    }

    /**
     * 列表查询
     *
     * @return
     */
    public BaseResponse<PopularSearchTermsListResponse> listPopularSearchTerms() {
        List<PopularSearchTerms> popularSearchTermsList =
                popularSearchTermsRespository.findByDelFlagOrderBySortNumberAscCreateTimeDesc(DeleteFlag.NO);
        List<PopularSearchTermsVO> popularSearchTerms = popularSearchTermsList.stream().map(popularSearch -> {
            PopularSearchTermsVO popularSearchTermsVO = new PopularSearchTermsVO();
            KsBeanUtil.copyPropertiesThird(popularSearch, popularSearchTermsVO);
            return popularSearchTermsVO;
        }).collect(Collectors.toList());
        return BaseResponse.success(new PopularSearchTermsListResponse(popularSearchTerms));
    }

    /**
     * 热口搜索词排序
     *
     * @param popularSearchTermsList
     * @return
     */
    @Transactional
    public BaseResponse sortPopularSearchTerms(List<PopularSearchTerms> popularSearchTermsList) {
        for (PopularSearchTerms popularSearchTerms : popularSearchTermsList) {
            popularSearchTermsRespository.sortPopularSearchTerms(popularSearchTerms.getSortNumber(),
                    popularSearchTerms.getId());
        }
        return BaseResponse.SUCCESSFUL();
    }
}
