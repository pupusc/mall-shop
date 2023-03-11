package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.SaleNumBO;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
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
        int addCount = 0;
        Map map = new HashMap<>();
        if (StringUtils.isBlank(saleNumBO.getSpuId())) {
            return BusinessResponse.error("操作失败,SPU_ID不可为空");
        }
        if (StringUtils.isBlank(saleNumBO.getSkuId())) {
            return BusinessResponse.error("操作失败,SKU_ID不可为空");
        }
        boolean spuExit = saleNumMapper.existSpu(saleNumBO.getSpuId()) > 0;
        if (spuExit) {
            boolean skuExit = saleNumMapper.existSku(saleNumBO.getSkuId()) > 0;
            if (skuExit) {
                List<SaleNum> saleNums = saleNumMapper.getBySpuAndSku(saleNumBO.getSpuId(), saleNumBO.getSkuId());
                if (null != saleNums && saleNums.size() > 0) {//该数据已存在，更新该条数据
                    SaleNum saleNum = saleNums.get(0);
                    saleNum.setSalesNum(StringUtils.isBlank(String.valueOf(saleNumBO.getSalesNum())) ? 0 : saleNumBO.getSalesNum());
                    saleNum.setFixPrice(saleNumBO.getFixPrice());
                    updateCount = saleNumMapper.update(saleNum);
                }
            } else {
                return BusinessResponse.error("操作失败,不存在该SKU_ID");
            }
        } else {
            return BusinessResponse.error("操作失败,不存在该SPU_ID");
        }
        return BusinessResponse.success("操作成功,成功更新" + updateCount + "!");
    }

    @Override
    public BusinessResponse<Integer> updateSaleNum(SaleNumBO saleNumBO) {
        return BusinessResponse.success(saleNumMapper.update(KsBeanUtil.convert(saleNumBO, SaleNum.class)));
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
        return BusinessResponse.success(saleNumBOS);
    }
}
