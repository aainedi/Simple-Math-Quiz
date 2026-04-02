import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Outer class
public class SimpleMathQuiz extends JFrame {
    
    //variables of outer class
    String title = "Simple Math Quiz";

    //GUI components
    private JLabel questionLabel;
    private JRadioButton[] answer;
    private ButtonGroup group;
    private JButton submitButton;

    //Quiz tracking variables
    private int currentQuestion = 0;
    private int score = 0;

    //array to store answer
    private int[] userAnswers = new int[3];

    //Inner class (Nested class)
    class Question {
        //variable of inner class
        String questionText;
        String[] choices;
        int correctAnswer;

        //Constructor
        Question(String questionText, String[] choices, int correctAnswer) {
            this.questionText = questionText;
            this.choices = choices;
            this.correctAnswer = correctAnswer;
        }
    }

    //Array of Question 
    private Question[] questions = {
        new Question("1) 5 + 3 = ?", new String[]{"6", "7", "8", "9"}, 2),
        new Question("2) 10 × 2 = ?", new String[]{"15", "20", "25", "30"}, 1),
        new Question("3) 12 ÷ 4 = ?", new String[]{"2", "3", "4", "5"}, 1)
    };

    public SimpleMathQuiz() {
        setTitle(title);
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ((JComponent) getContentPane()).setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        //1st layout manager: BorderLayout
        setLayout(new BorderLayout());

        questionLabel = new JLabel();
        add(questionLabel, BorderLayout.NORTH);

        //2nd layout manager: GridLayout
        JPanel optionPanel = new JPanel(new GridLayout(4, 1));
        answer = new JRadioButton[4];
        group = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            answer[i] = new JRadioButton();
            group.add(answer[i]);
            optionPanel.add(answer[i]);
        }

        add(optionPanel, BorderLayout.CENTER);

        submitButton = new JButton("Submit");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(submitButton);
        add(bottomPanel, BorderLayout.SOUTH);

        displayQuestion();

        //Anonymous class (Event Handling)
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int selected = getSelectedOption();

                //Exception handling
                if (selected == -1) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Please select an answer before proceeding.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                questionLabel.setForeground(Color.BLACK);
                userAnswers[currentQuestion] = selected;

                // Local class
                class AnswerChecker {
                    boolean isCorrect(int selected, int correct) {
                        return selected == correct;
                    }
                }

                AnswerChecker checker = new AnswerChecker();
                if (checker.isCorrect(selected, questions[currentQuestion].correctAnswer)) {
                    score++;
                }
                currentQuestion++;

                if (currentQuestion < questions.length) {
                    displayQuestion();
                } else {
                    submitButton.setEnabled(false);
                    showResultFrame();
                }
            }
        });
    }

    //method to display question and answer options
    private void displayQuestion() {
        Question q = questions[currentQuestion];
        questionLabel.setText(q.questionText);

        for (int i = 0; i < 4; i++) {
            answer[i].setText(q.choices[i]);
        }
        group.clearSelection();

        if (currentQuestion == questions.length - 1) {
            submitButton.setText("Submit");
        } else {
            submitButton.setText("Next");
        }
    }

    //getter answer input by user
    private int getSelectedOption() {
        for (int i = 0; i < 4; i++) {
            if (answer[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    //windows to show the result
    private void showResultFrame() {
        JFrame resultFrame = new JFrame("Quiz Result");
        resultFrame.setSize(300, 150);
        resultFrame.setLayout(new BorderLayout());

        JLabel finalScore = new JLabel("Your Final Score: " + score + "/" + questions.length);
        finalScore.setHorizontalAlignment(JLabel.CENTER);
        finalScore.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JButton exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(80, 30));

        JButton viewAnswerButton = new JButton("View Answer");
        viewAnswerButton.setPreferredSize(new Dimension(120, 30));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        bottomPanel.add(viewAnswerButton);
        bottomPanel.add(exitButton);

        resultFrame.add(finalScore, BorderLayout.CENTER);
        resultFrame.add(bottomPanel, BorderLayout.SOUTH);

        //event-driven programming
        exitButton.addActionListener(e -> System.exit(0));

        viewAnswerButton.addActionListener(e -> {
            resultFrame.setVisible(false);
            showAnswerFrame(resultFrame);
        });

        resultFrame.setLocationRelativeTo(null);
        resultFrame.setVisible(true);
    }

    //Review Answer layout
    private void showAnswerFrame(JFrame resultFrame) {
        // Shadowing happens here
        String title = "Here is your previous answer: ";
        JFrame answerFrame = new JFrame("Review Answers");
        answerFrame.setSize(400, 300);
        answerFrame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(questions.length, 1));
        JScrollPane scrollPane = new JScrollPane(mainPanel);

        for (int i = 0; i < questions.length; i++) {

            Question q = questions[i];

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel qLabel = new JLabel(q.questionText);
            panel.add(qLabel);

            ButtonGroup reviewGroup = new ButtonGroup();

            for (int j = 0; j < q.choices.length; j++) {

                JRadioButton optionBtn = new JRadioButton(q.choices[j]);

                optionBtn.setOpaque(true);
                optionBtn.setFocusable(false);
                optionBtn.setEnabled(true);
                
                //selected answer by user
                if (j == userAnswers[i]) {
                    optionBtn.setSelected(true);
                }

                optionBtn.setForeground(Color.BLACK);
                
                //style for display correct and wrong answer
                if (j == userAnswers[i]) {
                    if (j == q.correctAnswer) {
                        optionBtn.setForeground(new Color(0, 153, 0));
                        optionBtn.setFont(new Font("Arial", Font.BOLD, 12));
                    } else {
                        optionBtn.setForeground(new Color(204, 0, 0));
                    }
                }

                if (j == q.correctAnswer && userAnswers[i] != q.correctAnswer) {
                    optionBtn.setForeground(new Color(0, 153, 0));
                    optionBtn.setFont(new Font("Arial", Font.BOLD, 12));
                }

                reviewGroup.add(optionBtn);
                panel.add(optionBtn);
            }

            mainPanel.add(panel);
        }

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        answerFrame.add(titleLabel, BorderLayout.NORTH);
        answerFrame.add(scrollPane, BorderLayout.CENTER);

        //Back button styling
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(80, 30));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        bottomPanel.add(backButton);

        answerFrame.add(bottomPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            answerFrame.dispose();
            resultFrame.setVisible(true);
        });

        answerFrame.setLocationRelativeTo(null);
        answerFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new SimpleMathQuiz().setVisible(true);
    }
}