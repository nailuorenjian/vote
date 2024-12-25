package com.example.vote.mapper;

import com.example.vote.entity.Vote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VoteMapper {
    // 查询所有记录
    @Select("SELECT * FROM vote")
    List<Vote> findAll();

}
