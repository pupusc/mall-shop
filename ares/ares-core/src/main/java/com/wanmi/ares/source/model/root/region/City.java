package com.wanmi.ares.source.model.root.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class City implements Serializable{


    /**
     * 编码
     */
    @Id
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 父亲编码
     */
    private String parent_code;
}
