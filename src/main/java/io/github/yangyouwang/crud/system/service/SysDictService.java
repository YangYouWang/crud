package io.github.yangyouwang.crud.system.service;

import io.github.yangyouwang.crud.system.dao.SysDictTypeRepository;
import io.github.yangyouwang.crud.system.model.SysDictType;
import io.github.yangyouwang.crud.system.model.SysDictValue;
import io.github.yangyouwang.crud.system.model.req.SysDictAddReq;
import io.github.yangyouwang.crud.system.model.req.SysDictEditReq;
import io.github.yangyouwang.crud.system.model.req.SysDictListReq;
import io.github.yangyouwang.crud.system.model.resp.SysDictResp;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
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
public class SysDictService {

    @Autowired
    private SysDictTypeRepository sysDictTypeRepository;

    /**
     * 列表请求
     * @return 请求列表
     */
    public Page<SysDictType> list(SysDictListReq sysDictListReq) {
        Pageable pageable = PageRequest.of(sysDictListReq.getPageNum() - 1,sysDictListReq.getPageSize());
        Specification<SysDictType> query = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            String dictName = sysDictListReq.getDictName();
            if(Strings.isNotBlank(dictName)){
                predicates.add(criteriaBuilder.like(root.get("dictName"),"%" + dictName + "%"));
            }
            String dictKey = sysDictListReq.getDictKey();
            if(Strings.isNotBlank(dictKey)){
                predicates.add(criteriaBuilder.like(root.get("dictKey"),"%" +dictKey + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return sysDictTypeRepository.findAll(query,pageable);
    }

    /**
     * 跳转编辑
     * @return 编辑页面
     */
    public SysDictResp detail(Long id) {
        SysDictType sysDict = sysDictTypeRepository.findById(id).get();
        SysDictResp sysDictResp = new SysDictResp();
        BeanUtils.copyProperties(sysDict,sysDictResp);
        List<SysDictResp.SysDictValueDto> sysDictValues = sysDict.getSysDictValues().stream().map(sysDictValue -> {
            SysDictResp.SysDictValueDto sysDictValueDto = new SysDictResp.SysDictValueDto();
            BeanUtils.copyProperties(sysDictValue, sysDictValueDto);
            return sysDictValueDto;
        }).collect(Collectors.toList());
        sysDictResp.setSysDictValues(sysDictValues);
        return sysDictResp;
    }

    /**
     * 添加请求
     */
    public void add(SysDictAddReq sysDictAddReq) {
        SysDictType sysDict = new SysDictType();
        BeanUtils.copyProperties(sysDictAddReq,sysDict);
        List<SysDictValue> sysDictValues = sysDictAddReq.getSysDictValues().stream().map(sysDictValueDto -> {
            SysDictValue sysDictValue = new SysDictValue();
            BeanUtils.copyProperties(sysDictValueDto, sysDictValue);
            return sysDictValue;
        }).collect(Collectors.toList());
        sysDict.setSysDictValues(sysDictValues);
        sysDictTypeRepository.save(sysDict);
    }

    /**
     * 编辑请求
     */
    public void edit(SysDictEditReq sysDictEditReq) {
      // 删除字典值
      this.del(sysDictEditReq.getId());
      SysDictType sysDict = new SysDictType();
      List<SysDictValue> sysDictValues = sysDictEditReq.getSysDictValues().stream().map(sysDictValueDto -> {
            SysDictValue sysDictValue = new SysDictValue();
            BeanUtils.copyProperties(sysDictValueDto, sysDictValue);
            return sysDictValue;
      }).collect(Collectors.toList());
      sysDict.setSysDictValues(sysDictValues);
      BeanUtils.copyProperties(sysDictEditReq,sysDict);
      sysDictTypeRepository.save(sysDict);
    }

    /**
     * 删除请求
     */
    public void del(Long id) {
        if(sysDictTypeRepository.existsById(id)) {
            sysDictTypeRepository.deleteById(id);
        }
    }
}
