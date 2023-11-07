import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class ColorPaletteRandomizer extends JFrame {
    private Color baseColor = null;
    private JPanel colorDisplayPanel;
    private JButton monoButton, anaButton, compButton, triadButton;
    private Color selectedColor;
    private boolean colorLocked = false;

    public ColorPaletteRandomizer() {
        setTitle("Color Palette Randomizer");
        setSize(800, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        colorDisplayPanel = new JPanel();
        colorDisplayPanel.setLayout(new GridLayout(1, 5)); // To display five colors
        add(colorDisplayPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        monoButton = new JButton("Monochromatic");
        anaButton = new JButton("Analogous");
        compButton = new JButton("Complementary");
        triadButton = new JButton("Triadic");
        JButton colorWheelButton = new JButton("Choose Color");
        JToggleButton lockColorButton = new JToggleButton("Lock Color");

        // Add action listeners to buttons
        monoButton.addActionListener((ActionEvent e) -> generateMonochromaticPalette());
        anaButton.addActionListener((ActionEvent e) -> generateAnalogousPalette());
        compButton.addActionListener((ActionEvent e) -> generateComplementaryPalette());
        triadButton.addActionListener((ActionEvent e) -> generateTriadicPalette());
        lockColorButton.addActionListener((ActionEvent e) -> toggleLock());

        // Add buttons to the button panel
        buttonPanel.add(monoButton);
        buttonPanel.add(anaButton);
        buttonPanel.add(compButton);
        buttonPanel.add(triadButton);
        buttonPanel.add(colorWheelButton);
        buttonPanel.add(lockColorButton);

        // Add the button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
        
        colorWheelButton.addActionListener(e -> {
            selectedColor = JColorChooser.showDialog(ColorPaletteRandomizer.this, "Choose a base color", Color.white);
            if (selectedColor != null) {
                baseColor = selectedColor;
            }
        });
    }



    private void generateMonochromaticPalette() {
        generatePalette(PaletteType.MONOCHROMATIC);
    }

    private void generateAnalogousPalette() {
        generatePalette(PaletteType.ANALOGOUS);
    }

    private void generateComplementaryPalette() {
        generatePalette(PaletteType.COMPLEMENTARY);
    }

    private void generateTriadicPalette() {
        generatePalette(PaletteType.TRIADIC);
    }

    private void toggleLock() {
        if (colorLocked == true) {
            colorLocked = false;
        } 
        else {
            colorLocked = true;
        }
    }

    private void generatePalette(PaletteType type) {
        colorDisplayPanel.removeAll();

        float hue;
        float baseSaturation;
        float baseBrightness;

        if (baseColor !=null) {
            float[] hsbValues = Color.RGBtoHSB(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), null);
            hue = hsbValues[0];
            baseSaturation = hsbValues[1];
            baseBrightness = hsbValues[2];
        } else {
            Random rand = new Random();
            hue = rand.nextFloat();
            baseSaturation = 0.5f + rand.nextFloat() * 0.5f; // More vivid colors
            baseBrightness = 0.7f + rand.nextFloat() * 0.3f; // Not too dark or too bright
            baseColor = Color.getHSBColor(hue, baseSaturation, baseBrightness);
        }
                
        Color[] palette = new Color[5];
        palette[0] = baseColor; // The first color is always the base color

        // Generate the palette based on the type
        switch (type) {
            case MONOCHROMATIC:
                palette[1] = Color.getHSBColor(hue, Math.max(baseSaturation * 0.7f, 0.1f), Math.min(baseBrightness * 1.3f, 1.0f)); // Decrease saturation increase brightness
                palette[2] = Color.getHSBColor(hue, Math.min(baseSaturation * 1.3f, 1.0f),  Math.max(baseBrightness * 0.7f, 0.1f)); // Increase saturation decrease brightness
                palette[3] = Color.getHSBColor(hue, Math.max(baseSaturation * 0.7f, 0.1f), Math.max(baseBrightness * 0.7f, 0.1f)); // Decrease saturation decrease brightness
                palette[4] = Color.getHSBColor(hue, baseSaturation, Math.max(baseBrightness * 0.2f, 0.1f)); // Base saturation decreased brightness
                break;
            case ANALOGOUS:
                for (int i = 1; i < palette.length; i++) {
                    float newHue = (hue + (i * 1/12f)) % 1.0f;
                    palette[i] = Color.getHSBColor(newHue, baseSaturation, baseBrightness);
                }
                break;
            case COMPLEMENTARY:
                float complementaryHue = (hue + 0.5f) % 1.0f;
                palette[1] = Color.getHSBColor(hue, Math.max(baseSaturation * 0.7f, 0.1f), baseBrightness);
                palette[2] = Color.getHSBColor(hue, baseSaturation, Math.max(baseBrightness * 0.7f, 0.1f));
                palette[3] = Color.getHSBColor(hue, Math.max(baseSaturation * 0.5f, 0.1f), Math.max(baseBrightness * 0.5f, 0.1f));
                palette[4] = Color.getHSBColor(complementaryHue, baseSaturation, baseBrightness);
                palette[4] = Color.getHSBColor(complementaryHue, Math.max(baseSaturation * 0.5f, 0.1f), Math.min(baseBrightness * 1.4f, 1.0f));
                break;
            case TRIADIC:
                float triad1Hue = (hue + 1/3f) % 1.0f;
                float triad2Hue = (hue + 2/3f) % 1.0f;
                palette[1] = Color.getHSBColor(triad1Hue, baseSaturation, baseBrightness);
                palette[2] = Color.getHSBColor(triad1Hue, baseSaturation * 0.5f, baseBrightness * 0.8f);
                palette[3] = Color.getHSBColor(triad2Hue, baseSaturation, baseBrightness);
                palette[4] = Color.getHSBColor(triad2Hue, baseSaturation * 0.5f, baseBrightness * 0.8f);
                break;
        }

        // Add colors to the display panel
        for (Color color : palette) {
            colorDisplayPanel.add(createColorPanel(color));
        }

        // Update the display
        colorDisplayPanel.revalidate();
        colorDisplayPanel.repaint();


        if (colorLocked == false) {
            baseColor = null;
        }
        
    }

    private JPanel createColorPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        return panel;
    }

    // Define palette types
    private enum PaletteType {
        MONOCHROMATIC,
        ANALOGOUS,
        COMPLEMENTARY,
        TRIADIC
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ColorPaletteRandomizer::new);
    }
}