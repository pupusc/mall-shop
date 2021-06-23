package com.wanmi.sbc.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.ResultCode;
import com.wanmi.sbc.goods.api.provider.distributionmatter.DistributionGoodsMatterProvider;
import com.wanmi.sbc.goods.api.provider.distributionmatter.DistributionGoodsMatterQueryProvider;
import com.wanmi.sbc.goods.api.request.distributionmatter.*;
import com.wanmi.sbc.goods.api.response.distributionmatter.DistributionGoodsMatterPageResponse;
import com.wanmi.sbc.goods.bean.vo.DistributionGoodsMatterPageVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "DistributionGoodsMatterController", description = "分销商品素材")
@RestController
@RequestMapping("/distribution/goods-matter")
@Validated
public class DistributionGoodsMatterController {

    @Autowired
    private DistributionGoodsMatterQueryProvider distributionGoodsMatterQueryProvider;

    @Autowired
    private DistributionGoodsMatterProvider distributionGoodsMatterProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @ApiOperation(value = "分页分销商品素材")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<DistributionGoodsMatterPageResponse> page(@RequestBody @Valid DistributionGoodsMatterPageRequest distributionGoodsMatterPageRequest){
        BaseResponse<DistributionGoodsMatterPageResponse> response = distributionGoodsMatterQueryProvider.page(distributionGoodsMatterPageRequest);
        return response;
    }

    @ApiOperation(value = "新增分销商品素材")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public BaseResponse add(@RequestBody @Valid DistributionGoodsMatteAddRequest distributionGoodsMatteAddRequest){
        String operatorId = commonUtil.getOperatorId();
        distributionGoodsMatteAddRequest.setOperatorId(operatorId);
        BaseResponse response = distributionGoodsMatterProvider.add(distributionGoodsMatteAddRequest);
        //记录操作日志
        if(ResultCode.SUCCESSFUL.equals(response.getCode())){
            operateLogMQUtil.convertAndSend("分销商品素材", "新增分销商品素材", "SKU编码："+distributionGoodsMatteAddRequest.getGoodsInfoId());
        }
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "修改分销商品素材")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public BaseResponse modify(@RequestBody @Valid DistributionGoodsMatterModifyRequest distributionGoodsMatterModifyRequest){
        //如果是营销素材，配置了素材链接的，跟着的小程序码得重新生成
        BaseResponse response = distributionGoodsMatterProvider.modify(distributionGoodsMatterProvider.updataQrcode(distributionGoodsMatterModifyRequest).getContext());
        //记录操作日志
        if(ResultCode.SUCCESSFUL.equals(response.getCode())){
            operateLogMQUtil.convertAndSend("分销商品素材", "修改分销商品素材", "SKU编码："+distributionGoodsMatterModifyRequest.getGoodsInfoId());
        }
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "批量删除分销商品素材")
    @RequestMapping(value = "/delete-list", method = RequestMethod.POST)
    public BaseResponse deleteList(@RequestBody @Valid DeleteByIdListRequest deleteByIdListRequest){
        BaseResponse response = distributionGoodsMatterProvider.deleteList(deleteByIdListRequest);
        //记录操作日志
        if(ResultCode.SUCCESSFUL.equals(response.getCode())){
            operateLogMQUtil.convertAndSend("分销商品素材", "批量删除分销商品素材", "SKU编码集合："+deleteByIdListRequest.getIds());
        }
        return BaseResponse.SUCCESSFUL();
    }


    @ApiOperation(value = "查询分销商品素材")
    @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public BaseResponse<DistributionGoodsMatterPageVO> query(@PathVariable String id){
        QueryByIdListRequest request =new QueryByIdListRequest();
        List ids = new ArrayList();
        ids.add(id);
        request.setIds(ids);
        List<DistributionGoodsMatterPageVO> list = distributionGoodsMatterQueryProvider.queryByIds(request).getContext().getDistributionGoodsMatterList();
        if(list.size() > 0){
            return BaseResponse.success(list.get(0));
        }
        return BaseResponse.SUCCESSFUL();
    }


    @ApiOperation(value = "分页查询分销商品素材")
    @RequestMapping(value = "/page/new", method = RequestMethod.POST)
    public BaseResponse<DistributionGoodsMatterPageResponse> pageNew(@RequestBody DistributionGoodsMatterPageRequest distributionGoodsMatterPageRequest){
        BaseResponse<DistributionGoodsMatterPageResponse> response = distributionGoodsMatterQueryProvider.pageNewForBoss(distributionGoodsMatterPageRequest);
        return response;
    }
}
