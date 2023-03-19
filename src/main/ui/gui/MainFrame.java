package ui.gui;

import model.Event;
import model.EventLog;
import persistence.DataHandler;
import persistence.FileIOHandler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class MainFrame extends JFrame implements ActionListener {
    /// A class for the JFrame of TinySRVNT's GUI. Other elements will be handled by MainPanel.
    private static final int WIDTH = 1050;
    private static final int HEIGHT = 800;

    private MainPanel mainPanel;

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu fileMenu = new JMenu("File");
    private final JMenuItem fileLoadMenuItem = new JMenuItem("Load Workspace Image...");
    private final JMenuItem fileSaveMenuItem = new JMenuItem("Save Workspace Image...");

    private static final File DEFAULT_FILE = new File("save.json");

    private final JFileChooser fileChooser = new JFileChooser() {
        public void approveSelection() {
            File f = getSelectedFile();
            if (f.exists() && getDialogType() == SAVE_DIALOG) {
                int result = JOptionPane.showConfirmDialog(this,
                                                           "This save file already exists.\n"
                                                           + "Do you want to replace it?", "Confirm overwrite",
                                                           JOptionPane.YES_NO_OPTION);
                switch (result) {
                    case JOptionPane.YES_OPTION:
                        super.approveSelection();
                        return;
                    case JOptionPane.NO_OPTION:
                    default:
                        return;
                }
            }
            super.approveSelection();
        }
    }; //approveSelection() override code provided by kmindi at https://stackoverflow.com/a/9716431

    private final FileIOHandler ioHandler = new FileIOHandler();
    private DataHandlerWrapper data = new DataHandlerWrapper(new DataHandler());

    public MainFrame() {
        super("TinySRVNT v0.0003");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(false);
        centreOnScreen();
        initFileChooser();
        addFileMenu();
        addWindowAdapter();
        mainPanel = new MainPanel(data);
        reload();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: reloads all assets
    private void reload() {
        remove(mainPanel);
        mainPanel = new MainPanel(data);
        add(mainPanel);
        revalidate();
    }

    // MODIFIES: this
    // EFFECTS: adds file I/O systems to frame
    private void addFileMenu() {
        fileLoadMenuItem.addActionListener(this);
        fileSaveMenuItem.addActionListener(this);
        fileMenu.add(fileLoadMenuItem);
        fileMenu.add(fileSaveMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    // MODIFIES: this
    // EFFECTS: initializes values and settings for fileChooser
    private void initFileChooser() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".json", "json");
        fileChooser.setCurrentDirectory(new File(FileIOHandler.SAVE_FILE_DIRECTORY_PATH));
        fileChooser.setFileFilter(filter);
    }

    // MODIFIES: this
    // EFFECTS:  frame is centred on desktop
    private void centreOnScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
    }

    // MODIFIES: this
    // EFFECTS:  adds a window adapter to detect closing and output eventLog
    private void addWindowAdapter() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosed(e);
                for (Event v : EventLog.getInstance()) {
                    System.out.println(v.toString());
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (fileLoadMenuItem.equals(source)) {
            loadFile();
        } else if (fileSaveMenuItem.equals(source)) {
            saveFile();
        }
        reload();
    }

    // MODIFIES: this
    // EFFECTS: attempts to load a save file from fileChooser
    private void loadFile() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                data = new DataHandlerWrapper(ioHandler.loadSave(fileChooser.getSelectedFile()));
            } catch (IOException e) {
                throw new RuntimeException(e); //stub
            }
        }
    }

    // EFFECTS: attempts to save the current program state to fileChooser
    private void saveFile() {
        fileChooser.setSelectedFile(DEFAULT_FILE);
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                String fileName = fileChooser.getSelectedFile().getName().replaceAll("\\..+", "");
                ioHandler.writeSave(fileName, data.getData());
            } catch (IOException e) {
                throw new RuntimeException(e); //stub
            }
        }
    }
}
