package com.example.vote.controller;

import com.example.vote.entity.Vote;
import com.example.vote.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VoteController {

    @Autowired
    private VoteService voteService;

    @GetMapping("/votes")
    public List<Vote> getAllVotes() {
        return voteService.getAllVotes();
    }

}
