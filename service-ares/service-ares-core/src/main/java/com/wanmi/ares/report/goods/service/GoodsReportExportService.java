package com.wanmi.ares.report.goods.service;


import com.google.common.collect.Lists;
import com.wanmi.ares.report.base.model.ExportQuery;
import com.wanmi.ares.report.flow.dao.ReplaySkuFlowUserInfoMapper;
import com.wanmi.ares.report.goods.dao.*;
import com.wanmi.ares.report.goods.model.criteria.GoodsQueryCriteria;
import com.wanmi.ares.report.goods.model.root.GoodsBrandReport;
import com.wanmi.ares.report.goods.model.root.GoodsCateReport;
import com.wanmi.ares.report.goods.model.root.GoodsReport;
import com.wanmi.ares.report.goods.model.root.SkuReport;
import com.wanmi.ares.request.GoodsInfoQueryRequest;
import com.wanmi.ares.source.model.root.GoodsBrand;
import com.wanmi.ares.source.model.root.GoodsCate;
import com.wanmi.ares.source.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.ares.source.model.root.StoreCate;
import com.wanmi.ares.source.service.StoreService;
import com.wanmi.ares.utils.Constants;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.utils.KsBeanUtil;
import com.wanmi.ares.utils.excel.Column;
import com.wanmi.ares.utils.excel.ExcelHelper;
import com.wanmi.ares.utils.excel.impl.SpelColumnRender;
import com.wanmi.ares.utils.osd.OsdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Sku报表服务
 * Created by dyt on 2017/9/21.
 */
@Service
@Slf4j
public class GoodsReportExportService {

    @Autowired
    private OsdService osdService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ReplaySkuFlowUserInfoMapper skuFlowUserInfoMapper;

    @Autowired
    private GoodsInfoSpecDetailRelMapper specDetailRelMapper;
    @Autowired
    private GoodsCateMapper goodsCateMapper;

    @Autowired
    private GoodsStoreCateMapper goodsStoreCateMapper;
    @Autowired
    private GoodsBrandMapper goodsBrandMapper;

    /**
     * 商品SKU生成并上传Excel
     * @param query 条件
     * @return 返回下载链接
     */
    public List<String> generateSkuExcel(ExportQuery query) throws Exception {
        List<String> fileUrl = new ArrayList<>();
        LocalDate begDate = DateUtil.parse2Date(query.getDateFrom(), DateUtil.FMT_DATE_1);
        LocalDate endDate = DateUtil.parse2Date(query.getDateTo(), DateUtil.FMT_DATE_1);

        String commonFileName = String.format("goods/sku/%s/%s/%s商品报表_%s-%s", query.getCompanyId(), DateUtil.format(endDate, DateUtil.FMT_MONTH_2),  storeService.getStoreName(query) , query.getDateFrom(), query.getDateTo());
        commonFileName = osdService.getFileRootPath().concat(commonFileName);
        GoodsQueryCriteria criteria = new GoodsQueryCriteria();
        criteria.setBegDate(query.getDateFrom());
        criteria.setEndDate(query.getDateTo());
        if(!Constants.bossId.equals(query.getCompanyId())) {
            criteria.setCompanyId(query.getCompanyId());
        }
        Long totalCount = skuMapper.countSkuReportByGroup(criteria);

        if(totalCount < 1){
            ExcelHelper excelHelper = new ExcelHelper();
            this.setSkuExcel(excelHelper, null, new ArrayList<>());
            try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
                excelHelper.write(baos);
                String fileName = String.format("%s.xls",commonFileName);
                osdService.uploadExcel(baos , fileName);
                fileUrl.add(fileName);
            }
            return fileUrl;
        }

        long pageSize = 5000;//一个excel文档有5000条信息
        long pageCount = 0L;
        if (totalCount % pageSize > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }

        GoodsInfoQueryRequest infoRequest = new GoodsInfoQueryRequest();
        infoRequest.setPageSize(pageSize);

        BigDecimal hun = new BigDecimal("100");

        for (long i = 0L; i < pageCount; i++) {
            long pageNum = i * pageSize;
            criteria.setNumber(pageNum);
            criteria.setSize(pageSize);

            List<SkuReport> skuReports = skuMapper.querySkuReportByGroup(criteria);
            List<String> ids = skuReports.stream().map(SkuReport::getId).collect(Collectors.toList());
            Map<String,Long> skuUvMap = new HashMap<>(ids.size()>>1);
            Map<String,Long> skuCustomerMap = new HashMap<>(ids.size()>>1);
            Map<String,String> specDetailNameMap = new HashMap<>(ids.size()>>1);
            GoodsQueryCriteria queryCriteria = KsBeanUtil.convert(criteria,GoodsQueryCriteria.class);

            criteria.setEndDate(DateUtil.format(endDate.minusDays(-1),DateUtil.FMT_DATE_1));
            List<List<String>> idsPartition = Lists.partition(ids,1000);
            for(List<String> skuList: idsPartition){
                queryCriteria.setIds(skuList);
                List<SkuReport> skuUvList =  this.skuFlowUserInfoMapper.queryUvBySku(queryCriteria);
                if(skuUvList != null && !skuUvList.isEmpty()) {
                    skuUvMap.putAll(
                            skuUvList
                                    .stream()
                                    .collect(
                                            Collectors.toMap(SkuReport::getId, SkuReport::getTotalUv)
                                    )
                    );
                }

                skuCustomerMap.putAll(skuMapper.querySkuCustomer(criteria)
                        .stream()
                        .collect(
                                Collectors.toMap(SkuReport::getId,SkuReport::getCustomerCount)
                        )
                );

                List<GoodsInfoSpecDetailRel> specDetailRels = this.specDetailRelMapper.queryByGoodsId(skuList,0);

                if(specDetailRels!=null&&!specDetailRels.isEmpty()) {
                    specDetailRels
                            .stream()
                            .forEach(goodsInfoSpecDetailRel -> {
                                        String detailName = null;
                                        if (MapUtils.isNotEmpty(specDetailNameMap)) {
                                            detailName = specDetailNameMap.get(goodsInfoSpecDetailRel.getGoodsInfoId());
                                        }
                                        if (StringUtils.isNotBlank(detailName)) {
                                            detailName = detailName.concat(" ")
                                                    .concat(goodsInfoSpecDetailRel.getDetailName());
                                        } else {
                                            detailName = goodsInfoSpecDetailRel.getDetailName();
                                        }
                                        specDetailNameMap.put(goodsInfoSpecDetailRel.getGoodsInfoId(), detailName);
                                    }
                            );
                }
            }

            boolean uvNotEmpty = MapUtils.isNotEmpty(specDetailNameMap);
            if(CollectionUtils.isNotEmpty(skuReports)){
                skuReports.forEach(skuReport -> {

                    BigDecimal totalUv = new BigDecimal(0);
                    BigDecimal customerCount = new BigDecimal(0);
                    //一般生近日报表，月报表时 不为null
                    if(MapUtils.isNotEmpty(skuUvMap)){
                        totalUv = BigDecimal.valueOf(skuUvMap.get(skuReport.getId())==null?0:skuUvMap.get(skuReport.getId()));
                        customerCount = BigDecimal.valueOf(skuCustomerMap.get(skuReport.getId())==null?0:skuCustomerMap.get(skuReport.getId()));
                    }

                    //计算下单转换率
                    BigDecimal viewNum = totalUv.longValue()== 0L?BigDecimal.ONE:totalUv;
                    BigDecimal orderConversion = customerCount.divide(viewNum, 4, BigDecimal.ROUND_HALF_UP).multiply(hun).setScale(2, BigDecimal.ROUND_HALF_UP);
                    skuReport.setOrderConversion(orderConversion.compareTo(hun)>0?hun:orderConversion); //最大为100，大于100设为100
                    if(skuReport.getOrderConversion().compareTo(hun) > 0){
                        skuReport.setOrderConversion(hun);
                    }


                });


                infoRequest.setGoodsInfoIds(ids.stream().collect(Collectors.toList()));
                ExcelHelper excelHelper = new ExcelHelper();
                this.setSkuExcel(excelHelper, specDetailNameMap, skuReports);
                try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
                    excelHelper.write(baos);
                    //如果超过一页，文件名后缀增加(索引值)
                    String suffix = StringUtils.EMPTY;
                    if(pageCount > 1){
                        suffix ="(".concat(String.valueOf(i+1)).concat(")");
                    }
                    String fileName = String.format("%s%s.xls",commonFileName, suffix);
                    osdService.uploadExcel(baos , fileName);
                    fileUrl.add(fileName);
                }
            }
        }

        return fileUrl;
    }

    /**
     * 商品CATE生成并上传Excel
     * @param query 条件
     * @return 返回下载链接
     */
    public String generateCateExcel(ExportQuery query) throws Exception {
        LocalDate begDate = DateUtil.parse2Date(query.getDateFrom(), DateUtil.FMT_DATE_1);
        LocalDate endDate = DateUtil.parse2Date(query.getDateTo(), DateUtil.FMT_DATE_1);

        String commonFileName = String.format("goods/cate/%s/%s/%s分类报表_%s-%s.xls", query.getCompanyId(), DateUtil.format(endDate, DateUtil.FMT_MONTH_2), storeService.getStoreName(query) ,query.getDateFrom(), query.getDateTo());
        commonFileName = osdService.getFileRootPath().concat(commonFileName);

       GoodsQueryCriteria criteria = new GoodsQueryCriteria();
        criteria.setBegDate(query.getDateFrom());
        criteria.setEndDate(query.getDateTo());
        if(!Constants.bossId.equals(query.getCompanyId())) {
            criteria.setCompanyId(query.getCompanyId());
        }else{
            criteria.setCompanyId("0");
        }
        List<GoodsCateReport> cateReports = this.goodsCateMapper.queryGoodsCateReportByExport(criteria);
        if(CollectionUtils.isEmpty(cateReports)){
            ExcelHelper excelHelper = new ExcelHelper();
            this.setCateExcel(excelHelper, new ArrayList<>());
            try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
                excelHelper.write(baos);
                osdService.uploadExcel(baos , commonFileName);
            }
            return commonFileName;
        }

        //获取类目详情

        ExcelHelper excelHelper = new ExcelHelper();
        this.setCateExcel(excelHelper,cateReports);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            excelHelper.write(baos);
            osdService.uploadExcel(baos , commonFileName);
            return commonFileName;
        }
    }

    /**
     * 商品品牌生成并上传Excel
     * @param query 条件
     * @return 返回下载链接
     */
    public String generateBrandExcel(ExportQuery query) throws Exception {
        LocalDate begDate = DateUtil.parse2Date(query.getDateFrom(), DateUtil.FMT_DATE_1);
        LocalDate endDate = DateUtil.parse2Date(query.getDateTo(), DateUtil.FMT_DATE_1);

        String commonFileName = String.format("goods/brand/%s/%s/%s品牌报表_%s-%s.xls", query.getCompanyId(), DateUtil.format(endDate, DateUtil.FMT_MONTH_2), storeService.getStoreName(query), query.getDateFrom(), query.getDateTo());
        commonFileName = osdService.getFileRootPath().concat(commonFileName);
        GoodsQueryCriteria criteria = new GoodsQueryCriteria();
        criteria.setBegDate(query.getDateFrom());
        criteria.setEndDate(query.getDateTo());
        if(!Constants.bossId.equals(query.getCompanyId())) {
            criteria.setCompanyId(query.getCompanyId());
        }else{
            criteria.setCompanyId("0");
        }
        List<GoodsBrandReport> list = this.goodsBrandMapper.queryGoodsBrandReportByExport(criteria);
        if(CollectionUtils.isEmpty(list)){
            ExcelHelper excelHelper = new ExcelHelper();
            this.setBrandExcel(excelHelper, new ArrayList<>());
            try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
                excelHelper.write(baos);
                osdService.uploadExcel(baos , commonFileName);
            }
            return commonFileName;
        }

        ExcelHelper excelHelper = new ExcelHelper();
        this.setBrandExcel(excelHelper, list);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            excelHelper.write(baos);
            osdService.uploadExcel(baos , commonFileName);
            return commonFileName;
        }
    }

    /**
     * 商品CATE生成并上传Excel
     * @param query 条件
     * @return 返回下载链接
     */
    public String generateStoreCateExcel(ExportQuery query) throws Exception {
        LocalDate begDate = DateUtil.parse2Date(query.getDateFrom(), DateUtil.FMT_DATE_1);
        LocalDate endDate = DateUtil.parse2Date(query.getDateTo(), DateUtil.FMT_DATE_1);

        String commonFileName = String.format("goods/storecate/%s/%s/%s店铺分类报表_%s-%s.xls", query.getCompanyId(), DateUtil.format(endDate, DateUtil.FMT_MONTH_2) , storeService.getStoreName(query) ,query.getDateFrom(), query.getDateTo());
        commonFileName = osdService.getFileRootPath().concat(commonFileName);
        GoodsQueryCriteria criteria = new GoodsQueryCriteria();
        criteria.setBegDate(query.getDateFrom());
        criteria.setEndDate(query.getDateTo());
        if(!Constants.bossId.equals(query.getCompanyId())) {
            criteria.setCompanyId(query.getCompanyId());
        }else{
            criteria.setCompanyId("0");
        }
        List<GoodsCateReport> cateReports = this.goodsStoreCateMapper.queryGoodsStoreCateReportByExport(criteria);
        if(CollectionUtils.isEmpty(cateReports)){
            ExcelHelper excelHelper = new ExcelHelper();
            this.setCateExcel(excelHelper, new ArrayList<>());
            try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
                excelHelper.write(baos);
                osdService.uploadExcel(baos , commonFileName);
            }
            return commonFileName;
        }

        //获取类目详情

        ExcelHelper excelHelper = new ExcelHelper();
        this.setCateExcel(excelHelper,cateReports);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            excelHelper.write(baos);
            osdService.uploadExcel(baos , commonFileName);
            return commonFileName;
        }
       /* GoodsStoreCatePoolAggsResponse response = goodsStoreCateReportGenerateService.aggsDataPool(begDate, endDate, query.getCompanyId());
        //提取末级类目
        List<GoodsReport> cateReport = response.getGoodsCateReportList().stream()
                .filter(goodsReport -> EsConstants.yes.equals(goodsReport.getIsLeaf()))
                .sorted(Comparator.comparing(GoodsReport::getOrderNum).reversed().thenComparing(Comparator.comparing(GoodsReport::getId)))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(cateReport)){
            ExcelHelper excelHelper = new ExcelHelper();
            this.setCateExcel(excelHelper, null, new ArrayList<>());
            try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
                excelHelper.write(baos);
                aliYunService.uploadExcel(baos , commonFileName);
            }
            return commonFileName;
        }

        List<StoreCate>  storeCates = storeCateMapper.queryByIds(cateReport.stream().map(GoodsReport::getId).collect(Collectors.toList()));
        //获取店铺分类详情
        Map<String, StoreCate> goodsCateMap = storeCates.stream().collect(Collectors.toMap(StoreCate::getId, c->c));

        ExcelHelper excelHelper = new ExcelHelper();
        this.setStoreCateExcel(excelHelper, goodsCateMap, cateReport);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            excelHelper.write(baos);
            aliYunService.uploadExcel(baos , commonFileName);
            return commonFileName;
        }*/
    }

   /* *//**
     * Sku设置Excel信息
     * @param excelHelper
     * @param goodsMap
     * @param skuReports
     *//*
    private void setSkuExcel(ExcelHelper excelHelper, Map<String, GoodsInfo> goodsMap, List<SkuReport> skuReports){
        DecimalFormat format = new DecimalFormat("0.00");
        excelHelper.addSheet("商品销售报表_商品报表", new Column[]{
            new Column("商品名称", (cell, object) -> {
                SkuReport trade = (SkuReport) object;
                GoodsInfo info = (goodsMap==null ? null : goodsMap.get(trade.getId()));
                cell.setCellValue(info == null ? "无" : info.getGoodsInfoName());
            }),
            new Column("SKU编码", (cell, object) -> {
                SkuReport trade = (SkuReport) object;
                GoodsInfo info = (goodsMap==null ? null : goodsMap.get(trade.getId()));
                cell.setCellValue(info == null ? StringUtils.EMPTY : info.getGoodsInfoNo());
            }),
            new Column("规格", (cell, object) -> {
                SkuReport trade = (SkuReport) object;
                GoodsInfo info = (goodsMap==null ? null : goodsMap.get(trade.getId()));
                cell.setCellValue(info == null ? StringUtils.EMPTY : info.getDetailName());
            }),
            new Column("下单笔数", new SpelColumnRender<SkuReport>("orderCount")),
            new Column("下单金额", new SpelColumnRender<SkuReport>("orderAmt")),
            new Column("下单件数", new SpelColumnRender<SkuReport>("orderNum")),
            new Column("付款商品数", new SpelColumnRender<SkuReport>("payNum")),
            new Column("退单件数", new SpelColumnRender<SkuReport>("returnOrderNum")),
            new Column("退单笔数", new SpelColumnRender<SkuReport>("returnOrderCount")),
            new Column("退单金额", new SpelColumnRender<SkuReport>("returnOrderAmt")),
            new Column("单品转化率", (cell, object) -> {
                SkuReport trade = (SkuReport) object;
                cell.setCellValue(format.format(trade.getOrderConversion()).concat("%"));
            }),
        }, skuReports);
    }*/

    /**
     * Sku设置Excel信息
     * @param excelHelper
     * @param specDetailNameMap
     * @param skuReports
     */
    private void setSkuExcel(ExcelHelper excelHelper, Map<String, String> specDetailNameMap, List<SkuReport> skuReports){

        DecimalFormat format = new DecimalFormat("0.00");
        excelHelper.addSheet("商品销售报表_商品报表", new Column[]{
                new Column("商品名称", (cell, object) -> {
                    SkuReport trade = (SkuReport) object;
                    cell.setCellValue(trade.getName() == null ? "无" : trade.getName());}),
                new Column("SKU编码", new SpelColumnRender<SkuReport>("skuNo")),
                new Column("规格", (cell, object) -> {
                    SkuReport trade = (SkuReport) object;
                    String specDetailName = (specDetailNameMap==null ? null : specDetailNameMap.get(trade.getId()));
                    cell.setCellValue(specDetailName == null ? StringUtils.EMPTY : specDetailName);
                }),
                new Column("下单笔数", new SpelColumnRender<SkuReport>("orderCount")),
                new Column("下单金额", new SpelColumnRender<SkuReport>("orderAmt")),
                new Column("下单件数", new SpelColumnRender<SkuReport>("orderNum")),
                new Column("付款商品数", new SpelColumnRender<SkuReport>("payNum")),
                new Column("退单件数", new SpelColumnRender<SkuReport>("returnOrderNum")),
                new Column("退单笔数", new SpelColumnRender<SkuReport>("returnOrderCount")),
                new Column("退单金额", new SpelColumnRender<SkuReport>("returnOrderAmt")),
                new Column("单品转化率", (cell, object) -> {
                    SkuReport trade = (SkuReport) object;
                    cell.setCellValue(format.format(trade.getOrderConversion()).concat("%"));
                }),
        }, skuReports);
    }

    /**
     * 分类设置Excel信息
     * @param excelHelper
     * @param goodsCateMap
     * @param goodsReports
     */
    private void setCateExcel(ExcelHelper excelHelper, Map<String, GoodsCate> goodsCateMap, List<GoodsReport> goodsReports){
        excelHelper.addSheet("商品销售报表_分类报表", new Column[]{
                new Column("分类名称", (cell, object) -> {
                    GoodsReport report = (GoodsReport) object;
                    GoodsCate info = (goodsCateMap == null ? null : goodsCateMap.get(report.getId()));
                    cell.setCellValue(info == null ? "无" : info.getName());
                }),
                new Column("上级分类", (cell, object) -> {
                    GoodsReport report = (GoodsReport) object;
                    GoodsCate info = (goodsCateMap == null ? null : goodsCateMap.get(report.getId()));
                    cell.setCellValue(info == null ? "无" : info.getParentNames());
                }),
                new Column("下单笔数", new SpelColumnRender<GoodsReport>("orderCount")),
                new Column("下单金额", new SpelColumnRender<GoodsReport>("orderAmt")),
                new Column("下单件数", new SpelColumnRender<GoodsReport>("orderNum")),
                new Column("付款商品数", new SpelColumnRender<GoodsReport>("payNum")),
                new Column("退单件数", new SpelColumnRender<GoodsReport>("returnOrderNum")),
                new Column("退单笔数", new SpelColumnRender<GoodsReport>("returnOrderCount")),
                new Column("退单金额", new SpelColumnRender<GoodsReport>("returnOrderAmt"))
        }, goodsReports);
    }


    /**
     * 分类设置Excel信息
     * @param excelHelper
     * @param goodsReports
     */
    private void setCateExcel(ExcelHelper excelHelper, List<GoodsCateReport> goodsReports){
        excelHelper.addSheet("商品销售报表_分类报表", new Column[]{
                new Column("分类名称", (cell, object) -> {
                    GoodsCateReport report = (GoodsCateReport) object;
                    cell.setCellValue(report.getCateName() == null ? "无" : report.getCateName());
                }),
                new Column("上级分类", (cell, object) -> {
                    GoodsCateReport report = (GoodsCateReport) object;
                    cell.setCellValue(report.getCateParentName() == null ? "无" : report.getCateParentName());
                }),
                new Column("下单笔数", new SpelColumnRender<GoodsReport>("orderCount")),
                new Column("下单金额", new SpelColumnRender<GoodsReport>("orderAmt")),
                new Column("下单件数", new SpelColumnRender<GoodsReport>("orderNum")),
                new Column("付款商品数", new SpelColumnRender<GoodsReport>("payNum")),
                new Column("退单件数", new SpelColumnRender<GoodsReport>("returnOrderNum")),
                new Column("退单笔数", new SpelColumnRender<GoodsReport>("returnOrderCount")),
                new Column("退单金额", new SpelColumnRender<GoodsReport>("returnOrderAmt"))
        }, goodsReports);
    }

    /**
     * 品牌设置Excel信息
     * @param excelHelper
     * @param goodsBrandMap
     * @param goodsReports
     */
    private void setBrandExcel(ExcelHelper excelHelper, Map<String, GoodsBrand> goodsBrandMap, List<GoodsReport> goodsReports){
        excelHelper.addSheet("商品销售报表_品牌报表", new Column[]{
                new Column("品牌名称", (cell, object) -> {
                    GoodsReport report = (GoodsReport) object;
                    GoodsBrand info = (goodsBrandMap == null ? null : goodsBrandMap.get(report.getId()));
                    cell.setCellValue(info == null ? "无" : info.getName());
                }),
                new Column("下单笔数", new SpelColumnRender<GoodsReport>("orderCount")),
                new Column("下单金额", new SpelColumnRender<GoodsReport>("orderAmt")),
                new Column("下单件数", new SpelColumnRender<GoodsReport>("orderNum")),
                new Column("付款商品数", new SpelColumnRender<GoodsReport>("payNum")),
                new Column("退单件数", new SpelColumnRender<GoodsReport>("returnOrderNum")),
                new Column("退单笔数", new SpelColumnRender<GoodsReport>("returnOrderCount")),
                new Column("退单金额", new SpelColumnRender<GoodsReport>("returnOrderAmt"))
        }, goodsReports);
    }

    /**
     * 品牌设置Excel信息
     * @param excelHelper
     * @param goodsReports
     */
    private void setBrandExcel(ExcelHelper excelHelper, List<GoodsBrandReport> goodsReports){
        excelHelper.addSheet("商品销售报表_品牌报表", new Column[]{
                new Column("品牌名称", (cell, object) -> {
                    GoodsBrandReport report = (GoodsBrandReport) object;
                    cell.setCellValue(report.getBrandName() == null ? "无" : report.getBrandName());
                }),
                new Column("下单笔数", new SpelColumnRender<GoodsReport>("orderCount")),
                new Column("下单金额", new SpelColumnRender<GoodsReport>("orderAmt")),
                new Column("下单件数", new SpelColumnRender<GoodsReport>("orderNum")),
                new Column("付款商品数", new SpelColumnRender<GoodsReport>("payNum")),
                new Column("退单件数", new SpelColumnRender<GoodsReport>("returnOrderNum")),
                new Column("退单笔数", new SpelColumnRender<GoodsReport>("returnOrderCount")),
                new Column("退单金额", new SpelColumnRender<GoodsReport>("returnOrderAmt"))
        }, goodsReports);
    }

    /**
     * 分类设置Excel信息
     * @param excelHelper
     * @param goodsCateMap
     * @param goodsReports
     */
    private void setStoreCateExcel(ExcelHelper excelHelper, Map<String, StoreCate> goodsCateMap, List<GoodsReport> goodsReports){
        excelHelper.addSheet("商品销售报表_店铺分类报表", new Column[]{
                new Column("分类名称", (cell, object) -> {
                    GoodsReport report = (GoodsReport) object;
                    StoreCate info = (goodsCateMap == null ? null : goodsCateMap.get(report.getId()));
                    cell.setCellValue(info == null ? "无" : info.getCateName());
                }),
                new Column("上级分类", (cell, object) -> {
                    GoodsReport report = (GoodsReport) object;
                    StoreCate info = (goodsCateMap == null ? null : goodsCateMap.get(report.getId()));
                    cell.setCellValue(info == null ? "无" : info.getParentNames());
                }),
                new Column("下单笔数", new SpelColumnRender<GoodsReport>("orderCount")),
                new Column("下单金额", new SpelColumnRender<GoodsReport>("orderAmt")),
                new Column("下单件数", new SpelColumnRender<GoodsReport>("orderNum")),
                new Column("付款商品数", new SpelColumnRender<GoodsReport>("payNum")),
                new Column("退单件数", new SpelColumnRender<GoodsReport>("returnOrderNum")),
                new Column("退单笔数", new SpelColumnRender<GoodsReport>("returnOrderCount")),
                new Column("退单金额", new SpelColumnRender<GoodsReport>("returnOrderAmt"))
        }, goodsReports);
    }

}
