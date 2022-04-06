package com.wanmi.sbc.goods.provider.impl.blacklist;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.request.blacklist.*;
import com.wanmi.sbc.goods.api.request.blacklist.response.GoodsBlackListData;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.blacklist.model.root.GoodsBlackListDTO;
import com.wanmi.sbc.goods.blacklist.service.GoodsBlackListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/23 6:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@Slf4j
public class GoodsBlackListController implements GoodsBlackListProvider {

    @Autowired
    private GoodsBlackListService goodsBlackListService;

    /**
     * 新增黑名单
     * @param goodsBlackListProviderRequest
     * @return
     */
    @Override
    public BaseResponse add(GoodsBlackListProviderRequest goodsBlackListProviderRequest) {
        goodsBlackListService.add(goodsBlackListProviderRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除黑名单
     * @param id
     */
    @Override
    public BaseResponse delete(Integer id){
        goodsBlackListService.delete(id);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse update(GoodsBlackListCreateOrUpdateRequest goodsBlackListCreateOrUpdateRequest){
        goodsBlackListService.update(goodsBlackListCreateOrUpdateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 刷新黑名单到缓存
     * @param goodsBlackListPageProviderRequest
     * @return
     */
    @Override
    public BaseResponse<GoodsBlackListPageProviderResponse> flushBlackListCache(GoodsBlackListCacheProviderRequest goodsBlackListPageProviderRequest) {
        GoodsBlackListPageProviderResponse goodsBlackListPageProviderResponse =
                goodsBlackListService.flushBlackListCache(goodsBlackListPageProviderRequest);
        return BaseResponse.success(goodsBlackListPageProviderResponse);
    }

    /**
     * 获取黑名单列表
     * @param goodsBlackListCacheProviderRequest
     * @return
     */
    @Override
    public BaseResponse<List<GoodsBlackListData>> list(@RequestBody GoodsBlackListCacheProviderRequest goodsBlackListCacheProviderRequest) {
        List<GoodsBlackListDTO> blackListModels = goodsBlackListService.listSimpleNoPage(goodsBlackListCacheProviderRequest);
        List<GoodsBlackListData> dataList = blackListModels.stream().map(blackListModel -> {
            GoodsBlackListData goodsBlackListData = new GoodsBlackListData();
            BeanUtils.copyProperties(blackListModel, goodsBlackListData);
//            goodsBlackListData.setCreateTime(blackListModel.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return goodsBlackListData;
        }).collect(Collectors.toList());
        return BaseResponse.success(dataList);
    }

    /**
     * 获取黑名单列表
     * @param goodsBlackListPageProviderRequest
     * @return
     */
    @Override
    public BaseResponse<GoodsBlackListPageProviderResponse> listNoPage(GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest) {
        GoodsBlackListPageProviderResponse goodsBlackListPageProviderResponse =
                goodsBlackListService.listNoPage(goodsBlackListPageProviderRequest);
        return BaseResponse.success(goodsBlackListPageProviderResponse);
    }
}
