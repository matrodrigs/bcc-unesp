import javax.swing.*;

class DVDButton extends JFrame {
    JButton button = new JButton("DVD");
    int xSpeed = 5;
    int ySpeed = 5;

    DVDButton() {
        setLayout(null);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        button.setSize(80, 50);
        button.setLocation(250, 250);
        add(button);

        Timer timer = new Timer(30, e -> {
            int x = button.getX();
            int y = button.getY();
            int currentWidhtSize = getContentPane().getWidth();
            int currentHeightSize = getContentPane().getHeight();

            if (x + button.getWidth() > currentWidhtSize || x < 0) {
                xSpeed *= -1;
            }

            if (y + button.getHeight() > currentHeightSize || y < 0) {
                ySpeed *= -1;
            }

            button.setLocation(x + xSpeed, y + ySpeed);
        });

        setVisible(true);
        timer.start();
    }

    static public void main(String[] args) {
        new DVDButton();
    }
}