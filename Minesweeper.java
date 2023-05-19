import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MinesweeperGUI extends JFrame {
    private JPanel board;
    private JButton[][] squares;
    private int[][] values;
    private boolean[][] revealed;
    private int size = 10; // size of the board
    private int mines = 10; // number of mines
    private int flags = 0; // number of flagged squares
    private int revealedSquares = 0; // number of revealed squares
    private boolean gameOver = false; // flag for game over
    private JLabel statusLabel; // label for game status
   
    public MinesweeperGUI() {
        super("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board = new JPanel(new GridLayout(size, size));
        squares = new JButton[size][size];
        values = new int[size][size];
        revealed = new boolean[size][size];
        setupBoard();
        add(board, BorderLayout.CENTER);
        statusLabel = new JLabel("Mines: " + mines + ", Flags: " + flags);
        add(statusLabel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
   
    private void setupBoard() {
        // set up mines randomly
        int count = 0;
        while (count < mines) {
            int x = (int)(Math.random() * size);
            int y = (int)(Math.random() * size);
            if (values[x][y] != -1) {
                values[x][y] = -1;
                count++;
            }
        }
        // set up numbers for non-mine squares
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (values[i][j] != -1) {
                    int countMines = 0;
                    if (i > 0 && j > 0 && values[i-1][j-1] == -1) countMines++;
                    if (i > 0 && values[i-1][j] == -1) countMines++;
                    if (i > 0 && j < size-1 && values[i-1][j+1] == -1) countMines++;
                    if (j > 0 && values[i][j-1] == -1) countMines++;
                    if (j < size-1 && values[i][j+1] == -1) countMines++;
                    if (i < size-1 && j > 0 && values[i+1][j-1] == -1) countMines++;
                    if (i < size-1 && values[i+1][j] == -1) countMines++;
                    if (i < size-1 && j < size-1 && values[i+1][j+1] == -1) countMines++;
                    values[i][j] = countMines;
                }
            }
        }
        // add buttons to board
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                squares[i][j] = new JButton();
                squares[i][j].setPreferredSize(new Dimension(25, 25));
                int x = i;
                int y = j;
                squares[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (!gameOver && !revealed[x][y]) {
                             revealSquare(x, y);
                        checkWin();
                    }
                }
            });
            squares[i][j].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (!gameOver && !revealed[x][y]) {
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            flagSquare(x, y);
                            checkWin();
                        }
                    }
                }
            });
            board.add(squares[i][j]);
        }
    }
}

private void revealSquare(int x, int y) {
    revealed[x][y] = true;
    squares[x][y].setEnabled(false);
    revealedSquares++;
    if (values[x][y] == -1) {
        gameOver = true;
        squares[x][y].setText("*");
        JOptionPane.showMessageDialog(this, "Game Over!");
    } else if (values[x][y] == 0) {
        squares[x][y].setText("");
        if (x > 0 && y > 0 && !revealed[x-1][y-1]) revealSquare(x-1, y-1);
        if (x > 0 && !revealed[x-1][y]) revealSquare(x-1, y);
        if (x > 0 && y < size-1 && !revealed[x-1][y+1]) revealSquare(x-1, y+1);
        if (y > 0 && !revealed[x][y-1]) revealSquare(x, y-1);
        if (y < size-1 && !revealed[x][y+1]) revealSquare(x, y+1);
        if (x < size-1 && y > 0 && !revealed[x+1][y-1]) revealSquare(x+1, y-1);
        if (x < size-1 && !revealed[x+1][y]) revealSquare(x+1, y);
        if (x < size-1 && y < size-1 && !revealed[x+1][y+1]) revealSquare(x+1, y+1);
    } else {
        squares[x][y].setText(String.valueOf(values[x][y]));
    }
}

private void flagSquare(int x, int y) {
    if (squares[x][y].getText().equals("")) {
        squares[x][y].setText("F");
        flags++;
    } else if (squares[x][y].getText().equals("F")) {
        squares[x][y].setText("");
        flags--;
    }
    statusLabel.setText("Mines: " + mines + ", Flags: " + flags);
}

private void checkWin() {
    if (revealedSquares == size * size - mines) {
        gameOver = true;
        JOptionPane.showMessageDialog(this, "You win!");
    }
}

public static void main(String[] args) {
    new MinesweeperGUI();
}
}
