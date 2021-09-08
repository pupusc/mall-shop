package com.wanmi.sbc.goods.booklistmodel.service.impl;

import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
import com.wanmi.sbc.goods.booklistmodel.service.BusinessTypeBookListModelAbstract;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description: 排行榜 书单
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/8 4:04 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class RankingBookListModelService extends BusinessTypeBookListModelAbstract {

    public static final Integer MAX_SIZE = 2;

//    @Override

    /**
     * 书单排序
     * @param spuId
     * @return
     */
    @Override
    public List<BookListModelAndOrderNumProviderResponse> listBookListModelAndOrderNum(String spuId) {
        List<BookListGoodsPublishLinkModelResponse> bookListModelDTOList = super.listBookListModelBySpuId(Collections.singletonList(BusinessTypeEnum.RANKING_LIST.getCode()), spuId);
        List<BookListGoodsPublishLinkModelResponse> resultParam;
        if (bookListModelDTOList.size() > MAX_SIZE) {
            resultParam = bookListModelDTOList.subList(0, MAX_SIZE);
        } else {
            resultParam = bookListModelDTOList;
        }

        List<BookListModelAndOrderNumProviderResponse> result = new ArrayList<>();
        //根据id列表获取图书信息
        for (BookListGoodsPublishLinkModelResponse bookListGoodPublishLinkModelParam : resultParam) {
            result.add(super.packageBookListModelAndOrderNumProviderResponse(bookListGoodPublishLinkModelParam));
        }
        return result;
    }



//    public List<BookListModelAndOrderNumProviderResponse> listBookListModelAndGoodsDetail(String spuId){
//        return null;
//    }


//
//
//    public void test() {
//        int businessType = 1; //榜单
//        String spuId = "";
//        String operator = "";
//        //榜单
//        if (Objects.equals(businessType, BusinessTypeEnum.RANKING_LIST.getCode())) {
//            //根据商品获取书单,此处可以获取
//            List<BookListGoodPublishLinkModelResponse> listPublishGoodsAndBookListModel =
//                    bookListGoodsPublishService.listPublishGoodsAndBookListModel(Collections.singletonList(BusinessTypeEnum.RANKING_LIST.getCode()), CategoryEnum.BOOK_LIST_MODEL.getCode(), spuId);
//            if (CollectionUtils.isEmpty(listPublishGoodsAndBookListModel)) {
//                //当前为空 则不推荐榜单
//                return;
//            }
//
//            //只是获取前2个
//            List<BookListGoodPublishLinkModelResponse> result = new ArrayList<>();
//            for (BookListGoodPublishLinkModelResponse bookListGoodPublishLinkModelParam : listPublishGoodsAndBookListModel) {
//                result.add(bookListGoodPublishLinkModelParam);
//                if (result.size() >= 2) {
//                    break;
//                }
//            }
//
//            //根据spuId 获取商品列表 TODO
//        }
//
//        //书单
//        if (Objects.equals(businessType, BusinessTypeEnum.BOOK_LIST.getCode())){
//            //根据商品获取书单,此处可以获取
//            List<BookListGoodPublishLinkModelResponse> listPublishGoodsAndBookListModel =
//                    bookListGoodsPublishService.listPublishGoodsAndBookListModel(Arrays.asList(BusinessTypeEnum.BOOK_LIST.getCode(), BusinessTypeEnum.BOOK_RECOMMEND.getCode()), CategoryEnum.BOOK_LIST_MODEL.getCode(), spuId);
//            if (CollectionUtils.isEmpty(listPublishGoodsAndBookListModel)) {
//                //当前为空  获取导购类目下的书单 TODO
//                return;
//            }
//
//        }
//
//    }


}
