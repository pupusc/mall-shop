package com.wanmi.sbc.goods.appointmentsale.service;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelListByCustomerLevelNameRequest;
import com.wanmi.sbc.customer.bean.dto.MarketingCustomerLevelDTO;
import com.wanmi.sbc.customer.bean.vo.MarketingCustomerLevelVO;
import com.wanmi.sbc.goods.api.constant.AppointmentAndBookingSaleErrorCode;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleQueryRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSale;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSaleDO;
import com.wanmi.sbc.goods.appointmentsale.repository.AppointmentSaleRepository;
import com.wanmi.sbc.goods.appointmentsalegoods.model.root.AppointmentSaleGoods;
import com.wanmi.sbc.goods.appointmentsalegoods.model.root.AppointmentSaleGoodsDO;
import com.wanmi.sbc.goods.appointmentsalegoods.repository.AppointmentSaleGoodsRepository;
import com.wanmi.sbc.goods.appointmentsalegoods.service.AppointmentSaleGoodsService;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bookingsalegoods.model.root.BookingSaleGoods;
import com.wanmi.sbc.goods.bookingsalegoods.repository.BookingSaleGoodsRepository;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>预约抢购业务逻辑</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@Service("AppointmentSaleService")
public class AppointmentSaleService {
    @Autowired
    private AppointmentSaleRepository appointmentSaleRepository;

    @Autowired
    private AppointmentSaleGoodsRepository appointmentSaleGoodsRepository;

    @Autowired
    private AppointmentSaleGoodsService appointmentSaleGoodsService;

    @Autowired
    private BookingSaleGoodsRepository bookingSaleGoodsRepository;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;


    /**
     * 新增预约抢购
     *
     * @author zxd
     */
    @Transactional
    public AppointmentSale add(AppointmentSale request) {
        List<AppointmentSaleGoods> appointmentSaleGoodsList = request.getAppointmentSaleGoods();
        validateAppointmentPermission(request);

        // 同一商品同一时间只能参加一个预约购买或者预售活动
        //活动冲突的商品id
        List<String> clashGoodsInfoIdList = this.validateBookingActivityClash(request);
        List<AppointmentSaleGoods> haveAppointmentSaleGoods = appointmentSaleGoodsService.list(AppointmentSaleGoodsQueryRequest.builder().
                goodsInfoIdList(request.getAppointmentSaleGoods().stream().map(AppointmentSaleGoods::getGoodsInfoId).collect(Collectors.toList())).build());
        if (CollectionUtils.isNotEmpty(haveAppointmentSaleGoods)) {

            List<AppointmentSale> appointmentSales = list(AppointmentSaleQueryRequest.builder().idList(haveAppointmentSaleGoods.stream().
                    collect(Collectors.groupingBy(AppointmentSaleGoods::getAppointmentSaleId)).
                    keySet().stream().collect(Collectors.toList())).delFlag(DeleteFlag.NO).build()).stream().filter(a -> !validateAppointmentSaleRepeat(a, request)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(appointmentSales)) {
                // 活动重合,给前端返回导致活动冲突的商品id
                List<AppointmentSaleGoods> saleGoodsList = appointmentSaleGoodsService.list(AppointmentSaleGoodsQueryRequest.builder()
                        .appointmentSaleIdList(appointmentSales.stream().map(AppointmentSale::getId).collect(Collectors.toList()))
                        .build());
                List<String> goodsInfoIdList = saleGoodsList.stream().map(AppointmentSaleGoods::getGoodsInfoId).collect(Collectors.toList());
                List<String> clashGoodsInfoIds = appointmentSaleGoodsList.stream()
                        .filter(saleGoods -> goodsInfoIdList.contains(saleGoods.getGoodsInfoId()))
                        .map(AppointmentSaleGoods::getGoodsInfoId)
                        .collect(Collectors.toList());
                clashGoodsInfoIdList.addAll(clashGoodsInfoIds);
            }
        }
        if (CollectionUtils.isNotEmpty(clashGoodsInfoIdList)){
            throw new SbcRuntimeException("K-600005", new Object[]{clashGoodsInfoIdList.size()}, clashGoodsInfoIdList);
        }
        AppointmentSale appointmentSale = appointmentSaleRepository.save(request);

        List<AppointmentSaleGoods> saleGoods = request.getAppointmentSaleGoods();

        saleGoods.stream().forEach(k -> {
            k.setAppointmentSaleId(appointmentSale.getId());
            k.setStoreId(appointmentSale.getStoreId());
            k.setCreatePerson(appointmentSale.getCreatePerson());
        });

        appointmentSaleGoodsRepository.saveAll(saleGoods);
        appointmentSale.setAppointmentSaleGoods(saleGoods);
        return appointmentSale;
    }

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;
    private GoodsInfoService goodsInfoService;


    /**
     * 校验是否存在预售活动
     *
     * @param appointmentSale
     * @return
     */
    private boolean validateBookingActivityRepeat(AppointmentSale appointmentSale) {
        List<String> goodsInfoIds = appointmentSale.getAppointmentSaleGoods().stream().map(AppointmentSaleGoods::getGoodsInfoId).collect(Collectors.toList());
        LocalDateTime startTime = appointmentSale.getAppointmentStartTime();
        LocalDateTime endTime = appointmentSale.getSnapUpEndTime();
        List list = bookingSaleGoodsRepository.existBookingActivity(goodsInfoIds, startTime, endTime);
        if (CollectionUtils.isNotEmpty(list)) {
            return true;
        }
        return false;
    }

    /**
     * 校验是否存在预售活动
     *
     * @param appointmentSale
     * @return
     */
    private List<String> validateBookingActivityClash(AppointmentSale appointmentSale) {
        List<String> goodsInfoIds = appointmentSale.getAppointmentSaleGoods().stream().map(AppointmentSaleGoods::getGoodsInfoId).collect(Collectors.toList());
        LocalDateTime startTime = appointmentSale.getAppointmentStartTime();
        LocalDateTime endTime = appointmentSale.getSnapUpEndTime();
        List list = bookingSaleGoodsRepository.existBookingActivity(goodsInfoIds, startTime, endTime);
        List<String> goodsInfoIdList = new ArrayList<>();
        list.forEach(o -> {
            Object[] objects = (Object[]) o;
            BookingSaleGoods bookingSaleGoods = (KsBeanUtil.convert(objects[1], BookingSaleGoods.class));
            goodsInfoIdList.add(bookingSaleGoods.getGoodsInfoId());
        });
        return goodsInfoIdList;
    }


    /**
     * 活动满足条件校验
     *
     * @param request
     */
    private void validateAppointmentPermission(AppointmentSale request) {
        if (CollectionUtils.isEmpty(request.getAppointmentSaleGoods())) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        List<GoodsInfo> goodsInfoList = goodsInfoRepository.findByGoodsInfoIds(
                request.getAppointmentSaleGoods().stream().map(AppointmentSaleGoods::getGoodsInfoId).collect(Collectors.toList())
        );
        if (CollectionUtils.isEmpty(goodsInfoList) || goodsInfoList.size() != request.getAppointmentSaleGoods().size()) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<GoodsInfo> exceptionGoodsInfo = goodsInfoList.stream().filter(g -> Objects.isNull(g.getStoreId()) || !g.getStoreId().equals(request.getStoreId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(exceptionGoodsInfo)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 预约类型：必选，单选，默认选中不预约不可购买
        if (Objects.isNull(request.getJoinLevelType())) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        //预约时间（开始时间/结束时间）：必选，精确到时，分/秒数位自动补齐为00，不点击选择时间时，起止时间自动取00:00:00；
        //     开始时间（的日期）不可早于当前时间（的日期），点击日期弹窗的确定时校验，如有错误，需高亮日期选择框并在选择框下方提示：开始时间不可早于当前时间
        //     结束时间不可早于开始时间
        //     抢购时间的开始时间不可早于预约时间的结束时间(可以等于)
        if (Objects.isNull(request.getAppointmentStartTime()) || Objects.isNull(request.getAppointmentEndTime())) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }

        /*DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00");
        request.setAppointmentStartTime(LocalDateTime.parse(dateTimeFormatter.format(request.getAppointmentStartTime()), DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1)));
        request.setAppointmentEndTime(LocalDateTime.parse(dateTimeFormatter.format(request.getAppointmentEndTime()), DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1)));*/
        if (request.getAppointmentStartTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-600001");
        }
        if (request.getAppointmentStartTime().isAfter(request.getAppointmentEndTime())) {
            throw new SbcRuntimeException("K-600002");
        }

        //抢购时间（开始时间/结束时间）：必选，精确到时，分/秒数位自动补齐为00，不点击选择时间时，起止时间自动取00:00:00；
        //     开始时间（的日期）不可早于当前时间（的日期），点击日期弹窗的确定时校验，如有错误，需高亮日期选择框并在选择框下方提示：开始时间不可早于当前时间
        //     结束时间不可早于开始时间
        //     抢购时间的开始时间不可早于预约时间的结束时间(可以等于)

        if (Objects.isNull(request.getSnapUpStartTime()) || Objects.isNull(request.getSnapUpEndTime())) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        /*request.setSnapUpStartTime(LocalDateTime.parse(dateTimeFormatter.format(request.getSnapUpStartTime()), DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1)));
        request.setSnapUpEndTime(LocalDateTime.parse(dateTimeFormatter.format(request.getSnapUpEndTime()), DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1)));*/
        if (request.getSnapUpStartTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-600001");
        }
        if (request.getSnapUpStartTime().isAfter(request.getSnapUpEndTime())) {
            throw new SbcRuntimeException("K-600002");
        }
        if (request.getAppointmentEndTime().compareTo(request.getSnapUpStartTime()) > 0) {
            throw new SbcRuntimeException("K-600003");
        }

        //发货时间：必选，精确到天，发货时间不可早于抢购开始时间(可以等于)
        if (Objects.isNull(request.getDeliverTime())) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        if (LocalDateTime.parse(request.getDeliverTime() + " 00:00:00", DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1)).compareTo(request.getSnapUpStartTime()) < 0) {
            throw new SbcRuntimeException("K-600004");
        }


        // 预约价：非必填，仅限0-9999999.99间的数字，不填写时以市场价销售
        if (CollectionUtils.isNotEmpty(request.getAppointmentSaleGoods().stream().filter(g -> Objects.nonNull(g.getPrice()) && g.getPrice().compareTo(new BigDecimal("9999999.99")) > 0).collect(Collectors.toList()))) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }

        // 同一商品同一时间只能参加一个预约购买或者预售活动
//        if (validateBookingActivityRepeat(request)) {
//            throw new SbcRuntimeException("K-600005");
//        }
    }


    /**
     * 校验同一商品同一时间只能参加一个预约购买或者预售活动
     *
     * @param oldAppointmentSale 持有
     * @param newAppointmentSale 新增
     * @return
     */
    private boolean validateAppointmentSaleRepeat(AppointmentSale oldAppointmentSale, AppointmentSale newAppointmentSale) {

        if (newAppointmentSale.getAppointmentStartTime().compareTo(oldAppointmentSale.getAppointmentStartTime()) >= 0
                && newAppointmentSale.getAppointmentStartTime().compareTo(oldAppointmentSale.getSnapUpEndTime()) <= 0) {
            return false;
        }

        if (newAppointmentSale.getSnapUpEndTime().compareTo(oldAppointmentSale.getAppointmentStartTime()) >= 0
                && newAppointmentSale.getSnapUpEndTime().compareTo(oldAppointmentSale.getSnapUpEndTime()) <= 0) {
            return false;
        }

        if (newAppointmentSale.getAppointmentStartTime().compareTo(oldAppointmentSale.getAppointmentStartTime()) <= 0
                && newAppointmentSale.getSnapUpEndTime().compareTo(oldAppointmentSale.getSnapUpEndTime()) >= 0) {
            return false;
        }
        return true;
    }

    /**
     * 修改预约抢购
     *
     * @author zxd
     */
    @Transactional
    public AppointmentSale modify(AppointmentSale sale) {
        AppointmentSale appointmentSale = getOne(sale.getId(), sale.getStoreId());

        if (Objects.isNull(appointmentSale)) {
            throw new SbcRuntimeException("k-000001");
        }
        if (appointmentSale.getAppointmentStartTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-600007");
        }
        if (!sale.getStoreId().equals(appointmentSale.getStoreId())) {
            throw new SbcRuntimeException("K-110208");
        }

        validateAppointmentPermission(sale);
        // 同一商品同一时间只能参加一个预约购买或者预售活动
        //活动冲突的商品id
        List<String> clashGoodsInfoIdList = this.validateBookingActivityClash(sale);
        List<AppointmentSaleGoods> haveAppointmentSaleGoods = appointmentSaleGoodsService.list
                (AppointmentSaleGoodsQueryRequest.builder().goodsInfoIdList(
                        sale.getAppointmentSaleGoods().stream().map(AppointmentSaleGoods::getGoodsInfoId)
                                .collect(Collectors.toList())).build()).stream().filter(g -> !g.getAppointmentSaleId().equals(sale.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(haveAppointmentSaleGoods)) {
            List<AppointmentSale> appointmentSales = list(AppointmentSaleQueryRequest.builder().idList(haveAppointmentSaleGoods.stream().
                    collect(Collectors.groupingBy(AppointmentSaleGoods::getAppointmentSaleId)).
                    keySet().stream().collect(Collectors.toList())).delFlag(DeleteFlag.NO).build()).stream().filter(a -> !validateAppointmentSaleRepeat(a, sale)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(appointmentSales)) {
                // 活动重合,给前端返回导致活动冲突的商品id
                List<AppointmentSaleGoods> saleGoodsList = appointmentSaleGoodsService.list(AppointmentSaleGoodsQueryRequest.builder()
                        .appointmentSaleIdList(appointmentSales.stream().map(AppointmentSale::getId).collect(Collectors.toList()))
                        .build());
                List<String> goodsInfoIdList = saleGoodsList.stream().map(AppointmentSaleGoods::getGoodsInfoId).collect(Collectors.toList());
                List<String> clashGoodsInfoIds = sale.getAppointmentSaleGoods().stream()
                        .filter(saleGoods -> goodsInfoIdList.contains(saleGoods.getGoodsInfoId()))
                        .map(AppointmentSaleGoods::getGoodsInfoId)
                        .collect(Collectors.toList());
                clashGoodsInfoIdList.addAll(clashGoodsInfoIds);
            }
        }
        if (CollectionUtils.isNotEmpty(clashGoodsInfoIdList)){
            throw new SbcRuntimeException("K-600005", new Object[]{clashGoodsInfoIdList.size()}, clashGoodsInfoIdList);
        }
        sale.setId(appointmentSale.getId());
        sale.setDelFlag(appointmentSale.getDelFlag());
        sale.setUpdateTime(LocalDateTime.now());
        sale.setCreatePerson(appointmentSale.getCreatePerson());
        sale.setCreateTime(appointmentSale.getCreateTime());
        appointmentSaleRepository.save(sale);

        List<AppointmentSaleGoods> oldAppointmentSaleGoods = appointmentSaleGoodsRepository.findByAppointmentSaleIdAndStoreId(sale.getId(), sale.getStoreId());

        List<AppointmentSaleGoods> newSaleGoods = sale.getAppointmentSaleGoods();

        Map<String, AppointmentSaleGoods> oldAppointmentSaleGoodsMap = oldAppointmentSaleGoods.stream().collect(Collectors.toMap(AppointmentSaleGoods::getGoodsInfoId, m -> m));

        newSaleGoods.forEach(n -> {
            if (oldAppointmentSaleGoodsMap.containsKey(n.getGoodsInfoId())) {
                AppointmentSaleGoods appointmentSaleGoods = oldAppointmentSaleGoodsMap.get(n.getGoodsInfoId());
                n.setCreatePerson(appointmentSaleGoods.getCreatePerson());
                n.setCreateTime(appointmentSaleGoods.getCreateTime());
                n.setId(appointmentSaleGoods.getId());
                oldAppointmentSaleGoodsMap.remove(n.getGoodsInfoId());
            }
            n.setUpdateTime(LocalDateTime.now());
            n.setUpdatePerson(sale.getUpdatePerson());
            n.setAppointmentSaleId(appointmentSale.getId());
            n.setStoreId(appointmentSale.getStoreId());
        });

        if (!oldAppointmentSaleGoodsMap.isEmpty()) {
            List<AppointmentSaleGoods> appointmentSaleGoodsList = new ArrayList<>();
            oldAppointmentSaleGoodsMap.forEach((k, v) -> {
                appointmentSaleGoodsList.add(v);
            });
            appointmentSaleGoodsRepository.deleteAll(appointmentSaleGoodsList);
        }

        appointmentSaleGoodsRepository.saveAll(newSaleGoods);
        appointmentSale.setAppointmentSaleGoods(newSaleGoods);
        return appointmentSale;
    }

    /**
     * 单个删除预约抢购
     *
     * @author zxd
     */
    @Transactional
    public void deleteById(AppointmentSale sale) {
        appointmentSaleRepository.save(sale);
    }

    /**
     * 批量删除预约抢购
     *
     * @author zxd
     */
    @Transactional
    public void deleteByIdList(List<AppointmentSale> infos) {
        appointmentSaleRepository.saveAll(infos);
    }

    /**
     * 单个查询预约抢购
     *
     * @author zxd
     */
    public AppointmentSale getOne(Long id, Long storeId) {
        if (Objects.isNull(storeId)) {
            return appointmentSaleRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
                    .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "预约抢购不存在"));
        }
        return appointmentSaleRepository.findByIdAndStoreIdAndDelFlag(id, storeId, DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "预约抢购不存在"));
    }

    /**
     * 分页查询预约抢购
     *
     * @author zxd
     */
    public Page<AppointmentSale> page(AppointmentSaleQueryRequest queryReq) {

        Page<AppointmentSale> appointmentSalesPage = appointmentSaleRepository.findAll(
                AppointmentSaleWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
        return appointmentSalesPage;
    }

    /**
     * 列表查询预约抢购
     *
     * @author zxd
     */
    public List<AppointmentSale> list(AppointmentSaleQueryRequest request) {
        return appointmentSaleRepository.findAll(AppointmentSaleWhereCriteriaBuilder.build(request));
    }

    /**
     * 将实体包装成VO
     *
     * @author zxd
     */
    public AppointmentSaleVO wrapperVo(AppointmentSale appointmentSale) {
        if (appointmentSale != null) {
            AppointmentSaleVO appointmentSaleVO = KsBeanUtil.convert(appointmentSale, AppointmentSaleVO.class);
            return appointmentSaleVO;
        }
        return null;
    }

    @Transactional
    public void modifyStatus(AppointmentSale sale) {
        appointmentSaleRepository.save(sale);
    }


    /**
     * 获取正在进行中预约活动
     *
     * @param goodsInfoId
     * @return
     */
    public AppointmentSale isInProcess(String goodsInfoId) {
        Object object = appointmentSaleRepository.findByGoodsInfoIdInProcess(goodsInfoId);
        if (Objects.isNull(object)) {
            return null;
        }
        Object[] objects = (Object[]) object;
        AppointmentSale appointmentSale = KsBeanUtil.convert(objects[0], AppointmentSale.class);
        appointmentSale.setAppointmentSaleGood(KsBeanUtil.convert(objects[1], AppointmentSaleGoods.class));
        return appointmentSale;
    }


    /**
     * @param goodsInfoIdList
     * @return
     */
    public List<AppointmentSaleDO> inProgressAppointmentSaleInfoByGoodsInfoIdList(List<String> goodsInfoIdList) {
        if (CollectionUtils.isEmpty(goodsInfoIdList)){
            return  Collections.emptyList();
        }
        List list = appointmentSaleRepository.findByGoodsInfoIdInProcess(goodsInfoIdList);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<AppointmentSaleDO> appointmentSaleDOS = new ArrayList<>();
        list.forEach(o -> {
            Object[] objects = (Object[]) o;
            AppointmentSaleDO appointmentSaleDO = KsBeanUtil.convert(objects[0], AppointmentSaleDO.class);
            appointmentSaleDO.setAppointmentSaleGood(KsBeanUtil.convert(objects[1], AppointmentSaleGoodsDO.class));
            appointmentSaleDOS.add(appointmentSaleDO);
        });
        return appointmentSaleDOS;
    }

    /**
     * 根据spuid列表获取未结束的预约活动
     *
     * @param goodsId
     * @return
     */
    public List<AppointmentSaleDO> getNotEndActivity(String goodsId) {
        List list = appointmentSaleRepository.getNotEndActivity(goodsId);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<AppointmentSaleDO> appointmentSaleDOS = new ArrayList<>();
        list.forEach(o -> {
            Object[] objects = (Object[]) o;
            AppointmentSaleDO appointmentSaleDO = KsBeanUtil.convert(objects[0], AppointmentSaleDO.class);
            appointmentSaleDO.setAppointmentSaleGood(KsBeanUtil.convert(objects[1], AppointmentSaleGoodsDO.class));
            appointmentSaleDOS.add(appointmentSaleDO);
        });
        return appointmentSaleDOS;
    }

    /**
     * 下单时根据goodsInfoIds校验是否参加预约活动
     */
    public void validParticipateInAppointmentSale (List<String> goodsInfoIdList) {
        if (CollectionUtils.isEmpty(goodsInfoIdList)){
            return;
        }
        List appointmentList = appointmentSaleRepository.findByGoodsInfoIdInAppointment(goodsInfoIdList);
        if (CollectionUtils.isNotEmpty(appointmentList)) {
            throw new SbcRuntimeException(AppointmentAndBookingSaleErrorCode.CONTAIN_APPOINTMENT_SALE);
        }
    }


    /**
     * 分页查询预约抢购
     *
     * @author zxd
     */
    public MicroServicePage<AppointmentSaleVO> pageNew(AppointmentSaleQueryRequest queryReq) {
        Page<AppointmentSale> appointmentSalePage = this.page(queryReq);
        MicroServicePage<AppointmentSaleVO> newPage = KsBeanUtil.convertPage(appointmentSalePage,AppointmentSaleVO.class);
        if (CollectionUtils.isEmpty(newPage.getContent())) {
            return newPage;
        }

        List<AppointmentSaleVO> appointmentSaleVOS = newPage.getContent();

        List<Long> ids = appointmentSaleVOS.stream().map(AppointmentSaleVO::getId).collect(Collectors.toList());
        Map<Long, List<AppointmentSaleGoods>> saleGoodsMap = appointmentSaleGoodsService.list(AppointmentSaleGoodsQueryRequest.builder().
                appointmentSaleIdList(ids).build()).stream().collect(Collectors.groupingBy(AppointmentSaleGoods::getAppointmentSaleId));

        List<MarketingCustomerLevelDTO> customerLevelDTOList = appointmentSaleVOS.stream().map(m -> {
            MarketingCustomerLevelDTO dto = new MarketingCustomerLevelDTO();
            dto.setId(m.getId());
            dto.setStoreId(m.getStoreId());
            dto.setJoinLevel(m.getJoinLevel());
            return dto;
        }).collect(Collectors.toList());
        List<MarketingCustomerLevelVO> marketingCustomerLevelVOList = customerLevelQueryProvider.listByCustomerLevelName(new CustomerLevelListByCustomerLevelNameRequest(customerLevelDTOList)).getContext().getCustomerLevelVOList();
        Map<Long,MarketingCustomerLevelVO> levelVOMap = marketingCustomerLevelVOList.stream().collect(Collectors.toMap(MarketingCustomerLevelVO::getId, Function.identity()));
        appointmentSaleVOS.stream().forEach(s -> {
            MarketingCustomerLevelVO levelVO = levelVOMap.get(s.getId());
            s.setLevelName(levelVO.getLevelName());
            s.setStoreName(levelVO.getStoreName());
            if (saleGoodsMap.containsKey(s.getId())) {
                s.setAppointmentCount(saleGoodsMap.get(s.getId()).stream().mapToInt(AppointmentSaleGoods::getAppointmentCount).sum());
                s.setBuyerCount(saleGoodsMap.get(s.getId()).stream().mapToInt(AppointmentSaleGoods::getBuyerCount).sum());
            }
            s.buildStatus();
        });
        return newPage;
    }

}

