package io.github.yangyouwang.framework.util.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Description: Excel工具类<br/>
 * date: 2022/9/21 23:32<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
public class EasyExcelUtil {
    /**
     * 导出excel核心方法
     */
    public static void export(HttpServletResponse response, Class head, List data, String sheetName, HorizontalCellStyleStrategy horizontalCellStyleStrategy) throws IOException {
        //给定导出实体类
        EasyExcel.write(response.getOutputStream(),head)
                //给定工作表名称
                .sheet(sheetName)
                //给定样式
                .registerWriteHandler(horizontalCellStyleStrategy)
                //给定导出数据
                .doWrite(data);
    }

    /**
     * 设置请求头、文件名
     *
     * @param fileName excel文件名
     */
    public static void setResponse(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        //编码设置成UTF-8，excel文件格式为.xlsx
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        // 这里URLEncoder.encode可以防止中文乱码 和easyexcel本身没有关系
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
    }

    /**
     * 设置生成excel样式 去除默认表头样式及设置内容居中，如有必要可重载该方法给定参数配置不同样式
     *
     * @return HorizontalCellStyleStrategy
     */
    public static HorizontalCellStyleStrategy getStyleStrategy() {
        //内容样式策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //垂直居中,水平居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        //设置 自动换行
        contentWriteCellStyle.setWrapped(true);
        // 字体策略
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        //头策略使用默认
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }
}
