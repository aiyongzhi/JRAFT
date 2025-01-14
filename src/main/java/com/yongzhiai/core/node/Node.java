package com.yongzhiai.core.node;

/**
 * @ClassName Node
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/3 21:04
 * @Version 1.0
 **/

import com.yongzhiai.core.config.ConfigurationSnapshot;
import com.yongzhiai.core.node.lifecycle.Lifecycle;
import com.yongzhiai.election.VoteInfo;
import com.yongzhiai.factory.JRaftServiceFactory;
import com.yongzhiai.metadata.RaftMetaStorage;

/**
 * Node接口在Raft算法中是节点的抽象
 */
public interface Node extends Lifecycle<NodeOptions> {

    /**
     * 获取节点的状态
     * @return
     */
    public NodeState getState();

    /**
     * Raft集群的组名称
     * @return
     */
    public String getGroupName();

    /**
     * 获取节点的信息
     * @return
     */
    public PeerId getPeerId();

    /**
     * 获取当前节点的地址信息
     * @return
     */
    public Endpoint getEndpoint();

    /**
     * 获取当前节点本地持久化目录
     * @return
     */
    public String getLogPath();

    /**
     * 获取当前节点元数据本地持久化目录
     * @return
     */
    public String getRaftMetaPath();

    /**
     * 获取当前节点的超时选举时间的毫秒数
     * @return
     */
    public int getElectionTimeoutMills();

    /**
     * 获取当前集群的配置信息
     * @return
     */
    public ConfigurationSnapshot getConf();



    public VoteInfo getVoteInfo();

    /**
     * 获取jraft服务工厂
     * @return
     */
    public JRaftServiceFactory getServiceFactory();

    /**
     * 获取节点元数据存储器
     * @return
     */
    public RaftMetaStorage getRaftMetaStorage();
}
