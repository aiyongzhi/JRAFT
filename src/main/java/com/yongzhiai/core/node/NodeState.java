package com.yongzhiai.core.node;

/**
 * @ClassName NodeState
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2024/12/23 16:39
 * @Version 1.0
 **/

/**
 * 节点状态的枚举
 * 在主从集群架构中，节点有主节点和从节点之分
 *
 */
public enum NodeState {
    //领导者
    LEADER,
    //候选者
    CANDIDATE,
    //跟随者
    FOLLOWER,
    //节点出现错误
    ERROR,
    //未初始化
    UNINITIALIZED,
    //节点正在停止工作
    SHUTTING,
    //节点停止服务
    SHUTDOWN;


    public boolean isActive(){
        return this.ordinal()<NodeState.ERROR.ordinal();
    }

}
