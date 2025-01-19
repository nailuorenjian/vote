package com.example.vote.service;

import com.example.vote.entity.Vote;
import com.example.vote.mapper.VoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于公式的索引法
 * 通过数学公式直接计算每个排列的索引，避免生成所有数据后再筛选。
 */
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
        // 合并所有位置
        List<Integer> allPositions = new ArrayList<>();
        allPositions.addAll(bet1Positions);
        allPositions.addAll(bet2Positions);
        allPositions.addAll(bet3Positions);

        // 调用非递归方法生成所有排列
        return generateAllPermutationsIteratively(allPositions, 3); // 3 为排列长度
    }

    // 使用迭代生成所有排列
    private List<List<Integer>> generateAllPermutationsIteratively(List<Integer> elements, int k) {
        List<List<Integer>> permutations = new ArrayList<>();
        int n = elements.size();

        if (k > n) return permutations;

        // 初始化一个数组存储当前排列的索引
        int[] indices = new int[k];
        for (int i = 0; i < k; i++) {
            indices[i] = i;
        }

        while (true) {
            // 根据当前索引生成一个排列
            List<Integer> currentPermutation = new ArrayList<>();
            for (int index : indices) {
                currentPermutation.add(elements.get(index));
            }
            permutations.add(currentPermutation);

            // 生成下一个排列的索引
            int i = k - 1;
            while (i >= 0 && indices[i] == n - k + i) {
                i--;
            }

            if (i < 0) break; // 所有排列已生成，退出循环

            indices[i]++;
            for (int j = i + 1; j < k; j++) {
                indices[j] = indices[j - 1] + 1;
            }
        }

        return permutations;
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
