/*Authors Ross Satchel, and Sean Casaus*/

package team4_finalproject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import org.json.JSONException;

public class Team4_GUI extends JPanel implements ActionListener {

    private static JLabel questionLabel;
    private static JLabel answer1label;
    private static JLabel answer2label;
    private static JLabel answer3label;
    private static JLabel answer4label;
    private static String correctAnswer;
    private static int questionCounter = 0;

    private static JButton ansButton1;
    private static JButton ansButton2;
    private static JButton ansButton3;
    private static JButton ansButton4;

    private static String question;
    private static String answer1;
    private static String answer2;
    private static String answer3;
    private static String answer4;
    private static String answer_correct;
    private List<Boolean> gradedAnswer = new ArrayList<Boolean>();
    private List<Long> answerTime = new ArrayList<Long>();

    private static long timeAsked;
    private static long timeAnswered;
    private static CalculateQuestionScore cqs;
    private static double scaledTemp;

    private static JPanel questionPanel;
    private static JPanel answerPanel;
    private static JPanel basePanel;
    private static JPanel combinedPanel;

    private static QuestionsAndAnswers questionAndAnswerObject;
    private static List<QuestionsAndAnswers> listOfQsAndAs;

    public static CalculateQuestionScore calcQuesScore;
    private static JLayeredPane layeredPane = new JLayeredPane();
    private static Team4Ghost ghost = new Team4Ghost();
    private String questionAnswer = " ";


    public Team4_GUI() throws IOException, JSONException {      // constructor

        listOfQsAndAs = new ArrayList<QuestionsAndAnswers>();
        String CSVfilename = "src/team4_finalproject/QuestionsAndAnswer.csv";
        CSVUtils util = new CSVUtils();
        CalculateQuestionScore cqs = new CalculateQuestionScore();
        //calcQuesScore = new CalculateQuestionScore();

        // create an ArrayList of QuestionsAndAnswers objects
        listOfQsAndAs = util.CSVReadFile(CSVfilename);
/*
        // print all of the QuestionAndAnswer Objects
        /////////////////////////////////////////////////////////////////////////////
        for (int i = 0; i < 10; i++) {
            // print the contents of the whole file of questions and answers
            listOfQsAndAs.get(i).printQandAobject();
        }
*/
        ///////////////////////////////////////////////////////////////////////////
        // questionPanel and answerPanel sit on top of basePanel
        basePanel = new JPanel();
        basePanel.setBackground(Color.black);
        combinedPanel = new JPanel(new GridLayout(2, 1));
        combinedPanel.setBackground(Color.lightGray);
        questionPanel = new JPanel(new GridLayout(1, 1));
        questionPanel.setBackground(Color.red);
        answerPanel = new JPanel(new GridLayout(1, 4));
        answerPanel.setBackground(Color.lightGray);

        questionLabel = new JLabel();
        scaledTemp = cqs.getScaledTemp(cqs.getCurrentTemp());

        ansButton1 = new JButton();
        ansButton1.setBackground(Color.yellow);
        ansButton2 = new JButton();
        ansButton2.setBackground(Color.cyan);
        ansButton3 = new JButton();
        ansButton3.setBackground(Color.pink);
        ansButton4 = new JButton();
        ansButton4.setBackground(Color.orange);

        ansButton1.addActionListener(this);
        ansButton2.addActionListener(this);
        ansButton3.addActionListener(this);
        ansButton4.addActionListener(this);

        // ask first question
        setQuestion(questionCounter);

        // time that the quiz has begun
        long timeStart = System.currentTimeMillis();

    }

    public JPanel setQuestion(int questionCounter) {
        question = listOfQsAndAs.get(questionCounter).getQuestion();
        questionLabel.setText(question);           // get question, add it
        questionPanel.add(questionLabel);               // to the label

        answer1 = listOfQsAndAs.get(questionCounter).getAnswer1();
        answer2 = listOfQsAndAs.get(questionCounter).getAnswer2();
        answer3 = listOfQsAndAs.get(questionCounter).getAnswer3();
        answer4 = listOfQsAndAs.get(questionCounter).getAnswer4();

        ansButton1.setText(answer1);
        ansButton2.setText(answer2);
        ansButton3.setText(answer3);
        ansButton4.setText(answer4);

        answerPanel.add(ansButton1);
        answerPanel.add(ansButton2);
        answerPanel.add(ansButton3);
        answerPanel.add(ansButton4);

        if (questionCounter == 0) {
            combinedPanel.add(questionPanel);
            combinedPanel.add(answerPanel);
            basePanel.add(combinedPanel, BorderLayout.CENTER);
        }

        this.add(setGhost(basePanel));

        timeAsked = System.currentTimeMillis();

        return this;
    }

    public JPanel setQuestionPanel(QuestionsAndAnswers qAndA) {
        questionPanel = new JPanel();
        questionLabel = new JLabel(qAndA.getQuestion());
        questionPanel.add(questionLabel);
        return questionPanel;
    }

    public JPanel setAnswerPanel(QuestionsAndAnswers qAndA) {
        answer1label = new JLabel(qAndA.getAnswer1());
        answer2label = new JLabel(qAndA.getAnswer2());
        answer3label = new JLabel(qAndA.getAnswer3());
        answer4label = new JLabel(qAndA.getAnswer4());

        answerPanel.setLayout(new GridLayout(1, 4));
        answerPanel.add(answer1label);
        answerPanel.add(answer2label);
        answerPanel.add(answer3label);
        answerPanel.add(answer4label);

        return answerPanel;

    }

    public static JPanel setGhost(JPanel tempBase) {
        layeredPane.setPreferredSize(new Dimension(1000, 400));
        tempBase.setPreferredSize(new Dimension(1000, 400));
        tempBase.setBounds(1, 1, 1000, 400);
        layeredPane.setBounds(0, 0, 1000, 400);
        layeredPane.setOpaque(false);
        tempBase.setVisible(true);

        layeredPane.add(ghost, JLayeredPane.PALETTE_LAYER);
//        layeredPane.add(tempCombined, JLayeredPane.DEFAULT_LAYER);
        tempBase.add(layeredPane, BorderLayout.CENTER);
        tempBase.setSize(10, 10);
        tempBase.add(ghost, BorderLayout.CENTER);
//        tempBase.add(tempCombined, BorderLayout.CENTER);
        return tempBase;
    }

    public static void runGhost() {
        Thread newThread = new Thread(ghost); //Runs ghost movement algorithm
        newThread.start(); //Runs Ghost movement algorithm
    }

    public JPanel removeGhost() {
        this.remove(basePanel);
        this.revalidate();
        this.repaint();

        basePanel.remove(ghost);
        basePanel.revalidate();
        basePanel.repaint();

        this.add(basePanel);

        return this;
    }

    public void delayed() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public String score() {
        int right = 0;
        for (Boolean temp: gradedAnswer) {
            if (temp == true) {
                right++;
            }
        }
        return Integer.toString(right) + "/10";
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // get time when user clicked answer
        timeAnswered = System.currentTimeMillis();

        // questionCounter runs from 0 to 9
        if (questionCounter <= 9) {
            String selectedAnswer;
            System.out.println("Question Counter: " + questionCounter);

            if (e.getSource() == ansButton1) {
                System.out.println("Button 1 clicked");
                selectedAnswer = listOfQsAndAs.get(questionCounter).getAnswer1();
            } else if (e.getSource() == ansButton2) {
                System.out.println("Button 2 clicked");
                selectedAnswer = listOfQsAndAs.get(questionCounter).getAnswer2();
            } else if (e.getSource() == ansButton3) {
                System.out.println("Button 3 clicked");
                selectedAnswer = listOfQsAndAs.get(questionCounter).getAnswer3();
            } else {
                System.out.println("Button 4 clicked");
                selectedAnswer = listOfQsAndAs.get(questionCounter).getAnswer4();
            }

            System.out.println("Selected Answer: " + selectedAnswer);

            // tell user if they have selected correct answer or not
            if (selectedAnswer.equals(listOfQsAndAs.get(questionCounter).getCorrect_answer())) {
                System.out.println("QUESTION COUNTER:  " + questionCounter);
                System.out.println("CORRECT!");
                gradedAnswer.add(Boolean.TRUE);
            } else {
                System.out.println("WRONG!");
                System.out.println("QUESTION COUNTER:  " + questionCounter);
                gradedAnswer.add(Boolean.FALSE);
            }
            // make a new CalculateQuestionScore object for each answer

            // increment counter, add answer time to arraylist and select next question
            answerTime.add(timeAnswered - timeAsked);

            calcQuesScore = new CalculateQuestionScore();
            double getBooleanGradeAsDouble = calcQuesScore.getGradeFromBoolean(gradedAnswer, questionCounter);
            double rando = calcQuesScore.getRandom();
            double hour = (double) calcQuesScore.getCurrentHour();
            double scaledFactor = getBooleanGradeAsDouble * scaledTemp * rando * hour;
            System.out.println("Scaled Answer Factor: " + scaledFactor);

            if (scaledFactor < 2) {
                System.out.println("Wow, you're clever!");
                questionAnswer = "Wow, you're clever!";
            } else if (scaledFactor >= 2 && scaledFactor < 4) {
                System.out.println("Pretty good!");
                questionAnswer = "Pretty good!";
            } else if (scaledFactor >= 4 && scaledFactor < 6) {
                System.out.println("Doing well, keep up the good work!");
                questionAnswer = "Doing well, keep up the good work!";
            } else if (scaledFactor >= 6 && scaledFactor < 8) {
                System.out.println("You're above average!");
                questionAnswer = "You're above average!";
            } else if (scaledFactor >= 8 && scaledFactor < 10) {
                System.out.println("You are perfectly average!");
                questionAnswer = "You are perfectly average!";
            } else if (scaledFactor >= 10 && scaledFactor < 12) {
                System.out.println("Slightly below average, lift your game!");
                questionAnswer = "Slightly below average, lift your game!";
            } else if (scaledFactor >= 12 && scaledFactor < 14) {
                System.out.println("You're struggling a bit!");
                questionAnswer = "You're struggling a bit!";
            } else if (scaledFactor >= 14 && scaledFactor < 16) {
                System.out.println("Did you have to Google that one?");
                questionAnswer = "Did you have to Google that one?";
            } else if (scaledFactor >= 16 && scaledFactor < 18) {
                System.out.println("Looks like you Googled it and still got it wrong!");
                questionAnswer = "Looks like you Googled it and still got it wrong!";
            } else if (scaledFactor > 18) {
                System.out.println("You should probably go back to high school!");
                questionAnswer = "You should probably go back to high school!";
            }


            questionCounter++;

            removeGhost();
            ghost = new Team4Ghost(questionAnswer);
            runGhost();

            combinedPanel.add(questionPanel);
            combinedPanel.add(answerPanel);
            basePanel.add(combinedPanel, BorderLayout.CENTER);

            this.add(setGhost(basePanel));


            // if at end of quiz give results and answre times
            if (questionCounter == 10) {
                System.out.println("Results: " + gradedAnswer.toString());
                System.out.println("Answer times: " + answerTime.toString());
                System.out.println("End Of Test");

                removeGhost();
                ghost = new Team4Ghost("Results: " + score());
                runGhost();

                combinedPanel.add(questionPanel);
                combinedPanel.add(answerPanel);
                basePanel.add(combinedPanel, BorderLayout.CENTER);

                this.add(setGhost(basePanel));

                return;
            }
            else {
                setQuestion(questionCounter);
            }
        }
    }
}

