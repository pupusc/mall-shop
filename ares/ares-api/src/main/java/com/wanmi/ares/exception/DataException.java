
package com.wanmi.ares.exception;

import lombok.Data;


@Data
public class DataException {
  public java.lang.String message; // optional
  public java.lang.String callStack; // optional
  public java.lang.String date; // optional
}

