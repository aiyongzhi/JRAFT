package com.yongzhiai.core.command;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @ClassName Command
 * @Description TODO:具体的指令类
 * @Author 快乐的星球
 * @Date 2024/12/23 16:10
 * @Version 1.0
 **/
public class Command {

    //指令编号
    private final int code;
    //参数列表
    private final Object[] args;

    //参数map
    private final HashMap<String,Object> attributesMap;


    public Command(int code, Object[] args, HashMap<String, Object> attributesMap) {
        this.code = code;
        this.args = args;
        this.attributesMap = attributesMap;
    }

    public Command(int code, Object[] args) {
        this(code,args,null);

    }

    public Command(int code, HashMap<String, Object> attributesMap) {
        this(code,null,attributesMap);
    }



    public void setArgs(int index,Object arg){

        if(this.args==null || index<0 || index>=args.length || arg==null){
            throw new IllegalArgumentException("参数设置错误");
        }

        args[index]=arg;
    }


    public void setAttribute(String key,Object value){
        if(this.attributesMap==null || StrUtil.isBlank(key) || value==null){
            throw new IllegalArgumentException("请求参数异常或者方法不可用!");
        }
        attributesMap.put(key,value);
    }


    public void getAttribute(String key){
        if(this.attributesMap==null || StrUtil.isBlank(key)){
            throw new IllegalArgumentException("请求参数非法或者方法不可用!");
        }
        attributesMap.get(key);
    }

    public int getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public HashMap<String, Object> getAttributesMap() {
        return attributesMap;
    }

}
