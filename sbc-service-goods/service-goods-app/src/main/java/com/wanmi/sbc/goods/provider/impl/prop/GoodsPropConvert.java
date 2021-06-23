package com.wanmi.sbc.goods.provider.impl.prop;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.dto.GoodsPropDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsPropRequestDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsPropDetailVO;
import com.wanmi.sbc.goods.bean.vo.GoodsPropVO;
import com.wanmi.sbc.goods.prop.model.root.GoodsProp;
import com.wanmi.sbc.goods.prop.model.root.GoodsPropDetail;
import com.wanmi.sbc.goods.prop.request.GoodsPropRequest;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: wanggang
 * @createDate: 2018/11/7 19:20
 * @version: 1.0
 */
public class GoodsPropConvert {

    private GoodsPropConvert(){

    }

    /**
     * List<GoodsPropDTO> 对象 转换成 List<GoodsProp> 对象
     * @param goodsPropDTOList
     * @return
     */
    public static List<GoodsProp> toGoodsPropList(List<GoodsPropDTO> goodsPropDTOList){
        if (CollectionUtils.isEmpty(goodsPropDTOList)){
            return null;
        }
       return goodsPropDTOList.stream().map(goodsPropDTO -> {
            GoodsProp goodsProp = new GoodsProp();
            KsBeanUtil.copyPropertiesThird(goodsPropDTO,goodsProp);
            goodsProp.setGoodsPropDetails(goodsPropDTO.getGoodsPropDetails().stream().map(goodsPropDetailDTO -> {
                GoodsPropDetail goodsPropDetail = new GoodsPropDetail();
                KsBeanUtil.copyPropertiesThird(goodsPropDetailDTO,goodsPropDetail);
                return goodsPropDetail;
            }).collect(Collectors.toList()));
            return goodsProp;
        }).collect(Collectors.toList());
    }

    /**
     * extends GoodsPropRequestDTO 对象 转换成 GoodsPropRequest 对象
     * @param t extends GoodsPropRequestDTO
     * @return
     */
    public static <T extends GoodsPropRequestDTO> GoodsPropRequest toGoodsPropRequest(T t){
        GoodsPropRequest goodsPropRequest = new GoodsPropRequest();
        goodsPropRequest.setLastPropId(t.getLastPropId());
        GoodsPropDTO goodsPropDTO = t.getGoodsProp();
        GoodsProp goodsProp = new GoodsProp();
        KsBeanUtil.copyPropertiesThird(goodsPropDTO,goodsProp);
        if(CollectionUtils.isNotEmpty(goodsPropDTO.getGoodsPropDetails())){
            goodsProp.setGoodsPropDetails(goodsPropDTO.getGoodsPropDetails().stream().map(goodsPropDetailDTO -> {
                GoodsPropDetail goodsPropDetail = new GoodsPropDetail();
                KsBeanUtil.copyPropertiesThird(goodsPropDetailDTO,goodsPropDetail);
                return goodsPropDetail;
            }).collect(Collectors.toList()));
        }
        goodsPropRequest.setGoodsProp(goodsProp);
        goodsPropRequest.setGoodsProps(toGoodsPropList(t.getGoodsProps()));
        return goodsPropRequest;
    }

    /**
     * extends GoodsPropDTO 对象 转换成 GoodsProp 对象
     * @param t extends GoodsPropDTO
     * @return
     */
    public static <T extends GoodsPropDTO> GoodsProp toGoodsProp(T t){
        GoodsProp goodsProp = new GoodsProp();
        KsBeanUtil.copyPropertiesThird(t,goodsProp);
        goodsProp.setGoodsPropDetails(t.getGoodsPropDetails().stream().map(goodsPropDetailDTO -> {
            GoodsPropDetail goodsPropDetail = new GoodsPropDetail();
            KsBeanUtil.copyPropertiesThird(goodsPropDetailDTO,goodsPropDetail);
            return goodsPropDetail;
        }).collect(Collectors.toList()));
        return goodsProp;
    }

    /**
     * List<GoodsProp> 对象 转换成 List<GoodsPropVO> 对象
     * @param goodsPropList
     * @return
     */
    public static List<GoodsPropVO> toVO(List<GoodsProp> goodsPropList){
        return goodsPropList.stream().map(goodsProp -> {
            GoodsPropVO goodsPropVO = new GoodsPropVO();
            KsBeanUtil.copyPropertiesThird(goodsProp,goodsPropVO);
            goodsPropVO.setGoodsPropDetails(goodsProp.getGoodsPropDetails().stream().map(goodsPropDetail -> {
                GoodsPropDetailVO goodsPropDetailVO = new GoodsPropDetailVO();
                KsBeanUtil.copyPropertiesThird(goodsPropDetail,goodsPropDetailVO);
                return goodsPropDetailVO;
            }).collect(Collectors.toList()));
            return goodsPropVO;
        }).collect(Collectors.toList());
    }
}
