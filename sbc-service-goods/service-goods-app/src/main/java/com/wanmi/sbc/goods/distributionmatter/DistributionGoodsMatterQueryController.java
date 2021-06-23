package com.wanmi.sbc.goods.distributionmatter;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.DistributionCommissionUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.distribution.DistributorLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoQueryByIdsRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeListByIdsRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoQueryByIdsResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelByCustomerIdResponse;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.customer.bean.vo.EmployeeListByIdsVO;
import com.wanmi.sbc.goods.api.provider.distributionmatter.DistributionGoodsMatterQueryProvider;
import com.wanmi.sbc.goods.api.request.distributionmatter.DistributionGoodsMatterPageRequest;
import com.wanmi.sbc.goods.api.request.distributionmatter.QueryByIdListRequest;
import com.wanmi.sbc.goods.api.response.distributionmatter.DistributionByIdsResponse;
import com.wanmi.sbc.goods.api.response.distributionmatter.DistributionGoodsMatterPageResponse;
import com.wanmi.sbc.goods.bean.vo.DistributionGoodsMatterPageVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.distributionmatter.model.root.DistributionGoodsMatter;
import com.wanmi.sbc.goods.distributionmatter.service.DistributionGoodsMatterService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Validated
public class DistributionGoodsMatterQueryController implements DistributionGoodsMatterQueryProvider {

    @Autowired
    private DistributionGoodsMatterService distributionGoodsMatterService;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private GoodsInfoSpecDetailRelRepository goodsInfoSpecDetailRelRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private DistributorLevelQueryProvider distributorLevelQueryProvider;


    /**
     * 分页商品素材
     */
    @Override
    public BaseResponse<DistributionGoodsMatterPageResponse> page(@RequestBody @Valid DistributionGoodsMatterPageRequest distributionGoodsMatterPageRequest) {
        Page<DistributionGoodsMatter> page = distributionGoodsMatterService.page(distributionGoodsMatterPageRequest);

        //根据id查询操作员，查询操作员的名称和账号
        List<String> ids = page.getContent().stream().map(item ->
                item.getOperatorId()).distinct().collect(Collectors.toList());
        if (ids.size() == 0) {
            DistributionGoodsMatterPageResponse response = new DistributionGoodsMatterPageResponse();
            response.setDistributionGoodsMatterPage(KsBeanUtil.convertPage(page, DistributionGoodsMatterPageVO.class));
            return BaseResponse.success(response);
        }
        EmployeeListByIdsRequest employeeRequest = new EmployeeListByIdsRequest();
        employeeRequest.setEmployeeIds(ids);
        List<EmployeeListByIdsVO> employeeResponse = employeeQueryProvider.listByIds(employeeRequest).getContext().getEmployeeList();
        //设值
        List<DistributionGoodsMatterPageVO> pageVO = page.getContent().stream().map(item -> {
            DistributionGoodsMatterPageVO vo = KsBeanUtil.copyPropertiesThird(item, DistributionGoodsMatterPageVO.class);
            if(item.getGoodsInfo()!=null){
                GoodsInfoVO goodsInfoVO = KsBeanUtil.copyPropertiesThird(item.getGoodsInfo(), GoodsInfoVO.class);
                vo.setGoodsInfo(goodsInfoVO);
            }
            for (EmployeeListByIdsVO employee : employeeResponse) {
                if (employee.getEmployeeId().equals(item.getOperatorId())) {
                    vo.setName(employee.getEmployeeName());
                    vo.setAccount(employee.getAccountName());
                    break;
                }
                continue;
            }
            return vo;
        }).collect(Collectors.toList());
        DistributionGoodsMatterPageResponse response = new DistributionGoodsMatterPageResponse();
        response.setDistributionGoodsMatterPage(new MicroServicePage(pageVO, distributionGoodsMatterPageRequest.getPageable(), page.getTotalElements()));
        return BaseResponse.success(response);
    }

    /**
     * 查询商品素材
     *
     * @param idListRequest
     */
    @Override
    public BaseResponse<DistributionByIdsResponse> queryByIds(@RequestBody @Valid QueryByIdListRequest idListRequest) {
        List<DistributionGoodsMatter> matters = distributionGoodsMatterService.queryByIds(idListRequest.getIds());
        List<DistributionGoodsMatterPageVO> matterVOList = KsBeanUtil.convertList(matters, DistributionGoodsMatterPageVO.class);
        matterVOList.stream().forEach(distributionGoodsMatterPageVO -> {
            matters.stream().filter(distributionGoodsMatter -> distributionGoodsMatter.getId().equals(distributionGoodsMatter.getId()))
                    .forEach(distributionGoodsMatter -> {
                        if(distributionGoodsMatter.getGoodsInfo()!=null){
                            distributionGoodsMatterPageVO.setGoodsInfoId(distributionGoodsMatter.getGoodsInfo().getGoodsInfoId());
                        }
                    });
        });
        DistributionByIdsResponse response = new DistributionByIdsResponse();
        response.setDistributionGoodsMatterList(matterVOList);
        return BaseResponse.success(response);
    }

    /**
     * 分页查询商品素材
     *
     * @param distributionGoodsMatterPageRequest
     * @return
     */
    @Override
    public BaseResponse<DistributionGoodsMatterPageResponse> pageNew(@RequestBody DistributionGoodsMatterPageRequest distributionGoodsMatterPageRequest) {
        BaseResponse<DistributionGoodsMatterPageResponse> response = this.page(distributionGoodsMatterPageRequest);

        if (Objects.isNull(response) || Objects.isNull(response.getContext()) || Objects.isNull(response.getContext().getDistributionGoodsMatterPage())
                || response.getContext().getDistributionGoodsMatterPage().getTotalElements() < 1 || CollectionUtils.isEmpty(response.getContext().getDistributionGoodsMatterPage().getContent())) {
            return response;
        }

        //分页商品信息
        List<GoodsInfoVO> goodsInfoVOList = response.getContext().getDistributionGoodsMatterPage()
                .getContent().stream().filter(f->f.getGoodsInfo()!=null).map(DistributionGoodsMatterPageVO::getGoodsInfo).collect(Collectors.toList());

        if(goodsInfoVOList.size()>0){
            //查询所有sku的spu信息
            GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
            List<String> goodsIds = goodsInfoVOList.stream().map(GoodsInfoVO::getGoodsId).collect(Collectors.toList());
            goodsQueryRequest.setGoodsIds(goodsIds);
            List<Goods> goodsList = goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());

            //拿到商家相关消息
            List<Long> companyInfoIds = goodsInfoVOList.stream().map(GoodsInfoVO::getCompanyInfoId).collect(Collectors.toList());
            if(companyInfoIds.size()>0){
                CompanyInfoQueryByIdsRequest companyInfoQueryByIdsRequest = new CompanyInfoQueryByIdsRequest();
                companyInfoQueryByIdsRequest.setCompanyInfoIds(companyInfoIds);
                companyInfoQueryByIdsRequest.setDeleteFlag(DeleteFlag.NO);
                BaseResponse<CompanyInfoQueryByIdsResponse> companyInfoQueryByIdsResponseBaseResponse =
                        companyInfoQueryProvider.queryByCompanyInfoIds(companyInfoQueryByIdsRequest);
                response.getContext().setCompanyInfoList(companyInfoQueryByIdsResponseBaseResponse.getContext().getCompanyInfoList());
            }

            //查询所有SKU规格值关联
            List<String> goodsInfoIds =
                    goodsInfoVOList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            List<GoodsInfoSpecDetailRel> goodsInfoSpecDetails = goodsInfoSpecDetailRelRepository.findByGoodsInfoIds(goodsInfoIds);

            //获取登录人的佣金比例
            if(StringUtils.isNotBlank(distributionGoodsMatterPageRequest.getCustomerId())){
                BaseResponse<DistributorLevelByCustomerIdResponse> resultBaseResponse =
                        distributorLevelQueryProvider.getByCustomerId(new DistributorLevelByCustomerIdRequest
                                (distributionGoodsMatterPageRequest.getCustomerId()));
                DistributorLevelVO distributorLevelVO = Objects.isNull(resultBaseResponse) ? null :
                        resultBaseResponse.getContext().getDistributorLevelVO();
                if (Objects.nonNull(distributorLevelVO) && Objects.nonNull(distributorLevelVO.getCommissionRate())) {
                    BigDecimal commissionRate = distributorLevelVO.getCommissionRate();
                    //填充每个SKU
                    response.getContext().getDistributionGoodsMatterPage()
                            .getContent().stream().forEach(distributionGoodsMatterPageVO -> {
                        if(distributionGoodsMatterPageVO.getGoodsInfo()!=null){
                            //sku商品图片为空，则以spu商品主图
                            if (StringUtils.isBlank(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsInfoImg())) {
                                distributionGoodsMatterPageVO.getGoodsInfo().setGoodsInfoImg(
                                        goodsList.stream().filter(goods -> goods.getGoodsId()
                                                .equals(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsId())).findFirst()
                                                .orElse(new Goods()).getGoodsImg());
                            }
                            //填充分销员佣金
                            distributionGoodsMatterPageVO.getGoodsInfo().setDistributionCommission(
                                    DistributionCommissionUtils.calDistributionCommission(distributionGoodsMatterPageVO.getGoodsInfo().getDistributionCommission(),commissionRate));
                            //填充每个SKU的规格关系
                            distributionGoodsMatterPageVO.getGoodsInfo().setSpecDetailRelIds(goodsInfoSpecDetails.stream()
                                    .filter(specDetailRel -> specDetailRel.getGoodsInfoId().equals(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsInfoId()))
                                    .map(GoodsInfoSpecDetailRel::getSpecDetailRelId).collect(Collectors.toList()));
                        }
                    });
                }
            }else{
                //填充每个SKU
                response.getContext().getDistributionGoodsMatterPage()
                        .getContent().stream().forEach(distributionGoodsMatterPageVO -> {
                    if(distributionGoodsMatterPageVO.getGoodsInfo()!=null){
                        //sku商品图片为空，则以spu商品主图
                        if (StringUtils.isBlank(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsInfoImg())) {
                            distributionGoodsMatterPageVO.getGoodsInfo().setGoodsInfoImg(
                                    goodsList.stream().filter(goods -> goods.getGoodsId()
                                            .equals(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsId())).findFirst()
                                            .orElse(new Goods()).getGoodsImg());
                        }
                        //填充每个SKU的规格关系
                        distributionGoodsMatterPageVO.getGoodsInfo().setSpecDetailRelIds(goodsInfoSpecDetails.stream()
                                .filter(specDetailRel -> specDetailRel.getGoodsInfoId().equals(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsInfoId()))
                                .map(GoodsInfoSpecDetailRel::getSpecDetailRelId).collect(Collectors.toList()));
                    }
                });
            }

            if (CollectionUtils.isNotEmpty(goodsInfoSpecDetails)) {
                response.getContext().setGoodsInfoSpecDetails(KsBeanUtil.convertList(goodsInfoSpecDetails,
                        GoodsInfoSpecDetailRelVO.class));
            }
        }
        return response;
    }

    @Override
    public BaseResponse<DistributionGoodsMatterPageResponse> pageNewForBoss(DistributionGoodsMatterPageRequest distributionGoodsMatterPageRequest) {
        Page<DistributionGoodsMatter> page = distributionGoodsMatterService.page(distributionGoodsMatterPageRequest);

        //根据id查询操作员，查询操作员的名称和账号
        List<String> ids = page.getContent().stream().map(item ->
                item.getOperatorId()).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            DistributionGoodsMatterPageResponse response = new DistributionGoodsMatterPageResponse();
            response.setDistributionGoodsMatterPage(KsBeanUtil.convertPage(page, DistributionGoodsMatterPageVO.class));
            return BaseResponse.success(response);
        }
        EmployeeListByIdsRequest employeeRequest = new EmployeeListByIdsRequest();
        employeeRequest.setEmployeeIds(ids);
        List<EmployeeListByIdsVO> employeeResponse = employeeQueryProvider.listByIds(employeeRequest).getContext().getEmployeeList();
        //设值
        List<DistributionGoodsMatterPageVO> pageVO = page.getContent().stream().map(item -> {
            DistributionGoodsMatterPageVO vo = KsBeanUtil.copyPropertiesThird(item, DistributionGoodsMatterPageVO.class);
            if(item.getGoodsInfo()!=null){
                GoodsInfoVO goodsInfoVO = KsBeanUtil.copyPropertiesThird(item.getGoodsInfo(), GoodsInfoVO.class);
                vo.setGoodsInfo(goodsInfoVO);
            }
            for (EmployeeListByIdsVO employee : employeeResponse) {
                if (employee.getEmployeeId().equals(item.getOperatorId())) {
                    vo.setName(employee.getEmployeeName());
                    vo.setAccount(employee.getAccountName());
                    break;
                }
                continue;
            }
            return vo;
        }).collect(Collectors.toList());
        DistributionGoodsMatterPageResponse response = new DistributionGoodsMatterPageResponse();
        response.setDistributionGoodsMatterPage(new MicroServicePage(pageVO, distributionGoodsMatterPageRequest.getPageable(), page.getTotalElements()));

//        BaseResponse<DistributionGoodsMatterPageResponse> response = this.page(distributionGoodsMatterPageRequest);

        if (Objects.isNull(response)  || Objects.isNull(response.getDistributionGoodsMatterPage())
                || response.getDistributionGoodsMatterPage().getTotalElements() < 1 || CollectionUtils.isEmpty(response.getDistributionGoodsMatterPage().getContent())) {
            return BaseResponse.success(response);
        }

        //分页商品信息
        List<GoodsInfoVO> goodsInfoVOList = response.getDistributionGoodsMatterPage()
                .getContent().stream().filter(f->f.getGoodsInfo()!=null).map(DistributionGoodsMatterPageVO::getGoodsInfo).collect(Collectors.toList());

        if(goodsInfoVOList.size()>0){
            //查询所有sku的spu信息
            GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
            List<String> goodsIds = goodsInfoVOList.stream().map(GoodsInfoVO::getGoodsId).collect(Collectors.toList());
            goodsQueryRequest.setGoodsIds(goodsIds);
            List<Goods> goodsList = goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());

            //拿到商家相关消息
            List<Long> companyInfoIds = goodsInfoVOList.stream().map(GoodsInfoVO::getCompanyInfoId).collect(Collectors.toList());
            if(companyInfoIds.size()>0){
                CompanyInfoQueryByIdsRequest companyInfoQueryByIdsRequest = new CompanyInfoQueryByIdsRequest();
                companyInfoQueryByIdsRequest.setCompanyInfoIds(companyInfoIds);
                companyInfoQueryByIdsRequest.setDeleteFlag(DeleteFlag.NO);
                BaseResponse<CompanyInfoQueryByIdsResponse> companyInfoQueryByIdsResponseBaseResponse =
                        companyInfoQueryProvider.queryByCompanyInfoIds(companyInfoQueryByIdsRequest);
                response.setCompanyInfoList(companyInfoQueryByIdsResponseBaseResponse.getContext().getCompanyInfoList());
            }

            //查询所有SKU规格值关联
            List<String> goodsInfoIds =
                    goodsInfoVOList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            List<GoodsInfoSpecDetailRel> goodsInfoSpecDetails = goodsInfoSpecDetailRelRepository.findByGoodsInfoIds(goodsInfoIds);

            //获取登录人的佣金比例
            if(StringUtils.isNotBlank(distributionGoodsMatterPageRequest.getCustomerId())){
                BaseResponse<DistributorLevelByCustomerIdResponse> resultBaseResponse =
                        distributorLevelQueryProvider.getByCustomerId(new DistributorLevelByCustomerIdRequest
                                (distributionGoodsMatterPageRequest.getCustomerId()));
                DistributorLevelVO distributorLevelVO = Objects.isNull(resultBaseResponse) ? null :
                        resultBaseResponse.getContext().getDistributorLevelVO();
                if (Objects.nonNull(distributorLevelVO) && Objects.nonNull(distributorLevelVO.getCommissionRate())) {
                    BigDecimal commissionRate = distributorLevelVO.getCommissionRate();
                    //填充每个SKU
                    response.getDistributionGoodsMatterPage()
                            .getContent().stream().forEach(distributionGoodsMatterPageVO -> {
                        if(distributionGoodsMatterPageVO.getGoodsInfo()!=null){
                            //sku商品图片为空，则以spu商品主图
                            if (StringUtils.isBlank(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsInfoImg())) {
                                distributionGoodsMatterPageVO.getGoodsInfo().setGoodsInfoImg(
                                        goodsList.stream().filter(goods -> goods.getGoodsId()
                                                .equals(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsId())).findFirst()
                                                .orElse(new Goods()).getGoodsImg());
                            }
                            //填充分销员佣金
                            distributionGoodsMatterPageVO.getGoodsInfo().setDistributionCommission(
                                    DistributionCommissionUtils.calDistributionCommission(distributionGoodsMatterPageVO.getGoodsInfo().getDistributionCommission(),commissionRate));
                            //填充每个SKU的规格关系
                            distributionGoodsMatterPageVO.getGoodsInfo().setSpecDetailRelIds(goodsInfoSpecDetails.stream()
                                    .filter(specDetailRel -> specDetailRel.getGoodsInfoId().equals(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsInfoId()))
                                    .map(GoodsInfoSpecDetailRel::getSpecDetailRelId).collect(Collectors.toList()));
                        }
                    });
                }
            }else{
                //填充每个SKU
                response.getDistributionGoodsMatterPage()
                        .getContent().stream().forEach(distributionGoodsMatterPageVO -> {
                    if(distributionGoodsMatterPageVO.getGoodsInfo()!=null){
                        //sku商品图片为空，则以spu商品主图
                        if (StringUtils.isBlank(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsInfoImg())) {
                            distributionGoodsMatterPageVO.getGoodsInfo().setGoodsInfoImg(
                                    goodsList.stream().filter(goods -> goods.getGoodsId()
                                            .equals(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsId())).findFirst()
                                            .orElse(new Goods()).getGoodsImg());
                        }
                        //填充每个SKU的规格关系
                        distributionGoodsMatterPageVO.getGoodsInfo().setSpecDetailRelIds(goodsInfoSpecDetails.stream()
                                .filter(specDetailRel -> specDetailRel.getGoodsInfoId().equals(distributionGoodsMatterPageVO.getGoodsInfo().getGoodsInfoId()))
                                .map(GoodsInfoSpecDetailRel::getSpecDetailRelId).collect(Collectors.toList()));
                    }
                });
            }

            if (CollectionUtils.isNotEmpty(goodsInfoSpecDetails)) {
                response.setGoodsInfoSpecDetails(KsBeanUtil.convertList(goodsInfoSpecDetails,
                        GoodsInfoSpecDetailRelVO.class));
            }
        }
        return BaseResponse.success(response);
    }
}
