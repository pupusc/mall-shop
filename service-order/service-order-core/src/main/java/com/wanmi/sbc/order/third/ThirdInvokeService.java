package com.wanmi.sbc.order.third;
import java.time.LocalDateTime;
import java.util.List;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.order.api.enums.ThirdInvokeCategoryEnum;
import com.wanmi.sbc.order.api.enums.ThirdInvokePublishStatusEnum;
import com.wanmi.sbc.order.third.model.ThirdInvokeDTO;
import com.wanmi.sbc.order.third.repository.ThirdInvokeRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/19 2:26 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class ThirdInvokeService {

    @Autowired
    private ThirdInvokeRepository thirdInvokeRepository;

    /**
     * 新增推送日志信息
     * @param businessId
     * @param thirdInvokeCategoryEnum
     * @return
     */
    public ThirdInvokeDTO add(String businessId, ThirdInvokeCategoryEnum thirdInvokeCategoryEnum){

        List<ThirdInvokeDTO> thirdInvokeDTOS = thirdInvokeRepository.findAll(thirdInvokeRepository.buildSearchCondition(businessId));
        if (!CollectionUtils.isEmpty(thirdInvokeDTOS)) {
            return thirdInvokeDTOS.get(0);
        }
        ThirdInvokeDTO thirdInvokeDTO = new ThirdInvokeDTO();
        thirdInvokeDTO.setBusinessId(businessId);
//        thirdInvokeDTO.setPlatformId("");
        thirdInvokeDTO.setCategory(thirdInvokeCategoryEnum.getCode());
        thirdInvokeDTO.setTimes(0);
        thirdInvokeDTO.setPushStatus(ThirdInvokePublishStatusEnum.INIT.getCode());
        thirdInvokeDTO.setResult("");
        thirdInvokeDTO.setCreateTime(LocalDateTime.now());
        thirdInvokeDTO.setUpdateTime(LocalDateTime.now());
        thirdInvokeDTO.setDelFlag(DeleteFlag.NO.toValue());
        return thirdInvokeRepository.save(thirdInvokeDTO);
    }



    public
}
