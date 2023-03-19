package ui.gui;

import model.InventoryItem;
import persistence.DataHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InventoryItemListPanel extends JPanel implements ActionListener {
    /// A JPanel dedicated to managing item lists.
    private ArrayList<InventoryItemPanel> items = new ArrayList<>();
    private DataHandlerWrapper data;
    private AddItemPanel addPanel;

    public InventoryItemListPanel(DataHandlerWrapper data) {
        this.data = data;
        data.addActionListener(this);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        reloadWithAdd();
    }

    public InventoryItemListPanel(ArrayList<InventoryItem> items) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (InventoryItem i : items) {
            this.items.add(new InventoryItemPanel(i));
        }
        reload();
    }

    // MODIFIES: this
    // EFFECTS: reloads all assets using data as reference, with a new item adder
    public void reloadWithAdd() {
        removeAll();
        addPanel = new AddItemPanel(data);
        addPanel.setAlignmentX(LEFT_ALIGNMENT);
        add(addPanel);
        items.removeAll(items);
        for (InventoryItem i : data.getItems()) {
            items.add(new InventoryItemPanel(i));
        }
        for (InventoryItemPanel p : items) {
            add(p);
        }
        revalidate();
    }

    // MODIFIES: this
    // EFFECTS: reloads all assets
    public void reload() {
        removeAll();
        for (InventoryItemPanel p : items) {
            add(p);
        }
        revalidate();
    }

    public ArrayList<InventoryItemPanel> getItems() {
        return items;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(data)) {
            reloadWithAdd();
        }
    }
}
