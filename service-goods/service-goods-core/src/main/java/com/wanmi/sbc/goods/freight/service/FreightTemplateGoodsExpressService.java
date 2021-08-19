package com.wanmi.sbc.goods.freight.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.freight.model.root.FreightTemplateGoodsExpress;
import com.wanmi.sbc.goods.freight.repository.FreightTemplateGoodsExpressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 单品运费模板快递服务
 * Created by sunkun on 2018/5/3.
 */
@Service
public class FreightTemplateGoodsExpressService {

    @Autowired
    private FreightTemplateGoodsExpressRepository freightTemplateGoodsExpressRepository;


    /**
     * 根据单品运费模板id获取未删除和默认的单品运费模板快递
     * @param freightTempId 单品运费模板id
     * @return 单品运费模板快递
     */
    public FreightTemplateGoodsExpress findByFreightTempIdAndDelFlag(Long freightTempId){
        return freightTemplateGoodsExpressRepository.findByFreightTempIdAndDelFlagAndDefaultFlag(freightTempId,DeleteFlag.NO,DefaultFlag.YES);
    }
}
