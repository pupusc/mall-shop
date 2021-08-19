package com.wanmi.sbc.setting.provider.impl;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.setting.api.provider.InitProvider;
import com.wanmi.sbc.setting.api.request.systemresourcecate.SystemResourceCateQueryRequest;
import com.wanmi.sbc.setting.companyinfo.service.CompanyInfoService;
import com.wanmi.sbc.setting.syssms.service.SysSmsService;
import com.wanmi.sbc.setting.systemresourcecate.service.SystemResourceCateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class InitController implements InitProvider {

    @Autowired
    private SystemResourceCateService systemResourceCateService;

    @Autowired
    private SysSmsService sysSmsService;

    @Autowired
    private CompanyInfoService companyInfoService;

    @Override
    public BaseResponse init() {

        // //公司信息
        // companyInfoService.findCompanyInfos();

        //图片服务器,增票资质和快递100配置
        // configService.init( appKey);


        //默认素材分类
        SystemResourceCateQueryRequest resourceCate = new SystemResourceCateQueryRequest();
        resourceCate.setIsDefault(DefaultFlag.YES);
        systemResourceCateService.init(resourceCate);

        // //短信商家
        // SysSms sysSms = SysSms.builder()
        //         .isOpen(NumberUtils.INTEGER_ONE)
        //         .createTime(LocalDateTime.now())
        //         .build();
        // sysSmsService.add(sysSms);

        // ClassPathResource resource = new ClassPathResource("express-com");

        // //默认50个物流公司
        // List<ExpressCompany> expressCompanyList;
        // try {
        //     String expressCom = IOUtils.readStreamAsString( resource.getInputStream(), "UTF-8");
        //     expressCompanyList = Arrays.stream(expressCom.split("\\n")).map(express -> {
        //         String[] expr = express.split(",");
        //         return ExpressCompany.builder()
        //                 .expressName( expr[0])
        //                 .expressCode( expr[1])
        //                 .isAdd( DefaultFlag.NO)
        //                 .isChecked( DefaultFlag.NO)
        //                 .delFlag( DeleteFlag.NO)
        //                 .build();
        //     }).collect(Collectors.toList());
        //
        // } catch (IOException e) {
        //
        //     log.error(e.getMessage(), e.getCause());
        //     throw new SbcRuntimeException(SettingErrorCode.SYSTEM_UNKNOWN_ERROR);
        // } catch (ArrayIndexOutOfBoundsException e1) {
        //
        //     log.error(e1.getMessage(), e1.getCause());
        //     throw new SbcRuntimeException(SettingErrorCode.EXPRESS_FORMAT_ERROR);
        // }
        //
        // if (CollectionUtils.isNotEmpty( expressCompanyList)
        //         && expressCompanyList.size() == Consts.EXPRESS_COMPANY_DEFAULT_COUNT) {
        //     expressCompanyService.save( expressCompanyList);
        // } else {
        //     log.warn( "默认物流公司数量不是50");
        // }


        return BaseResponse.SUCCESSFUL();
    }
}
