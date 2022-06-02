package com.soybean.elastic.collect.service.booklistmodel.service;

import com.soybean.elastic.booklistmodel.model.EsBookListModel;
import com.soybean.elastic.collect.service.booklistmodel.AbstractBookListModelCollect;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.collect.CollectBookListModelProvider;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.CollectBookListModelProviderRequest;
import com.wanmi.sbc.goods.api.response.collect.CollectBookListGoodsPublishResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 10:44 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class BookListModelCollect extends AbstractBookListModelCollect {


    @Override
    public Set<Long> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {

        return null;
    }

    @Override
    public <F> List<F> collect(List<F> list) {
        List<Integer> bookListModelIdList = new ArrayList<>();
        for (F f : list) {
            EsBookListModel esBookListModel = (EsBookListModel) f;
            bookListModelIdList.add(esBookListModel.getBookListId().intValue());
        }

        //根据id获取书单信息

        //根据id获取商品信息

        return list;
    }
}
