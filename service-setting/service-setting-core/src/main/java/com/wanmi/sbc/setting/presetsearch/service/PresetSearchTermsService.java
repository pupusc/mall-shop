package com.wanmi.sbc.setting.presetsearch.service;


import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsListResponse;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsQueryResponse;
import com.wanmi.sbc.setting.bean.vo.PresetSearchTermsVO;
import com.wanmi.sbc.setting.presetsearch.model.PresetSearchTerms;
import com.wanmi.sbc.setting.presetsearch.repositoy.PresetSearchTermsRepositoy;
import com.wanmi.sbc.setting.util.error.SearchTermsErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PresetSearchTermsService {

    @Autowired
    PresetSearchTermsRepositoy presetSearchTermsRepositoy;


    /**
     * 新增预置搜索词
     *
     * @param presetSearchTerms
     * @return
     */
    public PresetSearchTerms add(PresetSearchTerms presetSearchTerms) {
        List<PresetSearchTerms> list = presetSearchTermsRepositoy.findAll();
        if (list.size()>=2){
            throw new SbcRuntimeException(SearchTermsErrorCode.PRESET_SEARCH_TERM_RESTRICTIONS);
        }
        presetSearchTermsRepositoy.save(presetSearchTerms);
        return presetSearchTerms;
    }

    /**
     * 新增预置搜索词
     *
     * @param presetSearchTerms
     * @return
     */
    public List<PresetSearchTerms> addBatch(List<PresetSearchTerms> presetSearchTerms) {
        List<PresetSearchTerms> presetSearchTermsList = presetSearchTermsRepositoy.saveAll(presetSearchTerms);
        return presetSearchTermsList;
    }

    /**
     * 编辑预置搜索词
     * @param presetSearchTerms
     * @return
     */
    public PresetSearchTerms modify(PresetSearchTerms presetSearchTerms){
        presetSearchTermsRepositoy.save(presetSearchTerms);
        return presetSearchTerms;
    }

    /**
     * 查询预置搜索词
     * @return
     */
    public PresetSearchTermsQueryResponse listPresetSearchTerms() {
        List<PresetSearchTerms> list = presetSearchTermsRepositoy.findAll();
        List<PresetSearchTermsVO> ListSearch = list.stream().map(search -> {
            PresetSearchTermsVO presetSearchTermsVO = new PresetSearchTermsVO();
            presetSearchTermsVO.setId(search.getId());
            presetSearchTermsVO.setPresetChannel(search.getPresetChannel());
            presetSearchTermsVO.setPresetSearchKeyword(search.getPresetSearchKeyword());
            presetSearchTermsVO.setPresetSearchType(search.getPresetSearchType());
            presetSearchTermsVO.setPresetSearchKeywordPageUrl(search.getPresetSearchKeywordPageUrl());
            return presetSearchTermsVO;
        }).collect(Collectors.toList());
        return new PresetSearchTermsQueryResponse(ListSearch);
    }

    /**
     * 查询预置搜索词
     * @return
     */
    public PresetSearchTermsQueryResponse listPresetSearchTermsV2() {
        List<PresetSearchTerms> list = presetSearchTermsRepositoy.findAll();
        List<PresetSearchTermsVO> ListSearch = new ArrayList<>();
        list.stream().filter(s->s.getPresetChannel()==0).forEach(search -> {
            if(search.getPresetSearchKeyword().contains(",")){
                String[] split = search.getPresetSearchKeyword().split(",");
                for (int i=0;i<split.length;i++) {
                    PresetSearchTermsVO presetSearchTermsVO = new PresetSearchTermsVO();
                    presetSearchTermsVO.setId(search.getId());
                    presetSearchTermsVO.setPresetChannel(search.getPresetChannel());
                    presetSearchTermsVO.setPresetSearchKeyword(split[i]);
                    presetSearchTermsVO.setPresetSearchType(search.getPresetSearchType());
                    presetSearchTermsVO.setPresetSearchKeywordPageUrl(search.getPresetSearchKeywordPageUrl());
                    ListSearch.add(presetSearchTermsVO);
                }
            }else {
                PresetSearchTermsVO presetSearchTermsVO = new PresetSearchTermsVO();
                presetSearchTermsVO.setId(search.getId());
                presetSearchTermsVO.setPresetChannel(search.getPresetChannel());
                presetSearchTermsVO.setPresetSearchKeyword(search.getPresetSearchKeyword());
                presetSearchTermsVO.setPresetSearchType(search.getPresetSearchType());
                presetSearchTermsVO.setPresetSearchKeywordPageUrl(search.getPresetSearchKeywordPageUrl());
                ListSearch.add(presetSearchTermsVO);
            }
        });
        return new PresetSearchTermsQueryResponse(ListSearch);
    }
}
