package com.wanmi.sbc.common.util.excel.impl;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.excel.ColumnRender;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by mac on 2017/5/6.
 */
public class SpelColumnRender<T> implements ColumnRender<T> {

    private String expString;

    /**
     * 时间格式化
     */
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 日期格式化
     */
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public SpelColumnRender(String expString) {
        this.expString = expString;
    }

    @Override
    public void render(HSSFCell cell, T object) {
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
        } else if (aClass.isAssignableFrom(Integer.class)) {
            cell.setCellValue(String.valueOf(o));
        }
        //
        else if (aClass.isAssignableFrom(LocalDateTime.class)) {
            LocalDateTime dd = (LocalDateTime) o;
            cell.setCellValue(dd.format(timeFormatter));
        } else if (aClass.isAssignableFrom(LocalDate.class)) {
            LocalDate dd = (LocalDate) o;
            cell.setCellValue(dd.format(dateFormatter));
        } else if (aClass.isAssignableFrom(BigDecimal.class)) {
            cell.setCellValue(new java.text.DecimalFormat("#,##0.00").format(o));
        } else if (aClass.isAssignableFrom(Long.class)) {
            cell.setCellValue(String.valueOf(o));
        }

        //
        else {
            throw new SbcRuntimeException("未检出的类型");
        }
    }

}
