package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.SaleNumBO;
import com.wanmi.sbc.bookmeta.entity.SaleNum;
import com.wanmi.sbc.bookmeta.mapper.SaleNumMapper;
import com.wanmi.sbc.bookmeta.provider.SaleNumProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/10/12:49
 * @Description:
 */
@Validated
@Slf4j
@RestController
public class SaleNumProviderImpl implements SaleNumProvider {
    @Resource
    SaleNumMapper saleNumMapper;

    @Override
    public List<Map> queryAllSaleNum() {
        return saleNumMapper.getAllSaleNum();
    }

    @Override
    @Transactional
    public BusinessResponse<String> importSaleNum(SaleNumBO saleNumBO) {
        int updateCount = 0;
        try {
            if (StringUtils.isNotBlank(saleNumBO.getSkuId())) {
                boolean skuExit = saleNumMapper.existSku(saleNumBO.getSkuId()) > 0;
                if (skuExit) {
                    SaleNum saleNum = new SaleNum();
                    saleNum.setSkuId(saleNumBO.getSkuId());
                    saleNum.setSalesNum(StringUtils.isBlank(String.valueOf(saleNumBO.getSalesNum())) ? 0 : saleNumBO.getSalesNum());
                    saleNum.setFixPrice(saleNumBO.getFixPrice());
                    saleNumMapper.update(saleNum);
                    updateCount++;
                }
            }
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "importSaleNum",
                    Objects.isNull(saleNumBO) ? "" : JSON.toJSONString(saleNumBO),
                    e);
        }
        return BusinessResponse.success("Success,update" + updateCount + "!");
    }

    @Override
    public BusinessResponse<Integer> updateSaleNum(SaleNumBO saleNumBO) {
        int update = 0;
        try {
            update = saleNumMapper.update(KsBeanUtil.convert(saleNumBO, SaleNum.class));
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "importSaleNum",
                    Objects.isNull(saleNumBO) ? "" : JSON.toJSONString(saleNumBO),
                    e);
        }
        return BusinessResponse.success(update);
    }

    @Override
    public BusinessResponse<List<SaleNumBO>> getSaleNum(SaleNumBO saleNumBO) {
        Page page = saleNumBO.getPage();
        List<SaleNumBO> saleNumBOS = new ArrayList<>();
        try {
            page.setTotalCount((int) saleNumMapper.getSaleNumCount(saleNumBO));
            if (page.getTotalCount() <= 0) {
                return BusinessResponse.success(Collections.EMPTY_LIST, page);
            }
            saleNumBO.setLimitIndex(page.getOffset());
            saleNumBO.setLimitSize(page.getPageSize());
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "getSaleNumCount",
                    Objects.isNull(saleNumBO) ? "" : JSON.toJSONString(saleNumBO),
                    e);
        }
        try {
            List<SaleNum> saleNum = saleNumMapper.getSaleNum(saleNumBO);
            saleNumBOS = KsBeanUtil.convertList(saleNum, SaleNumBO.class);
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "getSaleNum",
                    Objects.isNull(saleNumBO) ? "" : JSON.toJSONString(saleNumBO),
                    e);
        }
        return BusinessResponse.success(saleNumBOS, page);
    }
}
