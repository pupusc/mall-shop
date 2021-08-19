package com.wanmi.sbc.goods.pointsgoods.service;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.EmployeeListRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.EmployeeListVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.constant.GoodsStockErrorCode;
import com.wanmi.sbc.goods.api.constant.PointsGoodsErrorCode;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsQueryRequest;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsSalesModifyRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMinusStockDTO;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.enums.PointsGoodsStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.bean.vo.PointsGoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.PointsGoodsVO;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsInfoStockService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.pointsgoods.model.root.PointsGoods;
import com.wanmi.sbc.goods.pointsgoods.repository.PointsGoodsRepository;
import com.wanmi.sbc.goods.pointsgoodscate.model.root.PointsGoodsCate;
import com.wanmi.sbc.goods.pointsgoodscate.service.PointsGoodsCateService;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>积分商品表业务逻辑</p>
 *
 * @author yang
 * @date 2019-05-07 15:01:41
 */
@Slf4j
@Service("PointsGoodsService")
public class PointsGoodsService {
    @Autowired
    private PointsGoodsRepository pointsGoodsRepository;

    @Autowired
    private GoodsInfoSpecDetailRelRepository goodsInfoSpecDetailRelRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private PointsGoodsCateService pointsGoodsCateService;

    @Autowired
    private GoodsInfoStockService goodsInfoStockService;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;


    /**
     * 新增积分商品表
     *
     * @author yang
     */
    @Transactional
    public PointsGoods add(PointsGoods entity) {
        // 验证参数
        checkParam(entity);
        // 减库存
//        int state = goodsInfoRepository.subStockById(entity.getStock(), entity.getGoodsInfoId());
//        if (state != 1) {
//            throw new SbcRuntimeException(GoodsStockErrorCode.LACK_ERROR);
//        }
//        GoodsInfo goodsInfo = goodsInfoRepository.findByGoodsInfoIdAndDelFlag(entity.getGoodsInfoId(), DeleteFlag.NO);
//        if (!ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
//            goodsInfoStockService.subStockById(entity.getStock(), entity.getGoodsInfoId());
//        }
        pointsGoodsRepository.save(entity);
        return entity;
    }

    @Transactional
    public void batchAdd(List<PointsGoods> pointsGoodsList) {
        pointsGoodsList.forEach(pointsGoods -> {
            if (Objects.isNull(pointsGoods.getRecommendFlag())) {
                pointsGoods.setRecommendFlag(BoolFlag.NO);
            }
            add(pointsGoods);
        });
    }

    /**
     * 修改积分商品表
     *
     * @author yang
     */
    @Transactional
    public PointsGoods modify(PointsGoods entity) {
        PointsGoodsStatus pointsGoodsStatus = getPointsGoodsStatus(entity);
        // 活动已开始无法编辑
        if (!PointsGoodsStatus.NOT_START.equals(pointsGoodsStatus)) {
            throw new SbcRuntimeException(PointsGoodsErrorCode.MODIFY_ERROR);
        }
        // 开启的活动才验证
        checkParam(entity);
        PointsGoods pointsGoods = getById(entity.getPointsGoodsId());
        long stock = entity.getStock() - pointsGoods.getStock();
        // 增减库存
//        goodsInfoStockService.subStockById(stock, entity.getGoodsInfoId());
        /*int state = goodsInfoRepository.subStockById(stock, entity.getGoodsInfoId());
        // 验证库存
        if (state != 1) {
            throw new SbcRuntimeException(GoodsStockErrorCode.LACK_ERROR);
        }*/
        pointsGoodsRepository.save(entity);
        return entity;
    }

    @Transactional
    @GlobalTransactional
    public void modifyStatus(PointsGoods pointsGoods) {
        // 开启时才验证
        if (EnableStatus.ENABLE.equals(pointsGoods.getStatus())) {
            // 验证库存
            if (pointsGoods.getStock() == 0) {
                throw new SbcRuntimeException(PointsGoodsErrorCode.ENABLE_ERROR);
            }
            // 验证店铺状态
            checkStoreStatus(pointsGoods.getGoodsInfo());
            // 验证是否活动已结束
            PointsGoodsStatus pointsGoodsStatus = getPointsGoodsStatus(pointsGoods);
            if (PointsGoodsStatus.ENDED.equals(pointsGoodsStatus)) {
                throw new SbcRuntimeException(PointsGoodsErrorCode.START_ERROR);
            }
        }
        pointsGoodsRepository.save(pointsGoods);
    }

    // 验证添加参数
    private void checkParam(PointsGoods entity) {
        // 验证分类是否已删除
        PointsGoodsCate pointsGoodsCate = pointsGoodsCateService.getById(entity.getCateId());
        if (pointsGoodsCate.getDelFlag().equals(DeleteFlag.YES)) {
            throw new SbcRuntimeException(PointsGoodsErrorCode.CATE_ERROR,
                    new Object[]{pointsGoodsCate.getCateName()});
        }
        // 验证sku状态
        List<String> goodsInfoIds = new ArrayList<>();
        goodsInfoIds.add(entity.getGoodsInfoId());
        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder()
                .goodsInfoIds(goodsInfoIds)
                .delFlag(DeleteFlag.NO.toValue())
                .build());
        if (!(Objects.nonNull(goodsInfos) && goodsInfos.size() > 0)) {
            throw new SbcRuntimeException(PointsGoodsErrorCode.SKU_ERROR,
                    new Object[]{entity.getGoodsInfoId()});
        } else {
            GoodsInfo goodsInfo = goodsInfos.get(0);
            // 验证店铺状态
            checkStoreStatus(goodsInfo);
            // 验证兑换时间
            checkTime(entity);
        }
    }

    // 验证店铺状态
    private void checkStoreStatus(GoodsInfo goodsInfo) {
        // 判断店铺是否关店
        Goods goods = goodsService.getGoodsById(goodsInfo.getGoodsId());
        StoreVO storeVO = storeQueryProvider.getById(StoreByIdRequest.builder()
                .storeId(goods.getStoreId())
                .build()).getContext().getStoreVO();
        if (storeVO.getStoreState().equals(StoreState.CLOSED)) {
            throw new SbcRuntimeException(PointsGoodsErrorCode.STORE_CLOSE,
                    new Object[]{storeVO.getStoreName()});
        }
        if (storeVO.getDelFlag().equals(DeleteFlag.YES)) {
            throw new SbcRuntimeException(PointsGoodsErrorCode.STORE_DELETE,
                    new Object[]{storeVO.getStoreName()});
        }
        // 判断店铺是否已禁用
        EmployeeListRequest employeeListRequest = new EmployeeListRequest();
        employeeListRequest.setCompanyInfoId(storeVO.getCompanyInfo().getCompanyInfoId());
        List<EmployeeListVO> employeeList = employeeQueryProvider.list(employeeListRequest)
                .getContext().getEmployeeList();
        if (Objects.isNull(employeeList)) {
            throw new SbcRuntimeException(PointsGoodsErrorCode.STORE_DISABLE,
                    new Object[]{storeVO.getStoreName()});
        } else {
            List<EmployeeListVO> listVOS = employeeList.stream()
                    .filter(employeeListVO -> employeeListVO.getDelFlag().equals(DeleteFlag.NO))
                    .filter(employeeListVO -> employeeListVO.getAccountState().equals(AccountState.ENABLE))
                    .collect(Collectors.toList());
            if (listVOS.size() == 0) {
                throw new SbcRuntimeException(PointsGoodsErrorCode.STORE_DISABLE,
                        new Object[]{storeVO.getStoreName()});
            }
        }
    }

    // 验证兑换时间
    private void checkTime(PointsGoods entity) {
        List<PointsGoods> pointsGoodsList = list(PointsGoodsQueryRequest.builder()
                .goodsInfoId(entity.getGoodsInfoId())
                .delFlag(DeleteFlag.NO)
                .build());
        // 修改时排除自己在比较
        if (Objects.nonNull(entity.getPointsGoodsId())) {
            pointsGoodsList = pointsGoodsList.stream()
                    .filter(pointsGoods -> !pointsGoods.getPointsGoodsId().equals(entity.getPointsGoodsId()))
                    .collect(Collectors.toList());
        }
        // 验证结束时间是否在开始时间之前
        if (!entity.getBeginTime().isBefore(entity.getEndTime())) {
            throw new SbcRuntimeException(PointsGoodsErrorCode.END_DATE_ERROR);
        }
        if (Objects.nonNull(pointsGoodsList) && pointsGoodsList.size() > 0) {
            for (PointsGoods pointsGoods : pointsGoodsList) {
                // 开始时间等于已绑定积分商品的开始时间
                if (pointsGoods.getBeginTime().isEqual(entity.getBeginTime()) ||
                        // 开始时间等于已绑定积分商品的结束时间
                        pointsGoods.getEndTime().isEqual(entity.getBeginTime()) ||
                        // 开始时间在已绑定积分商品的时间段内
                        (pointsGoods.getBeginTime().isBefore(entity.getBeginTime())
                                && pointsGoods.getEndTime().isAfter(entity.getBeginTime())) ||
                        // 结束时间等于已绑定积分商品的开始时间
                        pointsGoods.getBeginTime().isEqual(entity.getEndTime()) ||
                        // 结束时间等于已绑定积分商品的结束时间
                        pointsGoods.getEndTime().isEqual(entity.getEndTime()) ||
                        // 结束时间在已绑定积分商品的时间段内
                        (pointsGoods.getBeginTime().isBefore(entity.getEndTime())
                                && pointsGoods.getEndTime().isAfter(entity.getEndTime())) ||
                        // 该商品绑定积分商品的时间段在该商品时间段内
                        (pointsGoods.getBeginTime().isAfter(entity.getBeginTime())
                                && pointsGoods.getEndTime().isBefore(entity.getEndTime()))
                ) {
                    throw new SbcRuntimeException(PointsGoodsErrorCode.TIME_ERROR,
                            new Object[]{pointsGoods.getGoods().getGoodsName(),
                                    pointsGoods.getBeginTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),
                                    pointsGoods.getEndTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))});
                }
                // 兑换开始时间应大于当前时间
                if (entity.getBeginTime().isBefore(LocalDateTime.now())) {
                    throw new SbcRuntimeException(PointsGoodsErrorCode.BEGIN_DATE_ERROR);
                }
            }
        }
    }

    /**
     * 单个删除积分商品表
     *
     * @author yang
     */
    @Transactional
    public void deleteById(String id) {
        PointsGoods pointsGoods = getById(id);
        PointsGoodsStatus pointsGoodsStatus = getPointsGoodsStatus(pointsGoods);
        // 活动开始无法删除
        if (!PointsGoodsStatus.NOT_START.equals(pointsGoodsStatus)) {
            throw new SbcRuntimeException(PointsGoodsErrorCode.DELETED_ERROR);
        }
        // 加库存
        // goodsInfoRepository.addStockById(pointsGoods.getStock(), pointsGoods.getGoodsInfoId());
//        goodsInfoStockService.addStockById(pointsGoods.getStock(), pointsGoods.getGoodsInfoId());
        pointsGoodsRepository.modifyDelFlagById(id);
    }

    /**
     * 批量删除积分商品表
     *
     * @author yang
     */
    @Transactional
    public void deleteByIdList(List<String> ids) {
        pointsGoodsRepository.deleteByIdList(ids);
    }

    /**
     * 批量删除积分商品表
     *
     * @author yang
     */
    @Transactional
    public void deleteByGoodInfoIdList(List<String> ids) {
        pointsGoodsRepository.deleteByGoodInfoIdList(ids);
    }

    /**
     * 单个查询积分商品表
     *
     * @author yang
     */

    public PointsGoods getById(String id) {
        return pointsGoodsRepository.findById(id).get();
    }

    /**
     * 根据积分商品Id减库存
     *
     * @param stock         库存数
     * @param pointsGoodsId 积分商品Id
     */
    @Transactional
    @GlobalTransactional
    public void subStockById(Long stock, String pointsGoodsId) {
        int updateCount = pointsGoodsRepository.subStockById(stock, pointsGoodsId);
        PointsGoods pointsGoods = pointsGoodsRepository.findById(pointsGoodsId).get();
        if (pointsGoods.getStock() == 0) {
            pointsGoods.setStatus(EnableStatus.DISABLE);
            modifyStatus(pointsGoods);
        }
        if (updateCount <= 0) {
            throw new SbcRuntimeException(GoodsStockErrorCode.LACK_ERROR);
        }

        if (!ThirdPlatformType.LINKED_MALL.equals(pointsGoods.getGoodsInfo().getThirdPlatformType())) {
            GoodsInfoMinusStockDTO dto = new GoodsInfoMinusStockDTO();
            dto.setGoodsInfoId(pointsGoods.getGoodsInfoId());
            dto.setStock(stock);
            goodsInfoService.batchSubStock(Collections.singletonList(dto));
        }
    }

    /**
     * 根据积分商品Id库存清零并停用
     *
     * @param pointsGoodsId
     */
    @Transactional
    public void resetStockById(String pointsGoodsId) {
        pointsGoodsRepository.resetStockById(pointsGoodsId);
    }

    /**
     * 分页查询积分商品表
     *
     * @author yang
     */
    public Page<PointsGoods> page(PointsGoodsQueryRequest queryReq) {
        Page<PointsGoods> pointsGoodsPage = pointsGoodsRepository.findAll(
                PointsGoodsWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = pointsGoodsPage.getContent().stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getGoods().getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getGoods().getThirdPlatformSpuId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(itemIds)) {
            List<QueryItemInventoryResponse.Item> stocks =
                    linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
            if (stocks != null) {
                for (PointsGoods pointsGoods : pointsGoodsPage.getContent()) {
                    Optional<QueryItemInventoryResponse.Item> stock = stocks.stream()
                            .filter(v -> String.valueOf(v.getItemId()).equals(pointsGoods.getGoods().getThirdPlatformSpuId())).findFirst();
                    if (stock.isPresent()) {
                        Long spuStock = stock.get().getSkuList().stream().map(v -> v.getInventory().getQuantity()).reduce(0L, (aLong, aLong2) -> aLong + aLong2);
                        pointsGoods.getGoods().setStock(spuStock);
                        Optional<QueryItemInventoryResponse.Item.Sku> skuStock = stock.get().getSkuList().stream()
                                .filter(v -> String.valueOf(v.getSkuId()).equals(pointsGoods.getGoodsInfo().getThirdPlatformSkuId())).findFirst();
                        if (skuStock.isPresent()) {
                            GoodsInfo goodsInfo = pointsGoods.getGoodsInfo();
                            Long quantity = skuStock.get().getInventory().getQuantity();
                            goodsInfo.setStock(quantity);
                            if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                                goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                            }
                        }
                    }
                }
            }
        }
        List<GoodsInfo> goodsInfos = pointsGoodsPage.getContent().stream().map(PointsGoods::getGoodsInfo).collect(Collectors.toList());
        goodsInfoService.updateGoodsInfoSupplyPriceAndStock(goodsInfos);

        // 同一商品可以多次添加为积分商品，Map的key会重复
//        Map<String, GoodsInfo> goodsInfoMap = goodsInfos.stream().collect(Collectors.toMap(GoodsInfo::getGoodsInfoId, Function.identity()));
//        pointsGoodsPage.getContent().forEach(i -> i.setStock(goodsInfoMap.get(i.getGoodsInfoId()).getStock()));

//        IteratorUtils.zip(pointsGoodsPage.getContent(), goodsInfos,
//                (pointsGoods, goodsInfo) -> pointsGoods.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId()),
//                (pointsGoods, goodsInfo) -> {
//                    pointsGoods.setStock(goodsInfo.getStock());
//                }
//        );

        return pointsGoodsPage;
    }

    /**
     * 列表查询积分商品表
     *
     * @author yang
     */
    public List<PointsGoods> list(PointsGoodsQueryRequest queryReq) {
        Sort sort = queryReq.getSort();
        if (Objects.nonNull(sort)) {
            return pointsGoodsRepository.findAll(PointsGoodsWhereCriteriaBuilder.build(queryReq), sort);
        } else {
            return pointsGoodsRepository.findAll(PointsGoodsWhereCriteriaBuilder.build(queryReq));
        }
    }

    /**
     * 查询过期的积分商品
     *
     * @return
     */
    public List<PointsGoods> queryOverdueList() {
        return pointsGoodsRepository.queryOverdueList();
    }

    /**
     * 根据店铺id查询
     *
     * @param storeId
     * @return
     */
    public List<PointsGoods> getByStoreId(Long storeId) {
        return pointsGoodsRepository.getByStoreId(storeId);
    }

    /**
     * 将实体包装成VO
     *
     * @author yang
     */
    public PointsGoodsVO wrapperVo(PointsGoods pointsGoods) {
        if (pointsGoods != null) {
            PointsGoodsVO pointsGoodsVO = new PointsGoodsVO();
            KsBeanUtil.copyPropertiesThird(pointsGoods, pointsGoodsVO);
            PointsGoodsCate pointsGoodsCate = pointsGoods.getPointsGoodsCate();
            if (Objects.nonNull(pointsGoodsCate)) {
                PointsGoodsCateVO pointsGoodsCateVO = new PointsGoodsCateVO();
                KsBeanUtil.copyPropertiesThird(pointsGoodsCate, pointsGoodsCateVO);
                pointsGoodsVO.setPointsGoodsCate(pointsGoodsCateVO);
            }
            Goods goods = pointsGoods.getGoods();
            if (Objects.nonNull(goods)) {
                GoodsVO goodsVO = new GoodsVO();
                KsBeanUtil.copyPropertiesThird(goods, goodsVO);
                pointsGoodsVO.setGoods(goodsVO);
            }
            GoodsInfo goodsInfo = pointsGoods.getGoodsInfo();
            if (Objects.nonNull(goodsInfo)) {
                GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
                KsBeanUtil.copyPropertiesThird(goodsInfo, goodsInfoVO);
                StoreVO storeVO = storeQueryProvider.getById(StoreByIdRequest.builder()
                        .storeId(goodsInfo.getStoreId())
                        .build()).getContext().getStoreVO();
                // 店铺名称
                goodsInfoVO.setStoreName(storeVO.getStoreName());
                // 最大可兑换库存
                pointsGoodsVO.setMaxStock(goodsInfo.getStock());
                pointsGoodsVO.setGoodsInfo(goodsInfoVO);
            }
            PointsGoodsStatus pointsGoodsStatus = getPointsGoodsStatus(pointsGoods);
            pointsGoodsVO.setPointsGoodsStatus(pointsGoodsStatus);
            String goodsInfoId = pointsGoods.getGoodsInfoId();
            List<GoodsInfoSpecDetailRel> GoodsInfoSpecDetailRels = goodsInfoSpecDetailRelRepository.findByGoodsInfoId(goodsInfoId);
            pointsGoodsVO.setSpecText(StringUtils.join(GoodsInfoSpecDetailRels.stream()
                    .map(GoodsInfoSpecDetailRel::getDetailName)
                    .collect(Collectors.toList()), " "));
            return pointsGoodsVO;
        }
        return null;
    }

    /**
     * 获取积分商品活动状态
     *
     * @param pointsGoods
     * @return
     */
    public PointsGoodsStatus getPointsGoodsStatus(PointsGoods pointsGoods) {
        if (LocalDateTime.now().isBefore(pointsGoods.getBeginTime())) {
            return PointsGoodsStatus.NOT_START;
        } else if (LocalDateTime.now().isAfter(pointsGoods.getEndTime())) {
            return PointsGoodsStatus.ENDED;
        } else {
            if (pointsGoods.getStatus().equals(EnableStatus.DISABLE)) {
                return PointsGoodsStatus.PAUSED;
            } else {
                return PointsGoodsStatus.STARTED;
            }
        }
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 更新积分商品销量数据
     * @Date 10:38 2019/5/29
     * @Param [pointsGoodsSalesModifyRequest]
     **/
    @Transactional
    public void updatePointsGoodsSalesNum(PointsGoodsSalesModifyRequest pointsGoodsSalesModifyRequest) {
        pointsGoodsRepository.updatePointsGoodsSalesNum(pointsGoodsSalesModifyRequest.getSalesNum(), pointsGoodsSalesModifyRequest.getPointsGoodsId());
    }

    /**
     * 将实体包装成VO
     *
     * @author yang
     */
    public PointsGoodsVO wrapperBaseVo(PointsGoods pointsGoods) {
        if (pointsGoods != null) {
            PointsGoodsVO pointsGoodsVO = new PointsGoodsVO();
            KsBeanUtil.copyPropertiesThird(pointsGoods, pointsGoodsVO);
            PointsGoodsCate pointsGoodsCate = pointsGoods.getPointsGoodsCate();
            if (Objects.nonNull(pointsGoodsCate)) {
                PointsGoodsCateVO pointsGoodsCateVO = new PointsGoodsCateVO();
                KsBeanUtil.copyPropertiesThird(pointsGoodsCate, pointsGoodsCateVO);
                pointsGoodsVO.setPointsGoodsCate(pointsGoodsCateVO);
            }
            Goods goods = pointsGoods.getGoods();
            if (Objects.nonNull(goods)) {
                GoodsVO goodsVO = new GoodsVO();
                KsBeanUtil.copyPropertiesThird(goods, goodsVO);
                pointsGoodsVO.setGoods(goodsVO);
            }
            GoodsInfo goodsInfo = pointsGoods.getGoodsInfo();
            if (Objects.nonNull(goodsInfo)) {
                GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
                KsBeanUtil.copyPropertiesThird(goodsInfo, goodsInfoVO);
//                StoreVO storeVO = storeQueryProvider.getById(StoreByIdRequest.builder()
//                        .storeId(goodsInfo.getStoreId())
//                        .build()).getContext().getStoreVO();
//                // 店铺名称
//                goodsInfoVO.setStoreName(storeVO.getStoreName());
                // 最大可兑换库存
                pointsGoodsVO.setMaxStock(goodsInfo.getStock() + pointsGoods.getStock());
                pointsGoodsVO.setGoodsInfo(goodsInfoVO);
            }
            PointsGoodsStatus pointsGoodsStatus = getPointsGoodsStatus(pointsGoods);
            pointsGoodsVO.setPointsGoodsStatus(pointsGoodsStatus);
            String goodsInfoId = pointsGoods.getGoodsInfoId();
            List<GoodsInfoSpecDetailRel> GoodsInfoSpecDetailRels = goodsInfoSpecDetailRelRepository.findByGoodsInfoId(goodsInfoId);
            pointsGoodsVO.setSpecText(StringUtils.join(GoodsInfoSpecDetailRels.stream()
                    .map(GoodsInfoSpecDetailRel::getDetailName)
                    .collect(Collectors.toList()), " "));
            return pointsGoodsVO;
        }
        return null;
    }
}
