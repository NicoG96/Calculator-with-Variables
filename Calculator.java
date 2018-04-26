import java.util.Scanner;
import java.util.StringTokenizer;

public class Calculator {
    private enum Keywords  {LET, PRINT, QUIT, EXIT, UNDEFINED}
    private static Dictionary dict;
    private Scanner console;
    private String prompt;

    private Calculator() {
        this.prompt = ">";
        this.console = new Scanner(System.in);
        dict = new Dictionary(50);
    }

    public String getPrompt() {
        return prompt;
    }

    private void process() throws QueueException, HashException, StackException {
        boolean done = false;

        //pointer to hold input
        String line;

        while (!done) {
            System.out.print(this.prompt);
            line = console.nextLine().toUpperCase();

            /* take the input and tokenize it into a max of 4 parts
            1. LET, 2. [VARIABLE], 3. =, 4. [EXPRESSION] */

            String splitString[] = line.split(" ", 4);

            /* run the first word through the doCommand function, which outputs False if word == QUIT/EXIT,
            thus exiting this function */

            done = doCommand(splitString);
        }
    }

    private boolean doCommand(String[] splitString) throws QueueException, StackException, HashException {
        boolean retval = false;
        Keywords command;

        try {
            command = Keywords.valueOf(splitString[0]);
        }
        //if input isn't a defined command, set it == to UNDEFINED
        catch (IllegalArgumentException e) {
            command = Keywords.UNDEFINED;
        }

        switch (command) {
            case LET:
                //assuming input is valid
                try {
                    String expression = splitString[3];
                    char var = splitString[1].charAt(0);
                    String equals = splitString[2];

                    //safety check to make sure there is an equals sign in the expression
                    if (!equals.equals("=")) {
                        System.out.println("Error in expression, please try again.");
                    }

                    //if everything looks good so far then...
                    else {
                        //pass expression through calculator & add and/or update the variable with the evaluated
                        // expression
                        try {
                            dict.insert(var, tokenizer(expression));
                        } catch (StackException e) {
                            System.out.println("Variable does not exist. Try again.");
                        }
                    }
                    break;
                }

                //throw error if invalid
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Invalid expression. Please try again (be mindful of spacing)");
                    break;
                }

            case PRINT:
                char print = splitString[1].charAt(0);

                //check if variable is already in the dictionary
                if (dict.lookup(print) != -1) {
                    //if so, print the value
                    System.out.println(dict.lookup(print));
                }

                //if it's not, then return an error
                else {
                    System.out.println("Error: Variable \"" + print + "\" not defined");
                }
                break;

            case QUIT:
            case EXIT:
                retval = true;
                break;

            case UNDEFINED:
                System.out.println("Undefined command: " + splitString[0]);
        }
        return retval;
    }

    private static double tokenizer(String string) throws QueueException, StackException {
        //parse string with parentheses and operands
        StringTokenizer tokenizer = new StringTokenizer(string, "+-/*()^", true);

        //initialize variable to hold first token
        String token = tokenizer.nextToken();

        //create array based on amount of tokens in string
        String tokens[] = new String[tokenizer.countTokens() + 1];

        //initialize array counter
        int i = 0;

        //while tokenizer can still tokenize string
        while(tokenizer.hasMoreTokens()) {
            //add that token to array
            tokens[i] = token;

            //get next token
            token = tokenizer.nextToken();
            //increment array index
            i++;
        }
        //add last token to array, since last token does not pass while test
        tokens[i] = token;

        return postfixer(tokens);
    }

    private static boolean isOperator(String tok) {
        String operators[] = {"+", "-", "/", "*", "^"};

        //for each element in operator array
        for(String k : operators) {
            //if input == one of these vals, return true
            if (tok.contains(k)) {
                return true;
            }
        }
        return false;
    }

    private static int precedence(String tok) {
        switch (tok) {
            case "^":
                return 3;
            case "*":
                return 2;
            case "/":
                return 2;
            case "+":
                return 1;
            case "-":
                return 1;
            default:
                return -1;
        }
    }

    private static double postfixer(String[] toks) throws QueueException, StackException {
        Queue<String> queue = new Queue<>();
        Stack<String> opstack = new Stack<>();

        //iterate through array of tokens
        for (int i = 0; i < toks.length; i++) {
            //remove all whitespace from the tokens first
            toks[i] = toks[i].replaceAll(" ", "");

            //if the token is an operator
            if (isOperator(toks[i])) {
                //then while the stack has operators higher in precedence than the one being evaluated && isn't empty
                while (!opstack.isEmpty() && precedence(toks[i]) <= precedence(opstack.getVal())) {
                    if (!opstack.getVal().equals("(")) {
                        //pop the stack onto queue
                        try {
                            queue.enqueue(opstack.pop());
                        } catch (StackException e) {
                            System.out.println("Queueing problem");
                        }
                    }
                }
                //but if top of stack is lower precedence or empty, then push the token
                opstack.push(toks[i]);

            //if the token ISN'T an operator but IS a parentheses
            } else if (toks[i].equals("(")) {
                opstack.push(toks[i]);

            //if token is an end parentheses
            } else if (toks[i].equals(")")) {
                //then until we reach the first parentheses...
                while (!opstack.getVal().equals("(") && !opstack.isEmpty()) {
                    //keep popping the stack onto the queue
                    try {
                        queue.enqueue(opstack.pop());
                    } catch (StackException e) {
                        System.out.println("Error enqueueing popped stack");
                    }
                }
                //then pop the parentheses without adding it to the queue
                try {
                    opstack.pop();
                } catch (StackException e) {
                    System.out.println("Error: No stack");
                }

            //if token is a variable
            } else if (Character.isLetter(toks[i].charAt(0))) {
                //put String token into type char variable
                char var = toks[i].charAt(0);

                //if it already exists in dictionary
                if (dict.lookup(var) != -1) {
                    //then look up and store its value
                    double storedVal = dict.lookup(var);

                    //change the token to its proper value
                    toks[i] = Double.toString(storedVal);

                    //then push it onto the queue
                    queue.enqueue(toks[i]);
                }
                //if not in the dictionary, throw an error
                else {
                    throw new StackException("");
                }
            }

            //if it's a number
            else {
                //add it straight to queue
                queue.enqueue(toks[i]);
            }
        }

        //pop the stack until its empty and get the final postfix equation
        while (!opstack.isEmpty()) {
            try {
                queue.enqueue(opstack.pop());
            } catch (StackException e) {
                System.out.println("Error: Postfixing problem");
            }
        }
        return evaluate(queue);
    }

    private static double evaluate(Queue<String> queue) throws QueueException, StackException {
        //create new stack of doubles to hold final evaluated amount
        Stack<Double> eval = new Stack<>();

        //dequeue until empty
        while (!queue.isEmpty()) {
            //if top of queue is a double ...
            try {
                if (Double.parseDouble(queue.getVal()) >= 0 || Double.parseDouble(queue.getVal()) <= 0) {
                    //....then push it to the stack
                    eval.push(Double.parseDouble(queue.dequeue()));
                }
            }

            //if it's not a double, it's an operator or whitespace
            catch (NumberFormatException e) {
                //if it's not whitespace, it's an operator
                if (!queue.getVal().equals(" ")) {
                    //pop 2 numbers from stack
                    double num1 = eval.pop();
                    double num2 = eval.pop();

                    //perform operations on those popped variables based on the queue operator
                    switch (queue.getVal()) {
                        case "+":
                            eval.push(num2 + num1);
                            break;
                        case "-":
                            eval.push(num2 - num1);
                            break;
                        case "*":
                            eval.push(num2 * num1);
                            break;
                        case "/":
                            eval.push(num2 / num1);
                            break;
                        case "^":
                            eval.push(Math.pow(num2, num1));
                            break;
                    }
                }
                //get next queue element
                queue.dequeue();
                }
            }
        return eval.pop();
    }

    public static void main(String args[]) throws QueueException, HashException, StackException {
        Calculator calc = new Calculator();

        System.out.println("=====================================");
        System.out.println("\t\tEXPRESSION FORMAT");
        System.out.println();
        System.out.println("\t1.) LET [variable] = [expression]");
        System.out.println("\t2.) PRINT [variable]");
        System.out.println("\t3.) QUIT / EXIT");
        System.out.println("=====================================");

        calc.process();
    }
}