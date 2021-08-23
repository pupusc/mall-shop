
package com.wanmi.ares.request.export;

import lombok.Data;

@Data
public class ExportDataRequest {

  /**
   * 主键
   */
  public long id; // optional
  /**
   * 用户标识
   */
  public String userId; // optional
  /**
   * 商家标识
   */
  public long companyInfoId; // optional
  /**
   * 开始日期
   */
  public String beginDate; // optional
  /**
   * 截止日期
   */
  public String endDate; // optional
  /**
   * 导出报表类别
   *
   * @see com.wanmi.ares.enums.ReportType
   */
  public com.wanmi.ares.enums.ReportType typeCd; // optional
  /**
   * 导出状态(等待生成导出文件,导出文件生成中,导出文件生成完毕)
   *
   * @see com.wanmi.ares.enums.ExportStatus
   */
  public com.wanmi.ares.enums.ExportStatus exportStatus; // optional
  /**
   * 发起导出请求时间
   */
  public String createTime; // optional
  /**
   * 文件成功生成时间/错误时间
   */
  public String finishTime; // optional
  /**
   * 导出文件下载全路径
   */
  public String filePath; // optional
  /**
   * 第几页
   */
  public int pageNum; // required
  /**
   * 每页多少条
   */
  public int pageSize; // required
  /**
   * 从第几条开始查询
   */
  public int startNum; // required
}