package com.wanmi.sbc.goods.goodsevaluate.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.goodsevaluate.*;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.EvaluateImgUpdateIsShowReq;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageQueryRequest;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateImageVO;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsImageVO;
import com.wanmi.sbc.goods.goodsevaluate.model.root.GoodsEvaluate;
import com.wanmi.sbc.goods.goodsevaluate.repository.GoodsEvaluateRepository;
import com.wanmi.sbc.goods.goodsevaluateimage.model.root.GoodsEvaluateImage;
import com.wanmi.sbc.goods.goodsevaluateimage.service.GoodsEvaluateImageService;
import com.wanmi.sbc.goods.goodstobeevaluate.model.root.GoodsTobeEvaluate;
import com.wanmi.sbc.goods.goodstobeevaluate.repository.GoodsTobeEvaluateRepository;
import com.wanmi.sbc.goods.goodstobeevaluate.service.GoodsTobeEvaluateWhereCriteriaBuilder;
import com.wanmi.sbc.goods.images.service.GoodsImageService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoEditResponse;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.mq.GoodsEvaluateNumMqService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>商品评价业务逻辑</p>
 *
 * @author liutao
 * @date 2019-02-25 15:14:16
 */
@Service("GoodsEvaluateService")
public class GoodsEvaluateService {
    @Autowired
    private GoodsEvaluateRepository goodsEvaluateRepository;

    @Autowired
    private GoodsImageService goodsImageService;

    @Autowired
    private GoodsEvaluateImageService goodsEvaluateImageService;

    @Autowired
    private GoodsEvaluateNumMqService goodsEvaluateNumMqService;

    @Autowired
    private GoodsTobeEvaluateRepository goodsTobeEvaluateRepository;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsInfoService goodsInfoService;

    /**
     * 新增商品评价
     *
     * @author liutao
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public GoodsEvaluate add(GoodsEvaluate entity) {
        //填充商品相关信息
        GoodsInfo goodsInfo = goodsInfoService.findOne(entity.getGoodsInfoId());
        if(Objects.nonNull(goodsInfo)) {
            entity.setCateTopId(goodsInfo.getCateTopId());
            entity.setCateId(goodsInfo.getCateId());
            entity.setBrandId(goodsInfo.getBrandId());
        }

        //获取待评价数据的商品图片以及商品购买时间
        GoodsTobeEvaluateQueryRequest queryRequest = new GoodsTobeEvaluateQueryRequest();
        queryRequest.setOrderNo(entity.getOrderNo());
        queryRequest.setGoodsInfoId(entity.getGoodsInfoId());
        GoodsTobeEvaluate goodsTobeEvaluate = goodsTobeEvaluateRepository.findOne(GoodsTobeEvaluateWhereCriteriaBuilder.build(queryRequest)).orElse(new GoodsTobeEvaluate());
        entity.setGoodsImg(goodsTobeEvaluate.getGoodsImg());
        entity.setBuyTime(goodsTobeEvaluate.getBuyTime());
        entity.setGoodNum(0);
        entity.setIsShow(0);
        entity.setIsSys(Constants.no);
        entity.setEvaluateCatetory(0);
        goodsEvaluateRepository.save(entity);
        //增加评论数
        if(entity.getIsShow().intValue() == 1){
            goodsEvaluateNumMqService.updateGoodsEvaluateNum(entity);
        }
        return entity;
    }

    /**
     * 新增书友说评价
     */
//    @Transactional(rollbackFor = Exception.class)
    public void addBookFriendEvaluate(BookFriendEvaluateAddRequest bookFriendEvaluateAddRequest) {
        if (StringUtils.isEmpty(bookFriendEvaluateAddRequest.skuId) || StringUtils.isEmpty(bookFriendEvaluateAddRequest.customerName)
                || StringUtils.isEmpty(bookFriendEvaluateAddRequest.evaluateContent)) {
            throw new SbcRuntimeException("K-000009");
        }

        GoodsInfoEditResponse res = goodsInfoService.findById(bookFriendEvaluateAddRequest.skuId);
        GoodsEvaluate goodsEvaluate = new GoodsEvaluate();
        //赋默认值
        goodsEvaluate.setIsSys(0);
        goodsEvaluate.setIsAnonymous(0);
        goodsEvaluate.setDelFlag(0);
        goodsEvaluate.setIsAnswer(0);
        goodsEvaluate.setIsEdit(0);
        goodsEvaluate.setIsUpload(0);
        goodsEvaluate.setEvaluateScore(5);
        goodsEvaluate.setOrderNo("null");
        goodsEvaluate.setCustomerId("null");

        goodsEvaluate.setStoreId(bookFriendEvaluateAddRequest.storeId);
        goodsEvaluate.setGoodsId(res.getGoods().getGoodsId());
        goodsEvaluate.setGoodsInfoId(res.getGoodsInfo().getGoodsInfoId());
        goodsEvaluate.setGoodsInfoName(res.getGoodsInfo().getGoodsInfoName());
        goodsEvaluate.setGoodsImg(res.getImages().get(0).getArtworkUrl());
        goodsEvaluate.setCustomerName(bookFriendEvaluateAddRequest.customerName);
        goodsEvaluate.setEvaluateContent(bookFriendEvaluateAddRequest.evaluateContent);
        goodsEvaluate.setEvaluateTime(bookFriendEvaluateAddRequest.evaluateTime == null ? LocalDateTime.now()
                : LocalDateTime.parse(bookFriendEvaluateAddRequest.evaluateTime, DateTimeFormatter.ISO_DATE_TIME));
        goodsEvaluate.setIsShow(bookFriendEvaluateAddRequest.isShow == null ? 0 : bookFriendEvaluateAddRequest.isShow);
        goodsEvaluate.setIsRecommend(bookFriendEvaluateAddRequest.isRecommend == null ? 0 : bookFriendEvaluateAddRequest.isRecommend);
        goodsEvaluate.setEvaluateCatetory(3);

        goodsEvaluateRepository.save(goodsEvaluate);
    }

    /**
     * 编辑书友说评价
     */
//    @Transactional(rollbackFor = Exception.class)
    public void editBookFriendEvaluate(BookFriendEvaluateEditRequest bookFriendEvaluateEditRequest) {
        if (StringUtils.isEmpty(bookFriendEvaluateEditRequest.skuId) || StringUtils.isEmpty(bookFriendEvaluateEditRequest.customerName)
                || StringUtils.isEmpty(bookFriendEvaluateEditRequest.evaluateContent) || StringUtils.isEmpty(bookFriendEvaluateEditRequest.evaluateId)) {
            throw new SbcRuntimeException("K-000009");
        }
        Optional<GoodsEvaluate> evaOpt = goodsEvaluateRepository.findById(bookFriendEvaluateEditRequest.evaluateId);
        if(evaOpt.isPresent()){
            GoodsEvaluate goodsEvaluate = evaOpt.get();
            if(!goodsEvaluate.getGoodsInfoId().equals(bookFriendEvaluateEditRequest.skuId)){
                GoodsInfoEditResponse res = goodsInfoService.findById(bookFriendEvaluateEditRequest.skuId);
                goodsEvaluate.setGoodsId(res.getGoods().getGoodsId());
                goodsEvaluate.setGoodsInfoId(res.getGoodsInfo().getGoodsInfoId());
                goodsEvaluate.setGoodsInfoName(res.getGoodsInfo().getGoodsInfoName());
                goodsEvaluate.setGoodsImg(res.getImages().get(0).getArtworkUrl());
            }
            goodsEvaluate.setStoreId(bookFriendEvaluateEditRequest.storeId);
            goodsEvaluate.setCustomerName(bookFriendEvaluateEditRequest.customerName);
            goodsEvaluate.setEvaluateContent(bookFriendEvaluateEditRequest.evaluateContent);
            goodsEvaluate.setEvaluateTime(bookFriendEvaluateEditRequest.evaluateTime == null ? LocalDateTime.now()
                    : LocalDateTime.parse(bookFriendEvaluateEditRequest.evaluateTime, DateTimeFormatter.ISO_DATE_TIME));
            goodsEvaluate.setIsShow(bookFriendEvaluateEditRequest.isShow == null ? 0 : bookFriendEvaluateEditRequest.isShow);
            goodsEvaluate.setIsRecommend(bookFriendEvaluateEditRequest.isRecommend == null ? 0 : bookFriendEvaluateEditRequest.isRecommend);
            goodsEvaluate.setEvaluateCatetory(3);
            goodsEvaluateRepository.save(goodsEvaluate);
        }
    }

    /**
     * 批量新增商品评价
     *
     * @author lvzhenwei
     */
    public void addList(List<GoodsEvaluate> entityList) {
        goodsEvaluateRepository.saveAll(entityList);
        //增加评论数
        entityList.forEach(goodsEvaluate -> {
            goodsEvaluateNumMqService.updateGoodsEvaluateNum(goodsEvaluate);
        });
    }

    /**
     * 修改商品评价
     *
     * @author liutao
     */
    @Transactional
    public GoodsEvaluate modify(GoodsEvaluate entity) {
        GoodsEvaluateImageQueryRequest queryReq = new GoodsEvaluateImageQueryRequest();
        queryReq.setEvaluateId(entity.getEvaluateId());
        List<GoodsEvaluateImage> goodsEvaluateImages = goodsEvaluateImageService.list(queryReq);
        entity.setGoodsEvaluateImages(goodsEvaluateImages);
        goodsEvaluateRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除商品评价
     *
     * @author liutao
     */
    @Transactional
    public void deleteById(String id) {
        goodsEvaluateRepository.deleteById(id);
    }

    /**
     * 批量删除商品评价
     *
     * @author liutao
     */
    @Transactional
    public void deleteByIdList(List<String> ids) {
        goodsEvaluateRepository.deleteAll(ids.stream().map(id -> {
            GoodsEvaluate entity = new GoodsEvaluate();
            entity.setEvaluateId(id);
            return entity;
        }).collect(Collectors.toList()));
    }

    /**
     * 单个查询商品评价
     *
     * @author liutao
     */
    public GoodsEvaluate getById(String id) {
        return goodsEvaluateRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询商品评价
     *
     * @author liutao
     */
    public Page<GoodsEvaluate> page(GoodsEvaluateQueryRequest queryReq) {
        return goodsEvaluateRepository.findAll(
                GoodsEvaluateWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询商品评价
     *
     * @author liutao
     */
    public List<GoodsEvaluate> list(GoodsEvaluateQueryRequest queryReq) {
        return goodsEvaluateRepository.findAll(GoodsEvaluateWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 获取已评价商品数量
     *
     * @param queryReq
     * @return
     */
    public Long getGoodsEvaluateNum(GoodsEvaluateQueryRequest queryReq) {
        return goodsEvaluateRepository.count(GoodsEvaluateWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * @param requset {@link GoodsEvaluateCountRequset}
     * @Description: 商品好评率
     * @Author: Bob
     * @Date: 2019-04-09 16:05
     */
    public String getGoodsPraise(GoodsEvaluateCountRequset requset) {
        return goodsEvaluateRepository.queryPraise(requset.getGoodsId());
    }

    /**
     * @Description: 查询最新评价<排除系统评价>
     * @param request
     * @Author: Bob
     * @Date: 2019-05-29 17:43
     */
    public List<GoodsEvaluate> getTop(GoodsEvaluatePageRequest request){
        return goodsEvaluateRepository.queryTopData(request, request.getPageRequest());
    }

    /**
     * 查询书友说评价
     */
    public List<GoodsEvaluate> getBookFriendEvaluate(GoodsEvaluatePageRequest request){
        return goodsEvaluateRepository.queryBookFriendEvaluate(request, request.getPageRequest());
    }

    /**
     * 将实体包装成VO
     *
     * @author liutao
     */
    public GoodsEvaluateVO wrapperVo(GoodsEvaluate goodsEvaluate) {
        if (goodsEvaluate != null) {
            GoodsEvaluateVO goodsEvaluateVO = new GoodsEvaluateVO();
            KsBeanUtil.copyPropertiesThird(goodsEvaluate, goodsEvaluateVO);
            goodsEvaluateVO.setGoodsImages(goodsImageService.findByGoodsId(goodsEvaluateVO.getGoodsId()));
            GoodsEvaluateImageQueryRequest goodsEvaluateImageQueryRequest = new GoodsEvaluateImageQueryRequest();
            goodsEvaluateImageQueryRequest.setEvaluateId(goodsEvaluateVO.getEvaluateId());
            List<GoodsEvaluateImage> goodsEvaluateImageList = goodsEvaluateImageService.list(goodsEvaluateImageQueryRequest);
            if (CollectionUtils.isNotEmpty(goodsEvaluateImageList)) {
                List<GoodsEvaluateImageVO> goodsEvaluateImageVOList = goodsEvaluateImageList.stream()
                        .map(goodsEvaluateImage -> goodsEvaluateImageService.wrapperVo(goodsEvaluateImage))
                        .collect(Collectors.toList());
                goodsEvaluateVO.setEvaluateImageList(goodsEvaluateImageVOList);
            }
            return goodsEvaluateVO;
        }
        return null;
    }

    //优化转换逻辑，避免循环调用微服务
    public List<GoodsEvaluateVO> wrapperVo(List<GoodsEvaluate> goodsEvaluates) {
        if (CollectionUtils.isEmpty(goodsEvaluates)) {
            return Lists.newArrayList();
        }
        List<String> goodsIds = goodsEvaluates.parallelStream().map(GoodsEvaluate::getGoodsId).collect(Collectors.toList());
        List<GoodsImageVO> goodsImageVOs = goodsImageService.findByGoodsIds(goodsIds);
        return goodsEvaluates.stream().map(goodsEvaluate -> {
            GoodsEvaluateVO goodsEvaluateVO = new GoodsEvaluateVO();
            KsBeanUtil.copyPropertiesThird(goodsEvaluate, goodsEvaluateVO);
            goodsEvaluateVO.setEvaluateImageList(goodsEvaluate.getGoodsEvaluateImages().stream()
                    .map(goodsEvaluateImage -> goodsEvaluateImageService.wrapperVo(goodsEvaluateImage))
                    .collect(Collectors.toList()));
            if(CollectionUtils.isNotEmpty(goodsImageVOs)) {
                goodsEvaluateVO.setGoodsImages(goodsImageVOs.stream()
                        .filter(goodsImageVO -> StringUtils.equals(goodsImageVO.getGoodsId(), goodsEvaluate.getGoodsId()))
                        .collect(Collectors.toList()));
            }
            return goodsEvaluateVO;
        }).collect(Collectors.toList());
    }

    /**
     * 修改商品评价
     *
     * @author liutao
     */
    @Transactional
    public GoodsEvaluate answer(GoodsEvaluateAnswerRequest request) {
        GoodsEvaluate goodsEvaluateDetail = this.getById(request.getEvaluateId());
        //如果有过回复的话，把以前的回复移到历史回复
        GoodsEvaluate entity = new GoodsEvaluate();
        KsBeanUtil.copyPropertiesThird(goodsEvaluateDetail,entity);
        if(Objects.nonNull(goodsEvaluateDetail) && StringUtils.isNotEmpty(goodsEvaluateDetail
                .getEvaluateAnswer())){
            entity.setHistoryEvaluateAnswerTime(goodsEvaluateDetail.getEvaluateAnswerTime());
            entity.setHistoryEvaluateAnswer(goodsEvaluateDetail.getEvaluateAnswer());
            entity.setHistoryEvaluateAnswerEmployeeId(goodsEvaluateDetail.getEvaluateAnswerEmployeeId());
            entity.setHistoryEvaluateAnswerAccountName(goodsEvaluateDetail.getEvaluateAnswerAccountName());
        }

        entity.setIsShow(request.getIsShow());
        entity.setEvaluateAnswer(request.getEvaluateAnswer());
        entity.setEvaluateAnswerAccountName(request.getName());
        entity.setEvaluateAnswerEmployeeId(request.getAdminId());
        entity.setEvaluateAnswerTime(LocalDateTime.now());
        entity.setIsAnswer(request.getIsAnswer());
        entity.setBuyTime(goodsEvaluateDetail.getBuyTime());
        entity.setGoodsImg(goodsEvaluateDetail.getGoodsImg());
        goodsEvaluateImageService.updateIsShowByEvaluateId(EvaluateImgUpdateIsShowReq.builder()
                .evaluateId(request.getEvaluateId()).isShow(request.getIsShow()).build());
        if (!goodsEvaluateDetail.getIsShow().equals(request.getIsShow()) && request.getIsShow().equals(NumberUtils.INTEGER_ONE)){
            goodsService.increaseGoodsEvaluateNum(goodsEvaluateDetail.getGoodsId());
        }
        if (!goodsEvaluateDetail.getIsShow().equals(request.getIsShow()) && request.getIsShow().equals(NumberUtils.INTEGER_ZERO)){
            goodsService.decreaseGoodsEvaluateNum(goodsEvaluateDetail.getGoodsId());
        }
        return this.modify(entity);
    }

    /**
     * 查询展示评价数量，根据goodsId分组
     * @param goodsIds
     * @return
     */
    public List<Object> countByGoodsIdsGroupByAndGoodsId(List<String> goodsIds){
        return goodsEvaluateRepository.countByGoodsIdsGroupByAndGoodsId(goodsIds);
    }

    /**
     * 查询展示好评数量，根据goodsId分组
     * @param goodsIds
     * @return
     */
    public List<Object> countFavorteByGoodsIdsGroupByAndGoodsId(List<String> goodsIds){
        return goodsEvaluateRepository.countFavorteByGoodsIdsGroupByAndGoodsId(goodsIds);
    }
}
