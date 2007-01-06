/*
 * ConfigForm.java
 *
 * Created on 22 de Marco de 2006, 21:22
 */

package datasoul.config;

import datasoul.*;
import datasoul.render.RenderConfigItf;
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
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPpMenuMain = new javax.swing.JPopupMenu();
        jPpMenuMonitor = new javax.swing.JPopupMenu();
        btnApply = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        mainOutputSizeWidth = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        mainOutputSizeHeight = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        mainOutputPositionTop = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        mainOutputPositionLeft = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        mainDisplayEngine = new javax.swing.JComboBox();
        mainOutput = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        btnSelectDeviceMain = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        btnConfigMain = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        monitorOutput = new javax.swing.JCheckBox();
        jLabel11 = new javax.swing.JLabel();
        monitorDisplayEngine = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        monitorOutputPositionLeft = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        monitorOutputPositionTop = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        monitorOutputSizeHeight = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        monitorOutputSizeWidth = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        btnSelectDeviceMonitor = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        btnConfigMonitor = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
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

        btnApply.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/apply.png")));
        btnApply.setText("Apply");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Main Output"));
        jLabel6.setText("Size");

        mainOutputSizeWidth.setText("jTextField1");

        jLabel16.setText("width");

        mainOutputSizeHeight.setText("jTextField1");

        jLabel17.setText("height");

        jLabel13.setText("top");

        mainOutputPositionTop.setText("jTextField1");

        jLabel12.setText("left");

        mainOutputPositionLeft.setText("jTextField1");

        jLabel3.setText("Position");

        jLabel10.setText("Display Engine");

        mainDisplayEngine.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        mainOutput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        mainOutput.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel1.setText("Enable main output");

        jLabel28.setText("Output Device");

        btnSelectDeviceMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/kcontrol.png")));
        btnSelectDeviceMain.setText("Select");
        btnSelectDeviceMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectDeviceMainActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Dialog", 2, 10));
        jLabel30.setText("* Using 640x480 is highly recommented for better performance");

        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/projector2_22x22.png")));

        jLabel24.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel24.setText("Main output is used for displaying information for the public,");

        jLabel32.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel32.setText("usually connected to a projector.");

        btnConfigMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/configure.png")));
        btnConfigMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigMainActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel37.setText("Some configurations apply only after application restart");

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(mainOutput))
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel28)
                            .add(jLabel3)
                            .add(jLabel6)
                            .add(jLabel10))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnSelectDeviceMain)
                            .add(jPanel5Layout.createSequentialGroup()
                                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(mainOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(mainOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(15, 15, 15)
                                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel12)
                                    .add(jLabel16))
                                .add(8, 8, 8)
                                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jPanel5Layout.createSequentialGroup()
                                        .add(mainOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabel13))
                                    .add(jPanel5Layout.createSequentialGroup()
                                        .add(mainOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabel17))))
                            .add(jPanel5Layout.createSequentialGroup()
                                .add(mainDisplayEngine, 0, 183, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnConfigMain)
                                .add(16, 16, 16))))
                    .add(jLabel30)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel23)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel32)
                            .add(jLabel24)))
                    .add(jLabel37))
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
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel1)
                            .add(mainOutput))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel10)
                            .add(mainDisplayEngine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel28)
                            .add(btnSelectDeviceMain)))
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(25, 25, 25)
                        .add(btnConfigMain)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel12)
                    .add(mainOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel13)
                    .add(jLabel3)
                    .add(mainOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel16)
                    .add(mainOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel17)
                    .add(jLabel6)
                    .add(mainOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel30)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel37)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Monitor Output"));
        jLabel2.setText("Enable monitor output");

        monitorOutput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        monitorOutput.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel11.setText("Display Engine");

        monitorDisplayEngine.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Position");

        monitorOutputPositionLeft.setText("jTextField1");

        jLabel14.setText("left");

        monitorOutputPositionTop.setText("jTextField1");

        jLabel15.setText("top");

        jLabel19.setText("height");

        monitorOutputSizeHeight.setText("jTextField1");

        jLabel18.setText("width");

        monitorOutputSizeWidth.setText("jTextField1");

        jLabel5.setText("Size");

        jLabel29.setText("Output Device");

        btnSelectDeviceMonitor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/kcontrol.png")));
        btnSelectDeviceMonitor.setText("Select");
        btnSelectDeviceMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectDeviceMonitorActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Dialog", 2, 10));
        jLabel31.setText("* Using 640x480 is highly recommented for better performance");

        jLabel20.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/display_22x22.png")));

        jLabel21.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel21.setText("Monitor output is used to display information in monitors on");

        jLabel22.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel22.setText("stage, providing feedback for musicans or preachers.");

        btnConfigMonitor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/configure.png")));
        btnConfigMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigMonitorActionPerformed(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel38.setText("Some configurations apply only after application restart");

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(monitorOutput))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel11)
                            .add(jLabel29)
                            .add(jLabel4)
                            .add(jLabel5))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(btnSelectDeviceMonitor)
                            .add(jPanel4Layout.createSequentialGroup()
                                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jPanel4Layout.createSequentialGroup()
                                        .add(monitorOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabel18))
                                    .add(jPanel4Layout.createSequentialGroup()
                                        .add(monitorOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabel14)))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, monitorOutputSizeHeight, 0, 0, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, monitorOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel19)
                                    .add(jLabel15)))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                                .add(monitorDisplayEngine, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnConfigMonitor)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))))
                    .add(jLabel31)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel20)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel22)
                            .add(jLabel21)))
                    .add(jLabel38))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(monitorOutput)
                            .add(jLabel2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel11)
                            .add(monitorDisplayEngine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel29)
                            .add(btnSelectDeviceMonitor)))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(26, 26, 26)
                        .add(btnConfigMonitor)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel4)
                    .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(monitorOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel14))
                    .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel15)
                        .add(monitorOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel5)
                    .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(monitorOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel18)
                        .add(monitorOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel19)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel31)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel38)
                .addContainerGap())
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Clock"));
        jLabel9.setText("Clock format");

        clockMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel36.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/clock.png")));
        jLabel36.setText("Specify the format to display the time in templates that contains clocks");

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
                .addContainerGap(364, Short.MAX_VALUE))
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
        jLabel7.setText("For monitor templates, show only itens containing *");

        txtMonitorTemplateFilter.setText("filter");

        txtAlertTemplateFilter.setText("filter");

        jLabel25.setText("For alert templates, show only itens containing *");

        jLabel26.setText("For general templates, show only itens containing *");

        txtGeneralTemplateFilter.setText("filter");

        jLabel27.setFont(new java.awt.Font("Dialog", 2, 10));
        jLabel27.setText("* leave blank to view all available templates");

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/datasoul/icons/template_filter.png")));

        jLabel34.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel34.setText("Template filters makes easy to find the right template to select in each situation.");

        jLabel8.setText("Default template for text items");

        templateText.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "monitor", "Template1", "Aviso 1", "Biblia40dias", "Musicas40Dias", "Avisos40Dias", "teste123" }));
        templateText.setOpaque(false);

        jLabel35.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel35.setText("You can also specify the default template selected for newly created items.");

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
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
                            .add(jPanel9Layout.createSequentialGroup()
                                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(txtMonitorTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(txtAlertTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(40, 40, 40)
                                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(templateText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 207, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(jLabel8))))))
                .addContainerGap(33, Short.MAX_VALUE))
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
                    .add(txtMonitorTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel8))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel25)
                    .add(txtAlertTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(templateText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel26)
                    .add(txtGeneralTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel27)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, btnApply)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                            .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 160, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnApply)
                .addContainerGap(121, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfigMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigMonitorActionPerformed
        showConfig(true);
    }//GEN-LAST:event_btnConfigMonitorActionPerformed

    private void btnConfigMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigMainActionPerformed
        showConfig(false);
    }//GEN-LAST:event_btnConfigMainActionPerformed

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
            JOptionPane.showMessageDialog(this, "Unable to create configuration for "+render, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void btnSelectDeviceMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectDeviceMonitorActionPerformed
        jPpMenuMonitor.show(btnSelectDeviceMonitor, btnSelectDeviceMonitor.getWidth(), 0);
    }//GEN-LAST:event_btnSelectDeviceMonitorActionPerformed

    private void btnSelectDeviceMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectDeviceMainActionPerformed
         jPpMenuMain.show(btnSelectDeviceMain, btnSelectDeviceMain.getWidth(), 0);
    }//GEN-LAST:event_btnSelectDeviceMainActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed

        try{
            ObjectManager.getInstance().setBusyCursor();
            refreshObjectValues();
            configObj.save();
        }finally{
            ObjectManager.getInstance().setDefaultCursor();
        }
        
            
    }//GEN-LAST:event_btnApplyActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnConfigMain;
    private javax.swing.JButton btnConfigMonitor;
    private javax.swing.JButton btnSelectDeviceMain;
    private javax.swing.JButton btnSelectDeviceMonitor;
    private javax.swing.JComboBox clockMode;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JLabel jLabel38;
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
    private javax.swing.JComboBox mainDisplayEngine;
    private javax.swing.JCheckBox mainOutput;
    private javax.swing.JTextField mainOutputPositionLeft;
    private javax.swing.JTextField mainOutputPositionTop;
    private javax.swing.JTextField mainOutputSizeHeight;
    private javax.swing.JTextField mainOutputSizeWidth;
    private javax.swing.JComboBox monitorDisplayEngine;
    private javax.swing.JCheckBox monitorOutput;
    private javax.swing.JTextField monitorOutputPositionLeft;
    private javax.swing.JTextField monitorOutputPositionTop;
    private javax.swing.JTextField monitorOutputSizeHeight;
    private javax.swing.JTextField monitorOutputSizeWidth;
    private datasoul.templates.TemplateComboBox templateText;
    private javax.swing.JTextField txtAlertTemplateFilter;
    private javax.swing.JTextField txtGeneralTemplateFilter;
    private javax.swing.JTextField txtMonitorTemplateFilter;
    // End of variables declaration//GEN-END:variables
    
}
