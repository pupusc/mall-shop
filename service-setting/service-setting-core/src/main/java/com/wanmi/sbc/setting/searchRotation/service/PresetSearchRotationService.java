package com.wanmi.sbc.setting.searchRotation.service;


import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.api.response.preserotation.PresetSearchRotationQueryResponse;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsQueryResponse;
import com.wanmi.sbc.setting.bean.vo.PresetSearchRotationVO;
import com.wanmi.sbc.setting.bean.vo.PresetSearchTermsVO;
import com.wanmi.sbc.setting.presetsearch.model.PresetSearchTerms;
import com.wanmi.sbc.setting.presetsearch.repositoy.PresetSearchTermsRepositoy;
import com.wanmi.sbc.setting.searchRotation.model.PresetSearchRotation;
import com.wanmi.sbc.setting.util.error.SearchTermsErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PresetSearchRotationService {

    //@Autowired
    //PresetSearchRatationRepositoy presetSearchRatationRepositoy;

    /**
     * 查询预置搜索词
     * @return
     */
    public PresetSearchRotationQueryResponse findRotation() {
        List<PresetSearchRotationVO> listSearch = new ArrayList<>();
        /*LocalDateTime now = LocalDateTime.now();

        List<PresetSearchRotation> list = presetSearchRatationRepositoy.findRotation();

        List<PresetSearchRotationVO> listSearch = new ArrayList<>();

        for(PresetSearchRotation search:list){



            PresetSearchRotationVO vo = new PresetSearchRotationVO();

            vo.setName(search.getName());
            vo.setType(search.getType());
            vo.setPage_url(search.getPage_url());
            vo.setSpu_id(search.getSpu_id());
            vo.setSku_id(search.getSku_id());

            if(null!=search.getBeginTime()&&null!=search.getEnd_time()){

                if((now.isAfter(search.getBeginTime())||now.isEqual(search.getBeginTime()))&&(now.isBefore(search.getEnd_time())||now.isEqual(search.getEnd_time()))){
                    listSearch.add(vo);
                }
            }else {
                listSearch.add(vo);
            }


        }*/

        return new PresetSearchRotationQueryResponse(listSearch);
    }

}
