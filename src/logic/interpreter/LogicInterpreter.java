package logic.interpreter;

import logic.base.Clause;
import logic.base.Operator;
import logic.clause.BinaryClause;
import logic.clause.UnaryClause;
import logic.clause.ValueClause;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LogicInterpreter {
    private String input;

    private Map<String, AtomicBoolean> variables;
    private Clause clause;

    public LogicInterpreter(String input) {
        this.input = input;
        if (!this.hasValidParentheses()) {
            throw new Error("Number of opening/closing parentheses do not match. Check the input.");
        }
        this.variables = new HashMap<>();
        this.evaluate();
    }

    public Clause getInterpretedClause() {
        return this.clause;
    }

    /**
     * Checks if this logic is satisfiable.
     * Runs through all 2^n (n: number of variables) combinations and directly checks them.
     * If this is satisfiable, then returns the variables map on one of those instances.
     * If not, then returns null.
     * @return If this logic is satisfiable.
     */
    public Map<String, Boolean> checkSatisfiable() {
        List<Map.Entry<String, AtomicBoolean>> variablesList = new ArrayList<>(this.variables.entrySet());
        boolean result = dfsSatisfiable(this.clause, variablesList, 0);
        if (result) {
            variablesList.forEach((e) -> this.variables.put(e.getKey(), e.getValue()));
            return this.getSimpleVariablesMap();
        }
        return null;
    }

    /**
     * Checks if this logic evaluates to true on any conditions of the variables.
     * Runs through all 2^n (n: number of variables) combinations and directly checks them.
     * If this is NOT a tautology, then returns a counter example.
     * If this is a tautology, returns null.
     * @return True if this logic is a tautology.
     */
    public Map<String, Boolean> checkTautology() {
        List<Map.Entry<String, AtomicBoolean>> variablesList = new ArrayList<>(this.variables.entrySet());
        boolean result = dfsTautology(this.clause, variablesList, 0);
        if (!result) {
            variablesList.forEach((e) -> this.variables.put(e.getKey(), e.getValue()));
            return this.getSimpleVariablesMap();
        }
        return null;
    }

    private Map<String, Boolean> getSimpleVariablesMap() {
        Map<String, Boolean> ret = new HashMap<>();
        this.variables.forEach((k, v) -> ret.put(k, v.get()));
        return ret;
    }

    /**
     * Checks if the given logic is satisfiable using depth-first-search algorithm.
     * Stops the search as soon as one satisfiable condition is found.
     * If then, example that satisfies the logic is stored in the given "variables" variable.
     * @param clause Logic to check.
     * @param variables List of variables.
     * @param depth Current depth. Input 0 to start the search.
     * @return True if this logic is satisfiable.
     */
    private static boolean dfsSatisfiable(Clause clause, List<Map.Entry<String, AtomicBoolean>> variables, int depth) {
        if (depth == variables.size()) {
            return clause.value();
        }

        AtomicBoolean variable = variables.get(depth).getValue();
        variable.set(false);
        boolean result = dfsSatisfiable(clause, variables, depth + 1);
        if (result) return true;

        variable.set(true);
        return dfsSatisfiable(clause, variables, depth + 1);
    }

    /**
     * Checks if the given logic is a tautology, by checking for all combination of variables.
     * Stops the search as soon as one counter example is found.
     * If then, the counter example is stored in the given "variables" variable.
     * @param clause Logic to check.
     * @param variables List of variables.
     * @param depth Current depth. Input 0 to start the search.
     * @return True if this logic is a tautology.
     */
    private static boolean dfsTautology(Clause clause, List<Map.Entry<String, AtomicBoolean>> variables, int depth) {
        if (depth == variables.size()) {
            return clause.value();
        }

        AtomicBoolean variable = variables.get(depth).getValue();
        variable.set(false);
        boolean result = dfsTautology(clause, variables, depth + 1);
        if (!result) return false;

        variable.set(true);
        return dfsTautology(clause, variables, depth + 1);
    }

    /**
     * Evaluates the input, and records the result to "clause" field.
     * To be called once.
     */
    private void evaluate() {
        if (this.clause != null) {
            return;
        }

        char[] chars = this.input.toCharArray();

        boolean isVariable = false;
        int varStart = 0;
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (!isVariable && isVariableChar(ch)) {
                isVariable = true;
                varStart = i;
            } else if (isVariable && !isVariableChar(ch)) {
                String varName = this.input.substring(varStart, i);
                this.createVariable(varName);
                isVariable = false;
            }
        }
        if (isVariable) {
            String varName = this.input.substring(varStart, chars.length);
            this.createVariable(varName);
        }

        this.clause = interpretClause(this.input, this.variables);
    }

    private void createVariable(String varName) {
        this.variables.put(varName, new AtomicBoolean());
    }

    private static Clause interpretClause(String input, Map<String, AtomicBoolean> variables) {
        // Check if given input is variable
        if (isVariableName(input)) {
            return new ValueClause(input, variables.get(input));
        }

        // Check if given input is closed with parentheses
        if (input.charAt(0) == '('
                && input.charAt(input.length()-1) == ')'
                && getClosingParenthesisIndex(input, 0) == input.length()-1) {
            return interpretClause(input.substring(1, input.length()-1), variables);
        }

        char[] chars = input.toCharArray();
        Map<Operator, Integer> operatorIndices = new HashMap<>();
        int parDepth = 0;
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            // Parentheses
            if (ch == '(') {
                parDepth++;
                continue;
            } else if (ch == ')') {
                parDepth--;
                continue;
            }

            // Do not evaluate characters inside parentheses immediately
            if (parDepth != 0) {
                continue;
            }

            // Check first operator indices
            if (isOperatorChar(ch)) {
                Operator op = getOperator(ch);
                if (!operatorIndices.containsKey(op)) {
                    operatorIndices.put(op, i);
                }
            }
        }

        // Operators
        List<Operator> list = Arrays.asList(Operator.getOrderedValues());
        Collections.reverse(list);
        Operator[] operators = list.toArray(new Operator[]{});
        for (Operator op : operators) {
            if (!operatorIndices.containsKey(op)) continue;

            switch (op) {
                case NOT:
                    // Only evaluate NOT operator when located at the beginning of input string
                    if (operatorIndices.get(op) != 0) {
                        continue;
                    }

                    Clause clause = interpretClause(input.substring(1), variables);
                    return new UnaryClause(clause, op);
                case AND:
                case OR:
                case IMPLY:
                case EQUIV:
                    int location = operatorIndices.get(op);
                    Clause firstClause = interpretClause(input.substring(0, location), variables);
                    Clause secondClause = interpretClause(input.substring(location+1), variables);
                    return new BinaryClause(firstClause, op, secondClause);
            }
        }

        throw new Error("Internal error on interpreting a clause: " + input);
    }

    private boolean hasValidParentheses() {
        int opens = 0;
        int closes = 0;
        char[] chars = this.input.toCharArray();
        for (char ch : chars) {
            if (ch == '(') opens++;
            if (ch == ')') closes++;
        }
        return opens == closes;
    }

    /**
     * Get the index of closing parenthesis corresponding to the given opening index.
     * @param string String to check.
     * @param opening Index of the opening parenthesis character.
     * @return Index of closing parenthesis if found. Returns -1 if not.
     */
    private static int getClosingParenthesisIndex(String string, int opening) {
        int openingDepth = 0;
        int depth = 0;
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == ')') depth--;

            if (i == opening) {
                openingDepth = depth;
            }

            if (ch == '(') depth++;
        }

        depth = 0;
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch == ')') {
                depth--;
                if (depth == openingDepth) {
                    return i;
                }
            }

            if (ch == '(') depth++;
        }

        return -1;
    }

    private static boolean isVariableChar(char ch) {
        if (ch == '(' || ch == ')') return false;
        return !isOperatorChar(ch);
    }

    /**
     * Checks if the given string is a variable name. <br>
     * Examples: <br>
     * (a∧b) -> false (This is a clause)
     * a -> true
     * @param input String to check.
     * @return True if the given string is a variable name.
     */
    private static boolean isVariableName(String input) {
        char[] chars = input.toCharArray();
        for (char ch : chars) {
            if (!isVariableChar(ch)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isOperatorChar(char ch) {
        for (Operator op : Operator.values()) {
            if (op.ch == ch) return true;
        }
        return false;
    }

    private static Operator getOperator(char ch) {
        for (Operator op : Operator.values()) {
            if (op.ch == ch) return op;
        }
        throw new Error("Unknown operator: " + ch);
    }
}
