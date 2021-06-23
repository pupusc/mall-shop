package com.wanmi.ares.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 实体属性copy,属性为空不copy
 * Created by hehu on 16/5/19.
 * @author QM-HEHU
 */
@SuppressWarnings("unchecked")
public class KsBeanUtil {

    /**
     * 复制List
     * @param source
     * @param target
     */
    public static void copyList(List source, List target){
        source.forEach(o -> {
            try {
                Object c = o.getClass().newInstance();
                BeanUtils.copyProperties(o, c);
                target.add(c);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public static List copyListProperties(List source, Class clazz){
        List target = new ArrayList();
        source.forEach(o -> {
            try {
                target.add( JSONObject.parseObject(JSONObject.toJSONString(o),clazz));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return target;
    }

    /**
     * 转换List泛型
     * @param source 源列表
     * @param clazz 目标列表
     */
    public static <T> List<T> convertList(List source, Class<T> clazz){
        List<T> res = new ArrayList<>();
        source.forEach(o -> res.add(KsBeanUtil.copyPropertiesThird(o, clazz)));
        return res;
    }

    /**
     * 源对象和目标对象的浅拷贝
     * 注意点：目标对象与源对象中的属性名称必须一致，否则无法拷贝
     *
     * @param sourceObj 源对象
     * @param targetObj 目标对象
     */
    public static void copyPropertiesThird(Object sourceObj, Object targetObj) {
        BeanUtils.copyProperties(sourceObj, targetObj);
    }

    /**
     * 源对象和目标对象的浅拷贝
     * 注意点：目标对象与源对象中的属性名称必须一致，否则无法拷贝
     *
     * @param sourceObj 源对象
     * @param clazz     目标对象的Class对象
     * @param <T>
     * @return          目标对象
     */
    public static <T> T copyPropertiesThird(Object sourceObj,  Class<T> clazz) {
        T c = null;
        try {
            c = clazz.newInstance();
            KsBeanUtil.copyPropertiesThird(sourceObj, c);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * 用于源对象和目标对象中属性类型不同的深度拷贝
     * 注意点：目标对象与源对象中的属性名称必须一致，否则无法拷贝
     * @param source        源对象
     * @param targetClass   目标对象的Class对象
     * @param <T>
     * @return              目标对象
     */
    public static <T> T convert(Object source, Class<T> targetClass,SerializerFeature... features) {
        String sourceJsonStr = JSONObject.toJSONString(source,features);
        T target = JSONObject.parseObject(sourceJsonStr, targetClass);
        return target;
    }

    /**
     * 用于源集合和目标集合之间的深度转换
     * 注意点：目标集合对象与源集合对象中的属性名称必须一致，否则无法拷贝
     * 测试示例可参考
     * @param sourceList    源集合
     * @param targetClass   目标对象的Class对象
     * @param <S>           源集合对象类型
     * @param <T>           目标集合对象类型
     * @return              目标集合
     */
    public static <S,T> List<T> convert(List<S> sourceList, Class<T> targetClass,SerializerFeature... features) {
        return sourceList.stream().map(s -> convert(s, targetClass,features)).collect(Collectors.toList());
    }

    /**
     * 获取类所有属性（包含父类属性）
     * @param cls 类
     * @param fields 属性List
     * @return 所有fields
     */
    private static List<Field> getBeanFields(Class cls, List<Field> fields){
        fields.addAll(Arrays.asList(cls.getDeclaredFields()));
        if(Objects.nonNull(cls.getSuperclass())){
            fields = getBeanFields(cls.getSuperclass() , fields);
        }
        return fields;
    }
}