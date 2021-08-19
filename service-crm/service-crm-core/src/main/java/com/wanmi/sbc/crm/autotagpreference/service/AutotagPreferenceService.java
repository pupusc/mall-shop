package com.wanmi.sbc.crm.autotagpreference.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.request.autotagpreference.AutoPreferenceListRequest;
import com.wanmi.sbc.crm.api.request.autotagpreference.AutoPreferencePageRequest;
import com.wanmi.sbc.crm.autotag.model.root.AutoTag;
import com.wanmi.sbc.crm.autotag.service.AutoTagService;
import com.wanmi.sbc.crm.autotagpreference.mapper.AutotagPreferenceMapper;
import com.wanmi.sbc.crm.autotagpreference.model.AutotagPreference;
import com.wanmi.sbc.crm.bean.vo.AutotagPreferenceVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class AutotagPreferenceService {

    @Autowired
    private AutotagPreferenceMapper autotagPreferenceMapper;

    @Autowired
    private AutoTagService autoTagService;

    public List<AutotagPreference> pageByTagId(AutoPreferencePageRequest request){
        if (request.getTagId()==null){
            return Collections.emptyList();
        }
        String tabFlag = autoTag(request.getTagId());
        request.setTabFlag(tabFlag);
        PageHelper.startPage(request.getPageNum() +1, request.getPageSize(), false);
        return autotagPreferenceMapper.findByTagId(request);
    }

    public long countByTagIdAndDetailName(AutoPreferencePageRequest request){
        String tabFlag = autoTag(request.getTagId());
        request.setTabFlag(tabFlag);
        return autotagPreferenceMapper.countByTagIdAndDetailName(request);
    }

    public long countByTagId(Long tagId){
        return autotagPreferenceMapper.countByTagId(tagId);
    }

    private String autoTag(Long tagId){
        String tabFlag = "";
        AutoTag autoTag = autoTagService.getOne(tagId);
        if(Objects.nonNull(autoTag)){
            String rulejson = autoTag.getRuleJson();
            if(StringUtils.isNotBlank(rulejson)){
                JSONObject jsonObject = JSON.parseObject(rulejson);
                if (jsonObject.containsKey("autoTagSelectMap")){
                    JSONObject autoTagSelectMap = (JSONObject) jsonObject.get("autoTagSelectMap");
                    if (autoTagSelectMap.containsKey("0")){
                        JSONObject JSON_0 = (JSONObject) autoTagSelectMap.get("0");
                        if (JSON_0.containsKey("autoTagSelectValues")){
                            JSONObject autoTagSelectValues = (JSONObject) JSON_0.get("autoTagSelectValues");
                            if (autoTagSelectValues.containsKey("0")){
                                JSONObject JSON_0_0 = (JSONObject) autoTagSelectValues.get("0");
                                if (JSON_0_0.containsKey("columnName")){
                                    tabFlag = JSON_0_0.get("columnName").toString();
                                }
                            }
                        }
                    }
                }
            }
        }
        return tabFlag;
    }

    public List<AutotagPreference> findByTagIdAndDimensionId(AutoPreferenceListRequest request){
        String tabFlag = autoTag(request.getTagId());
        request.setTabFlag(tabFlag);
        return autotagPreferenceMapper.findByTagIdAndDimensionId(request);
    }

    /**
     * 将实体包装成VO
     * @author dyt
     */
    public AutotagPreferenceVO wrapperVo(AutotagPreference autotagPreference) {
        if (autotagPreference != null){

            AutotagPreferenceVO autotagPreferenceVO = KsBeanUtil.convert(autotagPreference, AutotagPreferenceVO.class);
            return autotagPreferenceVO;
        }
        return null;
    }

}
