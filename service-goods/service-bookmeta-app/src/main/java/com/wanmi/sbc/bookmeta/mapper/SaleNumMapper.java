package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.bo.SaleNumBO;
import com.wanmi.sbc.bookmeta.entity.SaleNum;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/10/12:51
 * @Description:
 */

@Repository
public interface SaleNumMapper {
    List<Map> getAllSaleNum();
    List<SaleNum> getSaleNum(SaleNumBO saleNum);
    int getSaleNumCount(SaleNumBO saleNum);
    int existSpu(String spuId);
    int existSku(String skuId);
    List<SaleNum> getBySpuAndSku(String spuId, String skuId);
    int update(SaleNum saleNum);

}
