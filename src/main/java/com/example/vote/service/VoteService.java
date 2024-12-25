package com.example.vote.service;

import com.example.vote.entity.Vote;
import java.util.Map;
import java.util.List;

public interface VoteService {

    // 获取所有投票记录
    List<Vote> getAllVotes();

}
