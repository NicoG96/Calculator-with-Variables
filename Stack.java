public class Stack<T> {
    private LL<T> stacc;

    public Stack() {
        stacc = new LL<>();
    }

    public boolean isEmpty() {
        return stacc.isEmpty();
    }

    public void push(T value) {
        stacc.insertAtHead(value);
    }

    public T pop() throws StackException {
        T retval = null;
        try {
            retval = stacc.deleteFromHead();
        }
        catch (LLException e) {
            throw new StackException("Stack underflow.");
        }
        return retval;
    }

    public void peep() {
        stacc.printList();
    }

    public T getVal() {
        return stacc.getVal();
    }
}