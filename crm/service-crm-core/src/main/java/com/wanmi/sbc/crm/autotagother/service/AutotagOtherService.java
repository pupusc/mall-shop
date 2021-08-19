package com.wanmi.sbc.crm.autotagother.service;

import com.github.pagehelper.PageHelper;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.request.autotagother.AutoTagOtherPageRequest;
import com.wanmi.sbc.crm.autotagother.mapper.AutotagOtherMapper;
import com.wanmi.sbc.crm.autotagother.model.AutotagOther;
import com.wanmi.sbc.crm.bean.vo.AutotagOtherVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutotagOtherService {

    @Autowired
    private AutotagOtherMapper autotagOtherMapper;

    public List<AutotagOther> pageByTagId(AutoTagOtherPageRequest request){
        PageHelper.startPage(request.getPageNum() +1, request.getPageSize(), false);
        return autotagOtherMapper.findByIdAndType(request);
    }

    public long countByTagIdAndDetailName(AutoTagOtherPageRequest request){
        return autotagOtherMapper.countByIdAndType(request);
    }

    /**
     * 将实体包装成VO
     * @author dyt
     */
    public AutotagOtherVO wrapperVo(AutotagOther autotagOther) {
        if (autotagOther != null){
            AutotagOtherVO autotagOtherVO = KsBeanUtil.convert(autotagOther, AutotagOtherVO.class);
            return autotagOtherVO;
        }
        return null;
    }
}
