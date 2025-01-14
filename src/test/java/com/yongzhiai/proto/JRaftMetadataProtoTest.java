package com.yongzhiai.proto;

/**
 * @ClassName JRaftMetadataProtoTest
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/11 17:37
 * @Version 1.0
 **/

import com.google.protobuf.InvalidProtocolBufferException;
import com.yongzhiai.election.ElectionPriority;
import org.junit.Test;

/**
 * 测试JRaft元数据协议
 */
public class JRaftMetadataProtoTest {


    @Test
    public void createMetadata() throws InvalidProtocolBufferException {

        //1.构造endpoint

        JRaftMetadataProto.Endpoint endpoint
                =JRaftMetadataProto.Endpoint.newBuilder()
                .setHost("127.0.0.1")
                .setPort(8001)
                .build();

        //2. 构造NodeId
        JRaftMetadataProto.NodeId nodeId=
                JRaftMetadataProto.NodeId.newBuilder()
                        .setIdx(1)
                        .setGroupName("raft-group-1")
                        .build();

        //3. 构造PeerId 当前节点的信息类
        JRaftMetadataProto.PeerId peerId=
                JRaftMetadataProto.PeerId.newBuilder()
                        .setEndpoint(endpoint)
                        .setNodeId(nodeId)
                        .setGroupName("raft-group-1")
                        .setElectionPriority(ElectionPriority.DEFAULT.getPriority())
                        .build();

        //4. 构造Metadata

        JRaftMetadataProto.JRaftMetadata jRaftMetadata=
                JRaftMetadataProto.JRaftMetadata.newBuilder()
                        .setTerm(1)
                        .setPeerId(peerId)
                        .build();


        byte[] byteArray = jRaftMetadata.toByteArray();

        System.out.println(new String(byteArray));

        System.out.println("----------------------------");

        System.out.println(jRaftMetadata.toString());

        //测试反序列化

        System.out.println("----------------开始测试反序列化---------------");

        JRaftMetadataProto.JRaftMetadata raftMetadata
                =JRaftMetadataProto.JRaftMetadata.parseFrom(byteArray);

        System.out.println(raftMetadata);

    }
}
