package bgu.spl.mics;

import java.util.*;
import java.util.LinkedList;

public class MessageQueue {
    private LinkedList list;

    public MessageQueue() {
        this.list = new LinkedList();
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }

    public void enqueue(Message element) {
        list.add(element);
    }

    public Object dequeue() {
        if(isEmpty())
            throw new NoSuchElementException();
        Object output = list.removeFirst();
        return output;
    }

    public Object peek() {
        if(isEmpty())
            return null;
        return list.get(0);
    }
}
