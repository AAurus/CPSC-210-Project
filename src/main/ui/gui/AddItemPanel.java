package ui.gui;

import model.InventoryItem;
import persistence.DataHandler;
import utility.Utility;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.*;

public class AddItemPanel extends JPanel implements ActionListener, FocusListener {
    /// A panel to manage the item creation process.
    private static final String NAME_PROMPT = "Enter name here:";

    private static final int NAME_FIELD_WIDTH = 200;
    private static final int SPINNER_WIDTH = 60;
    private static final int FIELD_HEIGHT = 20;

    private final JLabel nameFieldLabel = new JLabel("Name:");

    private final JLabel weightFieldLabel = new JLabel("Weight:");
    private final JLabel worthFieldLabel = new JLabel("Worth:");

    private final JLabel weightUnitLabel = new JLabel("lb");
    private final JLabel worthUnitLabel = new JLabel("gp");

    private DataHandlerWrapper data;

    private JPanel namePanel = new JPanel();
    private JPanel weightPanel = new JPanel();
    private JPanel worthPanel = new JPanel();

    private JButton addItemButton = new JButton("Create new item");
    private JTextField nameField = new JTextField();
    private JSpinner weightSpinner = new JSpinner();
    private JSpinner worthSpinner = new JSpinner();

    public AddItemPanel(DataHandlerWrapper data) {
        super();

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.data = data;
        initNameField();
        initWorthSpinner();
        initWeightSpinner();
        initAddItemButton();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes nameField
    private void initNameField() {
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.LINE_AXIS));
        nameField.addFocusListener(this);
        nameField.setText(NAME_PROMPT);
        nameField.setPreferredSize(new Dimension(NAME_FIELD_WIDTH, FIELD_HEIGHT));
        nameField.setMaximumSize(new Dimension(NAME_FIELD_WIDTH, FIELD_HEIGHT));
        GuiUtility.addAllWithSpacing(namePanel, Arrays.asList(nameFieldLabel, nameField), 5);
        add(namePanel);
    }

    // MODIFIES: this
    // EFFECTS: initializes everything needed for worthSpinner
    private void initWorthSpinner() {
        worthPanel.setLayout(new BoxLayout(worthPanel, BoxLayout.LINE_AXIS));
        worthSpinner.setPreferredSize(new Dimension(SPINNER_WIDTH, FIELD_HEIGHT));
        worthSpinner.setMaximumSize(new Dimension(SPINNER_WIDTH, FIELD_HEIGHT));
        GuiUtility.addAllWithSpacing(worthPanel, Arrays.asList(worthFieldLabel, worthSpinner, worthUnitLabel), 5);
        add(Box.createRigidArea(new Dimension(5, 5)));
        add(worthPanel);
    }

    // MODIFIES: this
    // EFFECTS: initializes everything needed for weightSpinner
    private void initWeightSpinner() {
        weightPanel.setLayout(new BoxLayout(weightPanel, BoxLayout.LINE_AXIS));
        weightSpinner.setPreferredSize(new Dimension(SPINNER_WIDTH, FIELD_HEIGHT));
        weightSpinner.setMaximumSize(new Dimension(SPINNER_WIDTH, FIELD_HEIGHT));
        GuiUtility.addAllWithSpacing(weightPanel, Arrays.asList(weightFieldLabel, weightSpinner, weightUnitLabel), 5);
        add(Box.createRigidArea(new Dimension(5, 5)));
        add(weightPanel);
    }

    // MODIFIES: this
    // EFFECTS: initializes the item creation button
    private void initAddItemButton() {
        addItemButton.addActionListener(this);
        add(addItemButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(addItemButton) && !Utility.isBlank(nameField.getText())
                && !nameField.getText().equals(NAME_PROMPT)) {
            InventoryItem newItem = new InventoryItem(nameField.getText());
            newItem.setPrice(new BigDecimal(worthSpinner.getValue().toString()));
            newItem.setWeight(new BigDecimal(weightSpinner.getValue().toString()));
            data.addItem(newItem);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource().equals(nameField)) {
            if (nameField.getText().equals(NAME_PROMPT)) {
                nameField.setText("");
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (Utility.isBlank(nameField.getText())) {
            nameField.setText(NAME_PROMPT);
        }
    }
}
