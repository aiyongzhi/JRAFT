package com.yongzhiai.core.node;

import com.yongzhiai.election.ElectionPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * @ClassName PeerId
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/3 21:19
 * @Version 1.0
 **/

/**
 * Node节点的信息
 *
 */
public class PeerId implements Serializable {

    @Serial
    private static final long serialVersionUID= -4881890636909009192L;


    private static final Logger LOG = LoggerFactory.getLogger(PeerId.class);

    /**
     * 当前节点的地址信息
     */
    private Endpoint endpoint;

    /**
     * 当前服务器进程中当前节点的唯一编号
     */
    private NodeId nodeId;

    /**
     * 当前Node节点所属的Raft集群名称
     */
    private String groupName;

    /**
     * 当前节点的选举优先级
     */
    private ElectionPriority priority=ElectionPriority.DEFAULT;

    private boolean local;


    public PeerId(Endpoint endpoint, NodeId nodeId, String groupName,boolean local) {
        this(endpoint,nodeId,groupName,ElectionPriority.DEFAULT,local);
    }


    public PeerId(Endpoint endpoint, String groupName, boolean local) {
        this(endpoint, null, groupName, local);
    }

    public PeerId(Endpoint endpoint, NodeId nodeId, String groupName, ElectionPriority priority, boolean local) {
        this.endpoint = endpoint;
        this.nodeId = nodeId;
        this.groupName = groupName;
        this.priority = priority;
        this.local=local;
    }

    public PeerId(Endpoint endpoint, String groupName) {
        this(endpoint,groupName,true);
    }


    public PeerId() {
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public NodeId getNodeId() {
        return nodeId;
    }

    public void setNodeId(NodeId nodeId) {
        this.nodeId = nodeId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public ElectionPriority getPriority() {
        return priority;
    }

    public void setPriority(ElectionPriority priority) {
        this.priority = priority;
    }


    @Override
    public String toString() {
        return "PeerId{" +
                "endpoint=" + endpoint +
                ", nodeId=" + nodeId +
                ", groupName='" + groupName + '\'' +
                ", priority=" + priority +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeerId peerId = (PeerId) o;
        return Objects.equals(getEndpoint(), peerId.getEndpoint()) && Objects.equals(getNodeId(), peerId.getNodeId()) && Objects.equals(getGroupName(), peerId.getGroupName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEndpoint(), getNodeId(), getGroupName());
    }


    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }
}
