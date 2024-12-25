package com.example.vote.service;

import com.example.vote.entity.Vote;
import com.example.vote.mapper.VoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteMapper voteMapper;

    // 获取所有投票记录并打印排列组合
    @Override
    public List<Vote> getAllVotes() {
        List<Vote> list = voteMapper.findAll();

        // 按betType升序排序
        list.sort(Comparator.comparingInt(Vote::getBetType));

        // 获取排列组合结果并打印
        Map<String, Long> allCombinations = getAllCombinations(list);
        printCombinations(allCombinations);
        return list;
    }

    // 获取每条投票记录的所有排列组合，并统计每个组合的出现次数
    public Map<String, Long> getAllCombinations(List<Vote> votes) {
        // 使用stream来计算每个排列组合并按betType排序
        return votes.stream()
                .flatMap(vote -> {
                    // 提取bet1, bet2, bet3中的"1"所在的位置
                    List<Integer> bet1Positions = extractPositions(vote.getBet1());
                    List<Integer> bet2Positions = extractPositions(vote.getBet2());
                    List<Integer> bet3Positions = extractPositions(vote.getBet3());

                    // 计算bet1, bet2, bet3的所有排列组合
                    List<List<Integer>> allCombinationsForVote = getCombinations(bet1Positions, bet2Positions, bet3Positions);

                    // 将排列组合转换为Stream
                    return allCombinationsForVote.stream()
                            .map(combination -> "betType " + vote.getBetType() + ": " + combination.toString().replaceAll("[\\[\\],]", ""));
                })
                // 统计每个组合出现的次数
                .collect(Collectors.groupingBy(combination -> combination, Collectors.counting()));
    }

    // 提取bet中"1"出现的位置，返回位置列表
    private List<Integer> extractPositions(String bet) {
        List<Integer> positions = new ArrayList<>();

        // 遍历bet字符串，找到"1"的所有位置
        for (int i = 0; i < bet.length(); i++) {
            if (bet.charAt(i) == '1') {
                // 添加位置，位置从1开始，所以加1
                positions.add(i + 1);
            }
        }

        return positions;
    }

    // 计算bet1, bet2, bet3的所有排列组合
    private List<List<Integer>> getCombinations(List<Integer> bet1Positions, List<Integer> bet2Positions, List<Integer> bet3Positions) {
        List<List<Integer>> combinations = new ArrayList<>();

        // 使用三个循环生成所有可能的组合（笛卡尔积）
        for (Integer b1 : bet1Positions) {
            for (Integer b2 : bet2Positions) {
                for (Integer b3 : bet3Positions) {
                    // 生成一个组合
                    List<Integer> combination = new ArrayList<>();
                    combination.add(b1);
                    combination.add(b2);
                    combination.add(b3);
                    combinations.add(combination);
                }
            }
        }

        return combinations;
    }

    // 打印排列组合及其出现次数
    public void printCombinations(Map<String, Long> combinationCount) {
        System.out.println("排列组合结果：");

        // 按照组合的betType排序，首先分离出组合的betType部分进行排序
        combinationCount.entrySet().stream()
                .sorted(Comparator.comparing(entry -> {
                    // 提取betType的数字部分并进行排序
                    String key = entry.getKey();
                    return Integer.parseInt(key.split(":")[0].replaceAll("\\D+", ""));
                }))
                .forEach(entry -> {
                    System.out.println(entry.getKey() + " 出现次数: " + entry.getValue());
                });
    }
}
