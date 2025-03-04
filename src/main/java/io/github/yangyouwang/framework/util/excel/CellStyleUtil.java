package io.github.yangyouwang.framework.util.excel;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 列样式工具
 * 1.该工具实现设置了表格头的样式和表格内容的样式
 * 2.该工具类设置的样式建议和Test实体类中的样式相同
 * 3.该工具类设置的样式就是为了给自定义序号列使用
 *
 */
public class CellStyleUtil {

    public static CellStyle getHeaderStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();
        //设置水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置内容超出自动换行
        cellStyle.setWrapText(true);
        //设置边框为细边框，并且颜色为黑色
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor((short)8);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor((short)8);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor((short)8);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor((short)8);
        //设置字体为宋体，字体加粗，字体大小为12
        Font font = book.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)12);
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static CellStyle getContentStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();
        //设置水平居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //设置垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置内容超出自动换行
        cellStyle.setWrapText(true);
        //设置边框为细边框，并且颜色为黑色
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setTopBorderColor((short)8);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBottomBorderColor((short)8);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor((short)8);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor((short)8);
        //设置字体为宋体，字体不加粗，字体大小为12
        Font font = book.createFont();
        font.setBold(false);
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)12);
        cellStyle.setFont(font);
        return cellStyle;
    }
}