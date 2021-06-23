package com.wanmi.sbc.common.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.base.MicroServicePage;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
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
     * 合并实体属性值,为空不覆盖
     * @param sourceObj 源对象
     * @param targetObj 目标对象
     */
    public static void copyProperties(Object sourceObj, Object targetObj) {
        String fileName,str,getName,setName;
        List fields = new ArrayList();
        Method getMethod;
        Method setMethod;
        try {
            Class c1 = sourceObj.getClass();
            Class c2 = targetObj.getClass();
            List<Field> c1Fields = getBeanFields(c1, new ArrayList<>());
            List<Field> c2Fields = getBeanFields(c2, new ArrayList<>());

            //两个类属性比较剔除不相同的属性，只留下相同可修改的属性
            c2Fields.stream()
                    .filter(field2 -> !Modifier.isFinal(field2.getModifiers()))
                    .forEach(field2 -> c1Fields.stream()
                                .filter(field1 -> !Modifier.isFinal(field1.getModifiers()))
                                .forEach(field1 -> {
                                    if (field1.getName().equals(field2.getName()) && field1.getType().equals
                                            (field2.getType())) {
                                        fields.add(field1);
                                    }
                                }));

            if(fields.size() > 0){
                for (Object field : fields) {
                    //获取属性名称
                    Field f = (Field) field;
                    fileName = f.getName();
                    //属性名第一个字母大写
                    str = fileName.substring(0, 1).toUpperCase();
                    //拼凑getXXX和setXXX方法名
                    getName = "get" + str + fileName.substring(1);
                    setName = "set" + str + fileName.substring(1);
                    //获取get、set方法
                    getMethod = c1.getMethod(getName);
                    setMethod = c2.getMethod(setName, f.getType());

                    //获取属性值
                    Object o = getMethod.invoke(sourceObj);
                    //将属性值放入另一个对象中对应的属性
                    //如果有此注解，直接放值
                    if(f.isAnnotationPresent(CanEmpty.class)){
                        setMethod.invoke(targetObj, o);
                    }else if (null != o) {
                        setMethod.invoke(targetObj, o);
                    }
                }
            }
        } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
     * 测试示例可参考 {@link com.wanmi.sbc.convert.ConvertUtils}
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
     * 测试示例可参考 {@link com.wanmi.sbc.convert.ConvertUtils}
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
     * 用于源分页集合和目标分页集合之间的深度转换
     * 注意点：目标分页元素对象与源分页元素对象中的属性名称必须一致，否则无法拷贝
     * 测试示例可参考 {@link com.wanmi.sbc.convert.ConvertUtils}
     * @param sourcePage    源分页集合
     * @param targetClass   目标元素对象中Class对象
     * @param <S>           源分页对象中元素类型
     * @param <T>           目标分页对象中元素类型
     * @return              目标分页集合
     */
    public static <S,T> MicroServicePage<T> convertPage(Page<S> sourcePage, Class<T> targetClass) {
        if(CollectionUtils.isEmpty(sourcePage.getContent())){
            return new MicroServicePage<>(
                    Collections.emptyList(),
                    PageRequest.of(sourcePage.getNumber(), sourcePage.getSize(), sourcePage.getSort()),
                    0
            );
        }
        return new MicroServicePage<>(
                convert(sourcePage.getContent(), targetClass),
                PageRequest.of(sourcePage.getNumber(), sourcePage.getSize(), sourcePage.getSort()),
                sourcePage.getTotalElements()
        );
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



    /**
     * 使用Introspector，map集合成javabean
     *
     * @param map       map
     * @param beanClass bean的Class类
     * @return bean对象
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> beanClass) {

        if (MapUtils.isEmpty(map)) {
            return null;
        }

        try {
            T t = beanClass.newInstance();

            BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                Method setter = property.getWriteMethod();
                if (setter != null) {
                    setter.invoke(t, map.get(property.getName()));
                }
            }
            return t;
        } catch (Exception ex) {
            // log.error("########map集合转javabean出错######，错误信息，{}", ex.getMessage());getMessage
            throw new RuntimeException();
        }

    }

    /**
     * 使用Introspector，对象转换为map集合
     *
     * @param beanObj javabean对象
     * @return map集合
     */
    public static Map<String, Object> beanToMap(Object beanObj) {

        if (null == beanObj) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanObj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (key.compareToIgnoreCase("class") == 0) {
                    continue;
                }
                Method getter = property.getReadMethod();
                Object value = getter != null ? getter.invoke(beanObj) : null;
                map.put(key, value);
            }

            return map;
        } catch (Exception ex) {
            // log.error("########javabean集合转map出错######，错误信息，{}", ex.getMessage());
            throw new RuntimeException();
        }
    }


    /**
     * 将List<T>转换为List<Map<String, Object>>
     *
     * @param objList
     * @return
     */
    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

}