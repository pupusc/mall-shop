package com.wanmi.sbc.goods.info.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.goods.api.request.goods.ProviderGoodsNotSellRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsCheckResponse;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.distributor.goods.repository.DistributiorGoodsInfoRepository;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsCheckRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsRequest;
import com.wanmi.sbc.goods.log.GoodsCheckLog;
import com.wanmi.sbc.goods.log.service.GoodsCheckLogService;
import com.wanmi.sbc.goods.standard.model.root.StandardGoodsRel;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRelRepository;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRepository;
import com.wanmi.sbc.goods.standard.repository.StandardSkuRepository;
import com.wanmi.sbc.goods.standard.service.StandardImportService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * S2b商品服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Transactional(readOnly = true)
public class S2bGoodsService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StandardGoodsRepository standardGoodsRepository;

    @Autowired
    private StandardGoodsRelRepository standardGoodsRelRepository;

    @Autowired
    private StandardSkuRepository standardSkuRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GoodsCheckLogService goodsCheckLogService;

    @Autowired
    private DistributiorGoodsInfoRepository distributiorGoodsInfoRepository;

    @Autowired
    private StandardImportService standardImportService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 商品审核
     * @param checkRequest
     * @throws SbcRuntimeException
     */
    @Transactional(rollbackFor = Exception.class)
    public GoodsCheckResponse check(GoodsCheckRequest checkRequest) throws SbcRuntimeException {
        GoodsCheckResponse response = new GoodsCheckResponse();
        goodsRepository.updateAuditDetail(checkRequest.getAuditStatus(), checkRequest.getAuditReason(), checkRequest.getGoodsIds());
        goodsInfoRepository.updateAuditDetail(checkRequest.getAuditStatus(), checkRequest.getGoodsIds());

        //商品库禁售商品
        if(Boolean.TRUE == checkRequest.getDealStandardGoodsFlag()) {
            response.setDeleteStandardIds(this.dealStandardGoods(checkRequest));
        }
        //同步代码 处理供应商商品的编辑供应商商品 审核通过的情况(1.boss删除了供应商商品库 2.boss没删除供应商商品库，供应商二次编辑 3.供应商第一次发布商品)
        response.setStandardIds(dealProviderGoodsEdit(checkRequest));

        //更新商家代销商品可售性
        Boolean checkFlag = null;
        if(CheckStatus.FORBADE.equals(checkRequest.getAuditStatus())){
            checkFlag = Boolean.FALSE;
        } else if(CheckStatus.CHECKED.equals(checkRequest.getAuditStatus())) {
            checkFlag = Boolean.TRUE;
        }
        if(checkFlag != null){
            ProviderGoodsNotSellRequest request = ProviderGoodsNotSellRequest.builder().goodsIds(checkRequest.getGoodsIds()).checkFlag(checkFlag).build();
            goodsService.dealGoodsVendibility(request);
        }


        //商品禁售删除分销员分销商品
        checkRequest.getGoodsIds().forEach(goodsID->{
            distributiorGoodsInfoRepository.deleteByGoodsId(goodsID);
        });

        //新增审核记录
        checkRequest.getGoodsIds().forEach(goodsId -> {
            GoodsCheckLog checkLog = new GoodsCheckLog();
            checkLog.setId(UUIDUtil.getUUID());
            checkLog.setGoodsId(goodsId);
            checkLog.setChecker(checkRequest.getChecker());
            checkLog.setAuditReason(checkRequest.getAuditReason());
            checkLog.setAuditStatus(checkRequest.getAuditStatus());
            goodsCheckLogService.addGoodsCheckLog(checkLog);
        });
        return response;
        //重新索引
//        esGoodsInfoElasticService.initEsGoodsInfo(EsGoodsInfoRequest.builder().goodsIds(checkRequest.getGoodsIds()).build());
    }

    /**
     * 处理商品库商品
     * @param checkRequest
     */
    private List<String> dealStandardGoods(GoodsCheckRequest checkRequest){
        //如果商品库里有此商品    禁售状态 同步到商品库
        List<StandardGoodsRel> standardGoodsRels = standardGoodsRelRepository.findByGoodsIds(checkRequest.getGoodsIds());

        if(CollectionUtils.isNotEmpty(standardGoodsRels) && CheckStatus.FORBADE.equals(checkRequest.getAuditStatus())){
            List<String> standardGoodsIds = standardGoodsRels.stream().map(StandardGoodsRel::getStandardId).collect(Collectors.toList());
            standardGoodsRepository.deleteByGoodsIds(standardGoodsIds);
            standardSkuRepository.deleteByGoodsIds(standardGoodsIds);
            standardGoodsRelRepository.deleteByGoodsIds(checkRequest.getGoodsIds());
            return standardGoodsIds;
        }
        return Collections.emptyList();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<String> dealProviderGoodsEdit(GoodsCheckRequest checkRequest) {
        List<String> standardIds = new ArrayList<>();
        for (String goodsId : checkRequest.getGoodsIds()) {
            StandardGoodsRel standardGoodsRel = standardGoodsRelRepository.findByGoodsId(goodsId);
            //供应商商品 审核通过的情况(1.boss删除了供应商商品库 2.boss没删除供应商商品库，供应商二次编辑 3.供应商第一次发布商品)
            Goods goods = goodsRepository.getOne(goodsId);
            if(goods.getGoodsSource() == 0 && checkRequest.getAuditStatus() == CheckStatus.CHECKED){
                if(standardGoodsRel != null){
                    if(standardGoodsRel.getDelFlag().equals(DeleteFlag.YES)){
                        //1.boss删除了供应商商品库  暂时不管，等他导入商品库的时候，再同步供应商商品库
                    }else if(standardGoodsRel.getDelFlag().equals(DeleteFlag.NO)){
                        //2.boss没删除供应商商品库，供应商二次编辑-------需要同步供应商商品库，然后设置商家商品为待同步
                        standardImportService.synProviderGoods(goods.getGoodsId(),standardGoodsRel.getStandardId());
                        standardIds.add(standardGoodsRel.getStandardId());
                    }
                    //如果审核的时候，供应商商品是上架状态，那么关联的商家商品也要变为商家上架状态 如果是下架，商家商品也是下架20200506
//                    List<Goods> supplierGoods = goodsRepository.findAllByProviderGoodsId(goods.getGoodsId());
//                    supplierGoods.forEach(g->{g.setAddedFlag(goods.getAddedFlag());});
//                    goodsRepository.saveAll(supplierGoods);
                }else {
                    //3.供应商第一次发布商品，走正常流程
                    // 同步到商品库
                    GoodsRequest synRequest = new GoodsRequest();
                    synRequest.setGoodsIds(Arrays.asList(goodsId));
                    standardIds.addAll(standardImportService.importStandard(synRequest));
                }
            }
        }
        return standardIds;
    }

    /**
     * 待审核统计
     * @param request
     * @return
     */
    public Long countByTodo(GoodsQueryRequest request){
        request.setAuditStatus(CheckStatus.WAIT_CHECK);
        request.setDelFlag(DeleteFlag.NO.toValue());
        return goodsRepository.count(request.getWhereCriteria());
    }
}
