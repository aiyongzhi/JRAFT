package com.yongzhiai.metadata;

/**
 * @ClassName RaftMetadataInfo
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/11 18:52
 * @Version 1.0
 **/

import cn.hutool.core.io.FileUtil;
import com.yongzhiai.core.node.Endpoint;
import com.yongzhiai.core.node.NodeId;
import com.yongzhiai.core.node.PeerId;
import com.yongzhiai.election.VoteInfo;
import com.yongzhiai.metadata.exception.MetadataParsingError;
import com.yongzhiai.proto.JRaftMetadataProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Raft的元数据信息类
 */
public class RaftMetadataInfo {


    private static final Logger LOG= LoggerFactory.getLogger( RaftMetadataInfo.class );


    /**
     * 选举的元数据信息
     * 记录当前节点在哪个任期给哪个节点投过票
     */
    private VoteInfo voteInfo;


    public RaftMetadataInfo(VoteInfo voteInfo) {
        this.voteInfo = voteInfo;
    }


    public RaftMetadataInfo() {
    }


    /**
     * 解析protobuf序列化的协议
     * 将数据保存到RaftMetadataInfo
     * @param raftMetadata
     * @return
     */
    public static RaftMetadataInfo parseFrom(JRaftMetadataProto.JRaftMetadata raftMetadata){

        if(raftMetadata==null){
            throw new IllegalArgumentException("参数不能为空!");
        }

        RaftMetadataInfo metadataInfo=new RaftMetadataInfo();
        //任期
        long term = raftMetadata.getTerm();
        //节点的信息
        JRaftMetadataProto.PeerId peerId = raftMetadata.getPeerId();

        if(peerId!= JRaftMetadataProto.PeerId.getDefaultInstance()){
            Endpoint endpoint=null;
            NodeId nodeId=null;

            if(peerId.getEndpoint()== JRaftMetadataProto.Endpoint.getDefaultInstance()){
                throw new MetadataParsingError("peerId.getEndpoint() 不能为空");
            }
            endpoint=new Endpoint(peerId.getEndpoint().getHost(), peerId.getEndpoint().getPort());

            if(peerId.getNodeId()!= JRaftMetadataProto.NodeId.getDefaultInstance()){
                nodeId=new NodeId(peerId.getNodeId().getIdx(), peerId.getNodeId().getGroupName());
            }

            PeerId thisPeerId=new PeerId(endpoint,nodeId,peerId.getGroupName(),false);

            VoteInfo voteInfo=new VoteInfo(term,thisPeerId);

            metadataInfo.setVoteInfo(voteInfo);

        }else{
            metadataInfo.setVoteInfo(new VoteInfo(term,null));

        }

        return metadataInfo;
    }

    /**
     * 将当前对象写入到磁盘文件当中
     * @param localMetaFile
     * @return
     */
    public boolean writeToFile(File localMetaFile) {
        //判空
        if(getVoteInfo()==null || getVoteInfo().equals(new VoteInfo())){
            LOG.info("there is no ting to write to file!");
            return true;
        }
        //构建器
        JRaftMetadataProto.JRaftMetadata.Builder builder = JRaftMetadataProto.JRaftMetadata.newBuilder();
        //设置选举的任期
        builder.setTerm(getVoteInfo().getCurrTerm().get());

        //将当前对象转换为protobuf的对象，写入磁盘
        if(getVoteInfo().getVoteId()!=null){
            PeerId voteId = getVoteInfo().getVoteId();

            JRaftMetadataProto.PeerId.Builder peerBuilder = JRaftMetadataProto.PeerId.newBuilder();
            //设置Endpoint
            JRaftMetadataProto.Endpoint endpoint = JRaftMetadataProto.Endpoint.newBuilder().
                    setHost(voteId.getEndpoint().getHost()).
                    setPort(voteId.getEndpoint().getPort()).build();

            peerBuilder.setEndpoint(endpoint);

            //设置nodeId
            if(voteId.getNodeId()!=null){
                JRaftMetadataProto.NodeId nodeId = JRaftMetadataProto.NodeId.newBuilder()
                        .setIdx(voteId.getNodeId().getIdx())
                        .setGroupName(voteId.getNodeId().getGroupName()).build();

                peerBuilder.setNodeId(nodeId);
            }

            //设置集群名称group name
            peerBuilder.setGroupName(voteId.getGroupName());

            //设置优先级
            peerBuilder.setElectionPriority(voteId.getPriority().getPriority());


            builder.setPeerId(peerBuilder.build());
        }


        JRaftMetadataProto.JRaftMetadata raftMetadata = builder.build();

        //TODO: 使用IO流的时候一定要记得关闭资源
        try (BufferedOutputStream outputStream = FileUtil.getOutputStream(localMetaFile)){
            raftMetadata.writeTo(outputStream);
        } catch (IOException ex) {
            LOG.error("将RaftMetadataInfo按protobuf的格式序列化为字节流写入文件当中，出现异常 :{}",ex.getMessage());
            return false;
        }

        return true;
    }



    public VoteInfo getVoteInfo() {
        return voteInfo;
    }

    public void setVoteInfo(VoteInfo voteInfo) {
        this.voteInfo = voteInfo;
    }
}
