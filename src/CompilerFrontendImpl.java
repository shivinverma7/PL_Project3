public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    @Override
    protected void init_lexer() {
        Lexer lexer = new LexerImpl();

        // NUM automaton
        Automaton numAutomaton = new AutomatonImpl();
        numAutomaton.addState(0, true, false);
        numAutomaton.addState(1, false, false);
        numAutomaton.addState(2, false, true);

        for (char c = '0'; c <= '9'; c++) {
            numAutomaton.addTransition(0, c, 0);
            numAutomaton.addTransition(1, c, 2);
            numAutomaton.addTransition(2, c, 2);
        }
        numAutomaton.addTransition(0, '.', 1);

        lexer.add_automaton(TokenType.NUM, numAutomaton);

        Automaton plusAutomaton = createSimpleAutomaton('+');
        lexer.add_automaton(TokenType.PLUS, plusAutomaton);

        Automaton minusAutomaton = createSimpleAutomaton('-');
        lexer.add_automaton(TokenType.MINUS, minusAutomaton);

        Automaton timesAutomaton = createSimpleAutomaton('*');
        lexer.add_automaton(TokenType.TIMES, timesAutomaton);

        Automaton divAutomaton = createSimpleAutomaton('/');
        lexer.add_automaton(TokenType.DIV, divAutomaton);

        Automaton lparenAutomaton = createSimpleAutomaton('(');
        lexer.add_automaton(TokenType.LPAREN, lparenAutomaton);

        Automaton rparenAutomaton = createSimpleAutomaton(')');
        lexer.add_automaton(TokenType.RPAREN, rparenAutomaton);

        Automaton wsAutomaton = new AutomatonImpl();
        wsAutomaton.addState(0, true, true);
        wsAutomaton.addTransition(0, ' ', 0);
        wsAutomaton.addTransition(0, '\n', 0);
        wsAutomaton.addTransition(0, '\r', 0);
        wsAutomaton.addTransition(0, '\t', 0);
        lexer.add_automaton(TokenType.WHITE_SPACE, wsAutomaton);

        this.lex = lexer;
    }

    private Automaton createSimpleAutomaton(char symbol) {
        Automaton automaton = new AutomatonImpl();
        automaton.addState(0, true, false);
        automaton.addState(1, false, true);
        automaton.addTransition(0, symbol, 1);
        return automaton;
    }

}