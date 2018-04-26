public class Dictionary {
    private HashClass theDict[];
    private int size;
    private int used;

    public Dictionary(int maxSize) {
        this.theDict = new HashClass[maxSize];
        this.size = maxSize;
        this.used = 0;
    }

    public int hash(char c) {
        int retval = (int) c;

        //return index location
        return retval % size;
    }

    public boolean isFull() {
        return used >= size-1;
    }

    private int findWhere(char c) {
        int where = this.hash(c);

        while (theDict[where] != null && theDict[where].getKey() != c) {
            //linear probe to next element
            where = (where + 1) % size;
        }
        return where;
    }

    public void delete(char c) throws HashException {
        int where = findWhere(c);
        //if index isn't empty...
        if(theDict[where] != null) {
            //...then set it to empty
            theDict[where] = null;
            used--;
        }
        //if empty
        else {
            throw new HashException("Missing Key: " + c);
        }
    }

    public double lookup(char c) {
        int where = findWhere(c);

        if(theDict[where] != null) {
            return theDict[where].getValue();
        }
        else {
            return -1;
        }
    }

    public void insert(char c, double val) throws HashException {
        if(this.isFull()) {
            throw new HashException("Hash table overflow");
        }
        int where = findWhere(c);

        if (theDict[where] == null) {
            theDict[where] = new HashClass(c, val);
            used++;
        }
        else {
            theDict[where].setValue(val);
        }
    }

    public String toString() {
        String retval = "";
        for(int i = 0; i < size; i++) {
            if (theDict[i] != null) {
                retval += i + "  " + theDict[i] + "\n";
            }
        }
        return retval;
    }
}