package com.wanmi.ares.report.goods.service;

import com.wanmi.ares.report.goods.dao.GoodsTotalMapper;
import com.wanmi.ares.source.service.StoreService;
import com.wanmi.ares.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 商品概览报表服务
 * Created by dyt on 2017/9/21.
 */
@Service
public class GoodsTotalGenerateService {

    @Autowired
    private GoodsTotalService goodsTotalService;

    @Autowired
    private GoodsTotalMapper goodsReportMapper;

    @Autowired
    private StoreService storeService;

    /**
     * 生成商品概览
     * 仅支持昨日，内容为商品总数、上架商品数
     */
//    public void generate(){
//        String yesterday = DateUtil.format(LocalDateTime.now().minusDays(1), DateUtil.FMT_DATE_1);
//
//        //提取所有正常的店铺ID
//        Set<String> companyIds = storeService.queryOpenStore(yesterday, null).stream().map(Store::getCompanyInfoId).collect(Collectors.toSet());
//        //提取正常店铺的在销售商品数
//        Map<String, Long> saleTotals = new HashMap<>();
//        if(CollectionUtils.isNotEmpty(companyIds)){
//            Map<String, Long> t_saleTotals = goodsTotalService.getSaleTotalGroupCompany(companyIds, yesterday);
//            if(MapUtils.isNotEmpty(t_saleTotals)) {
//                saleTotals.putAll(t_saleTotals);
//            }
//        }
//
//        List<GoodsTotalResponse> goodsTotals = goodsTotalService.aggsGroupCompany(yesterday);
//
//        //累积平台数据
//        Long total = 0L;
//        Long addedTotal = 0L;
//        Long checkTotal = 0L;
//        Long saleTotal = 0L;
//
//        if(CollectionUtils.isNotEmpty(goodsTotals)){
//            for(GoodsTotalResponse goodsTotal: goodsTotals) {
//                goodsTotal.setId(goodsTotal.getCompanyId().concat("1"));
//                goodsTotal.setDate(yesterday);
//                //填充销售中商品数
//                goodsTotal.setSaleTotal(saleTotals.getOrDefault(goodsTotal.getCompanyId(), 0L));
//
//                if(goodsTotal.getTotal() != null && goodsTotal.getTotal() > 0) {
//                    total = total + goodsTotal.getTotal();
//                }
//
//                if(goodsTotal.getAddedTotal() != null && goodsTotal.getAddedTotal() > 0) {
//                    addedTotal = addedTotal + goodsTotal.getAddedTotal();
//                }
//
//                if(goodsTotal.getCheckedTotal() != null && goodsTotal.getCheckedTotal() > 0) {
//                    checkTotal = checkTotal + goodsTotal.getCheckedTotal();
//                }
//
//                if(goodsTotal.getSaleTotal() != null && goodsTotal.getSaleTotal() > 0) {
//                    saleTotal = saleTotal + goodsTotal.getSaleTotal();
//                }
//            }
//            goodsReportMapper.saveGoodsTotal(goodsTotals);
//        }
//        goodsTotals.clear();
//
//        //继续保存全局数据
//        GoodsTotalResponse goodsTotalResponse = new GoodsTotalResponse();
//        goodsTotalResponse.setId(Constants.bossId.concat("1"));
//        goodsTotalResponse.setDate(yesterday);
//        goodsTotalResponse.setCompanyId(Constants.bossId);
//        goodsTotalResponse.setTotal(total);
//        goodsTotalResponse.setAddedTotal(addedTotal);
//        goodsTotalResponse.setCheckedTotal(checkTotal);
//        goodsTotalResponse.setSaleTotal(saleTotal);
//        goodsReportMapper.saveGoodsTotal(Collections.singletonList(goodsTotalResponse));
//    }

    public void generate(int type){
        LocalDate statDate = null;
        switch (type){
            case Constants.GoodsGenerateType.TODAY:
                statDate = LocalDate.now();
                break;
            case Constants.GoodsGenerateType.YESTERDAY:
                statDate = LocalDate.now().minusDays(1);
                break;
        }
        if(statDate!=null) {
            this.goodsReportMapper.deleteGoodsTotalForSelect(statDate);
            this.goodsReportMapper.saveGoodsTotalForSelect(statDate);
        }
    }
}
