package com;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public class LeetCodeN2 {

    private static ListNode listNodeResult;

    public static void main(String[] args) {
        ListNode head = new ListNode();

        head = ListNode.builder()
                .val(1)
                .next(ListNode.builder()
                        .val(3)
                        .next(ListNode.builder()
                                .val(2)
                                .next(ListNode.builder()
                                        .val(9)
                                        .next(ListNode.builder()
                                                .val(4)
                                                .next(null)
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();

        System.out.println(head.val + ", " + head.next.val + ", " + head.next.next.val + ", " + head.next.next.next.val + ", " + head.next.next.next.next.val);

        ListNode result = reverseList(head);
        System.out.println(result.val + ", " + result.next.val + ", " + result.next.next.val + ", " + result.next.next.next.val+ ", " + result.next.next.next.next.val);
    }

    public static ListNode reverseList(ListNode head) {
        getNext(head, null);
        return listNodeResult;
    }

    private static void getNext(ListNode listNode1, ListNode listNode2) {
        ListNode list;

        if (listNode1 == null) {
            return;
        }

        if (listNode2 == null) {
            list = new ListNode(listNode1.val);
        } else {
            list = new ListNode(listNode1.val, listNode2);
        }

        if (listNode1.next != null) {
            getNext(listNode1.next, list);
        } else {
            listNodeResult = list;
        }
    }

    @Builder
    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
}

