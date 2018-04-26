public class HashClass {
    private char key;
    private double value;

    public HashClass(char k, double v) {
        this.key = k;
        this.value = v;
    }

    public String toString() {
        return this.key + ": " + this.value;
    }

    public char getKey() {
        return this.key;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double v) {
        this.value = v;
    }
}