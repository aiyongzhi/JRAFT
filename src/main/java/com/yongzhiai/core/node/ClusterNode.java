package com.yongzhiai.core.node;

/**
 * @ClassName ClusterNode
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2024/12/23 16:38
 * @Version 1.0
 **/

import com.yongzhiai.core.command.Command;
import com.yongzhiai.core.command.CommandHandler;
import com.yongzhiai.core.command.CommandHandlerRegistry;
import com.yongzhiai.core.log.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 集群节点
 */
public class ClusterNode {

    //日志工具类
    private static final Logger LOG=LoggerFactory.getLogger(ClusterNode.class);

    /**
     * 当前节点的地址信息
     */
    private Endpoint endpoint;

    /**
     * 主从集群中主节点的地址信息
     */
    private Endpoint master;

    /**
     * 节点状态
     */
    private NodeState state;

    /**
     * 从节点集合
     */
    private final HashSet<Endpoint> slaves;


    /**
     * 当前节点的日志偏移量
     * 可以理解为当前节点的复制进度
     */
    private final AtomicLong offset=new AtomicLong();

    /**
     * 指令注册表
     * 根据指令编码查找对应的指令处理函数
     */
    private CommandHandlerRegistry commandRegistry;


    public ClusterNode(Endpoint endpoint, Endpoint master, NodeState state, HashSet<Endpoint> slaves, CommandHandlerRegistry commandRegistry) {
        this.endpoint = endpoint;
        this.master = master;
        this.state = state;
        this.slaves = slaves;
        this.commandRegistry = commandRegistry;
    }


    public ClusterNode(Endpoint endpoint, Endpoint master, NodeState state, CommandHandlerRegistry commandRegistry) {
        this.endpoint = endpoint;
        this.master = master;
        this.state = state;
        this.commandRegistry = commandRegistry;

        this.slaves=new HashSet<>();
    }

    /**
     * 只有主节点才能执行这个函数
     *
     * 这个函数用于接受客户端的指令并处理
     * @param command
     */
    public Object onApply(Command command){
        if(this.state==NodeState.MASTER){
            //1. 获取指令处理函数
            CommandHandler commandHandler = getCommandRegistry().getHandler(command.getCode());
            //2. 执行指令处理函数
            Object result = commandHandler.invoke(command);
            //3. 增加日志偏移量
            long newOffset = this.offset.incrementAndGet();

            //4. 将指令发送给从节点
            sendCommandToSlaves(command,newOffset);

            return result;
        }

        return null;
    }


    /**
     * 只有主节点才能执行这个函数
     * 这个函数用于将指令发送给从节点
     * @param command 指令
     * @param offset 日志偏移量
     */
    public void sendCommandToSlaves(Command command,long offset){
        if(this.state==NodeState.MASTER){
            //1. 构建日志条目
            LogEntry logEntry = new LogEntry(offset, command);
            for (Endpoint slave : slaves) {
                //2. 发送指令给从节点
                doSendCommandToSlave(slave,logEntry);
            }
        }
    }


    /**
     * TODO: 通过网络将指令发送给从节点,这里暂不做实现，待后续迭代
     * @param slave
     * @param logEntry
     */
    private void doSendCommandToSlave(Endpoint slave,LogEntry logEntry){
        LOG.info("发送指令给从节点:{},指令编码:{},复制偏移量:{}",slave,logEntry.getCommand().getCode(),
                logEntry.getOffset());

    }


    /**
     * 从节点接受主节点发送的指令
     * @param logEntry 日志条目
     * @return
     */
    private Object acceptCommand(LogEntry logEntry){

        if(this.state!=NodeState.SLAVE){
            throw new IllegalArgumentException("只有从节点才能接受指令");
        }

        long localOffset = this.getOffset().get();

        if(logEntry.getOffset()<=localOffset){
            LOG.error("已过期的日志条目,编号:{},指令编码:{}",logEntry.getOffset(),
                    logEntry.getCommand().getCode());

            return null;
        }

        //1. 获取指令处理函数
        CommandHandler commandHandler = getCommandRegistry().getHandler(logEntry.getCommand().getCode());
        //2. 执行指令处理函数
        Object result = commandHandler.invoke(logEntry.getCommand());
        //3. 增加日志偏移量
        this.offset.set(logEntry.getOffset());
        return result;
    }







    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Endpoint getMaster() {
        return master;
    }

    public void setMaster(Endpoint master) {
        this.master = master;
    }

    public NodeState getState() {
        return state;
    }

    public void setState(NodeState state) {
        this.state = state;
    }

    public HashSet<Endpoint> getSlaves() {
        return slaves;
    }

    public AtomicLong getOffset() {
        return offset;
    }


    public void increaseOffset(){
        this.offset.incrementAndGet();
    }


    public CommandHandlerRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public void setCommandRegistry(CommandHandlerRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }


    public void addSalve(Endpoint endpoint){
        this.slaves.add(endpoint);
    }
}
