package com.wanmi.sbc.setting.sysswitch;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.api.response.SwitchGetByIdResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by yuanlinling on 2017/4/26.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class SwitchService {

    @Autowired
    SwitchRepository switchRepository;

    /**
     * 根据id查询开关
     *
     * @param id
     * @return
     */
    public SwitchGetByIdResponse findSwitchById(String id){
        SwitchGetByIdResponse response = new SwitchGetByIdResponse();

        Optional<Switch> optional = switchRepository.findById(id);

        if (optional.isPresent()) {
            BeanUtils.copyProperties(optional.get(), response);
        }

        return response;
    }

    /**
     * 开关开启关闭
     *
     * @param id
     * @param status
     * @return
     */
    @Transactional
    public int updateSwitch(String id,Integer status){
        if(StringUtils.isEmpty(id) || status == null ){
            throw new SbcRuntimeException("K-000009");
        }
        return switchRepository.updateSwitch(id,status);
    }

}
