
package com.wanmi.ares.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequest {

  public int page; // required

  public int pageSize; // required

  public int start; // required

}

