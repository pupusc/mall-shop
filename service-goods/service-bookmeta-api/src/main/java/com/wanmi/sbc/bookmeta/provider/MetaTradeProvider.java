package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaTradeBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/16:32
 * @Description:
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaTradeProvider")
public interface MetaTradeProvider {
    @PostMapping("/goods/${application.goods.version}/metaTrade/tree")
    List<MetaTradeBO> getMetaTadeTree(@RequestBody @NotNull int parentId);

    @PostMapping("/goods/${application.goods.version}/metaTrade/add")
    int addMetaTade(@RequestBody @NotNull MetaTradeBO metaTradeBO);

    @PostMapping("/goods/${application.goods.version}/metaTrade/update")
    int updateMetaTade(@RequestBody @NotNull MetaTradeBO metaTradeBO);

    @PostMapping("/goods/${application.goods.version}/metaTrade/delete")
    int deleteMetaTade(@RequestBody @NotNull int id);
}
