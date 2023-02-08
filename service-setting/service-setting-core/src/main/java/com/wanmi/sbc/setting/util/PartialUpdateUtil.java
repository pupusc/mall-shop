package com.wanmi.sbc.setting.util;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @Description: 利用泛型和反射封装的动态更新工具类
 * @Author zh
 * @Date 2023/2/8 17:09
 */
@Component
public class PartialUpdateUtil {
    /**
     * 根据主键id，实现JPA的动态更新
     * @param id  主键，一般为Integer类型
     * @param src 收集的用户数据
     * @param dao JpaRepository对象
     * @param <T> src对象对应的实体类型
     * @param <I> 主键参数对应的类型
     * @return 更新后的实体对象
     * @throws IllegalAccessException
     */
    public <T, I> T partialUpdate(I id, T src, JpaRepository<T, I> dao) throws IllegalAccessException {
        T orig = dao.findById(id).get();
        T dest = src;
        Field[] fields = src.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            //条件：对于某个Field,如果orig的值不为null,并且src的值为null
            if (field.get(orig) != null
                    && field.get(src) == null) {
                //操作：则将orig的当前Field的值，赋值给dest对应的Field
                field.set(dest, field.get(orig));
            }
        }
        T savedData = dao.save(dest);
        return savedData;
    }
}
