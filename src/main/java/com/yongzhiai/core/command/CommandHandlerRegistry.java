package com.yongzhiai.core.command;

/**
 * @ClassName CommandRegistry
 * @Description TODO: 指令注册表
 * @Author 快乐的星球
 * @Date 2024/12/23 15:52
 * @Version 1.0
 **/
public interface CommandHandlerRegistry {

        //注册指令
        public void register(int code,CommandHandler handler);

        //获取指令处理函数
        public CommandHandler getHandler(int code);
}
