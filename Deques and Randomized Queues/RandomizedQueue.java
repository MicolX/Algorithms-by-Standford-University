import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;
    private int count = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return count;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();
        else {
            if (count == queue.length) resize(queue.length * 2);
            queue[count++] = item;
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        else {
            int pick = StdRandom.uniform(0, count);
            Item item = queue[pick];
            queue[pick] = queue[count - 1];
            queue[count - 1] = null;
            count--;
            if (count > 0 && count == queue.length/4) resize(queue.length/2);
            return item;
        }
    }

    //return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        else {
            int pick = StdRandom.uniform(0, count);
            return queue[pick];
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new randomizedQueueIterator();
    }

    private class randomizedQueueIterator implements Iterator<Item> {

        private Item[] copy;
        private int x = 0;

        public randomizedQueueIterator() {
            copy = (Item[]) new Object[count];
            for (int i = 0; i < count; i++) copy[i] = queue[i];
            for (int a = 0; a < count; a++) {
                int r = StdRandom.uniform(a+1);
                exchange(copy, a, r);
            }
        }

        public boolean hasNext() {
            return x < copy.length;
        }

        public Item next() {
            if (hasNext()) return copy[x++];
            else throw new java.util.NoSuchElementException();
        }

        public void remove() { throw new java.lang.UnsupportedOperationException(); }
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int a = 0; a < queue.length; a++) {
            copy[a] = queue[a];
        }
        queue = copy;
    }

    private void exchange(Item[] array, int i, int j) {
        Item temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}
