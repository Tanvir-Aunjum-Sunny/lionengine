package com.b3dgs.lionengine.example.c_platform.e_lionheart.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.Editor;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.map.Map;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.map.Tile;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.map.TypeTileCollision;
import com.b3dgs.lionengine.utility.LevelRipConverter;
import com.b3dgs.lionengine.utility.UtilitySwing;

/**
 * Menu bar implementation.
 */
public class MenuBar
        extends JMenuBar
{
    /** Uid. */
    private static final long serialVersionUID = 1199844863419699405L;
    /** Editor reference. */
    private final Editor editor;
    /** Items list. */
    private final TreeMap<String, JMenuItem> items;

    /**
     * Constructor.
     * 
     * @param editor The editor reference.
     */
    public MenuBar(Editor editor)
    {
        super();
        this.editor = editor;
        items = new TreeMap<>();
        JMenu menu = addMenu("File");
        addItem(menu, "New", new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                fileNew();
            }
        });
        addItem(menu, "Load", new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                fileLoad();
            }
        });
        addItem(menu, "Save", new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                fileSave();
            }
        });
        addItem(menu, "Exit", new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                fileExit();
            }
        });

        addMenu("Edit");

        menu = addMenu("Tools");
        addItem(menu, "Import Map", new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                toolsImportMap();
            }
        });

        menu = addMenu("Help");
        addItem(menu, "About", new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                helpAbout();
            }
        });
    }

    /**
     * New action.
     */
    void fileNew()
    {
        final JDialog dialog = UtilitySwing.createDialog(editor, "New", 320, 240);
        dialog.setLayout(new BorderLayout());

        // Center panel
        final JPanel centerPanel = new JPanel(new GridLayout(0, 2));
        dialog.add(centerPanel, BorderLayout.CENTER);

        JPanel panel = UtilitySwing.createBorderedPanel("Background", 2);
        centerPanel.add(panel);

        panel = UtilitySwing.createBorderedPanel("Foreground", 2);
        centerPanel.add(panel);

        panel = UtilitySwing.createBorderedPanel("Map", 2);
        centerPanel.add(panel);

        panel = UtilitySwing.createBorderedPanel("Entities", 2);
        centerPanel.add(panel);

        // South panel
        final JPanel southPanel = new JPanel(new GridLayout());
        dialog.add(southPanel, BorderLayout.SOUTH);
        UtilitySwing.addButton("Accept", southPanel, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                // Nothing for the moment
            }
        });

        UtilitySwing.addButton("Cancel", southPanel, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                UtilitySwing.terminateDialog(dialog);
            }
        });

        UtilitySwing.startDialog(dialog);
    }

    /**
     * Load action.
     */
    void fileLoad()
    {
        final MapFilter filter = new MapFilter("Lionheart Remake Map", "lrm");
        final Media media = UtilitySwing.createOpenFileChooser(editor.getContentPane(), filter);
        if (media != null)
        {
            editor.world.loadLevel(media);
        }
    }

    /**
     * Save action.
     */
    void fileSave()
    {
        final String name = "test1.lrm";
        editor.world.saveLevel(Media.get(name));
    }

    /**
     * Exit action.
     */
    void fileExit()
    {
        editor.terminate();
        System.exit(0);
    }

    /**
     * Import map action.
     */
    void toolsImportMap()
    {
        final MapFilter filter = new MapFilter("Map Image Rip", "png", "bmp");
        final Media media = UtilitySwing.createOpenFileChooser(editor.getContentPane(), filter);
        if (media != null)
        {
            final Map map = editor.world.map;
            final LevelRipConverter<TypeTileCollision, Tile> rip = new LevelRipConverter<>();
            rip.start(media, map, Media.get("tiles", editor.toolBar.entitySelector.selectedWorld.asPathName()));
            editor.world.camera.setLimits(map);
            editor.repaint();
            rip.showResults();
        }
    }

    /**
     * About action.
     */
    void helpAbout()
    {
        final JDialog dialog = UtilitySwing.createDialog(editor, "About", 212, 96);
        final JTextArea txt = new JTextArea("LionEngine editor\nAuthor: Pierre-Alexandre\nWebsite: www.b3dgs.com");
        txt.setEditable(false);
        dialog.add(txt);
        UtilitySwing.startDialog(dialog);
    }

    /**
     * Add a menu to the menu bar.
     * 
     * @param name The menu name.
     * @return The menu added instance.
     */
    private JMenu addMenu(String name)
    {
        final JMenu menu = new JMenu(name);
        add(menu);
        return menu;
    }

    /**
     * Add an item to a menu.
     * 
     * @param menu The menu which will receive the item.
     * @param name The item name.
     * @param action The action listener.
     * @return The item added instance.
     */
    private JMenuItem addItem(JMenu menu, String name, ActionListener action)
    {
        final JMenuItem item = new JMenuItem(name);
        item.addActionListener(action);
        menu.add(item);
        items.put(name, item);
        return item;
    }
}
