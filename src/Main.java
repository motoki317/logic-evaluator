/*
Evaluates the given logic in string, and checks if the logic is satisfiable and if the logic is a tautology.
No algorithm for SAT solving used, this program directly checks all 2^n patterns (n: number of variables).

Some example inputs:
α⇒¬¬α
((p⇒q)∧(q⇒r)∧(r⇒p))⇔((p⇔q)∧(q⇔r))
((α⇒β)∧α)⇒β
(¬α∧¬β∧¬γ)∨(α∧¬β∧γ)∨(α∧β∧γ)
 */

import logic.interpreter.LogicInterpreter;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Input: ");
        String input = sc.nextLine();

        LogicInterpreter interpreter = new LogicInterpreter(input);
        if (!interpreter.getReplacedText().equals(input)) {
            println("Replaced text: " + interpreter.getReplacedText());
        }
        println("Evaluated: " + interpreter.getInterpretedSentence().toString());

        println("-----");

        Map<String, Boolean> solution = interpreter.checkSatisfiable();
        println("This logic is " + (solution != null ? "" : "NOT ") + "satisfiable.");
        if (solution != null) {
            println("Possible solution: " + solution);
        }

        println("-----");

        Map<String, Boolean> counterExample = interpreter.checkTautology();
        println("This logic is " + (counterExample == null ? "" : "NOT ") + "a tautology.");
        if (counterExample != null) {
            println("Counter example: " + counterExample);
        }
    }

    private static void println(String string) {
        System.out.println(string);
    }
}
