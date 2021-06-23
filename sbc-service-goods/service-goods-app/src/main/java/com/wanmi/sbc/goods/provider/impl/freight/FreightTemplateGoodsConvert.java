package com.wanmi.sbc.goods.provider.impl.freight;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsByIdResponse;
import com.wanmi.sbc.goods.bean.vo.FreightTemplateGoodsExpressVO;
import com.wanmi.sbc.goods.bean.vo.FreightTemplateGoodsFreeVO;
import com.wanmi.sbc.goods.bean.vo.FreightTemplateGoodsVO;
import com.wanmi.sbc.goods.freight.model.root.FreightTemplateGoods;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 单品运费模板转换类
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-08 11:34
 */
public class FreightTemplateGoodsConvert {

    /**
     * 实体转换vo
     * @param templateGoods 实体
     * @return vo
     */
    protected static FreightTemplateGoodsVO convertFreightTemplateGoodsToVo(FreightTemplateGoods templateGoods){
        FreightTemplateGoodsVO vo = new FreightTemplateGoodsVO();
        KsBeanUtil.copyPropertiesThird(templateGoods, vo);
        if(CollectionUtils.isNotEmpty(templateGoods.getFreightTemplateGoodsFrees())){
            vo.setFreightTemplateGoodsFrees(
                    KsBeanUtil.convertList(templateGoods.getFreightTemplateGoodsFrees(), FreightTemplateGoodsFreeVO.class));
        }

        if(CollectionUtils.isNotEmpty(templateGoods.getFreightTemplateGoodsExpresses())){
            vo.setFreightTemplateGoodsExpresses(
                    KsBeanUtil.convertList(templateGoods.getFreightTemplateGoodsExpresses(),
                            FreightTemplateGoodsExpressVO.class)
            );
        }
        return vo;
    }

    /**
     * 实体转换response
     * @param templateGoods 实体
     * @return response
     */
    protected static FreightTemplateGoodsByIdResponse convertFreightTemplateGoodsToResponse(FreightTemplateGoods templateGoods){
        FreightTemplateGoodsByIdResponse response = new FreightTemplateGoodsByIdResponse();
        KsBeanUtil.copyPropertiesThird(convertFreightTemplateGoodsToVo(templateGoods), response);
        return response;
    }

    /**
     * 批量实体转换vo
     * @param templateGoodsList 实体列表
     * @return vo列表
     */
    protected static List<FreightTemplateGoodsVO> convertFreightTemplateGoodsListToVoList(
            List<FreightTemplateGoods> templateGoodsList){
        return templateGoodsList.stream().map(FreightTemplateGoodsConvert::convertFreightTemplateGoodsToVo).collect
                (Collectors.toList());
    }
}
