package com.wanmi.sbc.goods.provider.impl.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateProvider;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateAddRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateBatchModifySortRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateDeleteByIdRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateModifyRequest;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateAddResponse;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateDeleteByIdResponse;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateModifyResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.cate.request.GoodsCateSaveRequest;
import com.wanmi.sbc.goods.cate.request.GoodsCateSortRequest;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * com.wanmi.sbc.goods.provider.impl.cate.GoodsCateController
 *
 * @author lipeng
 * @dateTime 2018/11/7 下午3:17
 */
@RestController
@Validated
public class GoodsCateController implements GoodsCateProvider {

    @Autowired
    private GoodsCateService goodsCateService;

    /**
     * 新增商品分类
     *
     * @param request {@link GoodsCateAddRequest}
     * @return 新增结果 {@link GoodsCateAddResponse}
     */
    @Override
    public BaseResponse<GoodsCateAddResponse> add(@RequestBody @Valid GoodsCateAddRequest request) {
        GoodsCateSaveRequest cateSaveRequest = KsBeanUtil.convert(request, GoodsCateSaveRequest.class);
        return BaseResponse.success(new GoodsCateAddResponse(KsBeanUtil.convert(goodsCateService.add(cateSaveRequest), GoodsCateVO.class)));
    }

    /**
     * 修改商品分类
     *
     * @param request {@link GoodsCateModifyRequest}
     * @return 修改结果 {@link GoodsCateModifyResponse}
     */
    @Override
    public BaseResponse<GoodsCateModifyResponse> modify(@RequestBody @Valid GoodsCateModifyRequest request) {
        List<GoodsCateVO> voList = KsBeanUtil.convert(goodsCateService.edit(request), GoodsCateVO.class);
        return BaseResponse.success(GoodsCateModifyResponse.builder().goodsCateListVOList(voList).build());
    }

    /**
     * 根据编号删除商品分类
     *
     * @param request {@link GoodsCateDeleteByIdRequest}
     * @return 删除的分类编号列表 {@link GoodsCateDeleteByIdResponse}
     */
    @Override
    public BaseResponse<GoodsCateDeleteByIdResponse> deleteById(@RequestBody @Valid GoodsCateDeleteByIdRequest request) {
        Long cateId = request.getCateId();
        List<Long> delIdList = goodsCateService.delete(cateId);

        GoodsCateDeleteByIdResponse response = new GoodsCateDeleteByIdResponse();
        response.setDelIdList(delIdList);
        return BaseResponse.success(response);
    }

    /**
     * 批量修改分类排序
     *
     * @param request 批量分类排序信息结构 {@link GoodsCateBatchModifySortRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override

    public BaseResponse batchModifySort(@RequestBody @Valid GoodsCateBatchModifySortRequest request){
        goodsCateService.dragSort(KsBeanUtil.convert(request.getGoodsCateSortDTOList(), GoodsCateSortRequest.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 初始化商品分类信息
     *
     */
    @Override
    public BaseResponse init() {
        goodsCateService.init();
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse synchronizePointsRule() {
        goodsCateService.synchronizePointsRule();
        return BaseResponse.SUCCESSFUL();
    }
}
