package io.github.yangyouwang.module.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.base.service.BaseService;
import io.github.yangyouwang.common.constant.ConfigConsts;
import io.github.yangyouwang.module.system.mapper.SysDictTypeMapper;
import io.github.yangyouwang.module.system.mapper.SysDictValueMapper;
import io.github.yangyouwang.module.system.entity.SysDictType;
import io.github.yangyouwang.module.system.entity.SysDictValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yangyouwang
 * @title: SysDictService
 * @projectName crud
 * @description: 字典业务层
 * @date 2021/4/13下午1:11
 */
@Service
public class SysDictTypeService extends BaseService<SysDictTypeMapper, SysDictType> {

    @Resource
    private SysDictTypeMapper sysDictTypeMapper;

    @Resource
    private SysDictValueMapper sysDictValueMapper;

    @Resource
    private HttpServletResponse response;

    /**
     * 列表请求
     * @param param 请求字典列表参数
     * @return 请求列表
     */
    @Transactional(readOnly = true)
    public List<SysDictType> list(SysDictType param) {
         return sysDictTypeMapper.selectDictPage(new LambdaQueryWrapper<SysDictType>()
                 .like(!StringUtils.isEmpty(param.getDictKey()), SysDictType::getDictKey,param.getDictKey())
                 .like(!StringUtils.isEmpty(param.getDictName()), SysDictType::getDictName,param.getDictName()));
    }

    /**
     * 添加请求
     * @param sysDictType 添加字典参数
     * @return 添加状态
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public boolean add(SysDictType sysDictType) {
        SysDictType old = sysDictTypeMapper.findDictByKey(sysDictType.getDictKey());
        Assert.isNull(old, "字典已存在");
        return this.save(sysDictType);
    }

    /**
     * 删除请求
     * @param id 字典id
     */
    @Transactional(isolation = Isolation.DEFAULT,propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public void remove(Long id) {
        if (this.removeById(id)) {
            sysDictValueMapper.delete(new LambdaQueryWrapper<SysDictValue>()
                    .eq(SysDictValue::getDictTypeId,id));
        }
    }
    /**
     * 缓存字典
     */
    public void cacheDict() {
        List<SysDictType> sysDictTypes = sysDictTypeMapper.selectDictPage(new LambdaQueryWrapper<SysDictType>());
        sysDictTypes.forEach(sysDictType -> {
            List<SysDictValue> dictValues = sysDictType.getDictValues().stream().filter(s -> ConfigConsts.SYS_YES.equals(s.getEnabled())).collect(Collectors.toList());
            String dictValue = JSONArray.parseArray(JSON.toJSONString(dictValues)).toJSONString();
            try {
                Cookie cookie = new Cookie(sysDictType.getDictKey(), URLEncoder.encode(dictValue, "utf-8"));
                if (ConfigConsts.SYS_YES.equals(sysDictType.getEnabled())) {
                    cookie.setMaxAge(7 * 24 * 60 * 60);
                } else {
                    cookie.setMaxAge(0);
                }
                cookie.setPath("/");
                response.addCookie(cookie);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }
}
