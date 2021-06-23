package com.wanmi.sbc.customer.api.request.fandeng;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @program: sbc-background
 * @description: 素材审核请求实体
 * @author: Mr.Tian
 * @create: 2021-03-08 15:21
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialCheckRequest implements Serializable {

    private static final long serialVersionUID = -4710838585425692691L;



    private String fileName;
}
