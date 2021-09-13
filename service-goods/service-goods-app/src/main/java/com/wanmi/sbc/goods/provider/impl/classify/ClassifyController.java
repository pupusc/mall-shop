package com.wanmi.sbc.goods.provider.impl.classify;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListMixProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.goods.booklistmodel.service.BookListModelService;
import com.wanmi.sbc.goods.classify.service.ClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/7 7:02 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RestController
@Validated
public class ClassifyController implements ClassifyProvider {

    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private BookListModelService bookListModelService;

    /**
     * 获取类目列表
     * @return
     */
    @Override
    public BaseResponse<List<ClassifyProviderResponse>> listClassify() {
        return BaseResponse.success(classifyService.listClassify());
    }

    @Override
    public BaseResponse<List<BookListMixProviderResponse>> listPublishGoodsByIds(Collection<Integer> bookListModelIdCollection){
        return BaseResponse.success(bookListModelService.listPublishGoodsByIds(bookListModelIdCollection, CategoryEnum.BOOK_CLASSIFY));
    }
}
