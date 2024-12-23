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
    MASTER,SLAVE
}
