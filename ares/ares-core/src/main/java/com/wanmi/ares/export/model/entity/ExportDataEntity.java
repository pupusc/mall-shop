package com.wanmi.ares.export.model.entity;

import com.wanmi.ares.enums.ExportStatus;
import com.wanmi.ares.enums.ReportType;
import com.wanmi.ares.request.export.ExportDataRequest;
import com.wanmi.ares.view.export.ExportDataView;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Author: bail
 * Time: 2017/11/3.17:12
 */
@Component
public class ExportDataEntity {

    /**
     * 主键
     */
    private long id;

    /**
     * 用户标识
     */
    private String userId;

    /**
     * 商家标识
     */
    private long companyInfoId;

    /**
     * 开始日期
     */
    private String beginDate;

    /**
     * 截止日期
     */
    private String endDate;

    /**
     * 导出报表类别
     */
    private Integer typeCd;

    /**
     * 导出状态(等待生成导出文件,导出文件生成中,导出文件生成完毕)
     */
    private Integer exportStatus;

    /**
     * 发起导出请求时间
     */
    private String createTime;

    /**
     * 文件成功生成时间/错误时间
     */
    private String finishTime;

    /**
     * 导出文件下载全路径
     */
    private String filePath;

    /**
     * 第几页
     */
    private int pageNum;

    /**
     * 每页多少条
     */
    private int pageSize;

    /**
     * 从第几条开始查询
     */
    private int startNum;

    /**
     * 从传入对象 转换成 数据库交互的实体
     * @param request
     */
    public ExportDataEntity convertEntityFromRequest(ExportDataRequest request){
        this.id = request.getId();
        this.userId = request.getUserId();
        this.companyInfoId = request.getCompanyInfoId();
        this.beginDate = request.getBeginDate();
        this.endDate = request.getEndDate();
        if(request.getTypeCd() != null){
            this.typeCd = request.getTypeCd().getValue();
        }
        if(request.getExportStatus() != null){
            this.exportStatus = request.getExportStatus().getValue();
        }
        this.createTime = request.getCreateTime();
        this.finishTime = request.getFinishTime();
        this.filePath = request.getFilePath();
        this.pageNum = request.getPageNum();
        this.pageSize = request.getPageSize();
        this.startNum = request.getStartNum();
        return this;
    }

    /**
     * 从数据库交互的实体 转换成 返回前端的对象
     */
    public ExportDataView convertViewFromEntity(String prefix){
        ExportDataView view = new ExportDataView();
        view.setId(this.id);
        view.setUserId(this.userId);
        view.setCompanyInfoId(this.companyInfoId);
        view.setBeginDate(this.beginDate);
        view.setEndDate(this.endDate);
        view.setTypeCd(ReportType.findByValue(this.typeCd));
        view.setExportStatus(ExportStatus.findByValue(this.exportStatus));
        view.setCreateTime(this.createTime);
        view.setFinishTime(this.finishTime);
        if(this.filePath!=null){
            //拼接完整的下载路径
            view.setFilePath(Pattern.compile(",").splitAsStream(this.filePath).map(str -> prefix+str).collect(Collectors.joining(",")));
        }
        return view;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCompanyInfoId() {
        return companyInfoId;
    }

    public void setCompanyInfoId(long companyInfoId) {
        this.companyInfoId = companyInfoId;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getTypeCd() {
        return typeCd;
    }

    public void setTypeCd(Integer typeCd) {
        this.typeCd = typeCd;
    }

    public Integer getExportStatus() {
        return exportStatus;
    }

    public void setExportStatus(Integer exportStatus) {
        this.exportStatus = exportStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }
}
