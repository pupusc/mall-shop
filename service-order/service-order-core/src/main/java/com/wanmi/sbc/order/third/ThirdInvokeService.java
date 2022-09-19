package com.wanmi.sbc.order.third;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.order.api.enums.ThirdInvokeCategoryEnum;
import com.wanmi.sbc.order.api.enums.ThirdInvokePublishStatusEnum;
import com.wanmi.sbc.order.third.model.ThirdInvokeDTO;
import com.wanmi.sbc.order.third.repository.ThirdInvokeRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

        List<ThirdInvokeDTO> thirdInvokeDTOS = thirdInvokeRepository.findAll(thirdInvokeRepository.buildSearchCondition(businessId, null));
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


    /**
     * 更新订单状态
     * @param thirdInvokeId
     * @param platformId
     * @param thirdInvokePublishStatusEnum
     * @param result
     */
    public void update(Integer thirdInvokeId, String platformId, ThirdInvokePublishStatusEnum thirdInvokePublishStatusEnum, String result) {
        ThirdInvokeDTO thirdInvokeDTO = thirdInvokeRepository.findById(thirdInvokeId).orElse(null);
        if (thirdInvokeDTO == null) {
            log.error("ThirdInvokeService update 数据不存在 thirdInvokeId {} return", thirdInvokeId);
            return;
        }
        if (Objects.equals(thirdInvokeDTO.getPushStatus(), ThirdInvokePublishStatusEnum.SUCCESS.getCode())) {
            log.error("ThirdInvokeService update 数据已经成功 thirdInvokeId {} return", thirdInvokeId);
            return;
        }
        thirdInvokeDTO.setPlatformId(platformId);
        thirdInvokeDTO.setTimes(thirdInvokeDTO.getTimes() + 1);
        thirdInvokeDTO.setPushStatus(thirdInvokePublishStatusEnum.getCode());
        thirdInvokeDTO.setResult(result);
        thirdInvokeDTO.setUpdateTime(LocalDateTime.now());
        thirdInvokeRepository.save(thirdInvokeDTO);
    }
}
