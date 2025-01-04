package com.yongzhiai.core.node.lifecycle;

/**
 * @ClassName Lifecycle
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/3 21:47
 * @Version 1.0
 **/

/**
 * 对于框架核心组件来说，其在不同状态执行不同功能
 * 其就像一个状态机，通过执行不同的函数，在不同状态之间进行流转
 */
public interface Lifecycle<T> {

    /**
     * 节点初始化方法
     *
     * 1. 执行初始化相关函数
     * 2. 状态变更，从UNINITIALIZED-->FOLLOWER
     * @param nodeOptions 节点配置参数
     * @return
     */
    public boolean initialize(T nodeOptions);


    /**
     * 节点停机方法
     *
     * 1. 节点状态变更，从LEADER/CANDIDATE/FOLLOWER/ERROR/UNINITIALIZED-->SHUTTING
     * 2. 执行停机前的钩子函数，实现优雅的停机功能
     * 3. 节点状态变更，从SHUTTING-->SHUTDOWN
     */
    public void shutdown();


}
