package com.yongzhiai.core.node.context;

import cn.hutool.core.io.FileUtil;
import com.yongzhiai.core.node.Endpoint;
import com.yongzhiai.core.node.Node;
import com.yongzhiai.core.node.PeerId;
import com.yongzhiai.core.node.impl.NodeImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * @ClassName NodeMangerTests
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/4 16:18
 * @Version 1.0
 **/
public class NodeMangerTests {


    private Node node1;

    private Node node2;

    private PeerId id1;

    private PeerId id2;

    private final static String groupName="test-raft";

    @Before
    public void initNode(){
        this.id1=new PeerId(new Endpoint("localhost",8001),
                groupName);


        this.id2=new PeerId(new Endpoint("localhost",8002),
                groupName);


        this.node1=new NodeImpl(groupName,id1);

        this.node2=new NodeImpl(groupName,id2);
    }

    @Test
    public void testAddNode(){




        NodeManger nodeManger = NodeManger.getInstance();

        nodeManger.addNode(id1,node1);
        nodeManger.addNode(id2,node2);

        System.out.println("----------start-----------");

        Node node = nodeManger.getNode(id1);
        System.out.printf("node1=="+node);

        System.out.println("--------------------------------");
        node=nodeManger.getNode(id2);
        System.out.printf("node2=="+node);
    }


    @Test
    public void testAddNodeToGroup(){

        NodeManger nodeManger = NodeManger.getInstance();

        nodeManger.addNodeToGroup(groupName,node1);

        nodeManger.addNodeToGroup(groupName,node2);

        System.out.println("---------------start-------------");
        Set<Node> nodeSet = nodeManger.getRaftGroupNodeSet(groupName);

        for (Node node : nodeSet) {
            System.out.println(node);
            System.out.println("-----------------------------");
        }


    }


}
