package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        int count = 0;
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        for (int i = 1; i <= n; i++) {
            if (valid(i)) {
                count++;
            }
        }
        System.out.println(count);
    }

    public static boolean valid(int num) {
        List<Integer> integers = new ArrayList<>();
        int a = 1;
        while (a < num) {
            if (num % a == 0) {
                integers.add(a);
            }
            a++;
        }
        int sum = integers.stream().mapToInt(Integer::intValue).sum();
        return sum == num;
    }
}
