package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.SaleNumBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/10/12:46
 * @Description:
 */
@FeignClient(value = "${application.goods.name}", contextId = "SaleNumProvider")
public interface SaleNumProvider {
    @PostMapping("/goods/${application.goods.version}/saleName/querySaleNum")
    List<Map> queryAllSaleNum();
    @PostMapping("/goods/${application.goods.version}/saleName/importSaleNum")
    BusinessResponse<String> importSaleNum(@RequestBody SaleNumBO saleNumBO);

    @PostMapping("/goods/${application.goods.version}/saleName/updateSaleNum")
    BusinessResponse<Integer> updateSaleNum(@RequestBody SaleNumBO saleNumBO);

    @PostMapping("/goods/${application.goods.version}/saleName/getSaleNum")
    BusinessResponse<List<SaleNumBO>> getSaleNum(@RequestBody SaleNumBO saleNumBO);
}
