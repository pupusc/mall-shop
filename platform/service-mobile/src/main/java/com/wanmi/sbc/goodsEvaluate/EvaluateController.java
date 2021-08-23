package com.wanmi.sbc.goodsEvaluate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.util.SensitiveUtils;
import com.wanmi.sbc.customer.api.provider.quicklogin.ThirdLoginRelationQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerIdsListRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerIdsListResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailWithImgVO;
import com.wanmi.sbc.goods.api.provider.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodsevaluate.GoodsEvaluateQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodsevaluateimage.GoodsEvaluateImageQueryProvider;
import com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise.CustomerGoodsEvaluatePraiseQueryRequest;
import com.wanmi.sbc.goods.api.request.goodsevaluate.GoodsEvaluateCountBySkuIdRequset;
import com.wanmi.sbc.goods.api.request.goodsevaluate.GoodsEvaluateCountRequset;
import com.wanmi.sbc.goods.api.request.goodsevaluate.GoodsEvaluatePageRequest;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateCountResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateListResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluatePageResponse;
import com.wanmi.sbc.goods.bean.vo.CustomerGoodsEvaluatePraiseVO;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateVO;
import com.wanmi.sbc.goodsEvaluate.response.GoodsDetailEvaluateTop3Resp;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @program: sbc-micro-service
 * @description: 商品详情评价相关服务
 * @create: 2019-05-20 15:27
 **/
@RestController
@RequestMapping(value = "/goodsDetailEvaluate")
@Api(tags = "EvaluateController", description = "商品详情评价相关服务")
public class EvaluateController {

    @Autowired
    GoodsEvaluateQueryProvider goodsEvaluateQueryProvider;

    @Autowired
    GoodsEvaluateImageQueryProvider imageQueryProvider;

    @Autowired
    private CustomerGoodsEvaluatePraiseQueryProvider customerGoodsEvaluatePraiseQueryProvider;


    @Autowired
    private ThirdLoginRelationQueryProvider thirdLoginRelationQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * @param goodsId
     * @Description: 获取某店铺某商品评价总数数量、好评率、top3评价信息
     * @Author: Bob
     * @Date: 2019-05-21 15:10
     */
    @ApiOperation(value = "获取某店铺某商品评价总数数量、好评率、top3评价信息")
    @RequestMapping(value = "/top3EvaluateAndPraise/{goodsId}", method = RequestMethod.GET)
    public BaseResponse<GoodsDetailEvaluateTop3Resp> top3EvaluateAndPraise(@PathVariable String goodsId) {

        GoodsEvaluateCountRequset request =
                GoodsEvaluateCountRequset.builder().goodsId(goodsId).build();
        //商品评价总数、晒单、好评率
        GoodsEvaluateCountResponse response = goodsEvaluateQueryProvider.getGoodsEvaluateSum(request).getContext();

        //top3评价
        GoodsEvaluatePageRequest pageRequest = GoodsEvaluatePageRequest.builder()
                .goodsId(goodsId)
                .delFlag(DeleteFlag.NO.toValue())
                .isShow(1)
                .build();
        pageRequest.setPageSize(3);
        pageRequest.setSortColumn("evaluateTime");
        pageRequest.setSortRole(SortType.DESC.toValue());
        GoodsEvaluateListResponse listResponse =
                goodsEvaluateQueryProvider.getGoodsEvaluateTopData(pageRequest).getContext();
        listResponse.getGoodsEvaluateVOList().forEach(goodsEvaluateVO -> {
            desensitization(goodsEvaluateVO);
        });
        // 评价其他信息-用户头像
        wrapEvaluateInfo(listResponse.getGoodsEvaluateVOList());
        return BaseResponse.success(GoodsDetailEvaluateTop3Resp.builder()
                .goodsEvaluateCountResponse(response)
                .listResponse(listResponse)
                .build());
    }


    /**
     * @param skuId
     * @Description: 获取某店铺某商品评价总数数量、好评率、top3评价信息
     * @Author: Bob
     * @Date: 2019-05-21 15:10
     */
    @ApiOperation(value = "获取某店铺某商品评价总数数量、好评率、top3评价信息")
    @RequestMapping(value = "/top3EvaluateAndPraiseBySkuId/{skuId}", method = RequestMethod.GET)
    public BaseResponse<GoodsDetailEvaluateTop3Resp> top3EvaluateAndPraiseBySkuId(@PathVariable String skuId) {
        if (StringUtils.isEmpty(skuId)) {
            return BaseResponse.error("skuId error");
        }

        GoodsEvaluateCountBySkuIdRequset request =
                GoodsEvaluateCountBySkuIdRequset.builder().skuId(skuId).build();
        //商品评价总数、晒单、好评率
        GoodsEvaluateCountResponse response = goodsEvaluateQueryProvider.getGoodsEvaluateSumByskuId(request).getContext();

        //top3评价
        GoodsEvaluatePageRequest pageRequest = GoodsEvaluatePageRequest.builder()
                .goodsId(response.getGoodsId())
                .delFlag(DeleteFlag.NO.toValue())
                .isShow(1)
                .build();
        pageRequest.setPageSize(3);
        pageRequest.setSortColumn("evaluateTime");
        pageRequest.setSortRole(SortType.DESC.toValue());
        GoodsEvaluateListResponse listResponse =
                goodsEvaluateQueryProvider.getGoodsEvaluateTopData(pageRequest).getContext();
        listResponse.getGoodsEvaluateVOList().forEach(goodsEvaluateVO -> {
            desensitization(goodsEvaluateVO);
        });
        // 评价其他信息-用户头像
        wrapEvaluateInfo(listResponse.getGoodsEvaluateVOList());
        return BaseResponse.success(GoodsDetailEvaluateTop3Resp.builder()
                .goodsEvaluateCountResponse(response)
                .listResponse(listResponse)
                .build());
    }




    private void desensitization(GoodsEvaluateVO goodsEvaluateVO) {
        goodsEvaluateVO.setCustomerAccount("");
        String name = goodsEvaluateVO.getCustomerName();
        // 是否手机号
        String handlerName = SensitiveUtils.handlerMobilePhone(name);
        if (!Objects.equals(name, handlerName)) {
            goodsEvaluateVO.setCustomerName(SensitiveUtils.handlerMobilePhone(name));
        } else {
            if (name.length() == 2) {
                String beg = name.substring(0, 1);
                goodsEvaluateVO.setCustomerName(beg + "****");
            } else if (name.length() != 1) {
                String beg = name.substring(0, 1);
                String end = name.substring(name.length() - 1, name.length());
                goodsEvaluateVO.setCustomerName(beg + "****" + end);
            }
        }

    }

    /**
     * @param pageRequest
     * @Description: 商品评价分页数据
     * @Author: Bob
     * @Date: 2019-05-21 15:33
     */
    @ApiOperation(value = "商品评价分页数据")
    @RequestMapping(value = "/evaluatePage", method = RequestMethod.POST)
    public BaseResponse<GoodsEvaluatePageResponse> evaluatePage(@RequestBody GoodsEvaluatePageRequest pageRequest) {
        pageRequest.putSort("isSys", SortType.ASC.toValue());
        pageRequest.putSort("evaluateTime", SortType.DESC.toValue());
        GoodsEvaluatePageResponse pageResponse = goodsEvaluateUtil(pageRequest, false);
        return BaseResponse.success(pageResponse);
    }

    /**
     * @param pageRequest
     * @Description: 商品评价分页数据
     * @Author: Bob
     * @Date: 2019-05-21 15:33
     */
    @ApiOperation(value = "商品评价分页数据")
    @RequestMapping(value = "/evaluatePageLogin", method = RequestMethod.POST)
    public BaseResponse<GoodsEvaluatePageResponse> evaluatePageLogin(@RequestBody GoodsEvaluatePageRequest pageRequest) {
        pageRequest.putSort("isSys", SortType.ASC.toValue());
        pageRequest.putSort("evaluateTime", SortType.DESC.toValue());
        GoodsEvaluatePageResponse pageResponse = goodsEvaluateUtil(pageRequest, true);
        return BaseResponse.success(pageResponse);
    }

    private GoodsEvaluatePageResponse goodsEvaluateUtil(GoodsEvaluatePageRequest pageRequest, boolean isLogin) {
        GoodsEvaluatePageResponse pageResponse = goodsEvaluateQueryProvider.page(pageRequest).getContext();
        pageResponse.getGoodsEvaluateVOPage().getContent().forEach(goodsEvaluateVO -> {
            desensitization(goodsEvaluateVO);
            if (isLogin) {
                CustomerGoodsEvaluatePraiseQueryRequest queryRequest = new CustomerGoodsEvaluatePraiseQueryRequest();
                queryRequest.setCustomerId(commonUtil.getCustomer().getCustomerId());
                queryRequest.setGoodsEvaluateId(goodsEvaluateVO.getEvaluateId());
                Optional<CustomerGoodsEvaluatePraiseVO> customerGoodsEvaluatePraiseOpt = Optional.ofNullable(
                        customerGoodsEvaluatePraiseQueryProvider.getCustomerGoodsEvaluatePraise(queryRequest).getContext().getCustomerGoodsEvaluatePraiseVO());
                customerGoodsEvaluatePraiseOpt.ifPresent(customerGoodsEvaluatePraiseInfo -> {
                    goodsEvaluateVO.setIsPraise(1);
                });
            }
        });
        // 评价其他信息-用户头像
        wrapEvaluateInfo(pageResponse.getGoodsEvaluateVOPage().getContent());
        return pageResponse;
    }

    /**
     * 会员头像
     * 1：现状-评价商品信息 会员名称等信息是在提交评论时持久化的，弊端 1 不能获取最新会员信 2 有新的展示项需要不断扩充数据结构
     * 后期增加需求显示会员头像，考虑到如果再要显示标签等信息直接拓展数据库字段也不合适，所以这里的头像是实时获取的
     * 2：如果以后会员名称需要获取最新会员信息直接在这里添加就可以了
     */
    private void wrapEvaluateInfo(List<GoodsEvaluateVO> goodsEvaluateVOList) {
        if (CollectionUtils.isNotEmpty(goodsEvaluateVOList)) {
            //获取用户信息-头像、昵称
            List<String> customerIds = goodsEvaluateVOList.stream().map(GoodsEvaluateVO::getCustomerId).collect
                    (Collectors.toList());
            CustomerIdsListRequest customerIdsListRequest = new CustomerIdsListRequest();
            customerIdsListRequest.setCustomerIds(customerIds);
            BaseResponse<CustomerIdsListResponse> listByCustomerIds = thirdLoginRelationQueryProvider
                    .listWithImgByCustomerIds(customerIdsListRequest);
            List<CustomerDetailWithImgVO> customerVOList = listByCustomerIds.getContext().getCustomerVOList();
            goodsEvaluateVOList.forEach(vo -> {
                CustomerDetailWithImgVO customerDetailWithImgVO = customerVOList.stream().filter(ivo -> ivo
                        .getCustomerId().equals(vo.getCustomerId())).findFirst().orElse(new CustomerDetailWithImgVO());
                //头像
                vo.setHeadimgurl(customerDetailWithImgVO.getHeadimgurl());
                //会员标签
                vo.setCustomerLabelList(customerDetailWithImgVO.getCustomerLabelList());
            });
        }
    }
}