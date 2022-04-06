package com.wanmi.sbc.goods.blacklist.service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.constant.RedisKeyConstant;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListTypeEnum;
import com.wanmi.sbc.goods.api.request.blacklist.*;
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
        GoodsBlackListCacheProviderRequest request = new GoodsBlackListCacheProviderRequest();
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
     * 更新黑名单
     */
    public void update(GoodsBlackListCreateOrUpdateRequest goodsBlackListCreateOrUpdateRequest){
        Optional<GoodsBlackListDTO> opt = goodsBlackListRepository.findById(goodsBlackListCreateOrUpdateRequest.getId());
        if(opt.isPresent()){
            GoodsBlackListDTO goodsBlackListDTO = opt.get();
            if(goodsBlackListDTO.getDelFlag().equals(DeleteFlag.YES.toValue())) throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, "黑名单不存在");
            if(goodsBlackListCreateOrUpdateRequest.getDelFlag() != null){
                goodsBlackListDTO.setDelFlag(goodsBlackListCreateOrUpdateRequest.getDelFlag());
            }
            goodsBlackListRepository.save(goodsBlackListDTO);
        }
    }

    /**
     * 获取查询列表
     * @param goodsBlackListPageProviderRequest
     * @return
     */
    public List<GoodsBlackListDTO> listSimpleNoPage(GoodsBlackListCacheProviderRequest goodsBlackListPageProviderRequest) {
        return goodsBlackListRepository.findAll(goodsBlackListRepository.packageWhere(goodsBlackListPageProviderRequest));
    }

    /**
     * 获取黑名单列表
     * @param goodsBlackListCacheProviderRequest
     * @return
     */
    public GoodsBlackListPageProviderResponse flushBlackListCache(GoodsBlackListCacheProviderRequest goodsBlackListCacheProviderRequest) {
        GoodsBlackListPageProviderResponse result = new GoodsBlackListPageProviderResponse();
        List<GoodsBlackListDTO> blackListDTOList = this.listSimpleNoPage(goodsBlackListCacheProviderRequest);
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
                BlackListCategoryProviderResponse blackListCategoryProviderResponse = this.packageBlackList(commonBlackListParam, result.getUnVipPriceBlackListModel());
                result.setUnVipPriceBlackListModel(blackListCategoryProviderResponse);
            } else if (Objects.equals(commonBlackListParam.getBusinessCategory(), GoodsBlackListCategoryEnum.POINT_NOT_SPLIT.getCode())) {
                BlackListCategoryProviderResponse blackListCategoryProviderResponse = this.packageBlackList(commonBlackListParam, result.getPointNotSplitBlackListModel());
                result.setPointNotSplitBlackListModel(blackListCategoryProviderResponse);
            } else if (Objects.equals(commonBlackListParam.getBusinessCategory(), GoodsBlackListCategoryEnum.UN_SHOW_WAREHOUSE.getCode())) {
                BlackListCategoryProviderResponse blackListCategoryProviderResponse = this.packageBlackList(commonBlackListParam, result.getWareHouseListModel());
                result.setWareHouseListModel(blackListCategoryProviderResponse);
            } else {
                log.error("===>>> CommonBlackListService flushBlackListCache 参数有误 {}", JSON.toJSONString(goodsBlackListCacheProviderRequest));
            }
        }

        //存入到redis中
        if (result.getNewBooksBlackListModel() != null) {
            //存入redis
            if (!CollectionUtils.isEmpty(result.getNewBooksBlackListModel().getGoodsIdList())) {
                redisService.putHashStrValueList(RedisKeyConstant.KEY_NEW_BOOKS_BLACK_LIST, RedisKeyConstant.KEY_SPU_ID, result.getNewBooksBlackListModel().getGoodsIdList());
            }
            if (!CollectionUtils.isEmpty(result.getNewBooksBlackListModel().getSecondClassifyIdList())) {
                redisService.putHashStrValueList(RedisKeyConstant.KEY_NEW_BOOKS_BLACK_LIST, RedisKeyConstant.KEY_CLASSIFY_ID_SECOND, result.getNewBooksBlackListModel().getSecondClassifyIdList());
            }
        }
        //存入到redis中
        if (result.getSellWellBooksBlackListModel() != null) {
            //存入redis
            if (!CollectionUtils.isEmpty(result.getSellWellBooksBlackListModel().getGoodsIdList())) {
                redisService.putHashStrValueList(RedisKeyConstant.KEY_SELL_WELL_BOOKS, RedisKeyConstant.KEY_SPU_ID, result.getSellWellBooksBlackListModel().getGoodsIdList());
            }
            if (!CollectionUtils.isEmpty(result.getSellWellBooksBlackListModel().getSecondClassifyIdList())) {
                redisService.putHashStrValueList(RedisKeyConstant.KEY_SELL_WELL_BOOKS, RedisKeyConstant.KEY_CLASSIFY_ID_SECOND, result.getSellWellBooksBlackListModel().getSecondClassifyIdList());
            }
        }
        //存入到redis中
        if (result.getSpecialOfferBooksBlackListModel() != null) {
            //存入redis
            if (!CollectionUtils.isEmpty(result.getSpecialOfferBooksBlackListModel().getGoodsIdList())) {
                redisService.putHashStrValueList(RedisKeyConstant.KEY_SPECIAL_OFFER_BOOKS, RedisKeyConstant.KEY_SPU_ID, result.getSpecialOfferBooksBlackListModel().getGoodsIdList());
            }
            if (!CollectionUtils.isEmpty(result.getSpecialOfferBooksBlackListModel().getSecondClassifyIdList())) {
                redisService.putHashStrValueList(RedisKeyConstant.KEY_SPECIAL_OFFER_BOOKS, RedisKeyConstant.KEY_CLASSIFY_ID_SECOND, result.getSpecialOfferBooksBlackListModel().getSecondClassifyIdList());
            }
        }
        //存入到redis中
        if (result.getUnVipPriceBlackListModel() != null) {
            //存入redis
            if (!CollectionUtils.isEmpty(result.getUnVipPriceBlackListModel().getGoodsIdList())) {
                redisService.putHashStrValueList(RedisKeyConstant.KEY_UN_SHOW_VIP_PRICE, RedisKeyConstant.KEY_SPU_ID, result.getUnVipPriceBlackListModel().getGoodsIdList());
            }
            if (!CollectionUtils.isEmpty(result.getUnVipPriceBlackListModel().getSecondClassifyIdList())) {
                redisService.putHashStrValueList(RedisKeyConstant.KEY_UN_SHOW_VIP_PRICE, RedisKeyConstant.KEY_CLASSIFY_ID_SECOND, result.getUnVipPriceBlackListModel().getSecondClassifyIdList());
            }
        }
        //存入redis
        if (result.getWareHouseListModel() != null) {
            //存入redis
            if (!CollectionUtils.isEmpty(result.getWareHouseListModel().getNormalList())) {
                redisService.putHashStrValueList(RedisKeyConstant.KEY_UN_WARE_HOUSE, RedisKeyConstant.KEY_NORMAL_KEY, result.getWareHouseListModel().getNormalList());
            }
        }

        if (result.getPointNotSplitBlackListModel() != null) {
            if (!CollectionUtils.isEmpty(result.getPointNotSplitBlackListModel().getGoodsIdList())) {
                redisService.putHashStrValueList(RedisKeyConstant.KEY_POINT_NOT_SPLIT, RedisKeyConstant.KEY_SPU_ID, result.getPointNotSplitBlackListModel().getGoodsIdList());
            }
        }

        return result;
    }

    /**
     * 获取缓存中的数据，尽量别直接获取，防止后续redis
     * @return
     */
    public GoodsBlackListPageProviderResponse listNoPage(GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest) {
        GoodsBlackListPageProviderResponse result = new GoodsBlackListPageProviderResponse();
        if (goodsBlackListPageProviderRequest == null) {
            return result;
        }
        if (CollectionUtils.isEmpty(goodsBlackListPageProviderRequest.getBusinessCategoryColl())) {
            return result;
        }

        for (Integer businessCateGoryId : goodsBlackListPageProviderRequest.getBusinessCategoryColl()) {
            if (Objects.equals(businessCateGoryId, GoodsBlackListCategoryEnum.NEW_BOOKS.getCode())) {
                List<String> goodsIdList = redisService.getHashStrValueList(RedisKeyConstant.KEY_NEW_BOOKS_BLACK_LIST, RedisKeyConstant.KEY_SPU_ID);
                List<String> classifyIdList = redisService.getHashStrValueList(RedisKeyConstant.KEY_CLASSIFY_ID_SECOND, RedisKeyConstant.KEY_CLASSIFY_ID_SECOND);
                BlackListCategoryProviderResponse newBooksBlackListModel = result.getNewBooksBlackListModel();
                if (newBooksBlackListModel == null) {
                    newBooksBlackListModel = new BlackListCategoryProviderResponse();
                }
                newBooksBlackListModel.setGoodsIdList(goodsIdList);
                newBooksBlackListModel.setSecondClassifyIdList(classifyIdList);
                result.setNewBooksBlackListModel(newBooksBlackListModel);
            } else if (Objects.equals(businessCateGoryId, GoodsBlackListCategoryEnum.SELL_WELL_BOOKS.getCode())) {
                List<String> goodsIdList = redisService.getHashStrValueList(RedisKeyConstant.KEY_SELL_WELL_BOOKS, RedisKeyConstant.KEY_SPU_ID);
                List<String> classifyIdList = redisService.getHashStrValueList(RedisKeyConstant.KEY_SELL_WELL_BOOKS, RedisKeyConstant.KEY_CLASSIFY_ID_SECOND);
                BlackListCategoryProviderResponse sellWellBooksBlackListModel = result.getSellWellBooksBlackListModel();
                if (sellWellBooksBlackListModel == null) {
                    sellWellBooksBlackListModel = new BlackListCategoryProviderResponse();
                }
                sellWellBooksBlackListModel.setGoodsIdList(goodsIdList);
                sellWellBooksBlackListModel.setSecondClassifyIdList(classifyIdList);
                result.setSellWellBooksBlackListModel(sellWellBooksBlackListModel);
            } else if (Objects.equals(businessCateGoryId, GoodsBlackListCategoryEnum.SPECIAL_OFFER_BOOKS.getCode())) {
                List<String> goodsIdList = redisService.getHashStrValueList(RedisKeyConstant.KEY_SPECIAL_OFFER_BOOKS, RedisKeyConstant.KEY_SPU_ID);
                List<String> classifyIdList = redisService.getHashStrValueList(RedisKeyConstant.KEY_SPECIAL_OFFER_BOOKS, RedisKeyConstant.KEY_CLASSIFY_ID_SECOND);
                BlackListCategoryProviderResponse specialOfferBooksBlackListModel = result.getSpecialOfferBooksBlackListModel();
                if (specialOfferBooksBlackListModel == null) {
                    specialOfferBooksBlackListModel = new BlackListCategoryProviderResponse();
                }
                specialOfferBooksBlackListModel.setGoodsIdList(goodsIdList);
                specialOfferBooksBlackListModel.setSecondClassifyIdList(classifyIdList);
                result.setSpecialOfferBooksBlackListModel(specialOfferBooksBlackListModel);
            } else if (Objects.equals(businessCateGoryId, GoodsBlackListCategoryEnum.UN_SHOW_VIP_PRICE.getCode())) {
                List<String> goodsIdList = redisService.getHashStrValueList(RedisKeyConstant.KEY_UN_SHOW_VIP_PRICE, RedisKeyConstant.KEY_SPU_ID);
                List<String> classifyIdList = redisService.getHashStrValueList(RedisKeyConstant.KEY_UN_SHOW_VIP_PRICE, RedisKeyConstant.KEY_CLASSIFY_ID_SECOND);
                BlackListCategoryProviderResponse unVipPriceBlackListModel = result.getUnVipPriceBlackListModel();
                if (unVipPriceBlackListModel == null) {
                    unVipPriceBlackListModel = new BlackListCategoryProviderResponse();
                }
                unVipPriceBlackListModel.setGoodsIdList(goodsIdList);
                unVipPriceBlackListModel.setSecondClassifyIdList(classifyIdList);
                result.setUnVipPriceBlackListModel(unVipPriceBlackListModel);
            } else if (Objects.equals(businessCateGoryId, GoodsBlackListCategoryEnum.POINT_NOT_SPLIT.getCode())) {
                List<String> goodsIdList = redisService.getHashStrValueList(RedisKeyConstant.KEY_POINT_NOT_SPLIT, RedisKeyConstant.KEY_SPU_ID);
                BlackListCategoryProviderResponse pointNotSplitBlackListModel = result.getPointNotSplitBlackListModel();
                if (pointNotSplitBlackListModel == null) {
                    pointNotSplitBlackListModel = new BlackListCategoryProviderResponse();
                }
                pointNotSplitBlackListModel.setGoodsIdList(goodsIdList);
                result.setPointNotSplitBlackListModel(pointNotSplitBlackListModel);
            } else if (Objects.equals(businessCateGoryId, GoodsBlackListCategoryEnum.UN_SHOW_WAREHOUSE.getCode())) {
                List<String> wareHouseCodeList = redisService.getHashStrValueList(RedisKeyConstant.KEY_UN_WARE_HOUSE, RedisKeyConstant.KEY_NORMAL_KEY);
                BlackListCategoryProviderResponse wareHouseListModel = result.getWareHouseListModel();
                if (wareHouseListModel == null) {
                    wareHouseListModel = new BlackListCategoryProviderResponse();
                }
                wareHouseListModel.setNormalList(wareHouseCodeList);
                result.setWareHouseListModel(wareHouseListModel);
            } else {
                log.error("===>>> CommonBlackListService listNoPage 参数有误 {}", JSON.toJSONString(goodsBlackListPageProviderRequest));
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
            List<String> secondClassifyIdList = blackListCategoryProviderResponse.getSecondClassifyIdList();
            if (CollectionUtils.isEmpty(secondClassifyIdList)) {
                secondClassifyIdList = new ArrayList<>();
                blackListCategoryProviderResponse.setSecondClassifyIdList(secondClassifyIdList);
            }
            secondClassifyIdList.add(commonBlackListParam.getBusinessId());
        }

        /**
         * 无
         */
        if (Objects.equals(commonBlackListParam.getBusinessType(), GoodsBlackListTypeEnum.NONE.getCode())) {
            List<String> normalList = blackListCategoryProviderResponse.getNormalList();
            if (CollectionUtils.isEmpty(normalList)) {
                normalList = new ArrayList<>();
                blackListCategoryProviderResponse.setNormalList(normalList);
            }
            normalList.add(commonBlackListParam.getBusinessId());
        }
        return blackListCategoryProviderResponse;
    }
}
