import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class EscapeRoomArray2D {

    private JFrame frame;
    private JPanel gamePanel;
    private JLabel[][] gridLabels;
    private int[][] roomGrid;
    private int playerX = 0;
    private int playerY = 0;
    private boolean hasKey = false;
    private boolean hasInteractedWithComputer = false;

    private final int EMPTY = 0;
    private final int PLAYER = 1;
    private final int COMPUTER = 2;
    private final int CABINET = 3;
    private final int DOOR = 4;

    private ImageIcon userIcon;
    private ImageIcon computerIcon;
    private ImageIcon cabinetIcon;
    private ImageIcon doorIcon;
    private ImageIcon floorIcon;

    public EscapeRoomArray2D() {
        // Load images
        userIcon = new ImageIcon("C:\\Users\\MSI MODERN\\Downloads/user.jpeg");
        computerIcon = new ImageIcon("C:\\Users\\MSI MODERN\\Downloads/komputer.jpeg");
        cabinetIcon = new ImageIcon("C:\\Users\\MSI MODERN\\Downloads/lemari.jpeg");
        doorIcon = new ImageIcon("C:\\Users\\MSI MODERN\\Downloads/pintu.jpeg");
        floorIcon = new ImageIcon("C:\\Users\\MSI MODERN\\Downloads/lantai.jpeg");

        frame = new JFrame("Escape Room Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        playSound("C:\\Users\\MSI MODERN\\Downloads/start.wav"); // Play start sound
        showStartScreen();
    }

    private void playSound(String soundFile) {
        try {
            File file = new File(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showStartScreen() {
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());
        startPanel.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Welcome To Escape Room Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 35));
        titleLabel.setForeground(Color.WHITE);
        startPanel.add(titleLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.PLAIN, 18));
        startButton.setForeground(new Color(139, 69, 19)); // Brown color
        startButton.setBackground(Color.WHITE);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSound("C:\\Users\\MSI MODERN\\Downloads/click.mav"); // Play click sound
                frame.getContentPane().removeAll();
                frame.repaint();
                initializeGame();
            }
        });
        startPanel.add(startButton, BorderLayout.SOUTH);

        frame.add(startPanel);
        frame.setVisible(true);
    }

    private void initializeGame() {
        roomGrid = new int[][]{
                {1, 0, 0, 0, 3},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {2, 0, 0, 0, 4}
        };

        gamePanel = new JPanel(new GridLayout(roomGrid.length, roomGrid[0].length));
        gridLabels = new JLabel[roomGrid.length][roomGrid[0].length];
        updateGamePanel();

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W: // Move Up
                        playSound("C:\\Users\\MSI MODERN\\Downloads/move.wav");
                        movePlayer(-1, 0);
                        break;
                    case KeyEvent.VK_S: // Move Down
                        playSound("C:\\Users\\MSI MODERN\\Downloads/move.wav");
                        movePlayer(1, 0);
                        break;
                    case KeyEvent.VK_A: // Move Left
                        playSound("C:\\Users\\MSI MODERN\\Downloads/move.wav");
                        movePlayer(0, -1);
                        break;
                    case KeyEvent.VK_D: // Move Right
                        playSound("C:\\Users\\MSI MODERN\\Downloads/move.wav");
                        movePlayer(0, 1);
                        break;
                }
            }
        });

        frame.add(gamePanel, BorderLayout.CENTER);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.revalidate();
        frame.repaint();
    }

    private void updateGamePanel() {
        gamePanel.removeAll();
        for (int i = 0; i < roomGrid.length; i++) {
            for (int j = 0; j < roomGrid[i].length; j++) {
                JLabel label = new JLabel();
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.setOpaque(true);

                switch (roomGrid[i][j]) {
                    case EMPTY:
                        label.setIcon(floorIcon);
                        label.setText("");
                        break;
                    case PLAYER:
                        label.setIcon(userIcon);
                        label.setText("");
                        break;
                    case COMPUTER:
                        label.setIcon(computerIcon);
                        label.setText("");
                        break;
                    case CABINET:
                        label.setIcon(cabinetIcon);
                        label.setText("");
                        break;
                    case DOOR:
                        label.setIcon(doorIcon);
                        label.setText("");
                        break;
                }

                gridLabels[i][j] = label;
                gamePanel.add(label);
            }
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    private void movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;

        if (newX >= 0 && newX < roomGrid.length && newY >= 0 && newY < roomGrid[0].length) {
            if (roomGrid[newX][newY] == EMPTY) {
                roomGrid[playerX][playerY] = EMPTY;
                playerX = newX;
                playerY = newY;
                roomGrid[playerX][playerY] = PLAYER;
            } else if (roomGrid[newX][newY] == COMPUTER) {
                playSound("C:\\Users\\MSI MODERN\\Downloads/nformation.wav"); // Play computer sound
                JOptionPane.showMessageDialog(frame, "Anda berinteraksi dengan komputer. Ini mengisyaratkan: 'Kuncinya ada di lemari.'");
                hasInteractedWithComputer = true;
            } else if (roomGrid[newX][newY] == CABINET) {
                if (hasInteractedWithComputer) {
                    playSound("C:\\Users\\MSI MODERN\\Downloads/information.wav"); // Play key found sound
                    JOptionPane.showMessageDialog(frame, "Anda menemukan kunci di dalam lemari!");
                    hasKey = true;
                } else {
                    playSound("C:\\Users\\MSI MODERN\\Downloads/nformation.wav"); // Play error sound
                    JOptionPane.showMessageDialog(frame, "Anda perlu petunjuk terlebih dahulu. Periksa komputer.");
                }
            } else if (roomGrid[newX][newY] == DOOR) {
                if (hasKey) {
                    playSound("C:\\Users\\MSI MODERN\\Downloads/nformation.wav"); // Play door open sound
                    presentRandomPuzzle();
                } else {
                    playSound("C:\\Users\\MSI MODERN\\Downloads/nformation.wav"); // Play locked door sound
                    JOptionPane.showMessageDialog(frame, "Pintunya terkunci. Anda membutuhkan kunci.");
                }
            }
        }

        updateGamePanel();
    }

    private void presentRandomPuzzle() {
        Random random = new Random();
        int num1 = random.nextInt(10) + 1; // Random number between 1 and 10
        int num2 = random.nextInt(10) + 1; // Random number between 1 and 10
        int correctAnswer = num1 * num2;

        String question = "Teka-teki: Berapa hasil dari " + num1 + " x " + num2 + "?";
        String answer = JOptionPane.showInputDialog(frame, question);

        if (answer != null && answer.matches("\\d+") && Integer.parseInt(answer) == correctAnswer) {
            playSound("C:\\Users\\MSI MODERN\\Downloads/win.wav"); // Play win sound
            JOptionPane.showMessageDialog(frame, "Selamat Anda Menang!");
            System.exit(0);
        } else {
            playSound("C:\\Users\\MSI MODERN\\Downloads/wrong.wav"); // Play wrong answer sound
            JOptionPane.showMessageDialog(frame, "Jawaban salah. Coba lagi.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EscapeRoomArray2D::new);
    }
}