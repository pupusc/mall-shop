package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.request.collect.CollectBookListModelProviderReq;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelProviderResponse;
import com.wanmi.sbc.goods.api.response.collect.CollectBookListGoodsPublishResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.repository.BookListGoodsPublishRepository;
import com.wanmi.sbc.goods.booklistmodel.model.root.BookListModelDTO;
import com.wanmi.sbc.goods.booklistmodel.repository.BookListModelRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/2 11:36 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class CollectBookListModelService {


    @Autowired
    private BookListGoodsPublishRepository bookListGoodsPublishRepository;

    @Autowired
    private BookListModelRepository bookListModelRepository;


    /**
     * 采集数据，独立于正常的搜索
     * @return
     */
    public List<CollectBookListGoodsPublishResponse> collectBookListGoodsPublishId(CollectBookListModelProviderReq request) {
        List<BookListGoodsPublishDTO> bookListGoodsPublishDTOList = bookListGoodsPublishRepository.
                collectBookListGoodsPublishId(request.getBeginTime(), request.getEndTime(), CategoryEnum.BOOK_LIST_MODEL.getCode(), request.getPageSize());
        List<CollectBookListGoodsPublishResponse> result = new ArrayList<>();
        for (BookListGoodsPublishDTO bookListGoodsPublish : bookListGoodsPublishDTOList) {
            CollectBookListGoodsPublishResponse collectBookListGoodsPublishResponse = new CollectBookListGoodsPublishResponse();
            BeanUtils.copyProperties(bookListGoodsPublish, collectBookListGoodsPublishResponse);
            result.add(collectBookListGoodsPublishResponse);
        }
        return result;
    }

    /**
     * 根据书单id获取发布的书单商品列表
     * @param request
     * @return
     */
    public List<CollectBookListGoodsPublishResponse> collectBookListGoodsPublishIdByBookListIds(CollectBookListModelProviderReq request) {
        List<BookListGoodsPublishDTO> bookListGoodsPublishDTOList =
                bookListGoodsPublishRepository.collectBookListGoodsPublishIdByBookListIds(request.getBookListModelIds());
        List<CollectBookListGoodsPublishResponse> result = new ArrayList<>();
        for (BookListGoodsPublishDTO bookListGoodsPublish : bookListGoodsPublishDTOList) {
            CollectBookListGoodsPublishResponse collectBookListGoodsPublishResponse = new CollectBookListGoodsPublishResponse();
            BeanUtils.copyProperties(bookListGoodsPublish, collectBookListGoodsPublishResponse);
            result.add(collectBookListGoodsPublishResponse);
        }
        return result;
    }


    /**
     * 根据商品id获取发布的书单商品列表
     * @param request
     * @return
     */
    public List<CollectBookListGoodsPublishResponse> collectBookListGoodsPublishIdBySpuIds(CollectBookListModelProviderReq request) {
        List<BookListGoodsPublishDTO> bookListGoodsPublishDTOList =
                bookListGoodsPublishRepository.collectBookListGoodsPublishIdBySpuIds(request.getSpuIds());
        List<CollectBookListGoodsPublishResponse> result = new ArrayList<>();
        for (BookListGoodsPublishDTO bookListGoodsPublish : bookListGoodsPublishDTOList) {
            CollectBookListGoodsPublishResponse collectBookListGoodsPublishResponse = new CollectBookListGoodsPublishResponse();
            BeanUtils.copyProperties(bookListGoodsPublish, collectBookListGoodsPublishResponse);
            result.add(collectBookListGoodsPublishResponse);
        }
        return result;
    }


    /**
     * 采集书单信息
     * @param request
     * @return
     */
    public List<BookListModelProviderResponse> collectBookListId(CollectBookListModelProviderReq request){
        List<BookListModelDTO> bookListModelDTOList =
                bookListModelRepository.collectBookListId(request.getBeginTime(), request.getEndTime(), request.getBusinesstypes(), request.getPageSize());
        List<BookListModelProviderResponse> result = new ArrayList<>();
        for (BookListModelDTO bookListModelDTO : bookListModelDTOList) {
            BookListModelProviderResponse bookListModelProviderResponse = new BookListModelProviderResponse();
            BeanUtils.copyProperties(bookListModelDTO, bookListModelProviderResponse);
            result.add(bookListModelProviderResponse);
        }
        return result;
    }

    /**
     * 根据书单id获取书单列表
     * @param request
     * @return
     */
    public List<BookListModelProviderResponse> collectBookListByBookListIds(CollectBookListModelProviderReq request) {
        List<BookListModelDTO> bookListModelDTOList = bookListModelRepository.collectBookListByBookListIds(request.getBookListModelIds());
        List<BookListModelProviderResponse> result = new ArrayList<>();
        for (BookListModelDTO bookListModelDTO : bookListModelDTOList) {
            BookListModelProviderResponse bookListModelProviderResponse = new BookListModelProviderResponse();
            BeanUtils.copyProperties(bookListModelDTO, bookListModelProviderResponse);
            result.add(bookListModelProviderResponse);
        }
        return result;
    }

}
