package com.codingapi.springboot.flow.content;

import com.codingapi.springboot.flow.bind.IBindData;
import com.codingapi.springboot.flow.domain.FlowNode;
import com.codingapi.springboot.flow.domain.FlowWork;
import com.codingapi.springboot.flow.domain.Opinion;
import com.codingapi.springboot.flow.error.NodeResult;
import com.codingapi.springboot.flow.error.OperatorResult;
import com.codingapi.springboot.flow.record.FlowRecord;
import com.codingapi.springboot.flow.result.MessageResult;
import com.codingapi.springboot.flow.service.FlowService;
import com.codingapi.springboot.flow.user.IFlowOperator;
import lombok.Getter;

import java.util.List;

/**
 * 流程groovy脚本回话对象
 */
@Getter
public class FlowSession {

    // 当前的流程记录
    private final FlowRecord flowRecord;
    // 当前的流程设计器
    private final FlowWork flowWork;
    // 当前的流程节点
    private final FlowNode flowNode;
    // 流程的创建者
    private final IFlowOperator createOperator;
    // 当前的操作者
    private final IFlowOperator currentOperator;
    // 流程绑定数据
    private final IBindData bindData;
    // 流程审批意见
    private final Opinion opinion;
    // 当前节点的审批记录
    private final List<FlowRecord> historyRecords;
    // bean提供者
    private final FlowSessionBeanProvider provider;

    public FlowSession(FlowRecord flowRecord,
                       FlowWork flowWork,
                       FlowNode flowNode,
                       IFlowOperator createOperator,
                       IFlowOperator currentOperator,
                       IBindData bindData,
                       Opinion opinion,
                       List<FlowRecord> historyRecords) {
        this.flowRecord = flowRecord;
        this.flowWork = flowWork;
        this.flowNode = flowNode;
        this.createOperator = createOperator;
        this.currentOperator = currentOperator;
        this.bindData = bindData;
        this.opinion = opinion;
        this.historyRecords = historyRecords;
        this.provider = FlowSessionBeanProvider.getInstance();
    }


    public Object getBean(String beanName) {
        return provider.getBean(beanName);
    }

    /**
     * 创建节点结果
     *
     * @param nodeCode 节点code
     * @return 节点结果
     */
    public NodeResult createNodeErrTrigger(String nodeCode) {
        return new NodeResult(nodeCode);
    }

    /**
     * 创建操作者结果
     *
     * @param operatorIds 操作者id
     * @return 操作者结果
     */
    public OperatorResult createOperatorErrTrigger(List<Long> operatorIds) {
        return new OperatorResult(operatorIds);
    }

    /**
     * 创建操作者结果
     *
     * @param operatorIds 操作者id
     * @return 操作者结果
     */
    public OperatorResult createOperatorErrTrigger(long... operatorIds) {
        return new OperatorResult(operatorIds);
    }

    /**
     * 创建流程提醒
     * @param title 提醒标题
     * @return 提醒对象
     */
    public MessageResult createMessageResult(String title) {
        return MessageResult.create(title);
    }

    /**
     * 创建流程提醒
     * @param title 提醒标题
     * @param closeable 是否可关闭流程
     * @return 提醒对象
     */
    public MessageResult createMessageResult(String title, boolean closeable) {
        return MessageResult.create(title, closeable);
    }


    /**
     * 创建流程提醒
     * @param title 提醒标题
     * @param items 提醒内容
     * @param closeable 是否可关闭流程
     * @return 提醒对象
     */
    public MessageResult createMessageResult(String title, List<MessageResult.Message> items, boolean closeable) {
        return MessageResult.create(title, items, closeable);
    }

    /**
     * 提交流程
     */
    public void submitFlsubmitFlowow() {
        if(flowRecord==null){
            throw new IllegalArgumentException("flow record is null");
        }
        FlowService flowService = loadFlowService();
        flowService.submitFlow(flowRecord.getId(), currentOperator, bindData, opinion);
    }

    /**
     * 保存流程
     */
    public void saveFlow() {
        if(flowRecord==null){
            throw new IllegalArgumentException("flow record is null");
        }
        FlowService flowService = loadFlowService();
        flowService.save(flowRecord.getId(), currentOperator, bindData, opinion.getAdvice());
    }


    /**
     * 催办流程
     */
    public void urgeFlow() {
        if(flowRecord==null){
            throw new IllegalArgumentException("flow record is null");
        }
        FlowService flowService = loadFlowService();
        flowService.urge(flowRecord.getId(), currentOperator);
    }

    /**
     * 撤回流程
     */
    public void recallFlow() {
        if(flowRecord==null){
            throw new IllegalArgumentException("flow record is null");
        }
        FlowService flowService = loadFlowService();
        flowService.recall(flowRecord.getId(), currentOperator);
    }


    private FlowService loadFlowService() {
        return (FlowService) getBean("flowService");
    }
}
