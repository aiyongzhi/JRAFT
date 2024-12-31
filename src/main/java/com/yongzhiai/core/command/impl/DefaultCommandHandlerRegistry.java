package com.yongzhiai.core.command.impl;

/**
 * @ClassName DefaultCommandRegistry
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2024/12/31 16:44
 * @Version 1.0
 **/


import com.yongzhiai.core.command.CommandHandler;
import com.yongzhiai.core.command.CommandHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * 默认指令处理器注册表
 */
public class DefaultCommandHandlerRegistry implements CommandHandlerRegistry {

    private static final Logger LOG= LoggerFactory.getLogger(DefaultCommandHandlerRegistry.class);

    private final HashMap<Integer,CommandHandler> commandHandlerMap=new HashMap<>(64);

    @Override
    public void register(int code, CommandHandler handler) {
        LOG.info("向指令注册表注册指令,指令编码为:{}",code);
        this.commandHandlerMap.put(code,handler);
    }

    @Override
    public CommandHandler getHandler(int code) {
        return this.commandHandlerMap.get(code);
    }
}

