package com.yongzhiai.core.node.impl;

import cn.hutool.core.util.StrUtil;
import com.yongzhiai.core.config.ConfigurationSnapshot;
import com.yongzhiai.core.node.*;
import com.yongzhiai.election.VoteInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName NodeImpl
 * @Description TODO: 把各个get/set方法调对，实现init方法和shutdown方法
 * @Author 快乐的星球
 * @Date 2025/1/3 21:46
 * @Version 1.0
 **/

/**
 * Raft节点的默认实现类
 */
public class NodeImpl implements Node, Serializable {

    @Serial
    private static final long serialVersionUID = 6105219322040205051L;

    private static final Logger LOG= LoggerFactory.getLogger(NodeImpl.class);

    /**
     * 节点的状态
     */
    private volatile NodeState state=NodeState.UNINITIALIZED;

    /**
     * 节点所属的Raft集群的名称
     */
    private final String groupName;

    /**
     * 当前节点所属Raft集群中，
     * leader节点的信息
     */
    private PeerId leaderId=new PeerId();


    /**
     * 当前节点自己的信息
     */
    private final PeerId serverId;

    /**
     * 当前集群的配置快照
     */
    private ConfigurationSnapshot conf;

    /**
     * 日志本地持久化目录
     */
    private String logPath;

    /**
     * 元数据本地持久化目录
     */
    private String raftMetaPath;

    /**
     * 超时选举时间毫秒数
     */
    private int electionTimeoutMills;


    private final ReentrantLock lock=new ReentrantLock();

    /**
     * 当前节点的投票信息
     */

    private VoteInfo voteInfo;




    public NodeImpl(final String groupName,final PeerId serverId) {

        if(StrUtil.isBlank(groupName) || serverId==null ||
        serverId.getEndpoint()==null ||
        StrUtil.isBlank(serverId.getGroupName())){

            throw new IllegalArgumentException("参数不能为空");
        }

        this.groupName = groupName;
        this.serverId = serverId;
    }


    @Override
    public boolean initialize(NodeOptions nodeOptions) {
        lock.lock();
        try {
            if(this.getState()==NodeState.UNINITIALIZED){
                //设置本地日志持久化目录
                setLogPath(nodeOptions.getLogPath());
                //设置本地元数据持久化目录
                setRaftMetaPath(nodeOptions.getRaftMetaPath());
                //设置超时选举时间毫秒数
                setElectionTimeoutMills(nodeOptions.getElectionTimeoutMills());
                //设置当前节点的集群配置
                ConfigurationSnapshot configurationSnapshot=new ConfigurationSnapshot(nodeOptions.getInitialConf());
                setConf(configurationSnapshot);

                //TODO: 这里后续需要改成从磁盘元数据持久化目录读取当前节点投票元数据信息
                setVoteInfo(new VoteInfo());

                //变更节点的状态为跟随着
                setState(NodeState.FOLLOWER);
                LOG.info("The current node was initialized successfully");
                return true;
            }else{
                LOG.error("The node has already been initialized; do not call the initializer repeatedly;" +
                        "current state is {}",this.getState().name());
                return false;
            }
        }finally {
            lock.unlock();
        }

    }

    @Override
    public void shutdown() {
        lock.lock();
        try {
            if(this.getState().ordinal()<NodeState.SHUTTING.ordinal()){
                //变更当前节点的状态为
                setState(NodeState.SHUTTING);

                //TODO: 调用停机前的所有回调函数 / 释放资源


                LOG.info("The current node was shut down successfully");
                setState(NodeState.SHUTDOWN);

            }else{
                LOG.error("The node is already shutting down; do not call the shutdown repeatedly;" +
                        "current state is {}",this.getState().name());
            }
        }finally {
            lock.unlock();
        }

    }



    @Override
    public NodeState getState() {
        return this.state;
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public PeerId getPeerId() {
        return this.serverId;
    }

    @Override
    public Endpoint getEndpoint() {
        return this.serverId.getEndpoint();
    }




    public void setState(NodeState state) {
        this.state = state;
    }

    public PeerId getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(PeerId leaderId) {
        this.leaderId = leaderId;
    }

    public PeerId getServerId() {
        return serverId;
    }

    public ConfigurationSnapshot getConf() {
        return conf;
    }



    public void setConf(ConfigurationSnapshot conf) {
        this.conf = conf;
    }



    @Override
    public String getLogPath() {
        return this.logPath;
    }

    @Override
    public String getRaftMetaPath() {
        return this.raftMetaPath;
    }

    @Override
    public int getElectionTimeoutMills() {
        return this.electionTimeoutMills;
    }


    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public void setRaftMetaPath(String raftMetaPath) {
        this.raftMetaPath = raftMetaPath;
    }

    public void setElectionTimeoutMills(int electionTimeoutMills) {
        this.electionTimeoutMills = electionTimeoutMills;
    }

    @Override
    public VoteInfo getVoteInfo() {
        return this.voteInfo;
    }

    public void setVoteInfo(VoteInfo voteInfo) {
        this.voteInfo = voteInfo;
    }


    @Override
    public String toString() {
        return "NodeImpl{" +
                "state=" + state +
                ", groupName='" + groupName + '\'' +
                ", leaderId=" + leaderId +
                ", serverId=" + serverId +
                ", conf=" + conf +
                ", logPath='" + logPath + '\'' +
                ", raftMetaPath='" + raftMetaPath + '\'' +
                ", electionTimeoutMills=" + electionTimeoutMills +
                ", voteInfo=" + voteInfo +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeImpl node = (NodeImpl) o;
        return Objects.equals(getServerId(), node.getServerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerId());
    }
}
