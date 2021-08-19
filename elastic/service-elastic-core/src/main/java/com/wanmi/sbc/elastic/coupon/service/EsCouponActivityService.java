package com.wanmi.sbc.elastic.coupon.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityAddListByActivityIdRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityInitRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponActivityPageRequest;
import com.wanmi.sbc.elastic.bean.constant.coupon.CouponActivityErrorCode;
import com.wanmi.sbc.elastic.bean.dto.coupon.EsCouponActivityDTO;
import com.wanmi.sbc.elastic.coupon.mapper.EsCouponActivityMapper;
import com.wanmi.sbc.elastic.coupon.model.root.EsCouponActivity;
import com.wanmi.sbc.elastic.coupon.repository.EsCouponActivityRepository;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponActivityListPageRequest;
import com.wanmi.sbc.marketing.bean.vo.CouponActivityBaseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 优惠券活动Service
 */
@Slf4j
@Service
public class EsCouponActivityService {

    @Autowired
    private EsCouponActivityRepository esCouponActivityRepository;

    @Autowired
    private EsCouponActivityMapper esCouponActivityMapper;

    @Autowired
    private CouponActivityQueryProvider couponActivityQueryProvider;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 初始化ES数据
     */
    public void init(EsCouponActivityInitRequest esCouponActivityInitRequest){
        Boolean initCouponActivity = Boolean.TRUE;
        int pageNum = esCouponActivityInitRequest.getPageNum();
        Integer pageSize = Objects.equals(10,esCouponActivityInitRequest.getPageSize().intValue()) ? 2000 : esCouponActivityInitRequest.getPageSize();
        CouponActivityListPageRequest request = KsBeanUtil.convert(esCouponActivityInitRequest,CouponActivityListPageRequest.class);
        try {
            while (initCouponActivity) {
                request.putSort("createTime", SortType.DESC.toValue());
                request.setPageNum(pageNum);
                request.setPageSize(pageSize);
                List<CouponActivityBaseVO> couponActivityBaseVOS = couponActivityQueryProvider.listByPage(request).getContext().getCouponActivityBaseVOList();
                if (CollectionUtils.isEmpty(couponActivityBaseVOS)){
                    initCouponActivity = Boolean.FALSE;
                    log.info("==========ES初始化优惠券活动结束，结束pageNum:{}==============",pageNum);
                }else {
                    List<EsCouponActivity> esCouponInfoDTOList = esCouponActivityMapper.couponInfoToEsCouponActivity(couponActivityBaseVOS);
                    this.saveAll(esCouponInfoDTOList);
                    log.info("==========ES初始化优惠券活动成功，当前pageNum:{}==============",pageNum);
                    pageNum++;
                }
            }
        }catch (Exception e){
            log.info("==========ES初始化优惠券活动异常，异常pageNum:{}==============",pageNum);
            throw new SbcRuntimeException(CouponActivityErrorCode.INIT_COUPON_ACTIVITY_FAIL,new Object[]{pageNum});
        }

    }

    /**
     * 保存优惠券活动ES数据
     * @param esCouponInfoDTOList
     * @return
     */
    public Iterable<EsCouponActivity>  saveAll(List<EsCouponActivity> esCouponInfoDTOList){
        //手动删除索引时，重新设置mapping
        if(!elasticsearchTemplate.indexExists(EsCouponActivity.class)){
            elasticsearchTemplate.createIndex(EsCouponActivity.class);
            elasticsearchTemplate.putMapping(EsCouponActivity.class);
        }
        return esCouponActivityRepository.saveAll(esCouponInfoDTOList);
    }



    /**
     * 保存优惠券活动ES数据
     * @param esCouponInfoDTO
     * @return
     */
    public EsCouponActivity save(EsCouponActivityDTO esCouponInfoDTO){
        EsCouponActivity esCouponActivity = esCouponActivityMapper.couponInfoToEsCouponActivity(esCouponInfoDTO);
        return save(esCouponActivity);
    }

    private EsCouponActivity save(EsCouponActivity esCouponActivity) {
        //手动删除索引时，重新设置mapping
        if(!elasticsearchTemplate.indexExists(EsCouponActivity.class)){
            elasticsearchTemplate.createIndex(EsCouponActivity.class);
            elasticsearchTemplate.putMapping(EsCouponActivity.class);
        }
        return esCouponActivityRepository.save(esCouponActivity);
    }

    /**
     * 根据活动ID批量新增ES数据
     * @param request
     * @return
     */
    public void saveAllById(EsCouponActivityAddListByActivityIdRequest request){
        CouponActivityListPageRequest pageRequest = new CouponActivityListPageRequest();
        pageRequest.setActivityIds(request.getActivityIdList());
        List<CouponActivityBaseVO> couponActivityBaseVOS = couponActivityQueryProvider.listByPage(pageRequest).getContext().getCouponActivityBaseVOList();
        if (CollectionUtils.isEmpty(couponActivityBaseVOS)){
            log.info("==========根据活动ID查询不到数据，批量新增ES数据失败，活动ID集合:{}==============",request.getActivityIdList());
           return;
        }

        List<EsCouponActivity> esCouponInfoDTOList = esCouponActivityMapper.couponInfoToEsCouponActivity(couponActivityBaseVOS);
        this.saveAll(esCouponInfoDTOList);
    }

    /**
     * 根据优惠券活动ID删除对应ES数据
     * @param activityId
     */
    public void deleteById(String activityId){
        esCouponActivityRepository.deleteById(activityId);
    }

    /**
     * 开启活动
     * @param activityId
     */
    public void start(String activityId) {
        EsCouponActivity esCouponActivity = esCouponActivityRepository.findById(activityId).orElseThrow(() -> new SbcRuntimeException(CouponActivityErrorCode.NOT_FUND_COUPON_ACTIVITY,new Object[]{activityId}));
        esCouponActivity.setPauseFlag(DefaultFlag.NO);
        save(esCouponActivity);
    }

    /**
     * 暂停活动
     * @param activityId
     */
    public void pause(String activityId) {
        EsCouponActivity esCouponActivity = esCouponActivityRepository.findById(activityId).orElseThrow(() -> new SbcRuntimeException(CouponActivityErrorCode.NOT_FUND_COUPON_ACTIVITY,new Object[]{activityId}));
        esCouponActivity.setPauseFlag(DefaultFlag.YES);
        save(esCouponActivity);
    }


    /**
     * 分页查询ES优惠券活动信息
     * @param request
     * @return
     */
    public Page<EsCouponActivity> page(EsCouponActivityPageRequest request){
        return esCouponActivityRepository.search(request.getSearchQuery());
    }

}
