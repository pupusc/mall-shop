package com.wanmi.sbc.goods.bookingsale.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.AppointmentAndBookingSaleErrorCode;
import com.wanmi.sbc.goods.api.request.bookingsale.BookingSaleQueryRequest;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.appointmentsalegoods.model.root.AppointmentSaleGoods;
import com.wanmi.sbc.goods.appointmentsalegoods.repository.AppointmentSaleGoodsRepository;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import com.wanmi.sbc.goods.bookingsale.model.root.BookingSale;
import com.wanmi.sbc.goods.bookingsale.repository.BookingSaleRepository;
import com.wanmi.sbc.goods.bookingsalegoods.model.root.BookingSaleGoods;
import com.wanmi.sbc.goods.bookingsalegoods.repository.BookingSaleGoodsRepository;
import com.wanmi.sbc.goods.bookingsalegoods.service.BookingSaleGoodsService;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>预售信息业务逻辑</p>
 *
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@Service("BookingSaleService")
public class BookingSaleService {
    @Autowired
    private BookingSaleRepository bookingSaleRepository;

    @Autowired
    private BookingSaleGoodsRepository bookingSaleGoodsRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private BookingSaleGoodsService bookingSaleGoodsService;

    @Autowired
    private AppointmentSaleGoodsRepository appointmentSaleGoodsRepository;

    /**
     * 新增预售信息
     *
     * @author dany
     */
    @Transactional
    public BookingSale add(BookingSale sale) {
        // 同一商品同一时间只能参加一个预约购买或者预售活动
        validateBookingPermission(sale);
        List<BookingSaleGoods> bookingSaleGoodsList = sale.getBookingSaleGoodsList();
        //活动冲突的商品id
        List<String> clashGoodsInfoIdList = this.validateAppointmentActivity(sale);
        List<BookingSaleGoods> existsBookingSaleGoods = bookingSaleGoodsService.list(BookingSaleGoodsQueryRequest.builder().
                goodsInfoIdList(sale.getBookingSaleGoodsList().stream().map(BookingSaleGoods::getGoodsInfoId).collect(Collectors.toList())).build());
        if (CollectionUtils.isNotEmpty(existsBookingSaleGoods)) {
            //查询冲突的活动
            List<BookingSale> bookingSales = list(BookingSaleQueryRequest.builder().idList(existsBookingSaleGoods.stream().
                    collect(Collectors.groupingBy(BookingSaleGoods::getBookingSaleId)).
                    keySet().stream().collect(Collectors.toList())).delFlag(DeleteFlag.NO).build()).stream().filter(a -> !validateBookingSaleRepeat(a, sale)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(bookingSales)) {
                // 活动重合,给前端返回导致活动冲突的商品id
                List<BookingSaleGoods> saleGoodsList = bookingSaleGoodsService.list(BookingSaleGoodsQueryRequest.builder()
                        .bookingSaleIdList(bookingSales.stream().map(BookingSale::getId).collect(Collectors.toList()))
                        .build());
                List<String> goodsInfoIdList = saleGoodsList.stream().map(BookingSaleGoods::getGoodsInfoId).collect(Collectors.toList());
                List<String> clashGoodsInfoIds = bookingSaleGoodsList.stream()
                        .filter(saleGoods -> goodsInfoIdList.contains(saleGoods.getGoodsInfoId()))
                        .map(BookingSaleGoods::getGoodsInfoId)
                        .collect(Collectors.toList());
                clashGoodsInfoIdList.addAll(clashGoodsInfoIds);
            }
        }
        if (CollectionUtils.isNotEmpty(clashGoodsInfoIdList)){
            throw new SbcRuntimeException("K-600005", new Object[]{clashGoodsInfoIdList.size()}, clashGoodsInfoIdList);
        }
        sale.setCreateTime(LocalDateTime.now());
        BookingSale bookingSale = bookingSaleRepository.save(sale);

        bookingSaleGoodsList.stream().forEach(k -> {
            k.setBookingSaleId(bookingSale.getId());
            k.setStoreId(bookingSale.getStoreId());
            k.setCreatePerson(bookingSale.getCreatePerson());
            k.setCreateTime(LocalDateTime.now());
            k.setCanBookingCount(k.getBookingCount());
        });

        bookingSale.setBookingSaleGoodsList(bookingSaleGoodsRepository.saveAll(bookingSaleGoodsList));
        return bookingSale;
    }


    /**
     * 校验是否存在预约活动
     *
     * @param sale
     * @return
     */
    private boolean validateAppointmentActivityRepeat(BookingSale sale) {
        List<String> goodsInfoIds = sale.getBookingSaleGoodsList().stream().map(BookingSaleGoods::getGoodsInfoId).collect(Collectors.toList());
        LocalDateTime startTime = sale.getBookingStartTime();
        LocalDateTime endTime = sale.getBookingEndTime();
        if (sale.getBookingType() == 1) {
            startTime = sale.getHandSelStartTime();
            endTime = sale.getTailEndTime();
        }
        List list = appointmentSaleGoodsRepository.existAppointmentActivity(goodsInfoIds, startTime, endTime);
        if (CollectionUtils.isNotEmpty(list)) {
            return true;
        }
        return false;
    }

    /**
     * 校验是否与预约活动冲突
     *
     * @param sale
     * @return
     */
    private List<String> validateAppointmentActivity(BookingSale sale) {
        List<String> goodsInfoIds = sale.getBookingSaleGoodsList().stream().map(BookingSaleGoods::getGoodsInfoId).collect(Collectors.toList());
        LocalDateTime startTime = sale.getBookingStartTime();
        LocalDateTime endTime = sale.getBookingEndTime();
        if (sale.getBookingType() == 1) {
            startTime = sale.getHandSelStartTime();
            endTime = sale.getTailEndTime();
        }
        List list = appointmentSaleGoodsRepository.existAppointmentActivity(goodsInfoIds, startTime, endTime);
        List<String> goodsInfoIdList = new ArrayList<>();
        list.forEach(o -> {
            Object[] objects = (Object[]) o;
            AppointmentSaleGoods appointmentSaleGoods = (KsBeanUtil.convert(objects[1], AppointmentSaleGoods.class));
            goodsInfoIdList.add(appointmentSaleGoods.getGoodsInfoId());
        });
        return goodsInfoIdList;
    }

    /**
     * 校验同一商品同一时间只能参加一个预约购买或者预售活动
     *
     * @param oldSale 持有
     * @param newSale 新增
     * @return
     */
    private boolean validateBookingSaleRepeat(BookingSale oldSale, BookingSale newSale) {
        LocalDateTime oldStartTime;
        LocalDateTime oldEndTime;
        LocalDateTime newStartTime;
        LocalDateTime newEndTime;
        if (oldSale.getBookingType().equals(NumberUtils.INTEGER_ZERO)) {
            oldStartTime = oldSale.getBookingStartTime();
            oldEndTime = oldSale.getBookingEndTime();
        } else if (oldSale.getBookingType().equals(NumberUtils.INTEGER_ONE)) {
            oldStartTime = oldSale.getHandSelStartTime();
            oldEndTime = oldSale.getTailEndTime();
        } else {
            throw new SbcRuntimeException("K-000001");
        }
        if (newSale.getBookingType().equals(NumberUtils.INTEGER_ZERO)) {
            newStartTime = newSale.getBookingStartTime();
            newEndTime = newSale.getBookingEndTime();
        } else if (newSale.getBookingType().equals(NumberUtils.INTEGER_ONE)) {
            newStartTime = newSale.getHandSelStartTime();
            newEndTime = newSale.getTailEndTime();
        } else {
            throw new SbcRuntimeException("K-000001");
        }
        if (newStartTime.compareTo(oldStartTime) >= 0 && newStartTime.compareTo(oldEndTime) <= 0) {
            return false;
        }
        if (newEndTime.compareTo(oldStartTime) >= 0 && newEndTime.compareTo(oldEndTime) <= 0) {
            return false;
        }
        if (newStartTime.compareTo(oldStartTime) <= 0 && newEndTime.compareTo(oldStartTime) >= 0) {
            return false;
        }
        return true;
    }

    /**
     * 活动满足条件校验
     *
     * @param sale
     */
    private void validateBookingPermission(BookingSale sale) {
        if (CollectionUtils.isEmpty(sale.getBookingSaleGoodsList())) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        List<GoodsInfo> goodsInfoList = goodsInfoRepository.findByGoodsInfoIds(
                sale.getBookingSaleGoodsList().stream().map(BookingSaleGoods::getGoodsInfoId).collect(Collectors.toList())
        );
        if (CollectionUtils.isEmpty(goodsInfoList) || goodsInfoList.size() != sale.getBookingSaleGoodsList().size()) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        List<GoodsInfo> exceptionGoodsInfo = goodsInfoList.stream().filter(g -> Objects.isNull(g.getStoreId()) || !g.getStoreId().equals(sale.getStoreId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(exceptionGoodsInfo)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 预约类型：必选，单选，默认选中不预约不可购买
        if (Objects.isNull(sale.getJoinLevelType())) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        //预定金支付时间（开始时间/结束时间）：必选，精确到时，分/秒数位自动补齐为00，不点击选择时间时，起止时间自动取00:00:00；
        //结束时间不可早于开始时间
        //     尾款支付的开始时间不可早于定金支付的结束时间(可以等于)
        //尾款支付时间（开始时间/结束时间）：必选，精确到时，分/秒数位自动补齐为00，不点击选择时间时，起止时间自动取00:00:00；
        //     开始时间（的日期）不可早于当前时间（的日期），点击日期弹窗的确定时校验，如有错误，需高亮日期选择框并在选择框下方提示：开始时间不可早于当前时间
        //     结束时间不可早于开始时间
        //     尾款支付的开始时间不可早于定金支付的结束时间(可以等于)
        //发货时间：必选，精确到天，发货时间不可早于尾款支付开始时间(可以等于)
        if (sale.getBookingType().equals(NumberUtils.INTEGER_ONE)) {
            if ((Objects.isNull(sale.getHandSelStartTime()) || Objects.isNull(sale.getHandSelEndTime()))) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
            if (sale.getHandSelStartTime().isBefore(LocalDateTime.now())) {
                throw new SbcRuntimeException("K-600001");
            }
            if (sale.getHandSelStartTime().isAfter(sale.getHandSelEndTime())
                    || sale.getHandSelStartTime().isEqual(sale.getHandSelEndTime())) {
                throw new SbcRuntimeException("K-600002");
            }

            if (Objects.isNull(sale.getTailStartTime()) || Objects.isNull(sale.getTailEndTime())) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
            if (sale.getTailStartTime().isBefore(LocalDateTime.now())) {
                throw new SbcRuntimeException("K-600001");
            }
            if (sale.getTailStartTime().isAfter(sale.getTailEndTime())
                    || sale.getTailStartTime().isEqual(sale.getTailEndTime())) {
                throw new SbcRuntimeException("K-600002");
            }
            if (sale.getHandSelEndTime().compareTo(sale.getTailStartTime()) > 0) {
                throw new SbcRuntimeException("K-600011");
            }
            //发货时间：必选，精确到天，发货时间不可早于抢购开始时间(可以等于)
            if (Objects.isNull(sale.getDeliverTime())) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
            //发货时间：必选，精确到天，发货时间不可早于尾款支付开始时间(可以等于)
            if (LocalDateTime.parse(sale.getDeliverTime() + " 23:59:59", DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1)).compareTo(sale.getTailStartTime()) < 0) {
                throw new SbcRuntimeException("K-600012");
            }
            sale.setStartTime(sale.getHandSelStartTime());
            sale.setEndTime(sale.getTailEndTime());
        }
        if (sale.getBookingType().equals(NumberUtils.INTEGER_ZERO)) {
            if (Objects.isNull(sale.getBookingStartTime()) || Objects.isNull(sale.getBookingEndTime())) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
            if (sale.getBookingStartTime().isBefore(LocalDateTime.now())) {
                throw new SbcRuntimeException("K-600001");
            }
            if (sale.getBookingStartTime().isAfter(sale.getBookingEndTime())
                    || sale.getBookingStartTime().isEqual(sale.getBookingEndTime())) {
                throw new SbcRuntimeException("K-600002");
            }
            if (Objects.isNull(sale.getDeliverTime())) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
            // 发货时间精确到天，可以等于全款预售开始时间
            if (LocalDateTime.parse(sale.getDeliverTime() + " 23:59:59", DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_1)).compareTo(sale.getBookingStartTime()) < 0) {
                throw new SbcRuntimeException("K-600016");
            }
            sale.setStartTime(sale.getBookingStartTime());
            sale.setEndTime(sale.getBookingEndTime());
        }


        // 定金：必填，默认填充市场价，支持修改，仅限0-9999999.99间的数字，定金膨胀金额必须大于定金
        // 定金膨胀：非必填，默认填充市场价，支持修改，仅限0-9999999.99间的数字，定金膨胀金额必须大于定金
        // 预售数量：非必填，仅限1-9999999间的数字
        // 膨胀金须小于商品单价
        sale.getBookingSaleGoodsList().forEach(s -> {
            if (sale.getBookingType().equals(NumberUtils.INTEGER_ONE) && Objects.isNull(s.getHandSelPrice())) {
                throw new SbcRuntimeException("K-600014");
            }
            if (sale.getBookingType().equals(NumberUtils.INTEGER_ONE) && s.getHandSelPrice().compareTo(new BigDecimal("9999999.99")) > 0) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
            if (sale.getBookingType().equals(NumberUtils.INTEGER_ONE) && Objects.nonNull(s.getInflationPrice())
                    && s.getInflationPrice().compareTo(s.getHandSelPrice()) <= 0) {
                throw new SbcRuntimeException("K-600013");
            }
            if (sale.getBookingType().equals(NumberUtils.INTEGER_ONE) && Objects.nonNull(s.getInflationPrice())
                    && s.getInflationPrice().compareTo(new BigDecimal("9999999.99")) > 0) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }

        });
        // 同一商品同一时间只能参加一个预约购买或者预售活动
        /*if (validateAppointmentActivityRepeat(sale)) {
            throw new SbcRuntimeException("K-600005");
        }*/
    }


    /**
     * 修改预售信息
     *
     * @author dany
     */
    @Transactional
    public BookingSale modify(BookingSale sale) {
        BookingSale bookingSale = getOne(sale.getId(), sale.getStoreId());

        if (Objects.isNull(bookingSale)) {
            throw new SbcRuntimeException("k-000001");
        }
        if (bookingSale.getBookingType().equals(NumberUtils.INTEGER_ZERO) &&
                bookingSale.getBookingStartTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-600007");
        }
        if (bookingSale.getBookingType().equals(NumberUtils.INTEGER_ONE) &&
                bookingSale.getHandSelStartTime().isBefore(LocalDateTime.now())) {
            throw new SbcRuntimeException("K-600007");
        }
        if (!sale.getStoreId().equals(bookingSale.getStoreId())) {
            throw new SbcRuntimeException("K-110208");
        }

        // 同一商品同一时间只能参加一个预约购买或者预售活动
        validateBookingPermission(sale);
        //活动冲突的商品id
        List<String> clashGoodsInfoIdList = this.validateAppointmentActivity(sale);
        List<BookingSaleGoods> haveBookingSaleGoods = bookingSaleGoodsService.list
                (BookingSaleGoodsQueryRequest.builder().goodsInfoIdList(
                        sale.getBookingSaleGoodsList().stream().map(BookingSaleGoods::getGoodsInfoId)
                                .collect(Collectors.toList())).build()).stream().filter(g -> !g.getBookingSaleId().equals(sale.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(haveBookingSaleGoods)) {
            List<BookingSale> bookingSales = list(BookingSaleQueryRequest.builder().idList(haveBookingSaleGoods.stream().
                    collect(Collectors.groupingBy(BookingSaleGoods::getBookingSaleId)).
                    keySet().stream().collect(Collectors.toList())).delFlag(DeleteFlag.NO).build()).stream().filter(a -> !validateBookingSaleRepeat(a, sale)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(bookingSales)) {
                // 活动重合,给前端返回导致活动冲突的商品id
                List<BookingSaleGoods> saleGoodsList = bookingSaleGoodsService.list(BookingSaleGoodsQueryRequest.builder()
                        .bookingSaleIdList(bookingSales.stream().map(BookingSale::getId).collect(Collectors.toList()))
                        .build());
                List<String> goodsInfoIdList = saleGoodsList.stream().map(BookingSaleGoods::getGoodsInfoId).collect(Collectors.toList());
                List<String> clashGoodsInfoIds = sale.getBookingSaleGoodsList().stream()
                        .filter(saleGoods -> goodsInfoIdList.contains(saleGoods.getGoodsInfoId()))
                        .map(BookingSaleGoods::getGoodsInfoId)
                        .collect(Collectors.toList());
                clashGoodsInfoIdList.addAll(clashGoodsInfoIds);
            }
        }
        if (CollectionUtils.isNotEmpty(clashGoodsInfoIdList)){
            throw new SbcRuntimeException("K-600005", new Object[]{clashGoodsInfoIdList.size()}, clashGoodsInfoIdList);
        }
        sale.setId(bookingSale.getId());
        sale.setDelFlag(bookingSale.getDelFlag());
        sale.setUpdateTime(LocalDateTime.now());
        sale.setCreatePerson(bookingSale.getCreatePerson());
        sale.setCreateTime(bookingSale.getCreateTime());
        bookingSaleRepository.save(sale);

        List<BookingSaleGoods> oldBookingSaleGoods = bookingSaleGoodsRepository.findByBookingSaleIdAndStoreId(sale.getId(), sale.getStoreId());

        List<BookingSaleGoods> newSaleGoodsList = sale.getBookingSaleGoodsList();

        Map<String, BookingSaleGoods> oldBookingSaleGoodsMap = oldBookingSaleGoods.stream().collect(Collectors.toMap(BookingSaleGoods::getGoodsInfoId, m -> m));

        newSaleGoodsList.forEach(n -> {
            if (oldBookingSaleGoodsMap.containsKey(n.getGoodsInfoId())) {
                BookingSaleGoods bookingSaleGoods = oldBookingSaleGoodsMap.get(n.getGoodsInfoId());
                n.setCreatePerson(bookingSaleGoods.getCreatePerson());
                n.setCreateTime(bookingSaleGoods.getCreateTime());
                n.setId(bookingSaleGoods.getId());
                oldBookingSaleGoodsMap.remove(n.getGoodsInfoId());
            }
            n.setUpdateTime(LocalDateTime.now());
            n.setUpdatePerson(sale.getUpdatePerson());
            n.setBookingSaleId(bookingSale.getId());
            n.setStoreId(bookingSale.getStoreId());
            n.setCanBookingCount(n.getBookingCount());
        });

        if (!oldBookingSaleGoods.isEmpty()) {
            List<BookingSaleGoods> bookingSaleGoodsArrayList = new ArrayList<>();
            oldBookingSaleGoodsMap.forEach((k, v) -> {
                bookingSaleGoodsArrayList.add(v);
            });
            bookingSaleGoodsRepository.deleteAll(bookingSaleGoodsArrayList);
        }

        bookingSaleGoodsRepository.saveAll(newSaleGoodsList);
        bookingSale.setBookingSaleGoodsList(newSaleGoodsList);
        return bookingSale;
    }

    /**
     * 单个删除预售信息
     *
     * @author dany
     */
    @Transactional
    public void deleteById(BookingSale entity) {
        bookingSaleRepository.save(entity);
    }

    /**
     * 批量删除预售信息
     *
     * @author dany
     */
    @Transactional
    public void deleteByIdList(List<BookingSale> infos) {
        bookingSaleRepository.saveAll(infos);
    }

    /**
     * 单个查询预售信息
     *
     * @author dany
     */
    public BookingSale getOne(Long id, Long storeId) {
        if (Objects.isNull(storeId)) {
            return getById(id);
        }
        return bookingSaleRepository.findByIdAndStoreIdAndDelFlag(id, storeId, DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "预售信息不存在"));
    }

    public BookingSale getById(Long id) {
        return bookingSaleRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "预售信息不存在"));
    }

    /**
     * 分页查询预售信息
     *
     * @author dany
     */
    public Page<BookingSale> page(BookingSaleQueryRequest queryReq) {
        return bookingSaleRepository.findAll(
                BookingSaleWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询预售信息
     *
     * @author dany
     */
    public List<BookingSale> list(BookingSaleQueryRequest queryReq) {
        return bookingSaleRepository.findAll(BookingSaleWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author dany
     */
    public BookingSaleVO wrapperVo(BookingSale bookingSale) {
        if (bookingSale != null) {
            BookingSaleVO bookingSaleVO = KsBeanUtil.convert(bookingSale, BookingSaleVO.class);
            return bookingSaleVO;
        }
        return null;
    }

    @Transactional
    public void modifyStatus(BookingSale sale) {
        bookingSaleRepository.save(sale);
    }


    public List<BookingSale> inProgressBookingSaleInfoByGoodsInfoIdList(List<String> goodsInfoIdList) {
        List list = bookingSaleRepository.findByGoodsInfoIdInProcess(goodsInfoIdList);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<BookingSale> bookingSales = new ArrayList<>();
        list.forEach(o -> {
            Object[] objects = (Object[]) o;
            BookingSale bookingSale = KsBeanUtil.convert(objects[0], BookingSale.class);
            bookingSale.setBookingSaleGoods(KsBeanUtil.convert(objects[1], BookingSaleGoods.class));
            bookingSales.add(bookingSale);
        });
        return bookingSales;

    }

    /**
     * 判断此商品此否正在进行预售活动
     *
     * @param goodsInfoId
     * @return
     */
    public BookingSale isInProcess(String goodsInfoId) {
        Object object = bookingSaleRepository.findByGoodsInfoIdInProcess(goodsInfoId);
        if (Objects.isNull(object)) {
            return null;
        }
        Object[] objects = (Object[]) object;
        BookingSale bookingSale = KsBeanUtil.convert(objects[0], BookingSale.class);
        bookingSale.setBookingSaleGoods(KsBeanUtil.convert(objects[1], BookingSaleGoods.class));
        return bookingSale;
    }


    /**
     * 根据spuid列表获取未结束的预售活动
     *
     * @param goodsId
     * @return
     */
    public List<BookingSale> getNotEndActivity(String goodsId) {
        List list = bookingSaleRepository.getNotEndActivity(goodsId);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<BookingSale> bookingSales = new ArrayList<>();
        list.forEach(o -> {
            Object[] objects = (Object[]) o;
            BookingSale bookingSale = KsBeanUtil.convert(objects[0], BookingSale.class);
            bookingSale.setBookingSaleGoods(KsBeanUtil.convert(objects[1], BookingSaleGoods.class));
            bookingSales.add(bookingSale);
        });
        return bookingSales;
    }


    /**
     * 下单时根据goodsInfoIds校验是否参加预售活动
     */
    public void validParticipateInBookingSale(List<String> goodsInfoIdList) {
        if (CollectionUtils.isEmpty(goodsInfoIdList)){
            return;
        }
        List list = bookingSaleRepository.findByGoodsInfoIdInBooking(goodsInfoIdList);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new SbcRuntimeException(AppointmentAndBookingSaleErrorCode.CONTAIN_BOOKING_SALE);
        }
    }

    /**
     * 增加预售活动过期校验
     *
     */
    public void validateBookingQualification(List<String> bookingSaleGoodsInfoIds,Map<String,Long> skuIdAndBookSaleIdMap) {
        bookingSaleGoodsInfoIds.stream().forEach(skuId -> {
            BookingSale bookingSaleVO =  this.isInProcess(skuId);
            if (Objects.isNull(bookingSaleVO)) {
                throw new SbcRuntimeException("K-600010");
            }
            if (!bookingSaleVO.getId().equals(skuIdAndBookSaleIdMap.get(skuId))) {
                throw new SbcRuntimeException("K-000009");
            }
            if (bookingSaleVO.getBookingType().equals(NumberUtils.INTEGER_ONE) &&
                    (bookingSaleVO.getHandSelEndTime().isBefore(LocalDateTime.now()) || bookingSaleVO.getHandSelStartTime().isAfter(LocalDateTime.now()))) {
                throw new SbcRuntimeException("K-170003");
            }
            if (bookingSaleVO.getBookingType().equals(NumberUtils.INTEGER_ZERO) &&
                    (bookingSaleVO.getBookingEndTime().isBefore(LocalDateTime.now()) || bookingSaleVO.getBookingStartTime().isAfter(LocalDateTime.now()))) {
                throw new SbcRuntimeException("K-170003");
            }
        });

    }
}

