package io.github.yangyouwang.common.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.yangyouwang.common.base.domain.TableDataInfo;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * Description: 抽象控制层 <br/>
 * date: 2022/8/1 17:21<br/>
 *
 * @author yangyouwang<br />
 * @version v1.0
 * @since JDK 1.8
 */
public class BaseController {

    /**
     * 设置请求分页数据
     */
    public void startPage()
    {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Assert.notNull(request.getParameter("page"), "page参数不存在");
        Assert.notNull(request.getParameter("limit"), "limit参数不存在");
        int page = Integer.parseInt(request.getParameter("page"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        PageHelper.startPage(page, limit);
    }
    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(0);
        tableDataInfo.setData(list);
        tableDataInfo.setCount(new PageInfo(list).getTotal());
        return tableDataInfo;
    }

}
