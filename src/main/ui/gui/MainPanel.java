package ui.gui;

import model.InventoryItem;
import persistence.DataHandler;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import model.Character;

public class MainPanel extends JPanel {
    /// A JPanel to handle and render all other components.
    private static final int SELECT_COLUMN_WIDTH = 220;
    private static final int SELECT_COLUMN_HEIGHT = 600;
    private CharacterListPanel charactersPanel = new CharacterListPanel();
    private InventoryItemListPanel itemsPanel;
    private DataHandlerWrapper data;

    public MainPanel(DataHandlerWrapper data) {
        super();
        this.data = data;
        itemsPanel = new InventoryItemListPanel(data);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setLayout(new BorderLayout());
        addTitleBar();
        addCharacters();
        addInventoryItems();
        add(new CharacterViewPanel(data), BorderLayout.CENTER);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes and then adds Characters section on west side
    private void addCharacters() {
        for (Character c : data.getCharacters()) {
            CharacterPanel panel = new CharacterPanel(c);
            charactersPanel.getCharacters().add(panel);
        }
        charactersPanel.reload();
        addScrollPanel(charactersPanel, SELECT_COLUMN_WIDTH, SELECT_COLUMN_HEIGHT, BorderLayout.WEST);
    }

    // MODIFIES: this
    // EFFECTS: initializes and then adds Items section on east side
    private void addInventoryItems() {
        for (InventoryItem i : data.getItems()) {
            InventoryItemPanel panel = new InventoryItemPanel(i);
            itemsPanel.getItems().add(panel);
        }
        itemsPanel.reloadWithAdd();
        addScrollPanel(itemsPanel, SELECT_COLUMN_WIDTH, SELECT_COLUMN_HEIGHT, BorderLayout.EAST);
    }

    // MODIFIES: this
    // EFFECTS: initializes and then adds title bar at north
    private void addTitleBar() {
        JPanel titleContainer = new JPanel();
        titleContainer.add(new TitlePanel());
        add(titleContainer, BorderLayout.NORTH);
    }

    // MODIFIES: this
    // EFFECTS: adds a scrollPane-loaded JPanel to this at position with given height and width,
    //          with component housed inside
    private void addScrollPanel(JComponent component, int width, int height, String position) {
        JPanel scrollPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(component, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                                                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setMaximumSize(new Dimension(width, height));
        scrollPane.setPreferredSize(new Dimension(width, height));
        scrollPane.setSize(new Dimension(width, height));

        scrollPanel.setPreferredSize(new Dimension(width, height));
        scrollPanel.setSize(new Dimension(width, height));
        scrollPanel.add(scrollPane);
        add(scrollPanel, position);
        revalidate();
    }
}
