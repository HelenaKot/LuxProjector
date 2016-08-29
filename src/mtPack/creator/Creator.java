/**
 * Created by tomaszew2 on 01.12.14.
 */

package mtPack.creator;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.EventObject;

public
    class Creator
    extends JFrame{

    Color[][] coTab;

    Color selected;

    JPanel actionPanel, dataPanel;
    JTable jt;
    int sy, sx;

    public Creator(int sy, int sx){

        selected = Color.red;

        this.sy = sy;
        this.sx = sx;

        makeCoTab();

        this.setLayout(new BorderLayout());

        actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout());

        JButton nextB = new JButton("<-");
        JButton coloB = new JButton("color");
        JButton prevB = new JButton("->");

        nextB.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent evt) {

                }
            }
        );

        prevB.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    try{
                        System.out.println("tu");
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("plansza0.obj"));
                        oos.writeObject(coTab);
                        oos.close();
                    }catch(Exception ex){
                        System.out.println(ex);
                    }
                }
            }
        );

        coloB.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JColorChooser jcc = new JColorChooser(selected);
                    Color c = jcc.showDialog(Creator.this, "", selected);
                    if(c != null)
                        selected = c;
                }
            }
        );

        actionPanel.add(nextB);
        actionPanel.add(coloB);
        actionPanel.add(prevB);


        dataPanel = new JPanel();

        jt = new JTable(
            new DefaultTableModel(){

                JLabel jl = new JLabel();

                @Override
                public int getRowCount() {
                    return Creator.this.sy;
                }

                @Override
                public int getColumnCount() {
                    return Creator.this.sx;
                }

                @Override
                public Object getValueAt(int row, int col) {
                    jl.setBackground(coTab[row][col]);
                    return coTab[row][col];
                }

                public Class getColumnClass(int c) {
                    return Color.class;
                }

                public boolean isCellEditable(int row, int col){
                    return true;
                }

            }
        ){

            public void changeSelection(final int row, final int column, boolean toggle, boolean extend)
            {
                coTab[row][column] = selected;
                ((DefaultTableModel)Creator.this.jt.getModel()).fireTableCellUpdated(row, column);
            }
        };

        jt.setDefaultRenderer(Color.class, new ColorRenderer());

        dataPanel.add(jt);

        this.setLayout(new BorderLayout());

        this.add( actionPanel, BorderLayout.SOUTH);
        this.add( dataPanel, BorderLayout.CENTER);

        setSize(640, 480);
        setVisible(true);
    }

    public void makeCoTab(){
        if(coTab == null){
            coTab = new Color[sy][sx];
            for( int i=0; i< coTab.length; i++)
                for( int j=0; j<coTab[i].length; j++){
                    coTab[i][j] = Color.black;
                }
        }else{
            Color[][] tmp = new Color[sy][sx];
            for( int i=0; i< coTab.length; i++)
                for( int j=0; j<coTab[i].length; j++){
                    tmp[i][j] = coTab[i][j];
                }
        }

    }
}

class ColorRenderer
    extends JLabel
    implements TableCellRenderer{

    public ColorRenderer(){
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object o, boolean b, boolean b1, int i, int i1) {
        this.setBackground((Color)o);
        return this;
    }
}