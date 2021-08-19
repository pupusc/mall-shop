package com.wanmi.sbc.goods.provider.impl.storecate;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.bean.dto.StoreCateRequestDTO;
import com.wanmi.sbc.goods.bean.vo.StoreCateResponseVO;
import com.wanmi.sbc.goods.bean.vo.StoreCateVO;
import com.wanmi.sbc.goods.storecate.model.root.StoreCate;
import com.wanmi.sbc.goods.storecate.request.StoreCateSaveRequest;
import com.wanmi.sbc.goods.storecate.response.StoreCateResponse;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: wanggang
 * @createDate: 2018/11/8 11:41
 * @version: 1.0
 */
public class StoreCateConvert {

    private StoreCateConvert(){

    }

    /**
     * List<StoreCateResponse> 对象 转换成 List<StoreCateResponseVO> 对象
     * @param storeCateResponseList
     * @return
     */
    public static List<StoreCateResponseVO> toStoreCateResponseVOList(List<StoreCateResponse> storeCateResponseList){
       return storeCateResponseList.stream().map(storeCateResponse -> {
            StoreCateResponseVO storeCateResponseVO = toStoreCateResponseVO(storeCateResponse);
            return storeCateResponseVO;
        }).collect(Collectors.toList());
    }

    /**
     * StoreCateResponse 对象 转换成 StoreCateResponseVO
     * @param storeCateResponse
     * @return
     */
    public static StoreCateResponseVO toStoreCateResponseVO(StoreCateResponse storeCateResponse){
        StoreCateResponseVO storeCateResponseVO = new StoreCateResponseVO();
        KsBeanUtil.copyPropertiesThird(storeCateResponse,storeCateResponseVO);
        if(CollectionUtils.isNotEmpty(storeCateResponse.getStoreCateList())) {
            storeCateResponseVO.setStoreCateList(storeCateResponse.getStoreCateList().stream().map(storeCate -> {
                StoreCateVO storeCateVO = new StoreCateVO();
                KsBeanUtil.copyPropertiesThird(storeCate, storeCateVO);
                storeCateVO.setStoreCateList(storeCate.getStoreCateList().stream().map(cate -> {
                    StoreCateVO cateVO = new StoreCateVO();
                    KsBeanUtil.copyPropertiesThird(cate, cateVO);
                    return cateVO;
                }).collect(Collectors.toList()));
                return storeCateVO;
            }).collect(Collectors.toList()));
        }
        return storeCateResponseVO;
    }

    /**
     * extends StoreCateRequestDTO 对象 转换成 StoreCateSaveRequest 对象
     * @param t extends StoreCateRequestDTO
     * @return
     */
    public static <T extends StoreCateRequestDTO> StoreCateSaveRequest toStoreCateSaveRequest(T t){
        StoreCateSaveRequest saveRequest = new StoreCateSaveRequest();
        KsBeanUtil.copyPropertiesThird(t,saveRequest);
        if(CollectionUtils.isNotEmpty(t.getStoreCateList())) {
            saveRequest.setStoreCateList(t.getStoreCateList().stream().map(storeCateDTO -> {
                StoreCate storeCate = new StoreCate();
                KsBeanUtil.copyPropertiesThird(storeCateDTO, storeCate);
                if (CollectionUtils.isNotEmpty(storeCateDTO.getStoreCateList())) {
                    storeCate.setStoreCateList(storeCateDTO.getStoreCateList().stream().map(cateDTO -> {
                        StoreCate cate = new StoreCate();
                        KsBeanUtil.copyPropertiesThird(cate, cateDTO);
                        return cate;
                    }).collect(Collectors.toList()));
                }
                return storeCate;
            }).collect(Collectors.toList()));
        }
        return saveRequest;
    }

    /**
     * List<StoreCate> 对象 转换成 List<StoreCateVO> 对象
     * @param storeCateList
     * @return
     */
    public static List<StoreCateVO> toStoreCateVOList(List<StoreCate> storeCateList){
        return  storeCateList.stream().map(storeCate -> {
            StoreCateVO storeCateVO = new StoreCateVO();
            KsBeanUtil.copyPropertiesThird(storeCate,storeCateVO);
            if(CollectionUtils.isNotEmpty(storeCate.getStoreCateList())) {
                storeCateVO.setStoreCateList(storeCate.getStoreCateList().stream().map(cate -> {
                    StoreCateVO cateVO = new StoreCateVO();
                    KsBeanUtil.copyPropertiesThird(cate, cateVO);
                    return cateVO;
                }).collect(Collectors.toList()));
            }
            return storeCateVO;
        }).collect(Collectors.toList());
    }
}
