package com.yongzhiai.election;

/**
 * @ClassName ElectionPriority
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/3 21:27
 * @Version 1.0
 **/

import java.util.Objects;

/**
 *
 * 选举优先级
 *
 * 当领导者节点宕机时，默认所有跟随者节点都有资格竞选领导者
 *
 * Disabled：不按优先级选举（默认） priority= -1
 *
 * NotElected: 当前节点不参与选举  priority= 0
 *
 * MinValue: 选举优先级最小值的节点作为主节点，如果在集群中有的服务器性能好，有的服务器性能差，
 * 就可以通过此配置，使这个服务器成为领导者的概率最大
 *
 *
 */
public class ElectionPriority {

    public static final ElectionPriority DISABLED=new ElectionPriority(-1);

    public static final ElectionPriority NOT_ELECTED=new ElectionPriority(0);


    public static final ElectionPriority DEFAULT=DISABLED;

    private int priority;


    public ElectionPriority(int priority) {
        this.priority = priority;
    }


    public ElectionPriority() {
        this.priority=ElectionPriority.DEFAULT.getPriority();
    }



    public boolean isDisabled() {
        return this.equals(DISABLED);
    }

    public boolean isNotElected() {
        return this.equals(NOT_ELECTED);
    }

    public boolean isPriorityElected() {
        return this.priority>0;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectionPriority that = (ElectionPriority) o;
        return getPriority() == that.getPriority();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPriority());
    }


    @Override
    public String toString() {
        return "ElectionPriority{" +
                "priority=" + priority +
                '}';
    }
}
