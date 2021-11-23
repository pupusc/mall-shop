package com.wanmi.sbc.goods.blacklist.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListTypeEnum;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListProviderRequest;
import com.wanmi.sbc.goods.api.response.blacklist.BlackListCategoryProviderResponse;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.blacklist.model.root.GoodsBlackListDTO;
import com.wanmi.sbc.goods.blacklist.repository.GoodsBlackListRepository;
import com.wanmi.sbc.goods.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

/**
 * Description: 黑名单
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/20 1:58 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class GoodsBlackListService {


    @Resource
    private GoodsBlackListRepository goodsBlackListRepository;

    @Autowired
    private RedisService redisService;


    /**
     * 新增黑名单
     * @param goodsBlackListProviderRequest
     */
    public void add(GoodsBlackListProviderRequest goodsBlackListProviderRequest) {
        GoodsBlackListDTO commonBlackListDTO = new GoodsBlackListDTO();
        commonBlackListDTO.setBusinessName(goodsBlackListProviderRequest.getBusinessName());
        commonBlackListDTO.setBusinessId(goodsBlackListProviderRequest.getBusinessId());
        commonBlackListDTO.setBusinessCategory(goodsBlackListProviderRequest.getBusinessCategory());
        commonBlackListDTO.setBusinessType(goodsBlackListProviderRequest.getBusinessType());
        commonBlackListDTO.setCreateTime(LocalDateTime.now());
        commonBlackListDTO.setUpdateTime(LocalDateTime.now());
        commonBlackListDTO.setDelFlag(DeleteFlagEnum.NORMAL.getCode());
        goodsBlackListRepository.save(commonBlackListDTO);
    }

    /**
     * 删除黑名单
     * @param id
     */
    public void delete(Integer id){
        GoodsBlackListPageProviderRequest request = new GoodsBlackListPageProviderRequest();
        request.setId(id);
        List<GoodsBlackListDTO> commonBlackListDTOS = this.listSimpleNoPage(request);
        if (CollectionUtils.isEmpty(commonBlackListDTOS)) {
            return;
        }
        GoodsBlackListDTO commonBlackListDTO = commonBlackListDTOS.get(0);
        commonBlackListDTO.setDelFlag(DeleteFlagEnum.DELETE.getCode());
        goodsBlackListRepository.save(commonBlackListDTO);
    }


    /**
     * 获取查询列表
     * @param goodsBlackListPageProviderRequest
     * @return
     */
    private List<GoodsBlackListDTO> listSimpleNoPage(GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest) {
        return goodsBlackListRepository.findAll(goodsBlackListRepository.packageWhere(goodsBlackListPageProviderRequest));
    }


    /**
     * 获取黑名单列表
     * @param commonBlackListPageProviderRequest
     * @return
     */
    public GoodsBlackListPageProviderResponse listNoPage(GoodsBlackListPageProviderRequest commonBlackListPageProviderRequest) {
        GoodsBlackListPageProviderResponse result = new GoodsBlackListPageProviderResponse();
        List<GoodsBlackListDTO> blackListDTOList = this.listSimpleNoPage(commonBlackListPageProviderRequest);
        for (GoodsBlackListDTO commonBlackListParam : blackListDTOList) {
            if (Objects.equals(commonBlackListParam.getBusinessCategory(), GoodsBlackListCategoryEnum.NEW_BOOKS.getCode())) {
                BlackListCategoryProviderResponse blackListCategoryProviderResponse = this.packageBlackList(commonBlackListParam, result.getNewBooksBlackListModel());
                result.setNewBooksBlackListModel(blackListCategoryProviderResponse);
            } else if (Objects.equals(commonBlackListParam.getBusinessCategory(), GoodsBlackListCategoryEnum.SELL_WELL_BOOKS.getCode())) {
                BlackListCategoryProviderResponse blackListCategoryProviderResponse = this.packageBlackList(commonBlackListParam, result.getSellWellBooksBlackListModel());
                result.setSellWellBooksBlackListModel(blackListCategoryProviderResponse);
            } else if (Objects.equals(commonBlackListParam.getBusinessCategory(), GoodsBlackListCategoryEnum.SPECIAL_OFFER_BOOKS.getCode())) {
                BlackListCategoryProviderResponse blackListCategoryProviderResponse = this.packageBlackList(commonBlackListParam, result.getSpecialOfferBooksBlackListModel());
                result.setSpecialOfferBooksBlackListModel(blackListCategoryProviderResponse);
            } else if (Objects.equals(commonBlackListParam.getBusinessCategory(), GoodsBlackListCategoryEnum.UN_SHOW_VIP_PRICE.getCode())) {
                BlackListCategoryProviderResponse blackListCategoryProviderResponse = this.packageBlackList(commonBlackListParam, result.getVipPriceBlackListModel());
                result.setVipPriceBlackListModel(blackListCategoryProviderResponse);
            } else {
                log.error("===>>> CommonBlackListService listNoPage 参数有误 {}", JSON.toJSONString(commonBlackListPageProviderRequest));
            }
        }
        return result;
    }


    /**
     * 拼接黑名单里面的对象信息
     * @param commonBlackListParam
     * @param blackListCategoryProviderResponse
     * @return
     */
    private BlackListCategoryProviderResponse packageBlackList (GoodsBlackListDTO commonBlackListParam, BlackListCategoryProviderResponse blackListCategoryProviderResponse) {
        if (blackListCategoryProviderResponse == null) {
            blackListCategoryProviderResponse = new BlackListCategoryProviderResponse();
        }

        /**
         * goodsId
         */
        if (Objects.equals(commonBlackListParam.getBusinessType(), GoodsBlackListTypeEnum.SPU_ID.getCode())) {
            List<String> goodsIdList = blackListCategoryProviderResponse.getGoodsIdList();
            if (CollectionUtils.isEmpty(goodsIdList)) {
                goodsIdList = new ArrayList<>();
                blackListCategoryProviderResponse.setGoodsIdList(goodsIdList);
            }
            goodsIdList.add(commonBlackListParam.getBusinessId());
        }

        /**
         * classifyId
         */
        if (Objects.equals(commonBlackListParam.getBusinessType(), GoodsBlackListTypeEnum.CLASSIFY_ID_SECOND.getCode())) {
            List<Integer> secondClassifyIdList = blackListCategoryProviderResponse.getSecondClassifyIdList();
            if (CollectionUtils.isEmpty(secondClassifyIdList)) {
                secondClassifyIdList = new ArrayList<>();
                blackListCategoryProviderResponse.setSecondClassifyIdList(secondClassifyIdList);
            }
            secondClassifyIdList.add(Integer.parseInt(commonBlackListParam.getBusinessId()));
        }
        return blackListCategoryProviderResponse;
    }
}
