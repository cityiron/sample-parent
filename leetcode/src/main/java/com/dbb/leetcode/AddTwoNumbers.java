package com.dbb.leetcode;

/**
 * @author tc
 * @date 2019-10-10
 */
public class AddTwoNumbers {

    public static void main(String[] args) {
        ListNode listNode1 = new ListNode(2);
        listNode1.next = new ListNode(4);
        listNode1.next.next = new ListNode(3);
        listNode1.next.next.next = new ListNode(9);

        ListNode listNode2 = new ListNode(5);
        listNode2.next = new ListNode(6);
        listNode2.next.next = new ListNode(4);
        listNode2.next.next.next = new ListNode(9);

        AddTwoNumbers addTwoNumbers = new AddTwoNumbers();
        ListNode listNode = addTwoNumbers.addTwoNumbers(listNode1, listNode2);

        while (listNode != null) {
            System.out.println(listNode.val);
            listNode = listNode.next;
        }

        addTwoNumbers.addTwoNumbers2(listNode1, listNode2);
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return addTwoNumbers(l1, l2, 0);
    }

    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        int v1 = 0;
        int v2 = 0;
        ListNode listNode = l1;
        int i = 0;
        while (listNode != null) {
            v1 += listNode.val * Math.pow(10, i);
            listNode = listNode.next;
            i++;
        }
        listNode = l2;
        i = 0;
        while (listNode != null) {
            v2 += listNode.val * Math.pow(10, i);
            listNode = listNode.next;
            i++;
        }

        int result = v1 + v2;

        return parseInt(i);
    }

    private ListNode parseInt(int i) {
        return null;
    }

    private ListNode addTwoNumbers(ListNode l1, ListNode l2, int next) {
        // 数量一致,并且肯定有一个
        int v = next;
        ListNode nextNode1 = null;
        ListNode nextNode2 = null;
        if (l1 != null) {
            v += l1.val;
            nextNode1 = l1.next;
        }
        if (l2 != null) {
            v += l2.val;
            nextNode2 = l2.next;
        }

        int setV = v;
        if (v >= 10) {
            setV = v - 10;
            next = 1;
        } else {
            next = 0;
        }

        ListNode listNode = new ListNode(setV);
        if (next != 0 || nextNode1 != null || nextNode2 != null) {
            listNode.next = addTwoNumbers(nextNode1, nextNode2, next);
        }
        return listNode;
    }

}

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) { val = x; }
}
