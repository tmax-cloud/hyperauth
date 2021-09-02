package com.tmax.hyperauth.rest;

public class testMain {
    public static void main(String[] args){



        

    }

}

class LinkedList {
    private Node head;
    private Node tail;
    private int size = 0;

    private class Node{
        private Object data;
        private Node next;
        public Node(Object input) {
            this.data = input;
            this.next = null;
        }
        public String toString() {
            return String.valueOf(this.data);
        }
    }
}
