/*
 * HelpKeyboard.java
 *
 * Created on 22 November 2006, 23:05
 */

package datasoul.help;

/**
 *
 * @author  samuelm
 */
public class HelpKeyboard extends javax.swing.JPanel {
    
    /** Creates new form HelpKeyboard */
    public HelpKeyboard() {
        initComponents();
        StringBuffer sb = new StringBuffer();
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("F4:_Set_focus_on_Live_Item"));
        sb.append("\n");
        sb.append("");
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("F5:_Show_Display_Controls"));
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("F6:_Show_Alert_Controls"));
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("F7:_Show_Timer_Controls"));
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("F8:_Show_Background_Controls"));
        sb.append("\n");
        sb.append("");
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("F9:_Hide"));
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("F10:_Show"));
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("F12:_Black"));
        sb.append("\n");
        sb.append("");
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Ctrl+1:_Show_Projector"));
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Ctrl+2:_Show_Templates"));
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Ctrl+3:_Show_Songs"));
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Ctrl+4:_Show_Config"));
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Ctrl+5:_Show_Help"));
        sb.append("\n");
        sb.append("");
        sb.append("\n");
        sb.append(java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("<Num>+Enter,_with_focus_on_Preview_or_Live_Item:_Jump_to_slide_<Num>"));
        sb.append("\n");

        jTextArea1.setText(sb.toString());


    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/key_bindings.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        jLabel1.setText(bundle.getString("Keyboard_Shortcuts")); // NOI18N

        jScrollPane1.setBorder(null);

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Dialog", 1, 12));
        jTextArea1.setRows(5);
        jTextArea1.setBorder(null);
        jTextArea1.setOpaque(false);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
    
}
