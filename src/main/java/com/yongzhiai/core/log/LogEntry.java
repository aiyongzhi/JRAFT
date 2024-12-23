package com.yongzhiai.core.log;

import com.yongzhiai.core.command.Command;

/**
 * @ClassName LogEntry
 * @Description TODO: 带索引的日志条目
 * @Author 快乐的星球
 * @Date 2024/12/23 15:38
 * @Version 1.0
 **/
public class LogEntry {

    /**
     * 日志索引编号为自增的索引
     * 为什么要自增呢？
     * 1. 可以保证幂等性，避免重复执行相同的指令
     * 2. 可以根据offset的递增关系顺序性的执行指令。
     */
    private long offset;

    /**
     *  日志指令
     */
    private Command command;


    public LogEntry() {
    }

    public LogEntry(long offset, Command command) {
        this.offset = offset;
        this.command = command;
    }


    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }


    @Override
    public String toString() {
        return "LogEntry{" +
                "offset=" + offset +
                ", command=" + command +
                '}';
    }
}
