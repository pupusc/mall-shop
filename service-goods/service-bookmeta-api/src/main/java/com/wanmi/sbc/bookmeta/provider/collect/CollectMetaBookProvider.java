package com.wanmi.sbc.bookmeta.provider.collect;


import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaBookResp;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaReq;
import com.wanmi.sbc.bookmeta.bo.collect.CollectMetaResp;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "${application.goods.name}", contextId = "CollectMetaBookProvider")
public interface CollectMetaBookProvider {

    /**
     * 奖项信息
     * @param collectMetaReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaAwardByTime")
    BaseResponse<CollectMetaResp> collectMetaAwardByTime(@RequestBody CollectMetaReq collectMetaReq);

    /**
     *
     * 丛书
     * @param collectMetaReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaBookClumpByTime")
    BaseResponse<CollectMetaResp> collectMetaBookClumpByTime(@RequestBody CollectMetaReq collectMetaReq);

    /**
     * 简介
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaBookContentByTime")
    BaseResponse<CollectMetaResp> collectMetaBookContentByTime(@RequestBody CollectMetaReq collectMetaReq);

    /**
     * 书组信息
     * @param collectMetaReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaBookGroupByTime")
    BaseResponse<CollectMetaResp> collectMetaBookGroupByTime(@RequestBody CollectMetaReq collectMetaReq);

    /**
     * 图书标签关联信息
     * @param collectMetaReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaBookLabelByTime")
    BaseResponse<CollectMetaResp> collectMetaBookLabelByTime(@RequestBody CollectMetaReq collectMetaReq);

    /**
     * 出品方
     * @param collectMetaReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaBookProducerByTime")
    BaseResponse<CollectMetaResp> collectMetaBookProducerByTime(@RequestBody CollectMetaReq collectMetaReq);


    /**
     * 出版社
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaBookPublisherByTime")
    BaseResponse<CollectMetaResp> collectMetaBookPublisherByTime(@RequestBody CollectMetaReq collectMetaReq);

    /**
     * 图书信息
     * @param collectMetaReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaBookByTime")
    BaseResponse<CollectMetaResp> collectMetaBookByTime(@RequestBody CollectMetaReq collectMetaReq);

    /**
     * 作者关联信息信息
     * @param collectMetaReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaFigureRelByTime")
    BaseResponse<CollectMetaResp> collectMetaFigureRelByTime(@RequestBody CollectMetaReq collectMetaReq);

    /**
     * 作者信息
     * @param collectMetaReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaFigureByTime")
    BaseResponse<CollectMetaResp> collectMetaFigureByTime(@RequestBody CollectMetaReq collectMetaReq);

    /**
     * 标签信息
     * @param collectMetaReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaLabelByTime")
    BaseResponse<CollectMetaResp> collectMetaLabelByTime(@RequestBody CollectMetaReq collectMetaReq);

    /**
     * 正向采集图书信息
     * @param collectMetaReq
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/collect/collectMetaBook")
    BaseResponse<List<CollectMetaBookResp>> collectMetaBook(@RequestBody CollectMetaReq collectMetaReq);
}
