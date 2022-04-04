package com.wanmi.sbc.goods.common;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.bean.dto.GoodsPackDetailDTO;
import io.seata.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GoodsPackService {

    @Autowired
    private GoodsPackDetailRepository goodsPackDetailRepository;

    public List<GoodsPackDetailDTO> listGoodsPackDetailByPackId(String packId) {
        if (StringUtils.isBlank(packId)) {
            log.info("params is error, packId = {}", packId);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        GoodsPackDetailDTO query = new GoodsPackDetailDTO();
        query.setGoodsId(packId);
        query.setDelFlag(0);
        return goodsPackDetailRepository.findAll(Example.of(query));
    }
}
