import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int count = 0;

    private class Node {
        Item item;
        Node next;
    }

    // construct an empty deque
    public Deque() {
        first = new Node();
        last = first;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        } else {
            if (isEmpty()) {
                first.item = item;
                last = first;
            } else {
                Node oldFirst = first;
                first = new Node();
                first.item = item;
                first.next = oldFirst;
                if (count == 1) last = oldFirst;
            }
            count++;
        }

    }

    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        } else {
            if (isEmpty()) {
                last.item = item;
                first = last;
            } else {
                Node oldLast = last;
                last = new Node();
                last.item = item;
                last.next = null;
                oldLast.next = last;
                if (count == 1) first = oldLast;
            }
            count++;
        }
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        } else {
            Item item = first.item;
            first = first.next;
            count--;
            if (count == 1) last = first;
            return item;
        }
    }

    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        } else {
            Item item = last.item;
            if (count == 1) {
                first = null;
                last = null;
            } else if (count == 2) {
                last = null;
                first.next = null;
            } else if (count >= 3) {
                Node start = first;
                while (true) {
                    if (start.next.next == null) {
                        start.next = null;
                        last = start;
                        break;
                    } else start = start.next;
                }
            }

            count--;
            return item;
        }
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new dequeIterator();
    }

    private class dequeIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() { return current != null; }

        public Item next() {
            if (current == null) {
                throw new java.util.NoSuchElementException();
            } else {
                Item item = current.item;
                current = current.next;
                return item;
            }

        }

        public void remove() { throw new java.lang.UnsupportedOperationException(); }
    }

}
