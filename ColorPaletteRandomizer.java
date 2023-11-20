import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
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
        colorDisplayPanel.setLayout(new GridLayout(1, 5)); 
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

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    // Method to copy text to clipboard
    private void CopyToClipboard(JTextField textField) {
        StringSelection stringSelection = new StringSelection(textField.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void generatePalette(PaletteType type) {
        if (colorLocked == false) {
            baseColor = null;
        }
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
            baseSaturation = 0.5f + rand.nextFloat() * 0.5f; 
            baseBrightness = 0.7f + rand.nextFloat() * 0.3f; 
            baseColor = Color.getHSBColor(hue, baseSaturation, baseBrightness);
        }
                
        Color[] palette = new Color[5];
        // The first color is always the base color
        palette[0] = baseColor; 
        // Generate the palette based on the type

        switch (type) {
            case MONOCHROMATIC:
                palette[1] = Color.getHSBColor(hue, Math.max(baseSaturation * 0.7f, 0.1f), Math.min(baseBrightness * 1.3f, 1.0f)); // Decrease saturation increase brightness
                palette[2] = Color.getHSBColor(hue, Math.min(baseSaturation * 1.3f, 1.0f),  Math.max(baseBrightness * 0.7f, 0.1f)); // Increase saturation decrease brightness
                palette[3] = Color.getHSBColor(hue, Math.max(baseSaturation * 0.7f, 0.1f), Math.max(baseBrightness * 0.7f, 0.1f)); // Decrease saturation decrease brightness
                palette[4] = Color.getHSBColor(hue, baseSaturation, Math.max(baseBrightness * 0.2f, 0.1f)); // Base saturation decreased brightness
                break;
            case ANALOGOUS:
                palette[0] = Color.getHSBColor((hue + (2 * -1/12f)) % 1.0f, baseSaturation, baseBrightness);
                palette[1] = Color.getHSBColor((hue + (1 * -1/12f)) % 1.0f, baseSaturation, baseBrightness);
                palette[2] = baseColor; 
                palette[3] = Color.getHSBColor((hue + (1 * 1/12f)) % 1.0f, baseSaturation, baseBrightness);
                palette[4] = Color.getHSBColor((hue + (2 * 1/12f)) % 1.0f, baseSaturation, baseBrightness);
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

        // Loop through the palette and create panels for each color
        for (int i = 0; i < palette.length; i++) {
            // Create a new panel for this color with a BoxLayout for vertical alignment
            JPanel colorPanel = new JPanel();
            colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.Y_AXIS));

            // Create the color display (a simple JPanel with a background color)
            JPanel colorSwatch = new JPanel();
            colorSwatch.setBackground(palette[i]);
            colorSwatch.setPreferredSize(new Dimension(50, 500)); // Or whatever size you want

            // Create the JTextField for the hex code
            JTextField hexField = new JTextField(toHexString(palette[i]));
            hexField.setEditable(false);
            hexField.setFocusable(false);
            hexField.setBorder(BorderFactory.createEmptyBorder());
            hexField.setHorizontalAlignment(JTextField.CENTER); // Center the text

            // Add the mouse listener to select the color code on click
            hexField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JTextField source = (JTextField) e.getSource();
                    String originalText = source.getText();
                    source.selectAll();
                    CopyToClipboard(source);
                    source.setText("Copied!");
                    new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            source.setText(originalText);
                        }
                    }) {{
                        setRepeats(false);
                        start();
                    }};
                }
            });

            // Add the color swatch and hex field to the color panel
            colorPanel.add(colorSwatch);
            colorPanel.add(hexField);

            // Add the color panel to the main display panel
            colorDisplayPanel.add(colorPanel);
        }

        // Update the display
        colorDisplayPanel.revalidate();
        colorDisplayPanel.repaint();
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