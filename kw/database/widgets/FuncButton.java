package kw.database.widgets;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;

public class FuncButton extends JButton {
    private static final Dimension D = new Dimension(55, 25);
    private static final Insets I = new Insets(2, 2, 2, 2);

    public FuncButton(String buttonLabel, String actionCommand) {
        setFont(new java.awt.Font("Tahoma", 1, 13));
        setMargin(I);
        setMaximumSize(D);
        setMinimumSize(D);
        setPreferredSize(D);
        setText(buttonLabel);
        setActionCommand(actionCommand);
    }
}
