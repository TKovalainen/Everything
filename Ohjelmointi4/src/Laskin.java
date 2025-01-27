import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import java.lang.Math;
import java.util.ArrayList;

public class Laskin implements ActionListener {

    JFrame frame;
    JTextArea textfield;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[13];
    JButton addButton, subButton, mulButton, divButton, decButton, equButton, delButton, clrButton;
    JButton sqrButton, piButton, sqrtButton, leftParenButton, rightParenButton;
    JPanel panel;
    JButton historyButton;
    ArrayList<String> calculations = new ArrayList<String>();


    Font myFont = new Font("Arial", Font.BOLD, 30);

    public Laskin() { //creates the calculator GUI

        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(null);
        textfield = new JTextArea();
        textfield.setBounds(50, 25, 500, 100);
        textfield.setFont(myFont);
        textfield.setEditable(false);
        textfield.setCaretColor(Color.WHITE);


        addButton = new JButton("+");
        subButton = new JButton("-");
        mulButton = new JButton("*");
        divButton = new JButton("/");
        decButton = new JButton(".");
        equButton = new JButton("=");
        delButton = new JButton("Del");
        clrButton = new JButton("Clr");

        sqrButton = new JButton("^");
        piButton = new JButton("π");
        sqrtButton = new JButton("√");
        leftParenButton = new JButton("(");
        rightParenButton = new JButton(")");

        functionButtons[0] = addButton;
        functionButtons[1] = subButton;
        functionButtons[2] = mulButton;
        functionButtons[3] = divButton;
        functionButtons[4] = decButton;
        functionButtons[5] = equButton;
        functionButtons[6] = delButton;
        functionButtons[7] = clrButton;
        functionButtons[8] = sqrButton;
        functionButtons[9] = piButton;
        functionButtons[10] = sqrtButton;
        functionButtons[11] = leftParenButton;
        functionButtons[12] = rightParenButton;

        for (int i = 0; i < 13; i++) { //adds the buttons to the GUI
            functionButtons[i].addActionListener(this);
            functionButtons[i].setFont(myFont);
            functionButtons[i].setFocusable(false);
        }
        
        for (int i = 0; i < 10; i++) { //adds the number buttons to the GUI
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberButtons[i].setFont(myFont);
            numberButtons[i].setFocusable(false);
        }

        delButton.setBounds(200, 475, 100, 50);
        clrButton.setBounds(300, 475, 100, 50);
        historyButton = new JButton("History");
        historyButton.addActionListener(this);
        historyButton.setBounds(100, 475, 100, 50);

        panel = new JPanel();
        panel.setBounds(50, 150, 400, 300);
        panel.setLayout(new GridLayout(4, 5, 10, 10));

        sqrtButton.setBounds(460, 151, 70, 67);

        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(addButton);
        panel.add(divButton);
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);
        panel.add(sqrButton);
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(mulButton);
        panel.add(piButton);
        panel.add(decButton);
        panel.add(numberButtons[0]);
        panel.add(equButton);
        panel.add(leftParenButton);
        panel.add(rightParenButton);

        frame.add(panel);
        frame.add(sqrtButton);
        frame.add(delButton);
        frame.add(clrButton);
        frame.add(historyButton);
        frame.add(textfield);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        @SuppressWarnings("unused")
        Laskin laskin = new Laskin();
    }

    @Override
    public void actionPerformed(ActionEvent e) { //checks which button was clicked and performs the corresponding action
        JButton clickedButton = (JButton) e.getSource();
        String buttonText = clickedButton.getText();

        if(e.getSource() == historyButton){
            @SuppressWarnings("unused")
            Historia historia = new Historia(calculations);
        }

        if (buttonText.equals("=")) {
            String expression = textfield.getText();
            double result = calculate(expression);
            textfield.setText(String.valueOf(result));
            String calculation = setCalculation(expression, String.valueOf(result));
            textfield.setText(calculation);

        } else if (buttonText.equals("Clr")) {
            textfield.setText("");
        } else if (buttonText.equals("Del")) {
            String text = textfield.getText();
            if (text.length() > 0) {
                textfield.setText(text.substring(0, text.length() - 1));
            }
        } else {
            if(e.getSource() != historyButton){
                textfield.setText(textfield.getText() + buttonText);                
            }

        }            
        

    }


    public double calculate(String expression){ //creates a stack for numbers and operators, then calculates the expression
        expression = expression.replaceAll("π", Double.toString(Math.PI));

    
        String[] tokens = expression.split("(?<=[()\\+\\-*/^√])|(?=[()\\+\\-*/^√])"); //splits the expression into tokens
    
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
    
        boolean sqrtNextNumber = false;

        try{ 
            
            for (String token : tokens) {
                if (token.isEmpty()) {
                    continue;
                }
                char firstChar = token.charAt(0);
                if (Character.isDigit(firstChar) || firstChar == '.') {
                    double number = Double.parseDouble(token);
                    if (sqrtNextNumber) {
                        number = Math.sqrt(number);
                        sqrtNextNumber = false;
                    }
                    numbers.push(number);
                } else if (token.length() > 1 && Character.isDigit(token.charAt(1))) {
                    double number = Double.parseDouble(token);
                    if (sqrtNextNumber) {
                        number = Math.sqrt(number);
                        sqrtNextNumber = false;
                    }
                    numbers.push(number); 
                } else if (firstChar == '(') {
                    operators.push(firstChar);
                } else if (firstChar == ')') {
                    while (operators.peek() != '(') {
                        numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                    }
                    operators.pop();
                    } else if (firstChar == '√') {
                        sqrtNextNumber = true;
                    }
                    else {
                        while (!operators.empty() && hasPrecedence(firstChar, operators.peek())) {
                            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                        }
                        operators.push(firstChar);
                    }
                }
            
            while (!operators.empty()) {
                numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
            }
         
        }catch(Exception e){
            System.out.println("Invalid expression");
            textfield.setText("Invalid expression");
        }
        return numbers.pop();   

    }
    

    private boolean hasPrecedence(char op1, char op2) { //checks if the operator has precedence
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    private double applyOperator(char operator, double b, double a) { //applies the operator to the numbers
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    textfield.setText("Cannot divide by zero!");
                    throw new ArithmeticException("Division by zero!");
                }
                return a / b;
            case '^':
                return Math.pow(a, b);
            case '√':
                return Math.sqrt(a);
            default:
                return 0;
        }
    }

    public String setCalculation(String calculation, String result){ //adds the calculation and result to the history
        calculations.add(""+calculation+"\n"+result+" =");
        return ""+calculation+"\n= "+result+"";

    }
}