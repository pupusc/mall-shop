
package com.wanmi.ares.view.export;

import lombok.Data;

@Data
public class ExportDataResponse {


  /**
   * 任务总条数
   */
  public long total; // required
  /**
   * 视图集合
   */
  public java.util.List<ExportDataView> viewList; // required
}