## logic-evaluator
Simple logic evaluator in Java.
Evaluates the given logic in string, and checks if the logic is satisfiable and if the logic is a tautology.
No algorithm for SAT solving used, this program directly checks all 2^n patterns (n: number of variables).

### Allowed Operators
You can use these operators to construct a sentence. Other characters are not supported and will be considered variables.
- NOT `¬`
- AND `∧`
- OR `∨`
- IMPLY `⇒`
- EQUIVALENT `⇔`

### Example Inputs
- `α⇒¬¬α`
- `((p⇒q)∧(q⇒r)∧(r⇒p))⇔((p⇔q)∧(q⇔r))`
- `((α⇒β)∧α)⇒β`
- `(¬α∧¬β∧¬γ)∨(α∧¬β∧γ)∨(α∧β∧γ)`

### Example Output
```
Input: α⇒¬¬α
Evaluated: (α⇒(¬(¬α)))
-----
This logic is satisfiable.
Possible solution: {α=false}
-----
This logic is a tautology.
```
