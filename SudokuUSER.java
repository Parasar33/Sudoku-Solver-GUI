import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class SudokuUSER extends JFrame {

    private static final int SIZE = 9;
    private static  int count = 0;
    private int[][] userGrid = new int[SIZE][SIZE];
    private int[][] selected = new int[SIZE][SIZE];

    private JTextField[][] fields = new JTextField[SIZE][SIZE];

    public SudokuUSER() {
        setTitle("TRY A SUDOKU");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Creating the text fields for input
        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                fields[i][j] = new JTextField(1);
                fields[i][j].setHorizontalAlignment(JTextField.CENTER);
                fields[i][j].setFont(font);
                gridPanel.add(fields[i][j]);
                // Add border to separate 3x3 squares
                int top = (i % 3 == 0) ? 2 : 0;
                int left = (j % 3 == 0) ? 2 : 0;
                int bottom = (i % 3 == 2) ? 2 : 1;
                int right = (j % 3 == 2) ? 2 : 1;
                fields[i][j].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
            }
        }

        add(gridPanel, BorderLayout.CENTER);
        // Add sample puzzle to the grid
        RandomAdd();
        // Creating the solve button
        JButton solveButton = new JButton("Solve");
        solveButton.setFont(font);
        solveButton.addActionListener(e -> {
            int[][] grid = getUserInput(); // updated to use the new getUserInput method
            if (solveSudoku(grid)) {
                JOptionPane.showMessageDialog(this, "Solution found.");
                updateFields(grid); // updated to use the new updateFields method
            } else {
                JOptionPane.showMessageDialog(this, "No solution found.");
            }
        });

        // Creating the submit button
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(font);
        submitButton.addActionListener(e -> {
            userGrid = getUserInput(); // updated to store user input
        });

        // Creating the validate button
        JButton validateButton = new JButton("Validate");
        validateButton.setFont(font);
        validateButton.addActionListener(e -> {
            boolean isValid = isValidSolution(userGrid); // added method to check user solution
            if (isValid) {
                JOptionPane.showMessageDialog(this, "Congratulations! You solved the puzzle.");
            } else {
                JOptionPane.showMessageDialog(this, "Sorry, your solution is incorrect.");
            }
        });

        // Adding the buttons to the panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(solveButton);
        buttonPanel.add(submitButton); // added submit button
        buttonPanel.add(validateButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Creating the reset button
        JButton resetButton = new JButton("Reset Question");
        resetButton.setFont(font);
        resetButton.addActionListener(e -> {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    fields[i][j].setText("");
                }
            }
            // Add sample puzzle to the grid
            addSamplePuzzle();
        });
        // Creating the getting question button
//        JButton giveQuestion = new JButton("Try Question");
//        resetButton.setFont(font);
//        resetButton.addActionListener(e -> {
//            for (int i = 0; i < SIZE; i++) {
//                for (int j = 0; j < SIZE; j++) {
//                    fields[i][j].setText("");
//                }
//            }
//            // Add sample puzzle to the grid
//            RandomAdd();
//        });
//        add(resetButton, BorderLayout.NORTH);

        // Creating the reset button for user question

        JPanel button2Panel = new JPanel(new FlowLayout());
        button2Panel.add(resetButton);
//        button2Panel.add(giveQuestion);
        add(button2Panel, BorderLayout.NORTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private static boolean solveSudoku(int[][] grid) {
        int row = -1;
        int col = -1;
        boolean isEmpty = true;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) {
                    row = i;
                    col = j;
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) {
                break;
            }
        }
        if (isEmpty) {
            return true;
        }
        for (int digit = 1; digit <= SIZE; digit++) {
            if (isValid(grid, row, col, digit)) {
                grid[row][col] = digit;
                if (solveSudoku(grid)) {
                    return true;
                } else {
                    grid[row][col] = 0;
                }
            }
        }
        return false;
    }

    private static boolean isValid(int[][] grid, int row, int col, int digit) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == digit) {
                return false;
            }
            if (grid[i][col] == digit) {
                return false;
            }
            int subRow = (row / 3) * 3 + i / 3;
            int subCol = (col / 3) * 3 + i % 3;
            if (grid[subRow][subCol] == digit) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidSolution(int[][] grid) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int digit = grid[i][j];
                if (digit == 0) {
                    return false;
                }
                grid[i][j] = 0;
                if (!isValid(grid, i, j, digit)) {
                    return false;
                }
                grid[i][j] = digit;
            }
        }
        return true;
    }
    private int[][] getUserInput() {
        int[][] grid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                try {
                    grid[i][j] = Integer.parseInt(fields[i][j].getText());
                } catch (NumberFormatException ex) {
                    grid[i][j] = 0;
                }
            }
        }
        return grid;
    }
    // Method to update text fields with values from 2D array
    private void updateFields(int[][] grid) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                fields[i][j].setText(String.valueOf(grid[i][j]));
            }
        }
    }
    private void addSamplePuzzle() {

        // Fill in the text fields with the puzzle values
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int value = selected[i][j];
                if (value != 0) {
                    fields[i][j].setText(Integer.toString(value));
                    fields[i][j].setEditable(false);
                }
            }
        }
    }
    private void RandomAdd()
    {
        //declaring puzzles
        int[][] puzzle1 =
                {
                        {0, 8, 0, 0, 0, 0, 0, 0, 9},
                        {0, 0, 0, 4, 0, 0, 3, 0, 0},
                        {0, 1, 0, 0, 3, 0, 0, 0, 0},
                        {2, 0, 0, 0, 0, 0, 7, 0, 0},
                        {0, 6, 0, 0, 0, 0, 0, 0, 1},
                        {0, 0, 0, 0, 0, 1, 0, 0, 5},
                        {0, 0, 5, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 8, 0, 0, 0},
                        {0, 0, 9, 5, 0, 0, 0, 0, 0}
                };
        int[][] puzzle2 = {
                {0, 0, 9, 0, 4, 0, 0, 0, 1},
                {0, 0, 3, 0, 0, 0, 6, 0, 0},
                {0, 0, 0, 0, 0, 9, 0, 2, 0},
                {0, 0, 0, 0, 0, 3, 0, 0, 0},
                {0, 2, 0, 7, 0, 0, 1, 0, 0},
                {0, 5, 0, 0, 0, 0, 0, 7, 8},
                {9, 0, 0, 0, 0, 0, 3, 4, 0},
                {0, 0, 0, 2, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 8, 0, 0, 0, 0}
        };
        int[][] puzzle3 = {
                {0, 0, 0, 2, 0, 0, 0, 0, 0},
                {0, 0, 0, 6, 0, 0, 0, 0, 3},
                {0, 0, 0, 0, 0, 0, 0, 5, 9},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {5, 0, 0, 0, 0, 0, 0, 7, 0},
                {0, 0, 0, 0, 4, 0, 0, 0, 0},
                {0, 0, 0, 0, 8, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 4, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        int[][] puzzle4 = {
                {0, 0, 5, 3, 0, 0, 0, 0, 0},
                {8, 0, 0, 0, 0, 0, 0, 2, 0},
                {0, 7, 0, 0, 1, 0, 5, 0, 0},
                {4, 0, 0, 0, 0, 5, 3, 0, 0},
                {0, 1, 0, 0, 7, 0, 0, 0, 6},
                {0, 0, 3, 2, 0, 0, 0, 8, 0},
                {0, 6, 0, 5, 0, 0, 0, 0, 9},
                {0, 0, 4, 0, 0, 0, 0, 3, 0},
                {0, 0, 0, 0, 0, 9, 7, 0, 0}
        };

        int[][] puzzle5 = {
                {0, 0, 6, 1, 0, 0, 0, 0, 4},
                {0, 0, 0, 0, 0, 9, 0, 8, 0},
                {0, 0, 0, 0, 4, 0, 0, 0, 0},
                {4, 0, 9, 0, 0, 8, 0, 0, 6},
                {0, 0, 0, 9, 0, 0, 0, 0, 0},
                {0, 5, 0, 0, 0, 0, 0, 2, 1},
                {0, 7, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 5, 1, 0},
                {5, 0, 0, 0, 0, 0, 8, 0, 0}
        };
        ArrayList<int[][]> puzzles = new ArrayList<>();
        puzzles.add(puzzle1);
        puzzles.add(puzzle2);
        puzzles.add(puzzle3);
        puzzles.add(puzzle4);
        puzzles.add(puzzle5);

// Select a random puzzle from the list
        int index = new Random().nextInt(puzzles.size());
        selected = puzzles.get(index);

// Fill in the text fields with the puzzle values
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int value = selected[i][j];
                if (value != 0) {
                    fields[i][j].setText(Integer.toString(value));
                    fields[i][j].setEditable(false);
                }
            }
        }
    }


    public static void main(String[] args) {
        int option = Integer.parseInt(JOptionPane.showInputDialog("Choose one of the operations to proceed\n1.Solve A Question\n2.Get A Solution "));
        switch (option)
        {
            case 1 -> {SwingUtilities.invokeLater(SudokuUSER::new);}
            case 2 -> {SwingUtilities.invokeLater(SudokuGUI::new);}
        }
    }
}

// executing the original class now,

class SudokuGUI extends JFrame {

    private static final int SIZE = 9;

    private JTextField[][] fields = new JTextField[SIZE][SIZE];

    public SudokuGUI() {
        setTitle("GET SOLUTION");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Creating the text fields for input
        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                fields[i][j] = new JTextField(1);
                fields[i][j].setHorizontalAlignment(JTextField.CENTER);
                fields[i][j].setFont(font);
                // Add border to separate 3x3 squares
                int top = (i % 3 == 0) ? 2 : 0;
                int left = (j % 3 == 0) ? 2 : 0;
                int bottom = (i % 3 == 2) ? 2 : 1;
                int right = (j % 3 == 2) ? 2 : 1;
                fields[i][j].setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
                gridPanel.add(fields[i][j]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        // Creating the solve button
        JButton solveButton = new JButton("Solve");
        solveButton.setFont(font);
        solveButton.addActionListener(e -> {
            int[][] grid = new int[SIZE][SIZE];
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    try {
                        grid[i][j] = Integer.parseInt(fields[i][j].getText());
                    } catch (NumberFormatException ex) {
                        grid[i][j] = 0;
                    }
                }
            }
            if (solveSudoku(grid)) {
                JOptionPane.showMessageDialog(this, "Solution found.");
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        fields[i][j].setText(String.valueOf(grid[i][j]));
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No solution found.");
            }
        });

        // Creating the reset button
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(font);
        resetButton.addActionListener(e -> {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    fields[i][j].setText("");
                }
            }
        });
        // Creating the feedback button
        JButton feedbackButton = new JButton("Rate Us");
        feedbackButton.setFont(font);
        feedbackButton.addActionListener(e -> {
            RateUs();
        });
        JPanel button3Panel = new JPanel(new FlowLayout());
        button3Panel.add(resetButton);
        button3Panel.add(solveButton);
        button3Panel.add(feedbackButton);
        add(button3Panel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);


    }

    private static boolean solveSudoku(int[][] grid) {
        int row = -1;
        int col = -1;
        boolean isEmpty = true;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) {
                    row = i;
                    col = j;
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty) {
                break;
            }
        }
        if (isEmpty) {
            return true;
        }
        for (int digit = 1; digit <= SIZE; digit++) {
            if (isValid(grid, row, col, digit)) {
                grid[row][col] = digit;
                if (solveSudoku(grid)) {
                    return true;
                } else {
                    grid[row][col] = 0;
                }
            }
        }
        return false;
    }
    private static boolean isValid(int[][] grid, int row, int col, int digit) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == digit) {
                return false;
            }
            if (grid[i][col] == digit) {
                return false;
            }
            int subRow = (row / 3) * 3 + i / 3;
            int subCol = (col / 3) * 3 + i % 3;
            if (grid[subRow][subCol] == digit) {
                return false;
            }
        }
        return true;
    }

    private void RateUs()
    {
        Integer.parseInt(JOptionPane.showInputDialog("Rate our model out of 10"));
        JOptionPane.showMessageDialog(this, "thanks for voting :>} ");
        System.exit(0);
    }
}
