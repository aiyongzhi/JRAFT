package com.yongzhiai.metadata.exception;

/**
 * @ClassName MetadataParsingError
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/11 23:22
 * @Version 1.0
 **/

/**
 * 元数据解析错误
 */
public class MetadataParsingError extends RuntimeException{
    public MetadataParsingError(String s) {
        super(s);
    }
}
