package com.wanmi.sbc.account.finance.record.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.account.api.request.finance.record.SettlementToExcelRequest;
import com.wanmi.sbc.account.api.response.finance.record.SettlementToExcelResponse;
import com.wanmi.sbc.account.bean.enums.SettleStatus;
import com.wanmi.sbc.account.bean.vo.SettlementVO;
import com.wanmi.sbc.account.bean.vo.SettlementViewVO;
import com.wanmi.sbc.account.finance.record.model.entity.Settlement;
import com.wanmi.sbc.account.finance.record.model.request.SettlementQueryRequest;
import com.wanmi.sbc.account.finance.record.model.response.SettlementView;
import com.wanmi.sbc.account.finance.record.model.response.TotalSettlement;
import com.wanmi.sbc.account.finance.record.repository.SettlementRepository;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.ListStoreByNameForAutoCompleteRequest;
import com.wanmi.sbc.customer.api.request.store.ListStoreRequest;
import com.wanmi.sbc.customer.api.request.store.StoreNameListByStoreIdsResquest;
import com.wanmi.sbc.customer.bean.vo.StoreNameVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 结算明细
 * Created by hht on 2017/12/7.
 */
@Service
@Slf4j
public class SettlementService {

    @Autowired
    private SettlementRepository settlementRepository;


    @Autowired
    private StoreQueryProvider storeQueryProvider;

    /**
     * 保存结算单
     *
     * @param settlement 结算单对象
     * @return 结算单
     */
    @Transactional
    public Settlement saveSettlement(Settlement settlement) {
        return settlementRepository.save(settlement);
    }

    /**
     * 删除结算和结算明细
     *
     * @param storeId 店铺Id
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    @Transactional
    public void deleteSettlement(Long storeId, String startTime, String endTime) {
        settlementRepository.deleteByStoreIdAndStartTimeAndEndTime(storeId, startTime, endTime);
    }

    /**
     * 分页查询结算单
     *
     * @param request 查询参数
     * @return 结算单分页对象
     */
    public Page<SettlementView> querySettlementPage(SettlementQueryRequest request) {
        request.setSortColumn("createTime");
        request.setSortType("DESC");
        //如果storeId和storeName都不为空
        if (request.getStoreId() != null && StringUtils.isNotBlank(request.getStoreName())) {
            //不相等只带入storeName，如果相等只查storeId
            if (request.getStoreId().toString().equals(request.getStoreName())) {
                request.setStoreName(null);
            } else {
                request.setStoreId(null);
            }
        }
        //按照店铺名称查询-  其实是要查供应商名称
        if (StringUtils.isNotBlank(request.getStoreName())) {
            List<StoreVO> storeList = storeQueryProvider.listStore(
                    ListStoreRequest.builder().supplierName(request.getStoreName()).delFlag(DeleteFlag.NO).build()).getContext().getStoreVOList();
            if (storeList != null && !storeList.isEmpty()) {
                request.setStoreListId(storeList.stream().mapToLong(StoreVO::getStoreId).boxed().collect(Collectors
                        .toList()));
            } else {
                return new PageImpl<>(new ArrayList<>());
            }
        }
        Page<Settlement> page = settlementRepository.findAll(request.getWhereCriteria(), request.getPageRequest());
        Page<SettlementView> settlementViewPage = page.map(Settlement -> {
            SettlementView settlementViewBean = new SettlementView();
            BeanUtils.copyProperties(Settlement, settlementViewBean);
            return settlementViewBean;
        });

        if (CollectionUtils.isNotEmpty(page.getContent())) {
            List<StoreVO> storeList = storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest(page
                    .getContent().stream().mapToLong
                    (Settlement::getStoreId).boxed().collect(Collectors.toList()))).getContext().getStoreVOList();
            settlementViewPage.getContent().stream().forEach(settlementView -> {
                settlementView.setSettlementCode(String.format("S%07d", settlementView.getSettleId()));
                Optional<StoreVO> optional = storeList.stream().filter(store -> store.getStoreId().longValue() ==
                        settlementView.getStoreId()).findFirst();
                settlementView.setStoreName(optional.orElse(null).getSupplierName());
            });
        }
        return settlementViewPage;
    }

    /**
     * 根据单条结算单获取信息并转换为结算单视图
     *
     * @param settlement 结算单
     * @return 结算单视图
     */
    public SettlementView getSettlementById(Settlement settlement) {
        SettlementView view = new SettlementView();
        BeanUtils.copyProperties(settlement, view);
        view.setSettlementCode(String.format("S%07d", settlement.getSettleId()));
        List<StoreVO> storeList = storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest(
                Collections.singletonList(settlement.getStoreId()))).getContext().getStoreVOList();
        if (storeList != null && !storeList.isEmpty()) {
            view.setStoreName(storeList.get(0).getSupplierName());
            if (storeList.get(0).getCompanyInfo() != null) {
                view.setCompanyCode(storeList.get(0).getCompanyInfo().getCompanyCode());
            }
        }
        return view;
    }


    /**
     * 修改结算单的状态
     *
     * @param settleIdList 批量结算单Id数据
     * @param settleStatus 结算单新状态
     */
    @Transactional
    public void updateSettleStatus(List<Long> settleIdList, SettleStatus settleStatus) {
        settlementRepository.updateSettleStatusBatch(settleStatus, settleIdList);
    }

    /**
     * 根据结算单id查询结算单
     *
     * @param settleId 结算单id
     * @return 结算单
     */
    public Settlement querySettlementById(Long settleId) {
        return settlementRepository.findById(settleId).orElse(null);
    }

    /**
     * 待结算统计
     *
     * @param request 结算单条件参数
     * @return 待结算统计量
     */
    public Long countByTodo(SettlementQueryRequest request) {
        request.setSettleStatus(SettleStatus.NOT_SETTLED);
        return settlementRepository.count(request.getWhereCriteria());
    }

    /**
     * 计算店铺总的结算资金、待结算资金
     *
     * @param storeId 店铺Id
     * @return 店铺统计结构
     */
    public List<TotalSettlement> queryToTalSettlement(Long storeId) {
        return settlementRepository.queryToTalSettlement(storeId);
    }

    /**
     * 得到财务结算报表导出数据
     * @param queryRequest
     * @return
     */
    public SettlementToExcelResponse getExportData(SettlementToExcelRequest queryRequest) {
        SettlementQueryRequest request = new SettlementQueryRequest();
        KsBeanUtil.copyPropertiesThird(queryRequest, request);
        SettlementToExcelResponse settlementViewExcel = new SettlementToExcelResponse();
        //未结算数据查询
        request.setSettleStatus(SettleStatus.NOT_SETTLED);
        List<SettlementViewVO> notSettledSettlements = getResSettle(request);
        if (CollectionUtils.isNotEmpty(notSettledSettlements)) {
            settlementViewExcel.setNotSettledSettlements(notSettledSettlements);
        } else {
            settlementViewExcel.setNotSettledSettlements(Collections.emptyList());
        }
        //已结算数据查询
        request.setSettleStatus(SettleStatus.SETTLED);
        List<SettlementViewVO> settledSettlements = getResSettle(request);
        if (CollectionUtils.isNotEmpty(settledSettlements)) {
            settlementViewExcel.setSettledSettlements(settledSettlements);
        } else {
            settlementViewExcel.setSettledSettlements(Collections.emptyList());
        }
        //暂不处理数据查询
        request.setSettleStatus(SettleStatus.SETTLE_LATER);
        List<SettlementViewVO> settleLaterSettlements =getResSettle(request);
        if (CollectionUtils.isNotEmpty(settleLaterSettlements)) {
            settlementViewExcel.setSettleLaterSettlements(settleLaterSettlements);
        } else {
            settlementViewExcel.setSettleLaterSettlements(Collections.emptyList());
        }
        return settlementViewExcel;
    }

    /**
     * 根据结算单状态查询
     * @param request
     * @return
     */
    private List<SettlementViewVO> getResSettle(SettlementQueryRequest request) {
        List<SettlementViewVO> settlementViewList;
        request = setRequest(request);
        if (request.getPageSize() == BigDecimal.ROUND_UP) {
            settlementViewList = Collections.emptyList();
        } else {
            settlementViewList = this.querySettlementPage(request).getContent().stream().map(view -> {
                SettlementViewVO vo = new SettlementViewVO();
                KsBeanUtil.copyPropertiesThird(view, vo);
                return vo;
            }).collect(Collectors.toList());
        }

        return settlementViewList;
    }

    /**
     * 财务结算报表设置request查询参数
     * @param request
     * @return
     */
    private SettlementQueryRequest setRequest(SettlementQueryRequest request) {
        if (request.getStoreId() != null && StringUtils.isNotBlank(request.getStoreName())) {
            //不相等只带入storeName，如果相等只查storeId
            if (request.getStoreId().toString().equals(request.getStoreName())) {
                request.setStoreName(null);
            } else {
                request.setStoreId(null);
            }
        }
        //按照店铺名称查询
        if (StringUtils.isNotBlank(request.getStoreName())) {
            List<StoreVO> storeList = storeQueryProvider.listByNameForAutoComplete(
                    ListStoreByNameForAutoCompleteRequest.builder().storeName(request.getStoreName()).build()).getContext().getStoreVOList();
            if (storeList != null && !storeList.isEmpty()) {
                request.setStoreListId(storeList.stream().mapToLong(StoreVO::getStoreId).boxed().collect(Collectors
                        .toList()));
            } else {
                request.setPageNum(BigDecimal.ROUND_UP);
                request.setPageSize(BigDecimal.ROUND_UP);
                return request;
            }
        }
        List<Settlement> settlementList = settlementRepository.findAll(request.getWhereCriteria());
        int total = settlementList.size();
        request.setPageNum(BigDecimal.ROUND_UP);
        request.setPageSize(total);
        return request;
    }

    /**
     * 根据结算单id查询结算单
     *
     * @param settleId 结算单id
     * @return 结算单
     */
    public Settlement getLastSettlementByStoreId(Long settleId) {
        return settlementRepository.getLastSettlementByStoreId(settleId).orElse(null);
    }



    /**
     * @Author yangzhen
     * @Description // 从mysql 中查询结算单数据，用于刷入es
     * @Date 17:10 2020/12/14
     **/
    public List<SettlementViewVO> initEsPage(SettlementQueryRequest request) {
        List<Long> settleIds = CollectionUtils.isNotEmpty(
                request.getStoreListId()) ? request.getStoreListId() : settlementRepository.listByPage(request.getPageRequest());

        if (CollectionUtils.isEmpty(settleIds)) {
            return Lists.newArrayList();
        }

        //查询结算单详情
        List<Settlement> settlementList = settlementRepository.findAllById(settleIds);

        //根据结算单id查询店铺id
        List<Long> storeIds = settlementRepository.getStoreIdsByIds(settleIds);
        //根据店铺id 查询店铺名称
        List<StoreNameVO> storeNameList = storeQueryProvider.listStoreNameByStoreIds(new StoreNameListByStoreIdsResquest(storeIds)).getContext()
                .getStoreNameList();
        Map<Long, StoreNameVO> storeNameMap = storeNameList.stream().collect(Collectors.toMap(StoreNameVO::getStoreId, Function.identity()));

        List<SettlementViewVO> settlementVOList = new ArrayList<>();
        settlementList.forEach(settlement -> {
            StoreNameVO storeNameVO = storeNameMap.get(settlement.getStoreId());
            SettlementViewVO settlementViewVO = KsBeanUtil.convert(settlement, SettlementViewVO.class);
            settlementViewVO.setStoreName(storeNameVO.getStoreName());
            settlementViewVO.setSettleCode(String.format("S%07d", settlementViewVO.getSettleId()));
            settlementVOList.add(settlementViewVO);
        });
        return settlementVOList;
    }


}
