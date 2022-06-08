package com.wanmi.sbc.setting.presetsearch.service;


import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsQueryResponse;
import com.wanmi.sbc.setting.bean.vo.PresetSearchTermsVO;
import com.wanmi.sbc.setting.presetsearch.model.PresetSearchTerms;
import com.wanmi.sbc.setting.presetsearch.repositoy.PresetSearchTermsRepositoy;
import com.wanmi.sbc.setting.util.error.SearchTermsErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            presetSearchTermsVO.setPresetSearchKeyword(search.getPresetSearchKeyword());
            return presetSearchTermsVO;
        }).collect(Collectors.toList());
        return new PresetSearchTermsQueryResponse(ListSearch);
    }
}
