package com.wanmi.sbc.goods.booklistmodel.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.enums.BusinessTypeEnum;
import com.wanmi.sbc.goods.api.response.booklistmodel.BookListModelAndOrderNumProviderResponse;
import com.wanmi.sbc.goods.booklistmodel.service.impl.BookRecommendBookListModelService;
import com.wanmi.sbc.goods.booklistmodel.service.impl.RankingBookListModelService;
import com.wanmi.sbc.goods.booklistmodel.service.impl.SpecialBookListModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/9 8:52 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class BusinessTypeBookListModelFactory {

    @Resource
    private RankingBookListModelService rankingBookListModelService;

    @Resource
    private BookRecommendBookListModelService bookRecommendBookListModelService;

    @Resource
    private SpecialBookListModelService specialBookListModelService;

    /**
     * 获取推荐书单
     * @param businessTypeEnum
     * @param spuId
     * @return
     */
    public BusinessTypeBookListModelAbstract newInstance(BusinessTypeEnum businessTypeEnum) {
        log.info("---->> BusinessTypeBookListModelFactory.newInstance, businessType is {}", businessTypeEnum);
        BusinessTypeBookListModelAbstract result;
        if (businessTypeEnum == BusinessTypeEnum.RANKING_LIST) {
            result = rankingBookListModelService;
        } else if (businessTypeEnum == BusinessTypeEnum.BOOK_LIST
                    || businessTypeEnum == BusinessTypeEnum.BOOK_RECOMMEND) {
            result = bookRecommendBookListModelService;
        } else if (businessTypeEnum == BusinessTypeEnum.SPECIAL_SUBJECT) {
            result = specialBookListModelService;
        } else {
            throw new SbcRuntimeException(String.format("当前获取推荐书单,businessType: %s 中的 businessType 不存在", businessTypeEnum));
        }
        return result;
    }

}
