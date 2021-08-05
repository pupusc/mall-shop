package com.wanmi.sbc.order.settlement.service;

// import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.wanmi.sbc.order.api.request.settlement.SettlementDetailPageSettleUuidRequest;
import com.wanmi.sbc.order.settlement.model.root.SettlementDetail;
import com.wanmi.sbc.order.settlement.repository.SettlementDetailRepository;
import com.wanmi.sbc.common.util.UUIDUtil;
// import com.wanmi.sbc.mongo.MongoTccHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by hht on 2017/12/7.
 */
@Service
public class SettlementDetailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SettlementDetailRepository settlementDetailRepository;

    @Autowired
    private SettlementDetailService settlementDetailService;

    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 新增文档
     * 专门用于数据新增服务,不允许数据修改的时候调用
     *
     * @param settlementDetail
     */
    public void addReturnOrder(SettlementDetail settlementDetail) {
        settlementDetailRepository.save(settlementDetail);
    }

    /**
     * 批量新增结算明细
     *
     * @param settlementDetailList 结算明细批量数据
     */
    @Transactional
    public void save(List<SettlementDetail> settlementDetailList) {
        logger.info("保存结算单明细列表");
        settlementDetailRepository.saveAll(settlementDetailList);
    }

    /*@Resource
    private MongoTccHelper mongoTccHelper;

    @SuppressWarnings("unused")
    public void confirmSave(SettlementDetail settlementDetail) {
        mongoTccHelper.confirm();
    }

    @SuppressWarnings("unused")
    public void cancelSave(SettlementDetail settlementDetail) {
        mongoTccHelper.cancel();
    }*/

    /**
     * 保存结算明细
     *
     * @param settlementDetail 结算明细
     */
    //@TccTransaction
    @Transactional
    public void save(SettlementDetail settlementDetail) {
        logger.info("保存结算单明细");
        if (StringUtils.isEmpty(settlementDetail.getId())) {
            settlementDetail.setId(UUIDUtil.getUUID());
        }
        settlementDetailService.addReturnOrder(settlementDetail);
    }

    /**
     * 删除结算明细
     *
     * @param storeId   店铺Id
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    @Transactional
    public void deleteSettlement(Long storeId, String startDate, String endDate) {
        settlementDetailRepository.deleteByStartTimeAndEndTimeAndStoreId(startDate, endDate, storeId);
        logger.info("商家id" + storeId + "删除" + startDate + "-" + endDate + "结算单明细");
    }

    /**
     * 根据订单id查询结算明细
     *
     * @param tradeId   订单id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 结算明细Optional
     */
    public Optional<SettlementDetail> getByTradeId(String tradeId, String startDate, String endDate) {
        return settlementDetailRepository.findBySettleTrade_TradeCodeAndStartTimeAndEndTime(tradeId, startDate,
                endDate);
    }

    /**
     * 查询结算明细
     *
     * @param settleUuid 结算单uuid
     * @return 结算明细列表
     */
    public List<SettlementDetail> getSettlementDetail(String settleUuid) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("settleUuid").is(settleUuid));
        Query query = new Query(criteria).with(Sort.by(new Sort.Order(Sort.Direction.DESC, "settleTrade" +
                ".tradeEndTime")));
        return mongoTemplate.find(query, SettlementDetail.class);
    }




    /**
     * 分页查询结算明细
     *
     * @param request 结算单uuid
     * @return 结算明细列表
     */
    public PageImpl<SettlementDetail> pageSettlementDetail(SettlementDetailPageSettleUuidRequest request) {

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("settleUuid").is(request.getSettleUuid()));
        Query query = new Query(criteria).with(Sort.by(new Sort.Order(Sort.Direction.DESC, "settleTrade" +
                ".tradeEndTime")));

        long totalSize = mongoTemplate.count(query, SettlementDetail.class);

        if (totalSize < 1) {
            return new PageImpl<>(new ArrayList<>(), request.getPageRequest(), totalSize);
        }
        return new PageImpl<>(mongoTemplate.find(query.with(request.getPageRequest()), SettlementDetail.class), request.getPageable(), totalSize);
    }

    /**
     * 导出结算明细
     *
     * @param settlement 结单单
     * @param outputStream 输出流
     */
    //已移入BFF层直接调用
  /*  public void exportSettlementDetail(Settlement settlement, OutputStream outputStream) {
        String fileKey = "settlement/" + settlement.getStoreId() + "/" + settlement.getSettleUuid() + ".xls";
        OutputStream excelOutputStream = aliYunFileService.downloadFileByKey(fileKey);
        if (excelOutputStream == null) {
            List<SettlementDetail> detailList = this.getSettlementDetail(settlement.getSettleUuid());
            List<SettlementDetailView> viewList = SettlementDetailView.renderSettlementDetailForView(detailList, true);
            ExcelHelper<SettlementDetailView> excelHelper = new ExcelHelper<>();
            excelHelper.addSheet("结算明细", new SpanColumn[]{
                    new SpanColumn("序号", "index", null),
                    new SpanColumn("订单创建时间", "tradeCreateTime", null),
                    new SpanColumn("订单编号", "tradeCode", null),
                    new SpanColumn("订单类型", "tradeType", null),
                    new SpanColumn("收款方", "gatherType", null),
                    new SpanColumn("商品名称/名称/规格", "goodsViewList", "goodsName"),
                    new SpanColumn("所属类目", "goodsViewList", "cateName"),
                    new SpanColumn("商品单价", "goodsViewList", "goodsPrice"),
                    new SpanColumn("数量", "goodsViewList", "num"),
                    new SpanColumn("满减优惠", "goodsViewList", "reductionPrice"),
                    new SpanColumn("满折优惠", "goodsViewList", "discountPrice"),
                    new SpanColumn("订单改价差额", "goodsViewList", "specialPrice"),
                    new SpanColumn("商品实付金额", "goodsViewList", "splitPayPrice"),
                    new SpanColumn("类目扣率", "goodsViewList", "cateRate"),
                    new SpanColumn("平台佣金", "goodsViewList", "platformPriceString"),
                    new SpanColumn("退货数量", "goodsViewList", "returnNum"),
                    new SpanColumn("退货返还佣金", "goodsViewList", "backPlatformPrice"),
                    new SpanColumn("应退金额", "goodsViewList", "shouldReturnPrice"),
                    new SpanColumn("实退金额", "returnPrice", null),
                    new SpanColumn("运费", "deliveryPrice", null),
//                    new SpanColumn("退款状态", "goodsViewList", "returnStatus"),
                    new SpanColumn("店铺应收金额", "storePrice", null)
            }, viewList, "goodsViewList");

            //向response写入流
            excelHelper.write(outputStream);

            //如果excel文件内容为空不上传至oss，后期可以设置超过一定的长度再上传oss
            if (viewList.size() > 1000) {
                //向oss写入流
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                excelHelper.write(byteArrayOutputStream);
                aliYunFileService.uploadFile(byteArrayOutputStream, fileKey);
            }
        } else {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) excelOutputStream)
                    .toByteArray());
            try {
                int ch;
                while ((ch = inputStream.read()) != -1) {
                    outputStream.write(ch);
                }
            } catch (IOException e) {
                logger.error("excel write error", e);
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (excelOutputStream != null) {
                        excelOutputStream.close();
                    }
                } catch (IOException e) {
                    logger.error("excel write error", e);
                }
            }
        }

    }*/

}