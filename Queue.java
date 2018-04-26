public class Queue<T> {
    private LL<T> theQueue;

    public Queue() {
        theQueue = new LL<>();
    }

    public boolean isEmpty() {
        return theQueue.isEmpty();
    }

    public void enqueue(T value) {
        theQueue.insertAtTail(value);
    }

    public T dequeue() throws QueueException {
        T retval = null;
        try {
            retval = theQueue.deleteFromHead();
        }
        catch (LLException e) {
            throw new QueueException("Queue underflow.");
        }
        return retval;
    }

    public void peep() {
        theQueue.printList();
    }

    public T getVal() {
        return theQueue.getVal();
    }
}