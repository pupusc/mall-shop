package com.wanmi.sbc.setting.defaultsearchterms.service;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.response.defaultsearchterms.DefaultSearchTermsListResponse;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsDeleteResponse;
import com.wanmi.sbc.setting.api.response.popularsearchterms.PopularSearchTermsListResponse;
import com.wanmi.sbc.setting.bean.vo.DefaultSearchTermsVO;
import com.wanmi.sbc.setting.bean.vo.PopularSearchTermsVO;
import com.wanmi.sbc.setting.defaultsearchterms.model.DefaultSearchTerms;
import com.wanmi.sbc.setting.defaultsearchterms.repository.DefaultSearchTermsRespository;
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
     * @Date  2023/2/4 14:13
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
}
