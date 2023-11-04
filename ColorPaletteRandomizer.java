import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ColorPaletteRandomizer extends JFrame {
    private JPanel colorDisplayPanel;
    private JButton generateButton;

    public ColorPaletteRandomizer() {
        setTitle("Color Palette Randomizer");
        setSize(800, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        colorDisplayPanel = new JPanel();
        colorDisplayPanel.setLayout(new GridLayout(1, 5)); // To display five colors
        add(colorDisplayPanel, BorderLayout.CENTER);

        generateButton = new JButton("Generate Palette");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateRandomPalette();
            }
        });
        add(generateButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void generateRandomPalette() {
        colorDisplayPanel.removeAll();

        // Generate a random base color
        Random rand = new Random();
        float hue = rand.nextFloat();
        float saturation = 0.5f + rand.nextFloat() * 0.5f; // more vivid colors
        float brightness = 0.7f + rand.nextFloat() * 0.3f; // not too dark or too bright

        Color baseColor = Color.getHSBColor(hue, saturation, brightness);

        // Add the base color
        colorDisplayPanel.add(createColorPanel(baseColor));

        // Generate complementary color
        float complementaryHue = (hue + 0.5f) % 1.0f;
        Color complementaryColor = Color.getHSBColor(complementaryHue, saturation, brightness);
        colorDisplayPanel.add(createColorPanel(complementaryColor));

        // Generate analogous colors (30 degrees apart)
        float analogousHue1 = (hue + 1/12f) % 1.0f;
        float analogousHue2 = (hue - 1/12f + 1.0f) % 1.0f; // Add 1.0f to ensure positive value
        Color analogousColor1 = Color.getHSBColor(analogousHue1, saturation, brightness);
        Color analogousColor2 = Color.getHSBColor(analogousHue2, saturation, brightness);
        colorDisplayPanel.add(createColorPanel(analogousColor1));
        colorDisplayPanel.add(createColorPanel(analogousColor2));

        // Update the display
        colorDisplayPanel.revalidate();
        colorDisplayPanel.repaint();
    }

    private JPanel createColorPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ColorPaletteRandomizer();
            }
        });
    }
}