public class LL<P> {
    private ListElement<P> head;
    private ListElement<P> tail;

    public LL() {
        head = null;
        tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void insertAtHead(P value) {
        if (head == null) {
            head = new ListElement<>(value, null);
            tail = head;
        } else {
            head = new ListElement<>(value, head);
        }
    }

    public void insertAtTail(P value) {
        if (tail == null) {
            head = new ListElement<>(value, null);
            tail = head;
        } else {
            tail.setLink(new ListElement<>(value, null));
            tail = tail.getLink();
        }
    }

    public P deleteFromHead() throws LLException {
        P retval = null;
        if (this.isEmpty()) {
            throw new LLException("Attempt to remove from empty list");
        } else {
            retval = head.getValue();
            head = head.getLink();
            if (head == null)
                tail = head;
        }
        return retval;
    }

    public void printList() {
        ListElement<P> where = head;
        while (where != null) {
            System.out.print(where.getValue() + " ");
            where = where.getLink();
        }
    }

    public P getVal() {
        return head.getValue();
    }
}

class ListElement <T> {
    private T value;
    private ListElement link;

    public ListElement(T value, ListElement link) {
        this.value = value;
        this.link = link;
    }

    public T getValue() {
        return this.value;
    }

    public ListElement getLink() {
        return this.link;
    }

    public void setLink(ListElement link) {
        this.link = link;
    }
}