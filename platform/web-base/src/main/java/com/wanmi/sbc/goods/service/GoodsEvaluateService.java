package com.wanmi.sbc.goods.service;

import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluateAddResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateAddResponse;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.growthvalue.CustomerGrowthValueProvider;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailSaveProvider;
import com.wanmi.sbc.customer.api.provider.storeevaluate.StoreEvaluateQueryProvider;
import com.wanmi.sbc.customer.api.provider.storeevaluate.StoreEvaluateSaveProvider;
import com.wanmi.sbc.customer.api.request.growthvalue.CustomerGrowthValueAddRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailAddRequest;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateAddRequest;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateListRequest;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluateListResponse;
import com.wanmi.sbc.customer.bean.enums.GrowthValueServiceType;
import com.wanmi.sbc.customer.bean.enums.OperateType;
import com.wanmi.sbc.customer.bean.enums.PointsServiceType;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateVO;
import com.wanmi.sbc.goods.api.provider.goodsevaluate.GoodsEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodsevaluate.GoodsEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.provider.goodsevaluateimage.GoodsEvaluateImageSaveProvider;
import com.wanmi.sbc.goods.api.provider.goodstobeevaluate.GoodsTobeEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.provider.storetobeevaluate.StoreTobeEvaluateSaveProvider;
import com.wanmi.sbc.goods.api.request.goodsevaluate.GoodsEvaluateAddRequest;
import com.wanmi.sbc.goods.api.request.goodsevaluate.GoodsEvaluateListRequest;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageAddRequest;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateListResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateVO;
import com.wanmi.sbc.goods.request.EvaluateAddRequest;
import com.wanmi.sbc.order.bean.vo.SupplierVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.evaluateratio.EvaluateRatioQueryProvider;
import com.wanmi.sbc.setting.bean.vo.EvaluateRatioVO;
import com.wanmi.sbc.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Auther: jiaojiao
 * @Date: 2019/3/7 09:45
 * @Description:商品评价
 */
@Service
public class GoodsEvaluateService {

    @Autowired
    GoodsEvaluateSaveProvider goodsEvaluateSaveProvider;

    @Autowired
    StoreEvaluateSaveProvider storeEvaluateSaveProvider;

    @Autowired
    GoodsEvaluateImageSaveProvider imageSaveProvider;

    @Autowired
    GoodsTobeEvaluateSaveProvider goodsTobeEvaluateSaveProvider;

    @Autowired
    StoreTobeEvaluateSaveProvider storeTobeEvaluateSaveProvider;
    @Autowired
    EvaluateRatioQueryProvider evaluateRatioQueryProvider;
    @Autowired
    GoodsEvaluateQueryProvider queryProvider;
    @Autowired
    StoreEvaluateQueryProvider storeEvaluateQueryProvider;
    @Autowired
    RedisService redisService;
    @Autowired
    private CustomerGrowthValueProvider customerGrowthValueProvider;
    @Autowired
    private CustomerPointsDetailSaveProvider customerPointsDetailSaveProvider;
    @Resource
    private CommonUtil commonUtil;


    /**
     * 添加商品评价
     *
     * @param evaluateAddRequest
     * @return
     */
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<StoreEvaluateAddResponse> addGoodsEvaluate(@RequestBody EvaluateAddRequest evaluateAddRequest, TradeVO tradeVO) {

        Operator operator = commonUtil.getOperator();
        //商品评价
        GoodsEvaluateAddRequest goodsEvaluateAddRequest = evaluateAddRequest.getGoodsEvaluateAddRequest();
        //服务评价
        StoreEvaluateAddRequest storeEvaluateAddRequest = evaluateAddRequest.getStoreEvaluateAddRequestList();
        //晒单图片
        List<GoodsEvaluateImageAddRequest> imageAddRequestList = evaluateAddRequest.getGoodsEvaluateImageAddRequest();

        if ((Objects.isNull(goodsEvaluateAddRequest) || (Objects.isNull(goodsEvaluateAddRequest.getEvaluateContent())
                && Objects.isNull(goodsEvaluateAddRequest.getEvaluateScore())
                && (Objects.isNull(imageAddRequestList) || !imageAddRequestList.isEmpty())))
                && (Objects.isNull(storeEvaluateAddRequest) || (Objects.isNull(storeEvaluateAddRequest.getServerScore())
                && Objects.isNull(storeEvaluateAddRequest.getLogisticsScore())
                && Objects.isNull(storeEvaluateAddRequest.getGoodsScore())))) {
            throw new SbcRuntimeException("K-000009");
        }
        // 获取商家信息
        SupplierVO supplierVO = tradeVO.getSupplier();
        // 设置商品评价内的店铺信息
        goodsEvaluateAddRequest.setStoreId(supplierVO.getStoreId());
        goodsEvaluateAddRequest.setStoreName(supplierVO.getStoreName());

        // 设置店铺评价内的店铺信息
        storeEvaluateAddRequest.setStoreId(supplierVO.getStoreId());
        storeEvaluateAddRequest.setStoreName(supplierVO.getStoreName());
        StoreEvaluateAddResponse res = null;
        //商品评价
        if (Objects.nonNull(goodsEvaluateAddRequest) && ((Objects.nonNull(goodsEvaluateAddRequest.getEvaluateContent())
                && Objects.nonNull(goodsEvaluateAddRequest.getEvaluateScore()))
                || (Objects.nonNull(imageAddRequestList) && !imageAddRequestList.isEmpty()))) {
            //是否重复评价
            GoodsEvaluateListResponse goodsEvaluateListResponse =
                    queryProvider.list(GoodsEvaluateListRequest.builder().goodsInfoId(goodsEvaluateAddRequest
                            .getGoodsInfoId()).orderNo(goodsEvaluateAddRequest.getOrderNo()).build()).getContext();
            if (goodsEvaluateListResponse.getGoodsEvaluateVOList() != null &&
                    !goodsEvaluateListResponse.getGoodsEvaluateVOList().isEmpty()) {
                return BaseResponse.error("商品已评价，请勿重复评价");
            }

            Map<String, TradeItemVO> skuMap =
                    tradeVO.getTradeItems().stream().collect(Collectors.toMap(TradeItemVO::getSkuId, c -> c));

            // 如果订单内不存在该商品
            TradeItemVO itemVO = skuMap.get(goodsEvaluateAddRequest.getGoodsInfoId());
            if (Objects.isNull(itemVO)) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }

            // 设置为从订单内获取, 不从前端传递过来
            goodsEvaluateAddRequest.setGoodsId(itemVO.getSpuId());
            goodsEvaluateAddRequest.setGoodsInfoName(itemVO.getSkuName());
            goodsEvaluateAddRequest.setSpecDetails(itemVO.getSpecDetails());

            goodsEvaluateAddRequest.setCreatePerson(operator.getUserId());
            goodsEvaluateAddRequest.setCreateTime(LocalDateTime.now());
            goodsEvaluateAddRequest.setCustomerAccount(commonUtil.getCustomer().getCustomerAccount());
            goodsEvaluateAddRequest.setCustomerId(commonUtil.getCustomer().getCustomerId());
            goodsEvaluateAddRequest.setCustomerName(commonUtil.getOperator().getName());
            goodsEvaluateAddRequest.setDelFlag(0);
            goodsEvaluateAddRequest.setIsAnswer(0);
            goodsEvaluateAddRequest.setEvaluateTime(LocalDateTime.now());
            goodsEvaluateAddRequest.setTerminalSource(commonUtil.getTerminal());
            boolean flag = (Objects.nonNull(imageAddRequestList) && !imageAddRequestList.isEmpty());
            if (flag) {
                goodsEvaluateAddRequest.setIsUpload(1);
            }

            GoodsEvaluateVO goodsEvaluateVO =
                    goodsEvaluateSaveProvider.add(goodsEvaluateAddRequest).getContext().getGoodsEvaluateVO();

            //服务评价订单购买时间
            storeEvaluateAddRequest.setBuyTime(goodsEvaluateVO.getBuyTime());
            if (Objects.nonNull(goodsEvaluateVO) && imageAddRequestList.size() > 0) {
                //晒单
                if (flag) {
                    for (GoodsEvaluateImageAddRequest imageAddRequest : imageAddRequestList) {
                        imageAddRequest.setEvaluateId(goodsEvaluateVO.getEvaluateId());
                        imageAddRequest.setCreateTime(LocalDateTime.now());
                        imageAddRequest.setGoodsId(goodsEvaluateVO.getGoodsId());
                        imageAddRequest.setDelFlag(0);
                        imageAddRequest.setIsShow(1);
                        imageSaveProvider.add(imageAddRequest);
                    }
                }
            }
            //删除待评价记录
            Integer i = goodsTobeEvaluateSaveProvider.deleteByOrderAndSku(GoodsTobeEvaluateQueryRequest.builder()
                    .orderNo(goodsEvaluateAddRequest.getOrderNo()).goodsInfoId(goodsEvaluateAddRequest.getGoodsInfoId())
                    .build()).getContext();
            if (i == 0) {
                throw new SbcRuntimeException("K-999997");
            }

            if (goodsEvaluateAddRequest.getEvaluateScore() >= 4
                    && goodsEvaluateAddRequest.getEvaluateContent().trim().length() >= 30) {
                // 增加成长值
                customerGrowthValueProvider.increaseGrowthValue(CustomerGrowthValueAddRequest.builder()
                        .customerId(commonUtil.getOperatorId())
                        .type(OperateType.GROWTH)
                        .serviceType(GrowthValueServiceType.EVALUATE)
                        .build());
                // 增加积分
                customerPointsDetailSaveProvider.add(CustomerPointsDetailAddRequest.builder()
                        .customerId(commonUtil.getOperatorId())
                        .type(OperateType.GROWTH)
                        .serviceType(PointsServiceType.EVALUATE)
                        .build());
            }
        }

        //店铺 服务评价
        if (Objects.nonNull(storeEvaluateAddRequest) && (Objects.nonNull(storeEvaluateAddRequest.getServerScore())
                && Objects.nonNull(storeEvaluateAddRequest.getLogisticsScore())
                && Objects.nonNull(storeEvaluateAddRequest.getGoodsScore()))) {

            //判断重复提交
            StoreEvaluateListResponse storeEvaluateListResponse =
                    storeEvaluateQueryProvider.list(StoreEvaluateListRequest.builder().storeId(storeEvaluateAddRequest
                            .getStoreId()).orderNo(storeEvaluateAddRequest.getOrderNo()).build()).getContext();

            if (storeEvaluateListResponse.getStoreEvaluateVOList() != null &&
                    !storeEvaluateListResponse.getStoreEvaluateVOList().isEmpty()) {
                return BaseResponse.error("店铺评价已评价，请勿重复评价");
            }

            EvaluateRatioVO evaluateRatioVO = redisService.getObj(CacheKeyConstant.EVALUATE_RATIO,
                    EvaluateRatioVO.class);
            if (Objects.isNull(evaluateRatioVO)) {
                evaluateRatioVO = evaluateRatioQueryProvider.findOne().getContext().getEvaluateRatioVO();
                redisService.setObj(CacheKeyConstant.EVALUATE_RATIO, evaluateRatioVO, -1);
            }

            BigDecimal goodsRation = evaluateRatioVO.getGoodsRatio();
            BigDecimal logisticsRation = evaluateRatioVO.getLogisticsRatio();
            BigDecimal serverRation = evaluateRatioVO.getServerRatio();

            BigDecimal goodsScore = new BigDecimal(storeEvaluateAddRequest.getGoodsScore());
            BigDecimal logisticsScore = new BigDecimal(storeEvaluateAddRequest.getLogisticsScore());
            BigDecimal serverScore = new BigDecimal(storeEvaluateAddRequest.getServerScore());

            BigDecimal compositeScore = goodsRation.multiply(goodsScore).add(logisticsRation.multiply(logisticsScore))
                    .add(serverRation.multiply(serverScore)).setScale(2);
            storeEvaluateAddRequest.setCompositeScore(compositeScore);

            storeEvaluateAddRequest.setCreatePerson(commonUtil.getOperator().getUserId());
            storeEvaluateAddRequest.setCreateTime(LocalDateTime.now());
            storeEvaluateAddRequest.setCustomerAccount(commonUtil.getCustomer().getCustomerAccount());
            storeEvaluateAddRequest.setCustomerId(commonUtil.getCustomer().getCustomerId());
            storeEvaluateAddRequest.setCustomerName(commonUtil.getOperator().getName());
            storeEvaluateAddRequest.setDelFlag(0);
            storeEvaluateAddRequest.setTerminalSource(commonUtil.getTerminal());

            StoreEvaluateAddResponse response = storeEvaluateSaveProvider.add(storeEvaluateAddRequest).getContext();
            StoreEvaluateVO storeEvaluateVO = response.getStoreEvaluateVO();
            res = response;

            Integer i = storeTobeEvaluateSaveProvider.deleteByOrderAndStoreId(StoreTobeEvaluateQueryRequest.builder()
                    .storeId(storeEvaluateAddRequest.getStoreId()).orderNo(storeEvaluateAddRequest.getOrderNo())
                    .build()).getContext();

            if (i == 0) {
                throw new SbcRuntimeException("K-999997");
            }
        }
        return BaseResponse.success(res);
    }

}
