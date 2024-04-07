package io.github.yangyouwang.framework.util.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.IoUtils;
import org.apache.logging.log4j.util.Strings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Description: 图片转换器<br/>
 * date: 2022/9/19 21:57<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
public class MyStringImageConverter implements Converter<String>
{
    @Override
    public Class supportJavaTypeKey()
    {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey()
    {
        return CellDataTypeEnum.IMAGE;
    }

    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                    GlobalConfiguration globalConfiguration)
    {
        throw new UnsupportedOperationException("Cannot convert images to string");
    }

    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty contentProperty,GlobalConfiguration globalConfiguration) throws IOException {
        InputStream inputStream = null;
        try {
            if(Strings.isBlank(value)) {
                return new CellData("图片路径为空");
            }
            URL urlValue = new URL(value);
            // 开启连接
            URLConnection uc = urlValue.openConnection();
            // 获取响应状态
            int statusCode = ((HttpURLConnection)uc).getResponseCode();
            if (statusCode == 200) {
                inputStream = urlValue.openStream();
            } else {
                return new CellData("无法加载图片");
            }
            byte[] bytes = IoUtils.toByteArray(inputStream);
            return new CellData(bytes);
        } catch (ConnectException | FileNotFoundException exception) {
            return new CellData("无法加载图片");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
