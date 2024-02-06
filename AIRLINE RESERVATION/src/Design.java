import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Design {

    public static JLabel createLabel(String name, Color color, int size, int style, String FontType) {
        JLabel label = new JLabel(name);
        Font font = new Font(FontType,style,size);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    public static JLabel createLabel(String name,Color color, Font font) {
        JLabel label = new JLabel(name);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    public static JLabel createLabel(String name, Font font) {
        JLabel label = new JLabel(name);
        label.setFont(font);
        return label;
    }

    public static JButton createButton(String name, Color color, int size, int style, String FontType){
        JButton button = new JButton(name);
        button.setForeground(color);
        button.setFont(new Font(FontType,style,size));
        return button;
    }

    public static JButton createButton(String path, int width, int height){
        JButton button = new JButton();
        ImageIcon imageIcon = new ImageIcon(path);
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH));
        button.setIcon(imageIcon);
        button.setFocusable(false);
        return button;
    }

    public static JButton createButton(String path, String text, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(path);
        imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH));
        JButton button = new JButton(text, imageIcon);
        button.setFocusable(false);
        button.setFont(new Font("Agency FB",Font.BOLD,20));
        button.setForeground(new Color(0x030f5b));
        button.setVerticalTextPosition(AbstractButton.BOTTOM);
        button.setHorizontalTextPosition(AbstractButton.CENTER);
        return button;
    }

        public static JButton createButton(String name, Color color, int size, int style, String FontType, boolean focusable, Border border){
        JButton button = new JButton(name);
        button.setForeground(color);
        button.setFont(new Font(FontType,style,size));
        button.setFocusable(focusable);
        button.setBorder(border);
        return button;
    }

    public static JButton createButton(String name, Color color, Font font, boolean focusable, Border border){
        JButton button = new JButton(name);
        button.setForeground(color);
        button.setFont(font);
        button.setFocusable(focusable);
        button.setBorder(border);
        return button;
    }

    public static JCheckBox createCheckBox(String name, Color color, int size, int style, String FontType){
        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setFont(new Font(FontType,style,size));
        checkBox.setForeground(color);
        return checkBox;
    }

    public static JCheckBox createCheckBox(String name, Color color, int size, int style, String FontType, boolean focusable, Color bg){
        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setFont(new Font(FontType,style,size));
        checkBox.setForeground(color);
        checkBox.setFocusable(focusable);
        checkBox.setBackground(bg);
        return checkBox;
    }

    public static JTextField createTextField(Color color, int size, int style, String FontType){
        JTextField textField = new JTextField();
        textField.setFont(new Font(FontType,style,size));
        textField.setForeground(color);
        return textField;
    }

    public static JTextField createTextField(Color color, int size, int style, String FontType,boolean editable){
        JTextField textField = new JTextField();
        textField.setFont(new Font(FontType,style,size));
        textField.setForeground(color);
        textField.setEditable(editable);
        return textField;
    }

    public static JTextField createTextField(Color color, Font font){
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setForeground(color);
        return textField;
    }

    public static JTextField createTextField(Color color, Font font,boolean editable){
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setForeground(color);
        textField.setEditable(editable);
        return textField;
    }

    public static JTextField createTextField( Font font,boolean editable){
        JTextField textField = new JTextField();
        textField.setFont(font);
        textField.setEditable(editable);
        return textField;
    }

    public static JPasswordField createPasswordField(Color color, Font font){
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(font);
        passwordField.setForeground(color);
        return passwordField;
    }

    public static JPanel createImageBackground(String path){
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage image = ImageIO.read(new File(path)); // Replace this with your image path
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        background.setLayout(null);
        return background;
    }

    public static JPanel createPanel(Color color, Border border){
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(border);
        return panel;
    }

    public static JPanel createPanel(Color color, Border border, LayoutManager layout){
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setBorder(border);
        panel.setLayout(layout);
        return panel;
    }

    public static TitledBorder createTitledBorder(Color lineColor, int thickness, String title, Color titleColor, String fontType, int style, int size){
        TitledBorder titledBorder = new TitledBorder(new LineBorder(lineColor, thickness),title);
        titledBorder.setTitleColor(titleColor);
        titledBorder.setTitleFont(new Font(fontType,style,size));
        return titledBorder;
    }

    public static JRadioButton createRadioButton(String name, Font font, boolean selected){
        JRadioButton radioButton = new JRadioButton(name);
        radioButton.setFont(font);
        radioButton.setSelected(selected);
        return  radioButton;
    }

}
