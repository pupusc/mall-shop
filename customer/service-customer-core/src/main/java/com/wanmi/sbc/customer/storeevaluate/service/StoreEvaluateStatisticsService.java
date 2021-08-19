package com.wanmi.sbc.customer.storeevaluate.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.constant.StoreEvaluateStatisticsDay;
import com.wanmi.sbc.customer.api.request.store.StorePageRequest;
import com.wanmi.sbc.customer.api.request.store.StoreQueryRequest;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluatePageRequest;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateQueryRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumAddRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumAddRequest;
import com.wanmi.sbc.customer.api.response.storeevaluate.StoreEvaluatePageResponse;
import com.wanmi.sbc.customer.api.response.storeevaluatenum.StoreEvaluateNumAddResponse;
import com.wanmi.sbc.customer.api.response.storeevaluatesum.StoreEvaluateSumAddResponse;
import com.wanmi.sbc.customer.bean.enums.EvaluateScoreLevel;
import com.wanmi.sbc.customer.bean.enums.EvaluateStatisticsType;
import com.wanmi.sbc.customer.bean.enums.ScoreCycle;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.customer.store.model.root.Store;
import com.wanmi.sbc.customer.store.service.StoreService;
import com.wanmi.sbc.customer.storeevaluate.model.root.StoreEvaluate;
import com.wanmi.sbc.customer.storeevaluate.repository.StoreEvaluateRepository;
import com.wanmi.sbc.customer.storeevaluatenum.model.root.StoreEvaluateNum;
import com.wanmi.sbc.customer.storeevaluatenum.service.StoreEvaluateNumService;
import com.wanmi.sbc.customer.storeevaluatesum.model.root.StoreEvaluateSum;
import com.wanmi.sbc.customer.storeevaluatesum.service.StoreEvaluateSumService;
import com.wanmi.sbc.setting.api.provider.evaluateratio.EvaluateRatioQueryProvider;
import com.wanmi.sbc.setting.bean.vo.EvaluateRatioVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>商品评价统计业务逻辑</p>
 *
 * @author liutao
 * @date 2019-02-25 15:14:16
 */
@Service("StoreEvaluateStatisticsService")
public class StoreEvaluateStatisticsService {


    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreEvaluateService storeEvaluateService;

    @Autowired
    private EvaluateRatioQueryProvider evaluateRatioQueryProvider;

    @Autowired
    private StoreEvaluateSumService storeEvaluateSumService;

    @Autowired
    private StoreEvaluateNumService storeEvaluateNumService;

    @Autowired
    private StoreEvaluateRepository storeEvaluateRepository;

    /**
     * 商品评价统计 30 90 180天的定时任务
     */
    @Transactional
    public void statistics() {
        //先删除所有表数据
        storeEvaluateSumService.deleteAll();
        storeEvaluateNumService.deleteAll();
        //查询统计30天数据的商家列表
        List<StoreVO> storeVOListForThirtyDay = getStoreList(30L);
        statisticsData(storeVOListForThirtyDay,StoreEvaluateStatisticsDay.THIRTY_DAY);

        //查询统计90天数据的商家列表
        List<StoreVO> storeVOListForNineTyDay = getStoreList(90L);
        statisticsData(storeVOListForNineTyDay,StoreEvaluateStatisticsDay.NINETY_DAY);

        //查询统计30天数据的商家列表
        List<StoreVO> storeVOListOneHundredAndEightyDay = getStoreList(180L);
        statisticsData(storeVOListOneHundredAndEightyDay,StoreEvaluateStatisticsDay.ONE_HUNDRED_AND_EIGHTY_DAY);
    }

    /**
     * 查询统计{days}天数据的商家列表
     * @param days
     * @return
     */
    private List<StoreVO> getStoreList(Long days){
        List<Object> storeMapListForThirtyDay = storeEvaluateRepository.queryStoreInfoByCreatetime(LocalDateTime.of(LocalDate.now(),LocalTime.MIN).minusDays(days));
        List<StoreVO> storeVOList = new ArrayList<>();
        storeMapListForThirtyDay.forEach(o -> {
            Object[] oArr = ((Object[]) o);
            StoreVO storeVO = new StoreVO();
            storeVO.setStoreId((Long)oArr[0]);
            storeVO.setStoreName(oArr[1].toString());
            storeVOList.add(storeVO);
        });
        return storeVOList;
    }

    /**
     * 统计对应数据
     * @param storeList
     */
    public void statisticsData( List<StoreVO> storeList,int storeEvaluateStatisticsDay){
        if (CollectionUtils.isEmpty(storeList)) {
            return;
        }
        EvaluateRatioVO evaluateRatioVO = evaluateRatioQueryProvider.findOne().getContext().getEvaluateRatioVO();
        //2得到之前30天的起始日期 90天的起始日期 180天的起始日期的商家评价信息
        List<StoreEvaluateSumAddRequest> storeEvaluateSumAddRequests = new ArrayList<>();
        //30天 90天 180天三种时间段的商家评分等级数集合
        List<StoreEvaluateNumAddRequest> storeEvaluateNumAddRequests = new ArrayList<>();
        storeList.stream().forEach(store -> {
            //查询满足条件的店铺评价数据
            StoreEvaluatePageRequest storeEvaluatePageRequest = new StoreEvaluatePageRequest();
            storeEvaluatePageRequest.setStoreId(store.getStoreId());
            storeEvaluatePageRequest.setDelFlag(DeleteFlag.NO.toValue());
            storeEvaluatePageRequest.setCreateTimeEnd(DateUtil.parseDay(this.getBeforeDateTimeStr(StoreEvaluateStatisticsDay.INT_ZERO)));
            //每次批量100条数据
            storeEvaluatePageRequest.setPageSize(200);
            //初始化
            storeEvaluatePageRequest.setPageNum(0);
            //查询所有对应天数店铺评价数据
            storeEvaluatePageRequest.setCreateTimeBegin(DateUtil.parseDay(this.getBeforeDateTimeStr(storeEvaluateStatisticsDay)));
            List<StoreEvaluateVO> responseOne = batchGetStoreEvaluateInfo(storeEvaluatePageRequest);
            ScoreCycle scoreCycle = ScoreCycle.THIRTY;
            if(storeEvaluateStatisticsDay == StoreEvaluateStatisticsDay.NINETY_DAY){
                scoreCycle = ScoreCycle.NINETY;
            } else if(storeEvaluateStatisticsDay == StoreEvaluateStatisticsDay.ONE_HUNDRED_AND_EIGHTY_DAY){
                scoreCycle = ScoreCycle.ONE_HUNDRED_AND_EIGHTY;
            }
            addStoreEvaluateSumRequests(this.dealStoreEvaluates(responseOne, evaluateRatioVO, store,scoreCycle), storeEvaluateSumAddRequests);
            addStoreEvaluateNumRequests(this.dealStoreEvaluatesNum(responseOne, store, scoreCycle), storeEvaluateNumAddRequests);
        });
        if (CollectionUtils.isEmpty(storeEvaluateSumAddRequests)) {
            return;
        }
        this.addSumList(storeEvaluateSumAddRequests);
        this.addNumList(storeEvaluateNumAddRequests);
    }

    /**
     * 增加所有统计数据
     *
     * @param storeEvaluateSumAddRequestList
     * @return
     */
    @Transactional
    public BaseResponse<StoreEvaluateSumAddResponse> addSumList( List<StoreEvaluateSumAddRequest> storeEvaluateSumAddRequestList) {
        if (CollectionUtils.isEmpty(storeEvaluateSumAddRequestList)){
            return BaseResponse.SUCCESSFUL();
        }
        storeEvaluateSumAddRequestList.stream().forEach(storeEvaluateSumAddRequest -> {
            StoreEvaluateSum storeEvaluateSum = new StoreEvaluateSum();
            KsBeanUtil.copyPropertiesThird(storeEvaluateSumAddRequest, storeEvaluateSum);
            storeEvaluateSumService.add(storeEvaluateSum);
        });
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 增加所有统计次数数据
     *
     * @param storeEvaluateNumAddRequestList
     * @return
     */
    @Transactional
    public BaseResponse<StoreEvaluateNumAddResponse> addNumList(List<StoreEvaluateNumAddRequest> storeEvaluateNumAddRequestList) {
        if (CollectionUtils.isEmpty(storeEvaluateNumAddRequestList)){
            return BaseResponse.SUCCESSFUL();
        }
        storeEvaluateNumAddRequestList.stream().forEach(storeEvaluateNumAddRequest -> {
            StoreEvaluateNum storeEvaluateNum = new StoreEvaluateNum();
            KsBeanUtil.copyPropertiesThird(storeEvaluateNumAddRequest, storeEvaluateNum);
            storeEvaluateNumService.add(storeEvaluateNum);
        });
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 批量递归查询商家信息
     *
     * @param storePageRequest
     * @return
     */
    @Transactional
    public Page<Store> batchGetStoreInfo(StorePageRequest storePageRequest) {
        StoreQueryRequest request = new StoreQueryRequest();
        KsBeanUtil.copyPropertiesThird(storePageRequest, request);
        request.setPageNum(storePageRequest.getPageNum());
        request.setPageSize(storePageRequest.getPageSize());
        Page<Store> storePage = storeService.page(request);
        return storePage;
    }

    /**
     * 批量递归查询商家评论信息
     *
     * @param storeEvaluatePageRequest
     * @return
     */
    @Transactional
    public List<StoreEvaluateVO> batchGetStoreEvaluateInfo(StoreEvaluatePageRequest storeEvaluatePageRequest) {
        List<StoreEvaluateVO> storeEvaluateVOList = new ArrayList<>();
        StoreEvaluateQueryRequest queryReq = new StoreEvaluateQueryRequest();
        KsBeanUtil.copyPropertiesThird(storeEvaluatePageRequest, queryReq);
        Page<StoreEvaluate> storeEvaluatePage = storeEvaluateService.page(queryReq);
        Page<StoreEvaluateVO> newPage = storeEvaluatePage.map(entity -> storeEvaluateService.wrapperVo(entity));
        MicroServicePage<StoreEvaluateVO> microPage = new MicroServicePage<>(newPage, storeEvaluatePageRequest.getPageable());
        StoreEvaluatePageResponse finalRes = new StoreEvaluatePageResponse(microPage);
        if (finalRes != null && finalRes.getStoreEvaluateVOPage() != null && CollectionUtils.isNotEmpty(finalRes.getStoreEvaluateVOPage().getContent())) {
            storeEvaluatePageRequest.setPageNum(storeEvaluatePageRequest.getPageNum() + 1);
            storeEvaluateVOList.addAll(finalRes.getStoreEvaluateVOPage().getContent());
            storeEvaluateVOList.addAll(this.batchGetStoreEvaluateInfo(storeEvaluatePageRequest));
        }
        return storeEvaluateVOList;
    }

    /**
     * 店铺评分聚合表数据
     *
     * @param storeEvaluateSumAddRequest
     * @param storeEvaluateSumAddRequests
     */
    @Transactional
    public void addStoreEvaluateSumRequests(StoreEvaluateSumAddRequest storeEvaluateSumAddRequest, List<StoreEvaluateSumAddRequest> storeEvaluateSumAddRequests) {
        if (storeEvaluateSumAddRequest == null) {
            return;
        }
        storeEvaluateSumAddRequests.add(storeEvaluateSumAddRequest);
    }

    /**
     * 店铺评价数统计表
     *
     * @param storeEvaluateNumAddRequests
     * @param storeEvaluateNumAddRequestList
     */
    @Transactional
    public void addStoreEvaluateNumRequests(List<StoreEvaluateNumAddRequest> storeEvaluateNumAddRequests, List<StoreEvaluateNumAddRequest> storeEvaluateNumAddRequestList) {
        if (CollectionUtils.isEmpty(storeEvaluateNumAddRequests)) {
            return;
        }
        storeEvaluateNumAddRequestList.addAll(storeEvaluateNumAddRequests);
    }


    /**
     * 处理30天数据
     *
     * @param storeEvaluateList
     * @param evaluateRatioVO
     * @return
     */
    @Transactional
    public StoreEvaluateSumAddRequest dealStoreEvaluates(List<StoreEvaluateVO> storeEvaluateList, EvaluateRatioVO evaluateRatioVO, StoreVO storeVO,ScoreCycle scoreCycle) {
        StoreEvaluateSumAddRequest storeEvaluateSumAddRequest = this.dealStoreEvaluates(storeEvaluateList, evaluateRatioVO, storeVO);
        if (null == storeEvaluateSumAddRequest) {
            return null;
        }
        storeEvaluateSumAddRequest.setScoreCycle(scoreCycle.toValue());
        return storeEvaluateSumAddRequest;
    }


    /**
     * 处理90天数据
     *
     * @param storeEvaluateList
     * @param evaluateRatioVO
     * @return
     */
    @Transactional
    public StoreEvaluateSumAddRequest dealNinetyDayStoreEvaluates(List<StoreEvaluateVO> storeEvaluateList, EvaluateRatioVO evaluateRatioVO, StoreVO storeVO) {
        StoreEvaluateSumAddRequest storeEvaluateSumAddRequest = this.dealStoreEvaluates(storeEvaluateList, evaluateRatioVO, storeVO);
        if (null == storeEvaluateSumAddRequest) {
            return null;
        }
        storeEvaluateSumAddRequest.setScoreCycle(ScoreCycle.NINETY.toValue());
        return storeEvaluateSumAddRequest;
    }

    /**
     * 处理180天数据
     *
     * @param storeEvaluateList
     * @param evaluateRatioVO
     * @return
     */
    @Transactional
    public StoreEvaluateSumAddRequest dealOneHundredAndEightyDayStoreEvaluates(List<StoreEvaluateVO> storeEvaluateList, EvaluateRatioVO evaluateRatioVO, StoreVO storeVO) {
        StoreEvaluateSumAddRequest storeEvaluateSumAddRequest = this.dealStoreEvaluates(storeEvaluateList, evaluateRatioVO, storeVO);
        if (null == storeEvaluateSumAddRequest) {
            return null;
        }
        storeEvaluateSumAddRequest.setScoreCycle(ScoreCycle.ONE_HUNDRED_AND_EIGHTY.toValue());
        return storeEvaluateSumAddRequest;
    }

    /**
     * 处理30天统计评分人数数据
     *
     * @param storeEvaluateList
     * @param storeVO
     * @return
     */
    @Transactional
    public List<StoreEvaluateNumAddRequest> dealStoreEvaluatesNum(List<StoreEvaluateVO> storeEvaluateList, StoreVO storeVO,ScoreCycle scoreCycle) {
        List<StoreEvaluateNumAddRequest> storeEvaluateNumAddRequests = this.dealStoreEvaluatesNum(storeEvaluateList);
        if (CollectionUtils.isEmpty(storeEvaluateNumAddRequests)) {
            return null;
        }
        storeEvaluateNumAddRequests.stream().forEach(storeEvaluateNumAddRequest -> {
            storeEvaluateNumAddRequest.setScoreCycle(scoreCycle.toValue());
            storeEvaluateNumAddRequest.setStoreId(storeVO.getStoreId());
            storeEvaluateNumAddRequest.setStoreName(storeVO.getStoreName());
        });
        return storeEvaluateNumAddRequests;
    }


    /**
     * 处理90天统计评分人数数据
     *
     * @param storeEvaluateList
     * @param storeVO
     * @return
     */
    @Transactional
    public List<StoreEvaluateNumAddRequest> dealNinetyDayStoreEvaluatesNum(List<StoreEvaluateVO> storeEvaluateList, StoreVO storeVO) {
        List<StoreEvaluateNumAddRequest> storeEvaluateNumAddRequests = this.dealStoreEvaluatesNum(storeEvaluateList);
        if (CollectionUtils.isEmpty(storeEvaluateNumAddRequests)) {
            return null;
        }
        storeEvaluateNumAddRequests.stream().forEach(storeEvaluateNumAddRequest -> {
            storeEvaluateNumAddRequest.setScoreCycle(ScoreCycle.NINETY.toValue());
            storeEvaluateNumAddRequest.setStoreId(storeVO.getStoreId());
            storeEvaluateNumAddRequest.setStoreName(storeVO.getStoreName());
        });
        return storeEvaluateNumAddRequests;
    }

    /**
     * 处理180天统计评分人数数据
     *
     * @param storeEvaluateList
     * @param storeVO
     * @return
     */
    @Transactional
    public List<StoreEvaluateNumAddRequest> dealOneHundredAndEightyDayStoreEvaluatesNum(List<StoreEvaluateVO> storeEvaluateList, StoreVO storeVO) {
        List<StoreEvaluateNumAddRequest> storeEvaluateNumAddRequests = this.dealStoreEvaluatesNum(storeEvaluateList);
        if (CollectionUtils.isEmpty(storeEvaluateNumAddRequests)) {
            return null;
        }
        storeEvaluateNumAddRequests.stream().forEach(storeEvaluateNumAddRequest -> {
            storeEvaluateNumAddRequest.setScoreCycle(ScoreCycle.ONE_HUNDRED_AND_EIGHTY.toValue());
            storeEvaluateNumAddRequest.setStoreId(storeVO.getStoreId());
            storeEvaluateNumAddRequest.setStoreName(storeVO.getStoreName());
        });
        return storeEvaluateNumAddRequests;
    }

    /**
     * 处理店铺评价数据
     *
     * @param storeEvaluateList
     * @param evaluateRatioVO
     * @return
     */
    @Transactional
    public StoreEvaluateSumAddRequest dealStoreEvaluates(List<StoreEvaluateVO> storeEvaluateList, EvaluateRatioVO evaluateRatioVO, StoreVO storeVO) {
        StoreEvaluateSumAddRequest storeEvaluateSumAddRequest = new StoreEvaluateSumAddRequest();

        //判断是否有值
        if (CollectionUtils.isEmpty(storeEvaluateList)) {
            return null;
        }

        //商品总评分
        BigDecimal goodsScoreSum = storeEvaluateList.stream().map(storeEvaluateVO -> new BigDecimal(storeEvaluateVO.getGoodsScore()))
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
        //服务总评分
        BigDecimal serverScoreSum = storeEvaluateList.stream().map(storeEvaluateVO -> new BigDecimal(storeEvaluateVO.getServerScore()))
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
        //物流总评分
        BigDecimal logisticsScoreSum = storeEvaluateList.stream().map(storeEvaluateVO -> new BigDecimal(storeEvaluateVO.getLogisticsScore()))
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
        //订单总数（一条订单一条数据）
        Integer orderNum = storeEvaluateList.size();

        //组装数据
        storeEvaluateSumAddRequest.setOrderNum(orderNum);
        storeEvaluateSumAddRequest.setStoreId(storeVO.getStoreId());
        storeEvaluateSumAddRequest.setStoreName(storeVO.getStoreName());
        //服务综合评分
        BigDecimal serverScore = serverScoreSum
                .divide(new BigDecimal(orderNum), 2, BigDecimal.ROUND_DOWN);
        //商品质量综合评分
        BigDecimal goodsScore = goodsScoreSum.divide(new BigDecimal(orderNum), 2, BigDecimal.ROUND_DOWN);
        //物流综合评分
        BigDecimal logisticsScoreScore = logisticsScoreSum.divide(new BigDecimal(orderNum), 2, BigDecimal.ROUND_DOWN);
        storeEvaluateSumAddRequest.setSumServerScore(serverScore);
        storeEvaluateSumAddRequest.setSumGoodsScore(goodsScore);
        storeEvaluateSumAddRequest.setSumLogisticsScoreScore(logisticsScoreScore);

        BigDecimal sumCompositeScore = logisticsScoreScore.multiply(evaluateRatioVO.getLogisticsRatio()).setScale(2, BigDecimal.ROUND_DOWN)
                .add(goodsScore.multiply(evaluateRatioVO.getGoodsRatio()).setScale(2, BigDecimal.ROUND_DOWN))
                .add(serverScore.multiply(evaluateRatioVO.getServerRatio()).setScale(2, BigDecimal.ROUND_DOWN));
        storeEvaluateSumAddRequest.setSumCompositeScore(sumCompositeScore);
        return storeEvaluateSumAddRequest;
    }

    /**
     * 处理店铺评价等级数
     *
     * @param storeEvaluateList
     * @return
     */
    @Transactional
    public List<StoreEvaluateNumAddRequest> dealStoreEvaluatesNum(List<StoreEvaluateVO> storeEvaluateList) {
        List<StoreEvaluateNumAddRequest> storeEvaluateNumAddRequestList = new ArrayList<>();

        //判断是否有值
        if (CollectionUtils.isEmpty(storeEvaluateList)) {
            return null;
        }

        StoreEvaluateNumAddRequest storeGoodsEvaluateNum = new StoreEvaluateNumAddRequest();
        StoreEvaluateNumAddRequest storeServerEvaluateNum = new StoreEvaluateNumAddRequest();
        StoreEvaluateNumAddRequest storeLogisticsEvaluateNum = new StoreEvaluateNumAddRequest();
        //好评人数
        //商品评价好评人数
        storeGoodsEvaluateNum.setExcellentNum(getGoodsEvaluateNumByScore(EvaluateScoreLevel.FIVE.toValue(), storeEvaluateList) + getGoodsEvaluateNumByScore(EvaluateScoreLevel.FOUR.toValue(), storeEvaluateList));
        //服务评价好评人数
        storeServerEvaluateNum.setExcellentNum(getServerEvaluateNumByScore(EvaluateScoreLevel.FIVE.toValue(), storeEvaluateList) + getServerEvaluateNumByScore(EvaluateScoreLevel.FOUR.toValue(), storeEvaluateList));
        //物流评价好评人数
        storeLogisticsEvaluateNum.setExcellentNum(getLogisticsEvaluateNumByScore(EvaluateScoreLevel.FIVE.toValue(), storeEvaluateList) + getLogisticsEvaluateNumByScore(EvaluateScoreLevel.FOUR.toValue(), storeEvaluateList));
        //中评人数
        //商品评价中评人数
        storeGoodsEvaluateNum.setMediumNum(getGoodsEvaluateNumByScore(EvaluateScoreLevel.THREE.toValue(), storeEvaluateList));
        //服务评价中评人数
        storeServerEvaluateNum.setMediumNum(getServerEvaluateNumByScore(EvaluateScoreLevel.THREE.toValue(), storeEvaluateList));
        //物流评价中评人数
        storeLogisticsEvaluateNum.setMediumNum(getLogisticsEvaluateNumByScore(EvaluateScoreLevel.THREE.toValue(), storeEvaluateList));
        //差评人数
        //商品评价差评人数
        storeGoodsEvaluateNum.setDifferenceNum(getGoodsEvaluateNumByScore(EvaluateScoreLevel.ONE.toValue(), storeEvaluateList) + getGoodsEvaluateNumByScore(EvaluateScoreLevel.TWO.toValue(), storeEvaluateList));
        //服务评价差评人数
        storeServerEvaluateNum.setDifferenceNum(getServerEvaluateNumByScore(EvaluateScoreLevel.ONE.toValue(), storeEvaluateList) + getServerEvaluateNumByScore(EvaluateScoreLevel.TWO.toValue(), storeEvaluateList));
        //物流评价差评人数
        storeLogisticsEvaluateNum.setDifferenceNum(getLogisticsEvaluateNumByScore(EvaluateScoreLevel.ONE.toValue(), storeEvaluateList) + getLogisticsEvaluateNumByScore(EvaluateScoreLevel.TWO.toValue(), storeEvaluateList));

        //商品总评分
        storeGoodsEvaluateNum.setSumCompositeScore(getGoodsEvaluateCompositeScoreByScore(storeEvaluateList));
        //服务总评分
        storeServerEvaluateNum.setSumCompositeScore(getServerEvaluateCompositeScoreByScore(storeEvaluateList));
        //物流总评分
        storeLogisticsEvaluateNum.setSumCompositeScore(getLogisticsEvaluateCompositeScoreByScore(storeEvaluateList));

        storeGoodsEvaluateNum.setNumType(EvaluateStatisticsType.GOODS.toValue());
        storeServerEvaluateNum.setNumType(EvaluateStatisticsType.SERVER.toValue());
        storeLogisticsEvaluateNum.setNumType(EvaluateStatisticsType.LOGISTICS.toValue());

        storeEvaluateNumAddRequestList.add(storeGoodsEvaluateNum);
        storeEvaluateNumAddRequestList.add(storeServerEvaluateNum);
        storeEvaluateNumAddRequestList.add(storeLogisticsEvaluateNum);
        return storeEvaluateNumAddRequestList;
    }

    /**
     * 根据评分查询商品评价人数
     *
     * @param score
     * @param storeEvaluateList
     * @return
     */
    @Transactional
    public Long getGoodsEvaluateNumByScore(Integer score, List<StoreEvaluateVO> storeEvaluateList) {
        if (CollectionUtils.isEmpty(storeEvaluateList)) {
            return null;
        }
        //商品评价人数
        return storeEvaluateList.stream()
                .filter(storeEvaluateVO -> storeEvaluateVO.getGoodsScore().equals(score))
                .map(storeEvaluateVO -> 1L).reduce(0L, (x, y) -> x + y);
    }

    /**
     * 根据评分查询服务评价人数
     *
     * @param score
     * @param storeEvaluateList
     * @return
     */
    @Transactional
    public Long getServerEvaluateNumByScore(Integer score, List<StoreEvaluateVO> storeEvaluateList) {
        if (CollectionUtils.isEmpty(storeEvaluateList)) {
            return null;
        }
        //服务评价人数
        return storeEvaluateList.stream()
                .filter(storeEvaluateVO -> storeEvaluateVO.getServerScore().equals(score))
                .map(storeEvaluateVO -> 1L).reduce(0L, (x, y) -> x + y);
    }

    /**
     * 根据评分查询物流评价人数
     *
     * @param score
     * @param storeEvaluateList
     * @return
     */
    @Transactional
    public Long getLogisticsEvaluateNumByScore(Integer score, List<StoreEvaluateVO> storeEvaluateList) {
        if (CollectionUtils.isEmpty(storeEvaluateList)) {
            return null;
        }
        //物流评价人数
        return storeEvaluateList.stream()
                .filter(storeEvaluateVO -> storeEvaluateVO.getLogisticsScore().equals(score))
                .map(storeEvaluateVO -> 1L).reduce(0L, (x, y) -> x + y);
    }

    /**
     * 查询服务评价平均分
     *
     * @param storeEvaluateList
     * @return
     */
    @Transactional
    public BigDecimal getServerEvaluateCompositeScoreByScore(List<StoreEvaluateVO> storeEvaluateList) {
        //判断是否有值
        if (CollectionUtils.isEmpty(storeEvaluateList)) {
            return null;
        }

        //服务总评分
        BigDecimal serverScoreSum = storeEvaluateList.stream().map(storeEvaluateVO -> new BigDecimal(storeEvaluateVO.getServerScore()))
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
        //订单总数（一条订单一条数据）
        Integer orderNum = storeEvaluateList.size();
        //服务质量综合评分
        BigDecimal serverScore = serverScoreSum.divide(new BigDecimal(orderNum), 2, BigDecimal.ROUND_DOWN);
        return serverScore;
    }

    /**
     * 查询商品评价平均分
     *
     * @param storeEvaluateList
     * @return
     */
    @Transactional
    public BigDecimal getGoodsEvaluateCompositeScoreByScore(List<StoreEvaluateVO> storeEvaluateList) {
        //判断是否有值
        if (CollectionUtils.isEmpty(storeEvaluateList)) {
            return null;
        }

        //商品总评分
        BigDecimal goodsScoreSum = storeEvaluateList.stream().map(storeEvaluateVO -> new BigDecimal(storeEvaluateVO.getGoodsScore()))
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
        //订单总数（一条订单一条数据）
        Integer orderNum = storeEvaluateList.size();
        //商品质量综合评分
        BigDecimal goodsScore = goodsScoreSum.divide(new BigDecimal(orderNum), 2, BigDecimal.ROUND_DOWN);
        return goodsScore;
    }

    /**
     * 查询物流评价平均分
     *
     * @param storeEvaluateList
     * @return
     */
    @Transactional
    public BigDecimal getLogisticsEvaluateCompositeScoreByScore(List<StoreEvaluateVO> storeEvaluateList) {
        //判断是否有值
        if (CollectionUtils.isEmpty(storeEvaluateList)) {
            return null;
        }

        //物流总评分
        BigDecimal logisticsScoreSum = storeEvaluateList.stream().map(storeEvaluateVO -> new BigDecimal(storeEvaluateVO.getLogisticsScore()))
                .reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
        //订单总数（一条订单一条数据）
        Integer orderNum = storeEvaluateList.size();
        //物流质量综合评分
        BigDecimal logisticsScore = logisticsScoreSum.divide(new BigDecimal(orderNum), 2, BigDecimal.ROUND_DOWN);
        return logisticsScore;
    }

    /**
     * 获得之前或者之后日期
     *
     * @param day 正数表示之后的天数 负数表示之前的天数
     * @return 返回的是需要的到之前或者之后的天数的日期
     */
    private String getBeforeDateTimeStr(Integer day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //当前日期
        Date today = new Date();
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(today);
        theCa.add(theCa.DATE, day);
        Date start = theCa.getTime();
        //三十天之前日期
        String startDate = sdf.format(start);
        return startDate;
    }
}
