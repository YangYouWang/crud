package io.github.yangyouwang.module.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.yangyouwang.common.constant.ConfigConsts;
import io.github.yangyouwang.common.domain.TreeSelectNode;
import io.github.yangyouwang.framework.util.converter.ListToTree;
import io.github.yangyouwang.framework.util.converter.impl.ListToTreeImpl;
import io.github.yangyouwang.framework.web.exception.CrudException;
import io.github.yangyouwang.module.system.entity.SysDept;
import io.github.yangyouwang.module.system.mapper.SysDeptMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/**
* <p>
 * 部门表 服务实现类
 * </p>
*
* @author yangyouwang
* @since 2022-09-03
*/
@Service
public class SysDeptService extends ServiceImpl<SysDeptMapper, SysDept> {

  @Resource
  private SysDeptMapper sysDeptMapper;

  /**
  * 部门表详情
  * @param id 主键
  * @return 结果
  */
  public SysDept info(Long id) {
    return sysDeptMapper.info(id);
  }

  /**
  * 部门表新增
  * @param param 根据需要进行传值
  */
  public void add(SysDept param) {
    save(param);
  }

  /**
  * 部门表修改
  * @param param 根据需要进行传值
  */
  public void modify(SysDept param) {
    updateById(param);
  }

  /**
  * 部门表删除(单个条目)
  * @param id 主键
  */
  public void remove(Long id) {
    // 删除子部门
    int count = this.count(new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getParentId, id));
    if (count != 0) {
      throw new CrudException("存在部门,删除失败!");
    }
    removeById(id);
  }

  /**
  * 部门表删除(多个条目)
  * @param ids 主键数组
  */
  public void removes(List<Long> ids) {
     removeByIds(ids);
   }
  /**
   * 查询部门树结构
   * @return 部门树结构
   */
  public List<TreeSelectNode> treeSelect() {
    List<SysDept> depts = this.list(new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getEnabled,ConfigConsts.SYS_YES));
    // 父部门ID是否存在顶级
    boolean flag = depts.stream().anyMatch(s -> 0 == s.getParentId());
    if (depts.isEmpty() || !flag) {
      return Collections.emptyList();
    }
    List<TreeSelectNode> result = depts.stream().map(sysMenu -> {
      TreeSelectNode treeNode = new TreeSelectNode();
      treeNode.setId(sysMenu.getId());
      treeNode.setParentId(sysMenu.getParentId());
      treeNode.setName(sysMenu.getDeptName());
      return treeNode;
    }).collect(Collectors.toList());
    ListToTree treeBuilder = new ListToTreeImpl();
    return treeBuilder.toTree(result);
  }
}
