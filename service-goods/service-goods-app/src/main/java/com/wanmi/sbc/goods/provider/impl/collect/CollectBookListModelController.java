package com.wanmi.sbc.goods.provider.impl.collect;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.collect.CollectBookListModelProvider;
import com.wanmi.sbc.goods.api.request.collect.CollectBookListModelProviderReq;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.collect.CollectBookListGoodsPublishResponse;
import com.wanmi.sbc.goods.collect.CollectBookListModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/3 1:50 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@Slf4j
public class CollectBookListModelController implements CollectBookListModelProvider {

    @Autowired
    private CollectBookListModelService collectBookListModelService;

    @Override
    public BaseResponse<List<CollectBookListGoodsPublishResponse>> collectBookListGoodsPublishId(@RequestBody CollectBookListModelProviderReq request){
        return BaseResponse.success(collectBookListModelService.collectBookListGoodsPublishId(request));
    }


    @Override
    public BaseResponse<List<CollectBookListGoodsPublishResponse>> collectBookListGoodsPublishIdByBookListIds(@RequestBody CollectBookListModelProviderReq request){
        return BaseResponse.success(collectBookListModelService.collectBookListGoodsPublishIdByBookListIds(request));
    }


    @Override
    public BaseResponse<List<CollectBookListGoodsPublishResponse>> collectBookListGoodsPublishIdBySpuIds(@RequestBody CollectBookListModelProviderReq request){
        return BaseResponse.success(collectBookListModelService.collectBookListGoodsPublishIdBySpuIds(request));
    }


    @Override
    public BaseResponse<List<BookListModelProviderResponse>> collectBookListId(@RequestBody CollectBookListModelProviderReq request){
        return BaseResponse.success(collectBookListModelService.collectBookListId(request));
    }


    @Override
    public BaseResponse<List<BookListModelProviderResponse>> collectBookListByBookListIds(@RequestBody CollectBookListModelProviderReq request){
        return BaseResponse.success(collectBookListModelService.collectBookListByBookListIds(request));
    }

}
