package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.SaleNumBO;
import com.wanmi.sbc.bookmeta.entity.SaleNum;
import com.wanmi.sbc.bookmeta.mapper.SaleNumMapper;
import com.wanmi.sbc.bookmeta.provider.SaleNumProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.util.KsBeanUtil;
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
        if (StringUtils.isBlank(saleNumBO.getSkuId())) {
            return BusinessResponse.error("failed,SKU_ID can't be blank");
        }
            boolean skuExit = saleNumMapper.existSku(saleNumBO.getSkuId()) > 0;
            if (skuExit) {
                    SaleNum saleNum = new SaleNum();
                    saleNum.setSkuId(saleNumBO.getSkuId());
                    saleNum.setSalesNum(StringUtils.isBlank(String.valueOf(saleNumBO.getSalesNum())) ? 0 : saleNumBO.getSalesNum());
                    saleNum.setFixPrice(saleNumBO.getFixPrice());
                    updateCount = saleNumMapper.update(saleNum);
            } else {
                return BusinessResponse.error("failed,SKU_ID is not exist");
            }
        return BusinessResponse.success("Success,update" + updateCount + "!");
    }

    @Override
    public BusinessResponse<Integer> updateSaleNum(SaleNumBO saleNumBO) {
        int update = saleNumMapper.update(KsBeanUtil.convert(saleNumBO, SaleNum.class));
        return BusinessResponse.success(update);
    }

    @Override
    public BusinessResponse<List<SaleNumBO>> getSaleNum(SaleNumBO saleNumBO) {
        Page page = saleNumBO.getPage();
        page.setTotalCount((int) saleNumMapper.getSaleNumCount(saleNumBO));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        saleNumBO.setLimitIndex(page.getOffset());
        saleNumBO.setLimitSize(page.getPageSize());
        List<SaleNum> saleNum = saleNumMapper.getSaleNum(saleNumBO);
        List<SaleNumBO> saleNumBOS = KsBeanUtil.convertList(saleNum, SaleNumBO.class);
        return BusinessResponse.success(saleNumBOS,page);
    }
}
