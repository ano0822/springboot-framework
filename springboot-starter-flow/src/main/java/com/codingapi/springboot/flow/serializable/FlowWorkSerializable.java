package com.codingapi.springboot.flow.serializable;

import com.codingapi.springboot.flow.domain.FlowNode;
import com.codingapi.springboot.flow.domain.FlowWork;
import com.codingapi.springboot.flow.em.ApprovalType;
import com.codingapi.springboot.flow.em.NodeType;
import com.codingapi.springboot.flow.repository.FlowOperatorRepository;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FlowWorkSerializable implements Serializable {

    /**
     * 流程的设计id
     */
    private long id;
    /**
     * 流程标题
     */
    private String title;
    /**
     * 流程描述
     */
    private String description;
    /**
     * 流程创建者
     */
    private long createUser;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 更新时间
     */
    private long updateTime;
    /**
     * 是否启用
     */
    @Setter
    private boolean enable;

    /**
     * 最大延期次数
     */
    @Setter
    private int postponedMax;

    /**
     * 流程的节点(发起节点)
     */
    private List<FlowNodeSerializable> nodes;

    /**
     * 流程的关系
     */
    private List<FlowRelationSerializable> relations;


    /**
     * 序列化
     *
     * @return 序列化对象
     */
    public byte[] toSerializable() {
        Kryo kryo = new Kryo();
        kryo.register(ArrayList.class);
        kryo.register(FlowNodeSerializable.class);
        kryo.register(FlowRelationSerializable.class);
        kryo.register(FlowWorkSerializable.class);
        kryo.register(ApprovalType.class);
        kryo.register(NodeType.class);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeObject(output, this);
        output.close();
        return outputStream.toByteArray();
    }


    public static FlowWorkSerializable fromSerializable(byte[] bytes) {
        Kryo kryo = new Kryo();
        kryo.register(ArrayList.class);
        kryo.register(FlowNodeSerializable.class);
        kryo.register(FlowRelationSerializable.class);
        kryo.register(FlowWorkSerializable.class);
        kryo.register(ApprovalType.class);
        kryo.register(NodeType.class);
        return kryo.readObject(new Input(bytes), FlowWorkSerializable.class);
    }


    public FlowWork toFlowWork(FlowOperatorRepository flowOperatorRepository) {
        List<FlowNode> flowNodes = nodes.stream().map(FlowNodeSerializable::toFlowNode).toList();
        return new FlowWork(
                id,
                title,
                description,
                flowOperatorRepository.getFlowOperatorById(createUser),
                createTime,
                updateTime,
                enable,
                postponedMax,
                flowNodes,
                relations.stream().map((item) -> item.toFlowRelation(flowNodes)).toList(),
                null
        );
    }


}
