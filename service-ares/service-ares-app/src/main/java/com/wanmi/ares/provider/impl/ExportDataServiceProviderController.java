package com.wanmi.ares.provider.impl;

import com.wanmi.ares.export.service.ExportDataServiceImpl;
import com.wanmi.ares.provider.ExportDataServiceProvider;
import com.wanmi.ares.request.export.ExportDataRequest;
import com.wanmi.ares.view.export.ExportDataResponse;
import com.wanmi.ares.view.export.ExportDataView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 14:28
 */
@RestController
public class ExportDataServiceProviderController implements ExportDataServiceProvider {

    @Autowired
    private ExportDataServiceImpl exportDataService;

    @Override
    public ExportDataView sendExportDataRequest(@RequestBody @Valid ExportDataRequest request) {
        return exportDataService.sendExportDataRequest(request);
    }

    @Override
    public ExportDataResponse queryExportDataRequestPage(@RequestBody @Valid ExportDataRequest request) {
        return exportDataService.queryExportDataRequestPage(request);
    }

    @Override
    public int deleteExportDataRequest(@RequestBody @Valid ExportDataRequest request) {
        return exportDataService.deleteExportDataRequest(request);
    }

    @Override
    public int deleteExportDataRequestAndFiles(@RequestBody @Valid ExportDataRequest request)  {
        return exportDataService.deleteExportDataRequestAndFiles(request);
    }
}
