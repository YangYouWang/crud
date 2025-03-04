package io.github.yangyouwang.framework.util.excel;

import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;

/**
 * 自定义EasyExcel Sheet
 *
 */
public class CustomSheetWriteHandler implements SheetWriteHandler{

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        //得到Sheet
        Sheet sheet = writeSheetHolder.getSheet();
        //设置将所有列调整为一页
        sheet.setFitToPage(true);
        //设置打印内容水平居中显示
        sheet.setHorizontallyCenter(true);
        //设置打印页面边距
        sheet.setMargin(Sheet.TopMargin, 0.3);
        sheet.setMargin(Sheet.BottomMargin, 0.5);
        sheet.setMargin(Sheet.LeftMargin, 0.3);
        sheet.setMargin(Sheet.RightMargin, 0.3);
        //打印设置对象
        PrintSetup print = sheet.getPrintSetup();
        //并缩减打印输出只有一页宽
        print.setFitHeight((short)0);
        //设置竖屏打印（false），横屏打印（true）
        print.setLandscape(false);
        //设置A4纸打印
        print.setPaperSize(PrintSetup.A4_PAPERSIZE);
    }
}