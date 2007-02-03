/*
 * AuxiliarPanel.java
 *
 * Created on 26 de Dezembro de 2005, 23:37
 */

package datasoul.datashow;

import datasoul.util.ObjectManager;

/**
 *
 * @author  Administrador
 */
public class AuxiliarPanel extends javax.swing.JPanel {

    private DatashowPanel objectManager;
    
    public static final int TAB_DISPLAY = 0;
    public static final int TAB_ALARM = 1;
    public static final int TAB_CLOCK = 2;
    public static final int TAB_BACKGROUND = 3;
    /**
     * Creates new form AuxiliarPanel
     */
    public AuxiliarPanel() {
        initComponents();

        ObjectManager.getInstance().setAuxiliarPanel(this);
    }

    public DisplayControlPanel getDisplayControlPanel(){
        return this.displayControlPanel1;
    }
    
    public void setVisibleTab(int tab) {
        switch(tab){
            case TAB_DISPLAY:
                break;
            case TAB_ALARM:
                break;
            case TAB_CLOCK:
                break;
            case TAB_BACKGROUND:
                break;
        }
        this.tabAuxiliar.setSelectedIndex(tab);
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        tabAuxiliar = new javax.swing.JTabbedPane();
        displayControlPanel1 = new datasoul.datashow.DisplayControlPanel();
        alertControlPanel1 = new datasoul.datashow.AlertControlPanel();
        timerControlPanel1 = new datasoul.datashow.TimerControlPanel();
        backgroundPanel1 = new datasoul.datashow.BackgroundPanel();

        tabAuxiliar.setToolTipText("");
        tabAuxiliar.setMaximumSize(new java.awt.Dimension(32767, 250));
        tabAuxiliar.setMinimumSize(new java.awt.Dimension(0, 0));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        tabAuxiliar.addTab(bundle.getString("Display"), new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/display.png")), displayControlPanel1); // NOI18N

        tabAuxiliar.addTab(bundle.getString("Alert"), new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/bell.png")), alertControlPanel1); // NOI18N

        tabAuxiliar.addTab(bundle.getString("Timer"), new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/kalarm.png")), timerControlPanel1); // NOI18N

        tabAuxiliar.addTab(bundle.getString("Background"), new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/looknfeel_1.png")), backgroundPanel1); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabAuxiliar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(tabAuxiliar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void templateMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_templateMonitorActionPerformed
        
        
    }//GEN-LAST:event_templateMonitorActionPerformed

    
        
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private datasoul.datashow.AlertControlPanel alertControlPanel1;
    private datasoul.datashow.BackgroundPanel backgroundPanel1;
    private datasoul.datashow.DisplayControlPanel displayControlPanel1;
    private javax.swing.JTabbedPane tabAuxiliar;
    private datasoul.datashow.TimerControlPanel timerControlPanel1;
    // End of variables declaration//GEN-END:variables
    
}
