package com.yongzhiai.core.command;

import java.util.concurrent.CompletableFuture;

/**
 * @ClassName CommandHandler
 * @Description TODO: 指令处理函数
 * @Author 快乐的星球
 * @Date 2024/12/23 16:07
 * @Version 1.0
 **/
public interface CommandHandler {
    //同步调用指令处理函数
    public Object invoke(Command command);

    //异步调用指令处理函数
    public CompletableFuture<Object> invokeAsync(Command command);
}
