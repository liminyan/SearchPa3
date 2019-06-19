import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;


class MyTreeModel extends DefaultTreeModel {

    DefaultMutableTreeNode rootNode = null;

    public MyTreeModel() {
        super(new DefaultMutableTreeNode("<html>Search Res </html>"));
        rootNode = (DefaultMutableTreeNode) getRoot();
    }

    public void load(String search) {

        rootNode.removeAllChildren();

        ArrayList<Data> final_list;
        try {
            final_list =  Search.getInstance().demo(search);
            int i = 0;
            DefaultMutableTreeNode head = new DefaultMutableTreeNode(
                    "<html> "
                            + "Serach Info : "
                            + search +
                            "</html>");
            rootNode.add(head);
            for (Data s : final_list) {
                System.out.println(s.getScore() + "  " + s.getTitle());
                i++;
                String content = s.getContent();
                content = content.replaceAll("\n", "");
                content = content.trim();
                String[] temp = search.split("");
                String[] target = search.split("");
                //String[] temp = {search};
                //String[] target = {search};
                for (int j =0; j < temp.length; j++) {
                	//System.out.println(temp[j]);
                	if (temp[j].equals(" ")) {
                		//System.out.println("passssss");
                		continue;
                	}
                	else {
                    	target[j] = "<B>" + temp[j] + "</B>";
                    	//target[j] = "<i>" + target[j] + "</i>";
                    	target[j] = "<font color= 'green'>" + target[j] + "</font>";
                    	content = content.replaceAll(temp[j], target[j]);
                	}
                }
                
                String title  = s.getTitle();
                title = title.replaceAll("\n", "");
                title = title.trim();
                String[] temp2 = search.split("");
                String[] target2 = search.split("");
//                String[] temp2 = {search};
//                String[] target2 = {search};
                for (int j =0; j < temp2.length; j++) {
                	if (temp2[j].equals(" ")) {
                		continue;
                	}
                	else {
                    	target2[j] = "<B>" + temp2[j] + "</B>";
                    	target2[j] = "<font color= 'red' >" + target2[j] + "</font>";
                    	title = title.replaceAll(temp2[j], target2[j]);
                	}
                }
            	//content = content.replaceAll(search, test);
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(
                        "<html> "
                        + (i) 
                        + "<a href=\""+s.getUrl()+"\">"+ title + "</a>" + "     "
                        + content 
                        + "</html>");
                childNode.setAllowsChildren(true);
                rootNode.add(childNode);
            }

            if ( i == 0) {
                 head = new DefaultMutableTreeNode(
                        "<html> "
                                + " 404  Not find ! "+
                                "</html>");
                rootNode.add(head);
            }
        } catch (IOException ex) {
            System.out.println("err IO");
        } catch (ParseException ex) {
            System.out.println("err Par");
        }



        nodeStructureChanged(rootNode);
    }

    public TreeNode getRootNode() {
        return rootNode;
    }
}


public class Main extends JFrame {
    private JButton button = new JButton("search");
    private JTextField text = new JTextField();
    private JFrame frame = new JFrame("Search");
    private JTree tree;
    MyTreeModel jarFilesDB;
    JPanel contentPane = new JPanel();

    public static String str = "";

    public Main() throws IOException, ParseException {

        Search.getInstance();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        setBounds(200, 200, 600, 400);

        contentPane.setBorder(new EmptyBorder(50, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        jarFilesDB = new MyTreeModel();
        jarFilesDB.load(str);


        tree = new JTree(jarFilesDB);
        tree.setCellRenderer(new MyTreeCellRenderer());
        HyperlinkMouseListener listener = new HyperlinkMouseListener(tree);
        tree.addMouseListener(listener);
        tree.addMouseMotionListener(listener);


        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                str = text.getText();
                System.out.println(str);
                text.setText("");
                jarFilesDB.load(str);



                if (str.equals(""))
                    {tree.setVisible(false);}
                else
                    {tree.setVisible(true);


                    }
            }
        });

        tree.setVisible(true);
        button.setVisible(true);
        text.setVisible(true);
        button.setBounds(200, 20, 50, 20);
        text.setBounds(250,20,200,20);
        tree.setBounds(50,50,200,500);

        JScrollPane scrollPane = new JScrollPane(tree);

        contentPane.add(text);
        contentPane.add(button);

        contentPane.add(scrollPane);

        contentPane.setVisible(true);

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        frame.add(text);
//        frame.add(button);
//        frame.add(scrollPane);
//
//        frame.pack();
//        frame.setSize(600,400);
//        frame.setVisible(true);
//        setVisible(true);
    }




    public static void main(String[] args) throws Exception {
        Main frame = new Main();
        frame.setVisible(true);
    }

}

class MyTreeCellRenderer extends DefaultTreeCellRenderer implements
        TreeCellRenderer {

    private JEditorPane editorPane;

    public MyTreeCellRenderer() {
        editorPane = new JEditorPane("text/html", null);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded, boolean leaf, int row,
                                                  boolean hasFocus) {
        Component c = super.getTreeCellRendererComponent(tree, value, selected,
                expanded, leaf, row, hasFocus);
        if (c instanceof JLabel) {
            JLabel label = (JLabel) c;
            editorPane.setText(label.getText());
            editorPane.setToolTipText(label.getToolTipText());
            editorPane.setOpaque(label.isOpaque());
            editorPane.setBackground(label.getBackground());
            editorPane.setBorder(label.getBorder());
        }
        return editorPane;
    }
}

class HyperlinkMouseListener extends MouseAdapter {
    JTree tree;

    public HyperlinkMouseListener(JTree tree) {
        this.tree = tree;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        tree.setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Element h = getHyperlinkElement(e);
        if (h != null) {
            Object attribute = h.getAttributes().getAttribute(HTML.Tag.A);
            if (attribute instanceof AttributeSet) {
                AttributeSet set = (AttributeSet) attribute;
                String href = (String) set.getAttribute(HTML.Attribute.HREF);
                if (href != null) {
                    try {
                        Desktop.getDesktop().browse(new URI(href));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        boolean isHyperlink = isHyperlink(event);
        if (isHyperlink) {
            tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            tree.setCursor(Cursor.getDefaultCursor());
        }

    }

    private boolean isHyperlink(MouseEvent event) {
        return getHyperlinkElement(event) != null;
    }

    private Element getHyperlinkElement(MouseEvent event) {
        Point p = event.getPoint();
        int selRow = tree.getRowForLocation(p.x, p.y);
        TreeCellRenderer r = tree.getCellRenderer();
        if (selRow == -1 || r == null) {
            return null;
        }
        TreePath path = tree.getPathForRow(selRow);
        Object lastPath = path.getLastPathComponent();
        Component rComponent = r.getTreeCellRendererComponent(tree, lastPath, tree
                .isRowSelected(selRow), tree.isExpanded(selRow), tree.getModel()
                .isLeaf(lastPath), selRow, true);

        if (rComponent instanceof JEditorPane == false) {
            return null;
        }
        Rectangle pathBounds = tree.getPathBounds(path);
        JEditorPane editor = (JEditorPane) rComponent;
        editor.setBounds(tree.getRowBounds(selRow));
        p.translate(-pathBounds.x, -pathBounds.y);
        int pos = editor.getUI().viewToModel(editor, p);
        if (pos >= 0 && editor.getDocument() instanceof HTMLDocument) {
            HTMLDocument hdoc = (HTMLDocument) editor.getDocument();
            Element elem = hdoc.getCharacterElement(pos);
            if (elem.getAttributes().getAttribute(HTML.Tag.A) != null) {
                return elem;
            }
        }
        return null;
    }
}
