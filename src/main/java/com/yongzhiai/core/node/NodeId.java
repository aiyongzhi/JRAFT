package com.yongzhiai.core.node;

/**
 * @ClassName NodeId
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/3 21:10
 * @Version 1.0
 **/

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * 一台服务器启动多个Raft节点，且可能是同一台服务器的同一个进程启动多个Raft节点
 * 这些Raft节点共用相同的IP地址和端口号
 */
public class NodeId implements Serializable {

    @Serial
    private static final long serialVersionUID= 7577842984343896144L;

    /**
     * 编号
     * 同一个进程启动的多个Node节点，IP和端口号可能相同
     * 通过这里的idx来区分
     */
    private int idx;

    /**
     * raft集群的组名称
     */
    private String groupName;


    public NodeId(int idx, String groupName) {
        this.idx = idx;
        this.groupName = groupName;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeId nodeId = (NodeId) o;
        return getIdx() == nodeId.getIdx() && Objects.equals(getGroupName(), nodeId.getGroupName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdx(), getGroupName());
    }


    @Override
    public String toString() {
        return "NodeId{" +
                "idx=" + idx +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
