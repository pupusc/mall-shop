package com.wanmi.sbc.goods.provider.impl.classify;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
import com.wanmi.sbc.goods.api.response.classify.ClassifyGoodsProviderResponse;
import com.wanmi.sbc.goods.api.response.classify.ClassifyProviderResponse;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.classify.model.root.ClassifyGoodsRelDTO;
import com.wanmi.sbc.goods.classify.service.ClassifyGoodsRelService;
import com.wanmi.sbc.goods.classify.service.ClassifyService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private ClassifyGoodsRelService classifyGoodsRelService;


//    @Autowired
//    private BookListModelService bookListModelService;

    /**
     * 获取类目列表
     * @return
     */
    @Override
    public BaseResponse<List<ClassifyProviderResponse>> listClassify() {
        return BaseResponse.success(classifyService.listClassify());
    }


    /**
     * 根据 商品id 获取商品所在分类的 父分类下的所有 子分类对应的商品列表
     * @param goodsId
     * @return
     */
    @Override
    public BaseResponse<List<ClassifyGoodsProviderResponse>> listGoodsIdOfChildOfParentByGoodsId(String goodsId) {
        List<ClassifyGoodsProviderResponse> result = new ArrayList<>();
        List<ClassifyGoodsRelDTO> classifyGoodsRelList = classifyGoodsRelService.listClassifyIdByGoodsId(Collections.singletonList(goodsId));
        if (CollectionUtils.isEmpty(classifyGoodsRelList)) {
            return BaseResponse.success(result);
        }
        List<Integer> classifyIdList = classifyGoodsRelList.stream().map(ClassifyGoodsRelDTO::getClassifyId).collect(Collectors.toList());
        List<ClassifyDTO> classifyModelList = classifyService.listNoPage(classifyIdList);
        if (CollectionUtils.isEmpty(classifyModelList)) {
            return BaseResponse.success(result);
        }
        //根据父id获取子分类列表
        Set<Integer> classifyParentIdSet = classifyModelList.stream().map(ClassifyDTO::getParentId).collect(Collectors.toSet());
        List<ClassifyDTO> classifyAllChildOfParentList = classifyService.listChildClassifyNoPageByParentId(classifyParentIdSet);
        //根据子分类id 获取商品列表
        if (CollectionUtils.isEmpty(classifyAllChildOfParentList)) {
            return BaseResponse.success(result);
        }
        //根据所有子分类获取商品列表
        Set<Integer> classifyIdOfAllChildSet = classifyAllChildOfParentList.stream().map(ClassifyDTO::getId).collect(Collectors.toSet());
        List<ClassifyGoodsRelDTO> classifyGoodsRelResultList = classifyGoodsRelService.listClassifyRelByClassifyId(classifyIdOfAllChildSet);
        if (CollectionUtils.isEmpty(classifyGoodsRelResultList)){
            return BaseResponse.success(result);
        }
        classifyGoodsRelResultList.forEach(ex -> {
            ClassifyGoodsProviderResponse response = new ClassifyGoodsProviderResponse();
            response.setGoodsId(ex.getGoodsId());
            response.setClassifyId(ex.getClassifyId());
            result.add(response);
        });
        return BaseResponse.success(result);
    }

//    @Override
//    public BaseResponse<List<BookListMixProviderResponse>> listPublishGoodsByIds(Collection<Integer> bookListModelIdCollection){
//        return BaseResponse.success(bookListModelService.listPublishGoodsByModelIds(bookListModelIdCollection, CategoryEnum.BOOK_CLASSIFY));
//    }
}