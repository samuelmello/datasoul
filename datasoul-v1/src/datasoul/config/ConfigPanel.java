/*
 * ConfigForm.java
 *
 * Created on 22 de Marco de 2006, 21:22
 */

package datasoul.config;

import datasoul.*;
import datasoul.render.ContentManager;
import datasoul.render.RenderConfigItf;
import datasoul.render.SwingContentRender;
import datasoul.util.ObjectManager;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author  Administrador
 */
public class ConfigPanel extends javax.swing.JPanel {

    private ConfigObj configObj;
    private ArrayList<Component> components;
    
    
    /** Creates new form ConfigForm */
    public ConfigPanel() {
        initComponents();
        
        initDevicePopupMenu();

        components = new ArrayList<Component>();
        registerComponents();
        
        configObj = ConfigObj.getInstance();
        refreshScreenValues();
        
        pnlMainCoord.setVisible(false);
        pnlMonitorCoord.setVisible(false);
        
    }

    private void registerComponents(){
        registerComponent(mainOutput,"MainOutputIdx");
        registerComponent(mainOutputPositionLeft,"MainOutputPositionLeft");
        registerComponent(mainOutputPositionTop,"MainOutputPositionTop");
        registerComponent(mainOutputSizeHeight,"MainOutputSizeHeight");
        registerComponent(mainOutputSizeWidth,"MainOutputSizeWidth");
        registerComponent(monitorOutput,"MonitorOutputIdx");
        registerComponent(monitorOutputPositionLeft,"MonitorOutputPositionLeft");
        registerComponent(monitorOutputPositionTop,"MonitorOutputPositionTop");
        registerComponent(monitorOutputSizeHeight,"MonitorOutputSizeHeight");
        registerComponent(monitorOutputSizeWidth,"MonitorOutputSizeWidth");
        registerComponent(templateText,"TemplateText");
        registerComponent(txtMonitorTemplateFilter, "MonitorTemplateFilter");
        registerComponent(txtAlertTemplateFilter, "AlertTemplateFilter");
        registerComponent(txtGeneralTemplateFilter, "GeneralTemplateFilter");
        
        registerComponent(clockMode,"ClockMode");
        clockMode.removeAllItems();
        for (int i=0; i<ConfigObj.CLOCKMODE_TABLE.length; i++){
            clockMode.addItem(ConfigObj.CLOCKMODE_TABLE[i]);
        }
        
        /*
        registerComponent( mainDisplayEngine, "MainDisplayEngine" );
        mainDisplayEngine.removeAllItems();
        mainDisplayEngine.addItem("SDLContentRender");
        mainDisplayEngine.addItem("SwingContentRender");
        mainDisplayEngine.addItem("SDLLiveContentRender");

        registerComponent( monitorDisplayEngine, "MonitorDisplayEngine" );
        monitorDisplayEngine.removeAllItems();
        monitorDisplayEngine.addItem("SDLContentRender");
        monitorDisplayEngine.addItem("SwingContentRender");
        monitorDisplayEngine.addItem("SDLLiveContentRender");
        */
    }

    private void initDevicePopupMenu(){
        GraphicsDevice gd[];
        gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

        for (int i=0; i<gd.length; i++){
            
            GraphicsConfiguration gc = gd[i].getDefaultConfiguration();

            Rectangle r = gc.getBounds();
            
            JMenuItemDevice jmi1 = new JMenuItemDevice(gd[i].getIDstring(), (int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight(), JMenuItemDevice.TARGET_MAIN);
            jPpMenuMain.add(jmi1);

            JMenuItemDevice jmi2 = new JMenuItemDevice(gd[i].getIDstring(), (int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight(), JMenuItemDevice.TARGET_MONITOR);
            jPpMenuMonitor.add(jmi2);

        }
        
    }
    
    private class JMenuItemDevice extends JMenuItem {

        private int width, height, x, y;
        
        private int target;
        public static final int TARGET_MAIN = 0;
        public static final int TARGET_MONITOR = 1;
        public JMenuItemDevice(String deviceId, int x, int y, int width, int height, int target ){
            super("Display:"+" \""+deviceId+"\" ("+width+"x"+height+" @ "+x+","+y+")");
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
            this.target = target;

            this.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    action();
                }
            });
        
        }
        
        public void action(){
            
            if (target == TARGET_MAIN){
                
                mainOutputPositionLeft.setText( Integer.toString(x) );
                mainOutputPositionTop.setText( Integer.toString(y) );
                mainOutputSizeWidth.setText( Integer.toString(width) );
                mainOutputSizeHeight.setText( Integer.toString(height) );
                
            }else if (target == TARGET_MONITOR){

                monitorOutputPositionLeft.setText( Integer.toString(x) );
                monitorOutputPositionTop.setText( Integer.toString(y) );
                monitorOutputSizeWidth.setText( Integer.toString(width) );
                monitorOutputSizeHeight.setText( Integer.toString(height) );
                
            }
            
        }
    }
    
    private void registerComponent(Component component, String string){
        components.add(component);
        component.setName(string);
    }
    
    private void setComponentValue(String string, Component component){
        if(component instanceof JCheckBox){
            if(string != null && string.compareToIgnoreCase("1")==0){
                ((JCheckBox)component).setSelected(true);
            }else{
                ((JCheckBox)component).setSelected(false);
            }
        }else if(component instanceof JComboBox){
            ((JComboBox)component).setSelectedItem(string);
        }else if(component instanceof JTextField){
            ((JTextField)component).setText(string);        
        }
    }

    private String getComponentValue(Component component){
        if(component instanceof JCheckBox){
            if(((JCheckBox)component).isSelected()){
                return "1";
            }else{
                return "0";
            }
        }else if(component instanceof JComboBox){
            return (String)((JComboBox)component).getSelectedItem();
        }else if(component instanceof JTextField){
            return ((JTextField)component).getText();
        }
        return "";
    }
    
    private void refreshScreenValues(){
        for(Component component:components){
            try {
                Object o = configObj.getClass().getMethod("get"+component.getName()).invoke(configObj);
                setComponentValue( o.toString(), component);            
            } catch (Exception ex) {
                ex.printStackTrace();
            }            
        }
    }

    private void refreshObjectValues(){
        for(Component component:components){
            try {
                configObj.getClass().getMethod("set"+component.getName(), String.class).invoke(configObj, getComponentValue(component));
            } catch (Exception ex) {
                System.err.println("Setting "+component.getName()+":");
                ex.printStackTrace();
            }            
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPpMenuMain = new javax.swing.JPopupMenu();
        jPpMenuMonitor = new javax.swing.JPopupMenu();
        btnApply = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        mainOutput = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        btnSelectDeviceMain = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        pnlMainCoord = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        mainOutputPositionTop = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        mainOutputSizeHeight = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        mainOutputSizeWidth = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        mainOutputPositionLeft = new javax.swing.JTextField();
        btnMainAdvanced = new javax.swing.JToggleButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        monitorOutput = new javax.swing.JCheckBox();
        jLabel29 = new javax.swing.JLabel();
        btnSelectDeviceMonitor = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        pnlMonitorCoord = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        monitorOutputPositionTop = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        monitorOutputSizeHeight = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        monitorOutputSizeWidth = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        monitorOutputPositionLeft = new javax.swing.JTextField();
        btnMonitorAdvanced = new javax.swing.JToggleButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        clockMode = new javax.swing.JComboBox();
        jLabel36 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtMonitorTemplateFilter = new javax.swing.JTextField();
        txtAlertTemplateFilter = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtGeneralTemplateFilter = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        templateText = new datasoul.templates.TemplateComboBox();
        jLabel35 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();

        btnApply.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/apply.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("datasoul/internationalize"); // NOI18N
        btnApply.setText(bundle.getString("Apply")); // NOI18N
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Main Output"));

        mainOutput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        jLabel1.setText(bundle.getString("Enable_main_output")); // NOI18N

        jLabel28.setText(bundle.getString("Output_Device")); // NOI18N

        btnSelectDeviceMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/kcontrol.png"))); // NOI18N
        btnSelectDeviceMain.setText(bundle.getString("Select")); // NOI18N
        btnSelectDeviceMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectDeviceMainActionPerformed(evt);
            }
        });

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/projector2_22x22.png"))); // NOI18N

        jLabel24.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel24.setText(bundle.getString("Main_output_is_used_for_displaying_information_for_the_public,")); // NOI18N

        jLabel32.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel32.setText(bundle.getString("usually_connected_to_a_projector.")); // NOI18N

        jLabel3.setText(bundle.getString("Position")); // NOI18N

        jLabel12.setText(bundle.getString("left")); // NOI18N

        mainOutputPositionTop.setText("jTextField1");

        jLabel13.setText(bundle.getString("top")); // NOI18N

        jLabel17.setText(bundle.getString("height")); // NOI18N

        mainOutputSizeHeight.setText("jTextField1");

        jLabel16.setText(bundle.getString("width")); // NOI18N

        mainOutputSizeWidth.setText("jTextField1");

        jLabel6.setText(bundle.getString("Size")); // NOI18N

        jLabel30.setFont(new java.awt.Font("Dialog", 2, 10));
        jLabel30.setText(bundle.getString("*_Using_640x480_is_highly_recommented_for_better_performance")); // NOI18N

        mainOutputPositionLeft.setText("jTextField1");

        org.jdesktop.layout.GroupLayout pnlMainCoordLayout = new org.jdesktop.layout.GroupLayout(pnlMainCoord);
        pnlMainCoord.setLayout(pnlMainCoordLayout);
        pnlMainCoordLayout.setHorizontalGroup(
            pnlMainCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlMainCoordLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(pnlMainCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnlMainCoordLayout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(pnlMainCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel3)
                            .add(jLabel6))
                        .add(46, 46, 46)
                        .add(pnlMainCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(mainOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(mainOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnlMainCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel16)
                            .add(jLabel12))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnlMainCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(mainOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(mainOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnlMainCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel17)
                            .add(jLabel13)))
                    .add(jLabel30)))
        );
        pnlMainCoordLayout.setVerticalGroup(
            pnlMainCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlMainCoordLayout.createSequentialGroup()
                .add(pnlMainCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel13)
                    .add(mainOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3)
                    .add(jLabel12)
                    .add(mainOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlMainCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel17)
                    .add(mainOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel16)
                    .add(mainOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel30))
        );

        btnMainAdvanced.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/configure.png"))); // NOI18N
        btnMainAdvanced.setText("Advanced");
        btnMainAdvanced.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMainAdvancedActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(pnlMainCoord, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(mainOutput))
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel23)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 324, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel32)))
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel28)
                        .add(14, 14, 14)
                        .add(btnSelectDeviceMain)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(btnMainAdvanced)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel23)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel24)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel32)))
                .add(6, 6, 6)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel1)
                    .add(mainOutput))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel28)
                    .add(btnSelectDeviceMain)
                    .add(btnMainAdvanced))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlMainCoord, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Backstage Monitor Output"));

        jLabel2.setText(bundle.getString("Enable_monitor_output")); // NOI18N

        monitorOutput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        jLabel29.setText(bundle.getString("Output_Device")); // NOI18N

        btnSelectDeviceMonitor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/kcontrol.png"))); // NOI18N
        btnSelectDeviceMonitor.setText(bundle.getString("Select")); // NOI18N
        btnSelectDeviceMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectDeviceMonitorActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/display_22x22.png"))); // NOI18N

        jLabel21.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel21.setText(bundle.getString("Monitor_output_is_used_to_display_information_in_monitors_on")); // NOI18N

        jLabel22.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel22.setText(bundle.getString("stage,_providing_feedback_for_musicans_or_preachers.")); // NOI18N

        jLabel4.setText(bundle.getString("Position")); // NOI18N

        jLabel14.setText(bundle.getString("left")); // NOI18N

        monitorOutputPositionTop.setText("jTextField1");

        jLabel15.setText(bundle.getString("top")); // NOI18N

        jLabel19.setText(bundle.getString("height")); // NOI18N

        monitorOutputSizeHeight.setText("jTextField1");

        jLabel18.setText(bundle.getString("width")); // NOI18N

        monitorOutputSizeWidth.setText("jTextField1");

        jLabel5.setText(bundle.getString("Size")); // NOI18N

        jLabel31.setFont(new java.awt.Font("Dialog", 2, 10));
        jLabel31.setText(bundle.getString("*_Using_640x480_is_highly_recommented_for_better_performance")); // NOI18N

        monitorOutputPositionLeft.setText("jTextField1");

        org.jdesktop.layout.GroupLayout pnlMonitorCoordLayout = new org.jdesktop.layout.GroupLayout(pnlMonitorCoord);
        pnlMonitorCoord.setLayout(pnlMonitorCoordLayout);
        pnlMonitorCoordLayout.setHorizontalGroup(
            pnlMonitorCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlMonitorCoordLayout.createSequentialGroup()
                .add(22, 22, 22)
                .add(pnlMonitorCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel31)
                    .add(pnlMonitorCoordLayout.createSequentialGroup()
                        .add(pnlMonitorCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel5)
                            .add(jLabel4))
                        .add(46, 46, 46)
                        .add(pnlMonitorCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(monitorOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(monitorOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnlMonitorCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel14)
                            .add(jLabel18))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnlMonitorCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, monitorOutputSizeHeight, 0, 0, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, monitorOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnlMonitorCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel19)
                            .add(jLabel15))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMonitorCoordLayout.setVerticalGroup(
            pnlMonitorCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlMonitorCoordLayout.createSequentialGroup()
                .add(pnlMonitorCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel15)
                    .add(monitorOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel14)
                    .add(jLabel4)
                    .add(monitorOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlMonitorCoordLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(monitorOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel19)
                    .add(jLabel5)
                    .add(jLabel18)
                    .add(monitorOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel31))
        );

        btnMonitorAdvanced.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/configure.png"))); // NOI18N
        btnMonitorAdvanced.setText("Advanced");
        btnMonitorAdvanced.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMonitorAdvancedActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(monitorOutput))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel20)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 377, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel22)))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel29)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnSelectDeviceMonitor)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnMonitorAdvanced))
                    .add(pnlMonitorCoord, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel20)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel21)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel22)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(monitorOutput)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnSelectDeviceMonitor)
                    .add(btnMonitorAdvanced)
                    .add(jLabel29))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnlMonitorCoord, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Clock"));

        jLabel9.setText(bundle.getString("Clock_format")); // NOI18N

        clockMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel36.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/clock.png"))); // NOI18N
        jLabel36.setText(bundle.getString("Specify_the_format_to_display_the_time_in_templates_that_contains_clocks")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel8Layout.createSequentialGroup()
                        .add(jLabel9)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(clockMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 207, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel36))
                .addContainerGap(438, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(jLabel36)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(clockMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Templates"));

        jLabel7.setText(bundle.getString("For_monitor_templates,_show_only_itens_containing_*")); // NOI18N

        txtMonitorTemplateFilter.setText(bundle.getString("filter")); // NOI18N

        txtAlertTemplateFilter.setText(bundle.getString("filter")); // NOI18N

        jLabel25.setText(bundle.getString("For_alert_templates,_show_only_itens_containing_*")); // NOI18N

        jLabel26.setText(bundle.getString("For_general_templates,_show_only_itens_containing_*")); // NOI18N

        txtGeneralTemplateFilter.setText(bundle.getString("filter")); // NOI18N

        jLabel27.setFont(new java.awt.Font("Dialog", 2, 10));
        jLabel27.setText(bundle.getString("*_leave_blank_to_view_all_available_templates")); // NOI18N

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/template_filter.png"))); // NOI18N

        jLabel34.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel34.setText(bundle.getString("Template_filters_makes_easy_to_find_the_right_template_to_select_in_each_situation.")); // NOI18N

        jLabel8.setText(bundle.getString("Default_template_for_text_items")); // NOI18N

        templateText.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "monitor", "Template1", "Aviso 1", "Biblia40dias", "Musicas40Dias", "Avisos40Dias", "teste123" }));
        templateText.setOpaque(false);

        jLabel35.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel35.setText(bundle.getString("You_can_also_specify_the_default_template_selected_for_newly_created_items.")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jLabel27)
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jLabel33)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel34)
                            .add(jLabel35)))
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel7)
                            .add(jLabel25)
                            .add(jLabel26))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtGeneralTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtMonitorTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtAlertTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jLabel8)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(templateText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(363, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel33)
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jLabel34)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel35)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(txtMonitorTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel25)
                    .add(txtAlertTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel26)
                    .add(txtGeneralTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel27)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(templateText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel37.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel37.setText(bundle.getString("Some_configurations_apply_only_after_application_restart")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jLabel37)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnApply)
                        .add(15, 15, 15))
                    .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnApply)
                    .add(jLabel37))
                .add(63, 63, 63))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    /*
    public void showConfig(boolean isMonitor){
        String render;
        if (isMonitor){
            render = monitorDisplayEngine.getSelectedItem().toString();
        }else{
            render = mainDisplayEngine.getSelectedItem().toString();
        }
        RenderConfigItf conf;
        try{
            conf = (RenderConfigItf) Class.forName("datasoul.render."+render+"Config").newInstance();
            conf.showConfiguration(isMonitor);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Unable_to_create_configuration_for_")+render, java.util.ResourceBundle.getBundle("datasoul/internationalize").getString("Error"), JOptionPane.ERROR_MESSAGE);
        }
        
    }
    */
    
    private void btnSelectDeviceMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectDeviceMainActionPerformed
         jPpMenuMain.show(btnSelectDeviceMain, btnSelectDeviceMain.getWidth(), 0);
    }//GEN-LAST:event_btnSelectDeviceMainActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed

        try{
            ObjectManager.getInstance().setBusyCursor();
            refreshObjectValues();
            configObj.save();
            
            if (ContentManager.isMainDisplayActive() && ContentManager.getMainDisplay() instanceof SwingContentRender){
                ((SwingContentRender)ContentManager.getMainDisplay()).updatePosition(
                        Integer.parseInt(configObj.getMainOutputSizeWidth()), 
                        Integer.parseInt(configObj.getMainOutputSizeHeight()), 
                        Integer.parseInt(configObj.getMainOutputPositionTop()), 
                        Integer.parseInt(configObj.getMainOutputPositionLeft()));
            }
            
            if (ContentManager.isMonitorDisplayActive() && ContentManager.getMonitorDisplay() instanceof SwingContentRender){
                ((SwingContentRender)ContentManager.getMonitorDisplay()).updatePosition(
                        Integer.parseInt(configObj.getMonitorOutputSizeWidth()), 
                        Integer.parseInt(configObj.getMonitorOutputSizeHeight()), 
                        Integer.parseInt(configObj.getMonitorOutputPositionTop()), 
                        Integer.parseInt(configObj.getMonitorOutputPositionLeft()));
            }

        
        }finally{
            ObjectManager.getInstance().setDefaultCursor();
        }
        
            
    }//GEN-LAST:event_btnApplyActionPerformed

    private void btnSelectDeviceMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectDeviceMonitorActionPerformed
        jPpMenuMonitor.show(btnSelectDeviceMonitor, btnSelectDeviceMonitor.getWidth(), 0);
    }//GEN-LAST:event_btnSelectDeviceMonitorActionPerformed

    private void btnMainAdvancedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMainAdvancedActionPerformed
        pnlMainCoord.setVisible( btnMainAdvanced.isSelected() );
    }//GEN-LAST:event_btnMainAdvancedActionPerformed

    private void btnMonitorAdvancedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMonitorAdvancedActionPerformed
        pnlMonitorCoord.setVisible( btnMonitorAdvanced.isSelected() );
    }//GEN-LAST:event_btnMonitorAdvancedActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JToggleButton btnMainAdvanced;
    private javax.swing.JToggleButton btnMonitorAdvanced;
    private javax.swing.JButton btnSelectDeviceMain;
    private javax.swing.JButton btnSelectDeviceMonitor;
    private javax.swing.JComboBox clockMode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPpMenuMain;
    private javax.swing.JPopupMenu jPpMenuMonitor;
    private javax.swing.JCheckBox mainOutput;
    private javax.swing.JTextField mainOutputPositionLeft;
    private javax.swing.JTextField mainOutputPositionTop;
    private javax.swing.JTextField mainOutputSizeHeight;
    private javax.swing.JTextField mainOutputSizeWidth;
    private javax.swing.JCheckBox monitorOutput;
    private javax.swing.JTextField monitorOutputPositionLeft;
    private javax.swing.JTextField monitorOutputPositionTop;
    private javax.swing.JTextField monitorOutputSizeHeight;
    private javax.swing.JTextField monitorOutputSizeWidth;
    private javax.swing.JPanel pnlMainCoord;
    private javax.swing.JPanel pnlMonitorCoord;
    private datasoul.templates.TemplateComboBox templateText;
    private javax.swing.JTextField txtAlertTemplateFilter;
    private javax.swing.JTextField txtGeneralTemplateFilter;
    private javax.swing.JTextField txtMonitorTemplateFilter;
    // End of variables declaration//GEN-END:variables
    
}
