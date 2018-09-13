package kw.database.widgets;

import java.io.Serializable;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ButtonBar extends JPanel implements Serializable {

    private final String dir = "../../../Resources/";
    private transient ResultSet rs = null;
    private NavButton firstRec, prevRec, nextRec, lastRec;
    private FuncButton newRec, editRec, delRec, saveRec, cancel;
    private JTextField curRec, totalRecs;
    private final Dimension D = new Dimension(65, 23);
    private transient int currentRecord, recordCount;

    private final int NEW = 1;
    private final int EDIT = 2;
    private final int DELETE = 4;
    private final int SAVE = 8;
    private final int CANCEL = 16;
    private final int ALL = 31;

    private ActionListener actionListener;

    public ButtonBar() {
        initComponents();
    }

    public void setResultSet(ResultSet rs) {
        this.rs = rs;
        try {
            rs.first();
            currentRecord = 1;
            setNavButtons();
            recordCount();
        } catch (SQLException s) {
        }
        curRec.setText(currentRecord + "");
    }

    public void addActionListener(ActionListener l) {
        actionListener = l;
    }

    public void removeActionListener(ActionListener l) {
        actionListener = null;
    }

    private void fireEvent(ActionEvent ae) {
        if (actionListener != null) {
            actionListener.actionPerformed(ae);
        }
    }

    private void recordCount() {
        int row = currentRecord; //store current
        try {
            rs.last();
            recordCount = rs.getRow();
            totalRecs.setText(recordCount + "");
            rs.absolute(row);
            setNavButtons();
        } catch (SQLException s) {
        }
    }

    public void updateRecordCount() {
        recordCount();
    }

    private void setNavButtons() {
        try {
            currentRecord = rs.getRow();
            curRec.setText(currentRecord + "");
            firstRec.setEnabled(!rs.isFirst());
            prevRec.setEnabled(!rs.isFirst());
            nextRec.setEnabled(!rs.isLast());
            lastRec.setEnabled(!rs.isLast());
        } catch (SQLException s) {
        }
    }

    private void setButtons(int flag) {
        newRec.setEnabled((flag & 1) > 0);
        editRec.setEnabled((flag & 2) > 0);
        delRec.setEnabled((flag & 4) > 0);
        saveRec.setEnabled((flag & 8) > 0);
        cancel.setEnabled((flag & 16) > 0);
    }

    private void initComponents() {
        firstRec = new NavButton(dir + "first.gif");
        firstRec.addActionListener(new B());
        prevRec = new NavButton(dir + "prev.gif");
        prevRec.addActionListener(new B());
        newRec = new FuncButton("New", "NEW");
        newRec.addActionListener(new B());
        editRec = new FuncButton("Edit", "EDIT");
        editRec.addActionListener(new B());
        delRec = new FuncButton("Delete", "RECORD_CHANGED");
        delRec.addActionListener(new B());
        saveRec = new FuncButton("Save", "SAVE");
        saveRec.addActionListener(new B());
        cancel = new FuncButton("Cancel", "CANCEL"); // should this be RECORD_CHANGED ?
        cancel.addActionListener(new B());
        nextRec = new NavButton(dir + "next.gif");
        nextRec.addActionListener(new B());
        lastRec = new NavButton(dir + "last.gif");
        lastRec.addActionListener(new B());
        curRec = new JTextField();
        curRec.setMaximumSize(D);
        curRec.setMinimumSize(D);
        curRec.setPreferredSize(D);
        curRec.addActionListener(new B());
        curRec.setActionCommand("RECORD_CHANGED");
        totalRecs = new JTextField();
        totalRecs.setMaximumSize(D);
        totalRecs.setMinimumSize(D);
        totalRecs.setPreferredSize(D);
        totalRecs.setEditable(false);

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        Box bv = Box.createVerticalBox();
        Box btn = Box.createHorizontalBox();
        Box ctr = Box.createHorizontalBox();

        btn.add(Box.createHorizontalStrut(2));
        btn.add(firstRec);
        btn.add(Box.createHorizontalStrut(2));
        btn.add(prevRec);
        btn.add(Box.createHorizontalStrut(2));
        btn.add(Box.createHorizontalGlue());
        btn.add(newRec);
        btn.add(Box.createHorizontalStrut(2));
        btn.add(editRec);
        btn.add(Box.createHorizontalStrut(2));
        btn.add(delRec);
        btn.add(Box.createHorizontalStrut(2));
        btn.add(saveRec);
        btn.add(Box.createHorizontalStrut(2));
        btn.add(cancel);
        btn.add(Box.createHorizontalStrut(2));
        btn.add(Box.createHorizontalGlue());
        btn.add(nextRec);
        btn.add(Box.createHorizontalStrut(2));
        btn.add(lastRec);
        btn.add(Box.createHorizontalStrut(2));

        ctr.add(Box.createHorizontalStrut(2));
        ctr.add(new JLabel("Record"));
        ctr.add(Box.createHorizontalStrut(2));
        ctr.add(curRec);
        ctr.add(new JLabel(" of "));
        ctr.add(totalRecs);
        ctr.add(Box.createHorizontalGlue());

        bv.add(btn);
        bv.add(Box.createVerticalStrut(3));
        bv.add(ctr);

        this.add(bv);

    }

    class B implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (rs != null) {
                switch (e.getActionCommand()) {
                    case "RECORD_CHANGED":
                        Object o = e.getSource();
                        try {
                            if (o.equals(firstRec)) {
                                rs.first();
                            } else if (o.equals(prevRec)) {
                                rs.previous();
                            } else if (o.equals(nextRec)) {
                                rs.next();
                            } else if (o.equals(lastRec)) {
                                rs.last();
                            } else if (o.equals(curRec)) { //new record number entered
                                String r = curRec.getText();
                                if (r.matches("^\\d+$")) {
                                    currentRecord = Integer.parseInt(r);
                                    rs.absolute(currentRecord > recordCount ? recordCount : currentRecord);
                                }
                            } else { //is delete
                                int dialogResult = JOptionPane.showConfirmDialog(null,
                                        "Are you sure you want to delete this record?",
                                        "Warning", JOptionPane.YES_NO_OPTION);
                                if (dialogResult == JOptionPane.YES_OPTION) {
                                    rs.deleteRow();
                                    rs.first();
                                    recordCount();
                                }
                            }
                            setNavButtons();
                        } catch (SQLException s) {
                            JOptionPane.showMessageDialog(null, s.getMessage());
                        }
                        break;
                    case "NEW":
                    case "EDIT":
                        setButtons(SAVE + CANCEL);
                        break;
                    case "SAVE":
                        recordCount();
                    case "CANCEL":
                        setButtons(ALL);
                        break;
                }
                fireEvent(e);
            }
        }
    }
}
