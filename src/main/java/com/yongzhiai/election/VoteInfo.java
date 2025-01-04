package com.yongzhiai.election;

/**
 * @ClassName VoteInfo
 * @Description TODO
 * @Author 快乐的星球
 * @Date 2025/1/4 11:03
 * @Version 1.0
 **/

import com.yongzhiai.core.node.PeerId;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 当前节点的投票信息
 *
 * 在leader宕机后，从节点会自发的进入超时选举流程
 * 候选者会向其他所有节点拉票，从节点按情况给主节点投票
 *
 * 当前节点记录的是投票信息
 * 存储当前节点在哪一个任期中，给哪一个节点投票了
 */
public class VoteInfo {


    /**
     * 当前任期
     */
    private final AtomicLong currTerm=new AtomicLong();

    /**
     * 当前任期给谁投票
     */
    private PeerId voteId;


    public VoteInfo(long currTerm, PeerId voteId) {
        this.currTerm.set(currTerm);
        this.voteId = voteId;
    }

    public VoteInfo() {
        this(0,null);
    }


    public AtomicLong getCurrTerm() {
        return currTerm;
    }

    public PeerId getVoteId() {
        return voteId;
    }

    public void setVoteId(PeerId voteId) {
        this.voteId = voteId;
    }


    @Override
    public String toString() {
        return "VoteInfo{" +
                "currTerm=" + currTerm +
                ", voteId=" + voteId +
                '}';
    }
}
