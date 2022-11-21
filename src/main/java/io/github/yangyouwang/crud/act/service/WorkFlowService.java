package io.github.yangyouwang.crud.act.service;

import com.alibaba.fastjson.JSON;
import io.github.yangyouwang.common.domain.TableDataInfo;
import io.github.yangyouwang.core.util.SecurityUtils;
import io.github.yangyouwang.crud.act.model.FlowVO;
import io.github.yangyouwang.crud.act.model.FormVO;
import io.github.yangyouwang.crud.act.model.StartDTO;
import io.github.yangyouwang.crud.act.model.TaskVO;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 工作流业务层
 * @author yangyouwang
 */
@Service
public class WorkFlowService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FormService formService;

    public TableDataInfo myFlow(String name, int page, int limit) {
        String userName = SecurityUtils.getUserName();
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(userName)
                .orderByTaskCreateTime().desc();
        if (StringUtils.isNotBlank(name)) {
            query.processDefinitionNameLike("%" + name + "%");
        }
        List<Task> tasks = query.listPage(page, limit);
        List<TaskVO> taskVOList = tasks.stream().map(s -> {
            TaskVO taskVO = new TaskVO();
            taskVO.setId(s.getId());
            taskVO.setCategory(s.getCategory());
            taskVO.setName(s.getName());
            taskVO.setAssignee(s.getAssignee());
            taskVO.setDescription(s.getDescription());
            taskVO.setCreateTime(s.getCreateTime());
            return taskVO;
        }).collect(Collectors.toList());
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setData(taskVOList);
        rspData.setCount(query.count());
        return rspData;
    }

    public TableDataInfo flow(String name, String key, String category, int page, int limit) {
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc();
        if (StringUtils.isNotBlank(name)) {
            query.processDefinitionNameLike("%" + name + "%");
        }
        if (StringUtils.isNotBlank(key)) {
            query.processDefinitionKeyLike(key);
        }
        if (StringUtils.isNotBlank(category)) {
            query.processDefinitionCategory(category);
        }
        List<ProcessDefinition> processDefinitions = query.listPage(page, limit);
        List<FlowVO> flowVOList = processDefinitions.stream().map(s -> {
            FlowVO flowVO = new FlowVO();
            flowVO.setId(s.getId());
            flowVO.setCategory(s.getCategory());
            flowVO.setName(s.getName());
            flowVO.setKey(s.getKey());
            flowVO.setDescription(s.getDescription());
            flowVO.setVersion(s.getVersion());
            flowVO.setDeploymentId(s.getDeploymentId());
            return flowVO;
        }).collect(Collectors.toList());
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setData(flowVOList);
        rspData.setCount(query.count());
        return rspData;
    }

    public TableDataInfo toDoTask(int page, int limit) {
        String userName = SecurityUtils.getUserName();
        TaskQuery query = taskService.createTaskQuery()
                .taskCandidateOrAssigned(userName)
                .orderByTaskCreateTime().desc();
        List<Task> tasks = query.listPage(page, limit);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setData(tasks);
        rspData.setCount(query.count());
        return rspData;
    }

    public TableDataInfo historicTask(int page, int limit, String name, String categoryId) {
        String userName = SecurityUtils.getUserName();
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        query.involvedUser(userName);
        if (StringUtils.isNotBlank(name)) {
            query.processInstanceNameLike("%" + name + "%");
        }
        if (StringUtils.isNotBlank(categoryId)) {
            query.processDefinitionCategory(categoryId);
        }
        query.orderByProcessInstanceStartTime().desc();
        List<HistoricProcessInstance> historicProcessInstances = query.listPage(page, limit);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setData(historicProcessInstances);
        rspData.setCount(query.count());
        return rspData;
    }

    public String start(StartDTO startDTO) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(startDTO.getDeploymentId()).singleResult();
        // 设置流程发起人用户信息
        String userName = SecurityUtils.getUserName();
        Authentication.setAuthenticatedUserId(userName);
        // 发起流程
        String businessKey = UUID.randomUUID().toString().replaceAll("-", "");
        Map formData = JSON.parseObject(startDTO.getFormData(), Map.class);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), businessKey , formData);
        // 设置流程实例名称
        runtimeService.setProcessInstanceName(processInstance.getId(),startDTO.getTitle());
        return businessKey;
    }

    public FormVO getStartFlowForm(String deploymentId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId).singleResult();
        FormVO formVO = new FormVO();
        formVO.setHasStartFormKey(processDefinition.hasStartFormKey());
        if (processDefinition.hasStartFormKey()) {
           // 外置表单判断
        }
        // 内置表单配置
        StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
        List<FormProperty> formProperties = startFormData.getFormProperties();
        if (formProperties.isEmpty()) {
            throw new RuntimeException("内置动态表单未配置");
        }
        List<FormVO.FormPropertyVO> formPropertyVOList = formProperties.stream().map(s -> {
            FormVO.FormPropertyVO formPropertyVO = new FormVO.FormPropertyVO();
            formPropertyVO.setId(s.getId());
            formPropertyVO.setName(s.getName());
            formPropertyVO.setValue(s.getValue());
            formPropertyVO.setTypeName(s.getType().getName());
            return formPropertyVO;
        }).collect(Collectors.toList());
        formVO.setFormPropertyVOList(formPropertyVOList);
        return formVO;
    }

    public BufferedImage getFlowDiagram(String deploymentId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deploymentId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        return diagramGenerator.generateImage(bpmnModel, "png",
                Collections.emptyList(), Collections.emptyList(),
                "宋体", "宋体", "宋体",
                null, 0);
    }
}
