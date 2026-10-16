import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class NewJFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(NewJFrame.class.getName());

    private Timer timer;
    private int timeLeft = 20;
    private int score;
    private int totalQuestions;
    private int questionNum;
    private int roundNumber = 1;
    private ArrayList<QuizItem> questions;
    private String fileName;

    public NewJFrame() {
        initComponents();
        removeTitleBars();

        // set the size of the window and center on the screen
        this.setSize(457, 350);
        this.setLocationRelativeTo(null);

        // button to start the game
        jButton1.addActionListener(e -> startGame());

        // level difficulty buttons
        jButton3.addActionListener(e -> showLevels());
        jButton5.addActionListener(e -> showLevels());
        jButton12.addActionListener(e -> easyLevel());
        jButton13.addActionListener(e -> mediumLevel());
        jButton14.addActionListener(e -> hardLevel());
        jButton15.addActionListener(e -> startGame());

        // menu buttons
        jButton6.addActionListener(e -> showHistory());
        jButton9.addActionListener(e -> startGame());
        jButton7.addActionListener(e -> System.exit(0));

        // question choices buttons
        jLabel4.setText("Please select an option.");
        jRadioButton1.addActionListener(e -> jLabel4.setText("You selected: A"));
        jRadioButton2.addActionListener(e -> jLabel4.setText("You selected: B"));
        jRadioButton3.addActionListener(e -> jLabel4.setText("You selected: C"));
        jRadioButton4.addActionListener(e -> jLabel4.setText("You selected: D"));

        // submit button
        jButton2.addActionListener(e -> submitAnswer());

        // back button and go back to menu
        jButton4.addActionListener(e -> startGame());

        // quit button
        jButton8.addActionListener(e -> showQuit());

        // yes or no button in the quit panel
        jButton10.addActionListener(e -> startGame());
        jButton11.addActionListener(e -> showContinue());
    }

    // remove the title bars of the internal frames used
    private void removeTitleBars() {
        removeBar(jInternalFrame1);
        removeBar(jInternalFrame2);
        removeBar(jInternalFrame3);
        removeBar(jInternalFrame4);
        removeBar(jInternalFrame5);
    }

    private void removeBar(JInternalFrame frame) {
        BasicInternalFrameUI ui = (BasicInternalFrameUI) frame.getUI();
        ui.setNorthPane(null);
    }

    // menu panel
    private void startGame() {
        if (timer != null) timer.stop();
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "card5");
    }

    // panel for the level difficulty
    private void showLevels() {
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "card9");
    }

    private void easyLevel() {
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "card3");
        startNewGame("src/easy");
        startTimer();
    }

    private void mediumLevel() {
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "card3");
        startNewGame("src/medium");
        startTimer();
    }

    private void hardLevel() {
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "card3");
        startNewGame("src/hard");
        startTimer();
    }

    // timer for the game
    private void startTimer() {
        timeLeft = 20;
        jLabel3.setText(Integer.toString(timeLeft));

        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        timer = new Timer(1000, (ActionEvent e) -> {
            timeLeft--;
            jLabel3.setText(Integer.toString(timeLeft));
            if (timeLeft <= 0) {
                timer.stop();
                jButton2.setEnabled(false); 
                jRadioButton1.setEnabled(false);
                jRadioButton2.setEnabled(false);
                jRadioButton3.setEnabled(false);
                jRadioButton4.setEnabled(false);

                questionNum++;
                if (questionNum < totalQuestions) {
                    loadNextQuestion();
                    startTimer();
                } else {
                    showFinalScore();
                }
            }
        });
        timer.start();
    }

    // class to store quiz items
    class QuizItem {
        String question;
        String[] choices = new String[4];
        String correct;
    }

    // read the file with the quiz questions
    private void startNewGame(String file) {
        this.fileName = file;
        try {
            ArrayList<QuizItem> questionList = new ArrayList<>();
            try (Scanner sc = new Scanner(new File(file))) {
                while (sc.hasNextLine()) {
                    QuizItem item = new QuizItem();

                    item.question = sc.nextLine();

                    for (int i = 0; i < 4; i++) {
                        if (!sc.hasNextLine()) break;
                        item.choices[i] = sc.nextLine();
                    }

                    if (!sc.hasNextLine()) break;
                    item.correct = sc.nextLine();

                    questionList.add(item);
                }
            }

            shuffleQuestions(questionList);

            this.questions = questionList;
            this.totalQuestions = questions.size();
            this.questionNum = 0;
            this.score = 0;

            loadNextQuestion();

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading questions!");
            System.exit(0);
        }
    }

    // shuffle the quiz questions
    private void shuffleQuestions(ArrayList<QuizItem> list) {
        Random rand = new Random();
        for (int i = list.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            QuizItem temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }

    // load the question for the current number
    private void loadNextQuestion() {
        if (questionNum >= totalQuestions) {
            showFinalScore();
            return;
        }

        QuizItem q = questions.get(questionNum);

        jLabel2.setText((questionNum + 1) + ". " + q.question);

        jRadioButton1.setText("A. " + q.choices[0]);
        jRadioButton2.setText("B. " + q.choices[1]);
        jRadioButton3.setText("C. " + q.choices[2]);
        jRadioButton4.setText("D. " + q.choices[3]);

        jButton2.setEnabled(true);
        jRadioButton1.setEnabled(true);
        jRadioButton2.setEnabled(true);
        jRadioButton3.setEnabled(true);
        jRadioButton4.setEnabled(true);

        buttonGroup1.clearSelection();
        jLabel4.setText("Please select a letter.");
    }

    // panel when quit button is clicked
    private void showQuit() {
        if (timer != null) timer.stop(); 
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "card8");
    }

    // 'no' button (continue the quiz and not quit)
    private void showContinue() {
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "card3");
        startTimer(); 
    }

    // handle the answer and submit
    private void submitAnswer() {
        String selectedLetter = null;
        if (jRadioButton1.isSelected()) { selectedLetter = "A"; }
        else if (jRadioButton2.isSelected()) { selectedLetter = "B"; }
        else if (jRadioButton3.isSelected()) { selectedLetter = "C"; }
        else if (jRadioButton4.isSelected()) { selectedLetter = "D"; }

        if (selectedLetter != null) {

            boolean correct = selectedLetter.equals(questions.get(questionNum).correct.trim());

            // panel which shows if the answer is right or wrong
            CardLayout card = (CardLayout) mainPanel.getLayout();
            card.show(mainPanel, "card7");

            if (correct) {
                jLabel13.setText("Correct!");
                jLabel10.setText("Well done!");
                score++;
            } else {
                jLabel13.setText("Incorrect!");
                jLabel10.setText("Oops, that's wrong.");
            }

            jButton2.setEnabled(false);
            jRadioButton1.setEnabled(false);
            jRadioButton2.setEnabled(false);
            jRadioButton3.setEnabled(false);
            jRadioButton4.setEnabled(false);
            timer.stop();

            Timer delay = new Timer(1000, e -> {
                questionNum++;

                if (questionNum < totalQuestions) {
                    CardLayout c = (CardLayout) mainPanel.getLayout();
                    c.show(mainPanel, "card3");
                    loadNextQuestion();
                    startTimer();
                } else {
                    showFinalScore();
                }
            });
            delay.setRepeats(false);
            delay.start();

        } else {
            jLabel4.setText("Please select a letter.");
        }
    }

    // panel to show the result at the end of the game
    private void showFinalScore() {
        if (timer != null) timer.stop();

        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "card4");

        String finalScore = "Your Score: " + score + "/" + totalQuestions;
        jLabel6.setText(finalScore);

        addQuizResult(finalScore);
    }

    // write the record of the results to the history file
    private void addQuizResult(String result) {
        File file = new File("src/historyFile");
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                roundNumber = 0;
                while (br.readLine() != null) {
                    roundNumber++;
                }
                roundNumber++; // next round
            } catch (IOException e) {
                logger.log(java.util.logging.Level.WARNING, "Could not read history", e);
            }
        } else {
            roundNumber = 1;
        }

        result = "(" + fileName.replace("src/", "") + ")" + "Round " + roundNumber + ": " + score + "/" + totalQuestions;

        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(result + "\n");
        } catch (IOException e) {
            logger.log(java.util.logging.Level.WARNING, "Could not write history", e);
        }

        loadHistory();
    }

    // load the history file to show it on screen
    private void loadHistory() {
        panel3.removeAll();

        File file = new File("src/historyFile");
        if (!file.exists()) {
            panel3.revalidate();
            panel3.repaint();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                JLabel label = new JLabel(line);
                label.setFont(new java.awt.Font("SimSun", 0, 14));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                panel3.add(label);
            }
        } catch (IOException e) {
            logger.log(java.util.logging.Level.WARNING, "Could not read history", e);
        }

        panel3.revalidate();
        panel3.repaint();
    }

    // panel to show the game history
    private void showHistory() {
        loadHistory();  
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "card6");
    }

    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        panel1 = new java.awt.Panel();
        jLabel1 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jLabel8 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jInternalFrame3 = new javax.swing.JInternalFrame();
        jLabel13 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        panel3 = new java.awt.Panel();
        jLabel30 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jInternalFrame5 = new javax.swing.JInternalFrame();
        panel2 = new java.awt.Panel();
        jLabel9 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jInternalFrame4 = new javax.swing.JInternalFrame();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        getContentPane().setLayout(null); 

        mainPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        mainPanel.setMinimumSize(new java.awt.Dimension(460, 390));
        mainPanel.setPreferredSize(new java.awt.Dimension(500, 350));
        mainPanel.setLayout(new java.awt.CardLayout());

        // ---------- card2: title screen ----------
        panel1.setBackground(new java.awt.Color(209, 209, 209));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabel1.setForeground(new java.awt.Color(51, 153, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("MATHQUIZ");

        jLabel14.setFont(new java.awt.Font("Sitka Text", 0, 12));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Get ready to test your math skills!");

        jButton1.setBackground(new java.awt.Color(135, 185, 236));
        jButton1.setFont(new java.awt.Font("SimSun", 1, 14));
        jButton1.setText("Start");

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(162, 162, 162))))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(jLabel14)
                .addGap(27, 27, 27)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainPanel.add(jPanel1, "card2");

        // ---------- card3: question screen ----------
        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Questions");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Time");

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("A.");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("B.");

        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("C.");

        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setText("D.");

        jButton2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12));
        jButton2.setText("Submit");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Please select a letter.");

        jButton8.setBackground(new java.awt.Color(255, 0, 0));
        jButton8.setText("Quit");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(jButton2)
                        .addGap(0, 149, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(62, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8)
                        .addContainerGap())))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jRadioButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8)
                        .addContainerGap())))
        );

        mainPanel.add(jPanel3, "card3");

        // ---------- card4: final score ----------
        jInternalFrame1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jInternalFrame1.setVisible(true);

        jLabel5.setFont(new java.awt.Font("Arial", 1, 18));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Quiz Finished!");

        jLabel6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Your Score: 24/25");

        jButton3.setBackground(new java.awt.Color(170, 220, 183));
        jButton3.setText("Play Again");

        jButton4.setBackground(new java.awt.Color(131, 189, 217));
        jButton4.setText("Back");

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jInternalFrame1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 51, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel5, "card4");

        // ---------- card5: main menu ----------
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("MATHQUIZ");

        jInternalFrame2.setVisible(true);

        jLabel8.setFont(new java.awt.Font("Georgia", 1, 18));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Main Menu");

        jButton5.setBackground(new java.awt.Color(204, 204, 204));
        jButton5.setFont(new java.awt.Font("SimSun", 0, 12));
        jButton5.setText("Start Game");

        jButton6.setBackground(new java.awt.Color(204, 204, 204));
        jButton6.setFont(new java.awt.Font("SimSun", 0, 12));
        jButton6.setText("View Scores");

        jButton7.setBackground(new java.awt.Color(204, 204, 204));
        jButton7.setFont(new java.awt.Font("SimSun", 0, 12));
        jButton7.setText("Exit");

        javax.swing.GroupLayout jInternalFrame2Layout = new javax.swing.GroupLayout(jInternalFrame2.getContentPane());
        jInternalFrame2.getContentPane().setLayout(jInternalFrame2Layout);
        jInternalFrame2Layout.setHorizontalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addGroup(jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame2Layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );
        jInternalFrame2Layout.setVerticalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame2Layout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addGap(0, 26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jInternalFrame2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jInternalFrame2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        mainPanel.add(jPanel2, "card5");

        // ---------- card7: correct / incorrect ----------
        jInternalFrame3.setForeground(new java.awt.Color(255, 255, 255));
        jInternalFrame3.setVisible(true);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Correct!");

        jLabel10.setFont(new java.awt.Font("SimSun", 0, 12));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Well Done!");

        javax.swing.GroupLayout jInternalFrame3Layout = new javax.swing.GroupLayout(jInternalFrame3.getContentPane());
        jInternalFrame3.getContentPane().setLayout(jInternalFrame3Layout);
        jInternalFrame3Layout.setHorizontalGroup(
            jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame3Layout.createSequentialGroup()
                .addGroup(jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jInternalFrame3Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jInternalFrame3Layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40))
        );
        jInternalFrame3Layout.setVerticalGroup(
            jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel10)
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(jInternalFrame3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jInternalFrame3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel4, "card7");

        // ---------- card6: history ----------
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));

        jPanel8.add(panel3);

        jScrollPane1.setViewportView(jPanel8);

        jLabel30.setFont(new java.awt.Font("SimSun", 1, 18));
        jLabel30.setText("QUIZ HISTORY");

        jButton9.setBackground(new java.awt.Color(158, 220, 241));
        jButton9.setText("Back");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainPanel.add(jPanel6, "card6"); 

        //--------- card8: quit confirmation ----------
        jInternalFrame5.setVisible(true);

        panel2.setBackground(new java.awt.Color(202, 57, 57));

        jLabel9.setFont(new java.awt.Font("Sitka Text", 1, 12));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Are you sure you want to quit?");

        jButton10.setFont(new java.awt.Font("Segoe UI Black", 0, 12));
        jButton10.setForeground(new java.awt.Color(255, 51, 51));
        jButton10.setText("Yes");

        jButton11.setFont(new java.awt.Font("Segoe UI Black", 0, 12));
        jButton11.setForeground(new java.awt.Color(51, 255, 51));
        jButton11.setText("No");

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton10)
                .addGap(31, 31, 31))
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(jButton11))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout jInternalFrame5Layout = new javax.swing.GroupLayout(jInternalFrame5.getContentPane());
        jInternalFrame5.getContentPane().setLayout(jInternalFrame5Layout);
        jInternalFrame5Layout.setHorizontalGroup(
            jInternalFrame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jInternalFrame5Layout.setVerticalGroup(
            jInternalFrame5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame5Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(jInternalFrame5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(81, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jInternalFrame5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel7, "card8");

        // ---------- card9: difficulty ----------
        jInternalFrame4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Choose Difficulty", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Semibold", 0, 12)));
        jInternalFrame4.setVisible(true);

        jButton12.setBackground(new java.awt.Color(204, 204, 204));
        jButton12.setFont(new java.awt.Font("SimSun", 1, 12));
        jButton12.setText("Easy");

        jButton13.setBackground(new java.awt.Color(204, 204, 204));
        jButton13.setFont(new java.awt.Font("SimSun", 1, 12));
        jButton13.setText("Medium");

        jButton14.setBackground(new java.awt.Color(204, 204, 204));
        jButton14.setFont(new java.awt.Font("SimSun", 1, 12));
        jButton14.setText("Hard");

        jButton15.setBackground(new java.awt.Color(158, 220, 241));
        jButton15.setText("Back");

        javax.swing.GroupLayout jInternalFrame4Layout = new javax.swing.GroupLayout(jInternalFrame4.getContentPane());
        jInternalFrame4.getContentPane().setLayout(jInternalFrame4Layout);
        jInternalFrame4Layout.setHorizontalGroup(
            jInternalFrame4Layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING
            )
            .addGroup(jInternalFrame4Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jInternalFrame4Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.CENTER
                )
                    .addComponent(jButton12, 88, 88, 88)
                    .addComponent(jButton13, 88, 88, 88)
                    .addComponent(jButton14, 88, 88, 88)
                )
                .addContainerGap(54, Short.MAX_VALUE)
            )
            .addGroup(jInternalFrame4Layout.createSequentialGroup()
                .addContainerGap(0, Short.MAX_VALUE)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 63,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()
            )
        );
        jInternalFrame4Layout.setVerticalGroup(
            jInternalFrame4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jButton12)
                .addGap(12, 12, 12)
                .addComponent(jButton13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton14)
                .addGap(20, 20, 20)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(104, Short.MAX_VALUE)
                .addComponent(jInternalFrame4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jInternalFrame4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel9, "card9");

        mainPanel.setBounds(20, 20, 400, 270);
        getContentPane().add(mainPanel);

        pack();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new NewJFrame().setVisible(true));
    }

    // Variables declaration
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButton15;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JInternalFrame jInternalFrame3;
    private javax.swing.JInternalFrame jInternalFrame4;
    private javax.swing.JInternalFrame jInternalFrame5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private java.awt.Panel panel1;
    private java.awt.Panel panel2;
    private java.awt.Panel panel3;
}