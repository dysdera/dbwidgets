package kw.database.widgets;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class NavButton extends JButton {
    private static final Dimension D = new Dimension(33, 25);

    public NavButton(String imageFile) {
        setIcon(new ImageIcon(getClass().getResource(imageFile)));
        setActionCommand("RECORD_CHANGED");
        setMaximumSize(D);
        setMinimumSize(D);
        setPreferredSize(D);
    }
}
