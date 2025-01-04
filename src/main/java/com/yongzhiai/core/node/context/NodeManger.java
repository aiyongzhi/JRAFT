package com.yongzhiai.core.node.context;

/**
 * @ClassName NodeManger
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/4 15:37
 * @Version 1.0
 **/

import cn.hutool.core.util.StrUtil;
import com.yongzhiai.core.node.Node;
import com.yongzhiai.core.node.PeerId;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 一台服务器上完全可以部署多个raft节点，这多个RAFT节点甚至可能是同一个进程启动的
 *
 * 所以需要在JVM进程上下文中创建一个全局单例的NodeManger来管理所有的节点
 *
 */
public class NodeManger {

    private static final NodeManger INSTANCE=new NodeManger();

    /**
     * 节点的信息和节点的映射Map
     *
     * key是节点唯一ID
     *
     * value是节点的实例
     */
    private static final ConcurrentHashMap<PeerId, Node> nodeMap
            =new ConcurrentHashMap<>();


    /**
     * raft集群下所有节点
     *
     * key：集群的名称
     * value：集群下的所有节点的集合
     */
    private static final ConcurrentHashMap<String, Set<Node>> groupMap=
            new ConcurrentHashMap<>();


    private NodeManger() {}

    public static NodeManger getInstance() {
        return INSTANCE;
    }


    public void addNode(PeerId peerId,Node node){
        if(peerId==null || peerId.getEndpoint()==null ||
                StrUtil.isBlank(peerId.getGroupName()) || node==null){
            throw new IllegalArgumentException("参数不能为空");
        }

        nodeMap.put(peerId,node);
    }


    public Node getNode(PeerId peerId){
        return nodeMap.get(peerId);
    }


    public void removeNode(PeerId peerId){
        nodeMap.remove(peerId);
    }



    public void addNodeToGroup(String groupName,Node node){

        groupMap.compute(groupName,(key,nodeSet)->{
            if(nodeSet==null){
                nodeSet= Collections.synchronizedSet(new HashSet<>());
            }

            nodeSet.add(node);

            return nodeSet;
        });
    }


    public void removeNodeFromGroup(String groupName,Node node){
        groupMap.computeIfPresent(groupName,(key,nodeSet)->{
            nodeSet.remove(node);
            return nodeSet;
        });
    }


    public Set<Node> getRaftGroupNodeSet(String groupName){
        return groupMap.get(groupName);
    }


    public Set<Node> getAllNodeSet(){
        return groupMap.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }
}
