package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.*;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/03/16:45
 * @Description:
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsSearchKeyProvider")
public interface GoodsSearchKeyProvider {

    @PostMapping("/goods/${application.goods.version}/goodsSearchKey/getGoodsNameBySpuId/")
    List<GoodsNameBySpuIdBO> getGoodsNameBySpuId(@RequestBody @NotNull String name);

    @PostMapping("/goods/${application.goods.version}/goodsSearchKey/getAll")
    BusinessResponse<List<GoodsNameBySpuIdBO>> getAllGoodsSearchKey(@RequestBody @NotNull GoodsNameBySpuIdBO goodsNameBySpuIdBO);

    @PostMapping("/goods/${application.goods.version}/goodsSearchKey/add")
    int addGoodsSearchKey(@RequestBody @NotNull GoodsSearchKeyAddBo goodsSearchKeyAddBo);

    @PostMapping("/goods/${application.goods.version}/goodsSearchKey/update")
    int updateGoodsSearchKey(@RequestBody @NotNull GoodsSearchKeyAddBo goodsSearchKeyAddBo);

    @PostMapping("/goods/${application.goods.version}/goodsSearchKey/delete")
    int deleteGoodsSearchKey(@RequestBody @NotNull GoodsNameBySpuIdBO goodsNameBySpuIdBO);

    @PostMapping("/goods/${application.goods.version}/goodsSearchKey/goodsList")
    BusinessResponse<List<GoodsBO>> getGoodsList(@RequestBody @NotNull GoodsNameBySpuIdBO goodsNameBySpuIdBO);
    @PostMapping("/goods/${application.goods.version}/goodsSearchKey/downloadQuery")
    List<GoodsKeyWordsDownLoadBO> downloadQuery();
    @PostMapping("/goods/${application.goods.version}/goodsSearchKey/importGoodsSearchKey")
    BusinessResponse<String> importGoodsSearchKey(@RequestBody GoodsSearchKeyAddBo goodsSearchKeyAddBo);

}
