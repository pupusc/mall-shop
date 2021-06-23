package com.wanmi.sbc.common.annotation;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: songhanlin
 * @Date: Created In 11:34 2019-02-01
 * @Description: Swagger enum支持
 */
@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
public class ApiEnumDescriptionPlugin implements ModelPropertyBuilderPlugin, ParameterBuilderPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiEnumDescriptionPlugin.class);

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    @Override
    public void apply(ModelPropertyContext context) {
        try {
            Optional<BeanPropertyDefinition> beanDef = context.getBeanPropertyDefinition();
            if (beanDef.isPresent()) {
                AnnotatedField aField = beanDef.get().getField();
                if (aField != null) {
                    Field field = aField.getAnnotated();
                    if (field != null) {
                        ApiModelProperty property = field.getAnnotation(ApiModelProperty.class);
                        if (property != null) {
                            Class<?> clazz = field.getType();
                            buildDescription(context, property, clazz, aField);
                            if (StringUtils.isNotBlank(property.dataType())) {
                                Class<?> clazz2 = Class.forName(property.dataType());
                                buildDescription(context, property, clazz2, aField);
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            // 生吞活剥异常, 忽略Class.forName()的转换异常
        } catch (Throwable t) {
            // The exception will be logged, because Springfox will not.
            LOGGER.warn("Cannot process ApiModelProperty. Will throw new RuntimeException now.", t);
            throw new RuntimeException(t);
        }
    }

    private void buildDescription(ModelPropertyContext context, ApiModelProperty property, Class<?> clazz,
                                  AnnotatedField aField) {
        if (clazz.isEnum()) {
            // 读取ApiEnum注解类型
            readEnumType(context, (Class<? extends Enum<?>>) clazz, aField);
            String description = property.value();
            @SuppressWarnings("unchecked")
            String markdown = createMarkdownDescription((Class<? extends Enum<?>>) clazz);
            if (markdown != null) {
                description += "\n" + markdown;
                context.getBuilder().description(description);
            }
        } else if (Objects.nonNull(aField.getType().getContentType()) && aField.getType().getContentType().isEnumType()) {
            // 读取ApiEnum注解类型
            readEnumType(context, (Class<? extends Enum<?>>) aField.getType().getContentType().getRawClass(), aField);
            String description = property.value();
            @SuppressWarnings("unchecked")
            String markdown =
                    createMarkdownDescription((Class<? extends Enum<?>>) aField.getType().getContentType().getRawClass());
            if (markdown != null) {
                description += "\n" + markdown;
                context.getBuilder().description(description);
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see springfox.documentation.spi.service.ParameterBuilderPlugin#apply(
     * springfox.documentation.spi.service.contexts.ParameterContext)
     */
    @Override
    public void apply(ParameterContext context) {
        try {
            ResolvedMethodParameter param = context.resolvedMethodParameter();
            if (param != null) {
                ResolvedType resType = param.getParameterType();
                if (resType != null) {
                    Class<?> clazz = resType.getErasedType();
                    if (clazz.isEnum()) {
                        Optional<ApiParam> annotation = param.findAnnotation(ApiParam.class);
                        if (annotation.isPresent()) {
                            String description = annotation.get().value();
                            @SuppressWarnings("unchecked")
                            String markdown = createMarkdownDescription((Class<? extends Enum<?>>) clazz);

                            if (markdown != null) {
                                description += "\n" + markdown;
                                context.parameterBuilder().description(description);
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            // The exception will be logged, because Springfox will not.
            LOGGER.warn("Cannot process ApiParameter. Will throw new RuntimeException now.", t);
            throw new RuntimeException(t);
        }
    }

    /**
     * Creates a markdown description of all enums of <i>clazz</i> including the
     * description which is being pulled from {@link ApiEnum}.
     */
    static String createMarkdownDescription(Class<? extends Enum<?>> clazz) {
        List<String> lines = new ArrayList<>();

        boolean foundAny = false;
        for (Enum<?> enumVal : clazz.getEnumConstants()) {
            String desc = readApiDescription(enumVal);
            if (desc != null) {
                foundAny = true;
            }
            String line = "* " + enumVal.name() + ": " + (desc == null ? "_@ApiEnumPropertyProperty annotation not " +
                    "available_" : desc);
            lines.add(line);
        }

        if (foundAny)
            return StringUtils.join(lines, "\n");
        else
            return null;
    }

    /**
     * @return the value of {@link ApiEnumProperty#value()}, if present for <i>e</i>.
     */
    static String readApiDescription(Enum<?> e) {
        try {
            ApiEnumProperty annotation = e.getClass().getField(e.name()).getAnnotation(ApiEnumProperty.class);
            if (annotation != null)
                return annotation.value();
        } catch (NoSuchFieldException e1) {
            throw new RuntimeException("impossible?", e1);
        } catch (SecurityException e1) {
            throw new RuntimeException("could not read annotation", e1);
        }
        return null;
    }

    /**
     * ApiEnum类型转换
     *
     * @param resolver
     * @return
     */
    static Function<ApiEnum, ResolvedType> toType(final TypeResolver resolver) {
        return annotation -> {
            try {
                return resolver.resolve(Class.forName(annotation.dataType()));
            } catch (ClassNotFoundException e) {
                return resolver.resolve(Object.class);
            }
        };
    }

    /**
     * ApiEnum类型转换
     *
     * @param resolver
     * @return
     */
    static Function<ApiEnum, ResolvedType> toArrayType(final TypeResolver resolver) {
        return annotation -> {
            try {
                return resolver.arrayType(Class.forName(annotation.dataType()));
            } catch (ClassNotFoundException e) {
                return resolver.arrayType(Object.class);
            }
        };
    }

    /**
     * 读取设置ApiEnum类型
     *
     * @param context
     * @param clazz
     */
    static void readEnumType(ModelPropertyContext context, Class<? extends Enum<?>> clazz, AnnotatedField aField) {
        ApiEnum annotation = clazz.getAnnotation(ApiEnum.class);
        if(annotation == null){
            return;
        }
        Optional<ApiEnum> apiEnumOptional = Optional.fromNullable(annotation);
        ResolvedType resolvedType;
        if(aField.getType().isCollectionLikeType()){
            resolvedType = apiEnumOptional.transform(toArrayType(context.getResolver())).orNull();
        }else{
            resolvedType = apiEnumOptional.transform(toType(context.getResolver())).orNull();
        }
        context.getBuilder().type(resolvedType);
    }
}