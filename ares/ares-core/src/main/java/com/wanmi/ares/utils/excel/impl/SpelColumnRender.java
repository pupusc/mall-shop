package com.wanmi.ares.utils.excel.impl;

import com.wanmi.ares.exception.AresRuntimeException;
import com.wanmi.ares.utils.excel.ColumnRender;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by mac on 2017/5/6.
 */
@Slf4j
public class SpelColumnRender<T> implements ColumnRender<T> {

    private String expString;

    /**
     * 时间格式化
     */
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SpelColumnRender(String expString) {
        this.expString = expString;
    }

    @Override
    public void render(HSSFCell cell, T object) throws AresRuntimeException {
        ExpressionParser parser = new SpelExpressionParser();
        Object o;
        try {
            Expression exp = parser.parseExpression(expString);
            o = exp.getValue(object);
        } catch (SpelEvaluationException e) {
            cell.setCellValue("");
            return;
        }
        if (o == null) {
            cell.setCellValue("");
            return;
        }

        Class<?> aClass = o.getClass();
        //
        if (aClass.isAssignableFrom(String.class)) {
            cell.setCellValue((String) o);
        } else if (aClass.isAssignableFrom(Integer.class) || aClass.isAssignableFrom(Long.class)) {
            cell.setCellValue(String.valueOf(o));
        }
        //
        else if (aClass.isAssignableFrom(LocalDateTime.class)) {
            LocalDateTime dd = (LocalDateTime) o;
            cell.setCellValue(dd.format(timeFormatter));
        } else if (aClass.isAssignableFrom(BigDecimal.class) || aClass.isAssignableFrom(Double.class)) {
            cell.setCellValue(new DecimalFormat("0.00").format(o));
        }
        //
        else {
            throw new AresRuntimeException("未检出的类型");
        }
    }

}
