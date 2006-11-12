/*
 * ConfigForm.java
 *
 * Created on 22 de Marco de 2006, 21:22
 */

package datasoul.config;

import datasoul.*;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
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
        
        for(int i=0;i<this.tabConfig.getTabCount();i++){
            if(this.tabConfig.getTitleAt(i).equals("SDL Renderer")){
                this.tabConfig.removeTabAt(i);
            }
        }
        
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
        
        /*
        registerComponent(videoInput,"VideoInput");
        videoInput.removeAllItems();
        for (int i=0; i<ConfigObj.VIDEOINPUT_TABLE.length; i++){
            videoInput.addItem(ConfigObj.VIDEOINPUT_TABLE[i]);
        }
        
        registerComponent(videoDeintrelace,"VideoDeintrelace");
        videoDeintrelace.removeAllItems();        
        for (int i=0; i<ConfigObj.VIDEODEINTRELACE_TABLE.length; i++){
            videoDeintrelace.addItem(ConfigObj.VIDEODEINTRELACE_TABLE[i]);
        }
        
        registerComponent(videoMode,"VideoMode");
        videoMode.removeAllItems();
        for (int i=0; i<ConfigObj.VIDEOMODE_TABLE.length; i++){
            videoMode.addItem(ConfigObj.VIDEOMODE_TABLE[i]);
        }
        */        
        
        registerComponent(clockMode,"ClockMode");
        clockMode.removeAllItems();
        for (int i=0; i<ConfigObj.CLOCKMODE_TABLE.length; i++){
            clockMode.addItem(ConfigObj.CLOCKMODE_TABLE[i]);
        }
        
        registerComponent(videoDebugMode, "VideoDebugModeIdx");
        
        registerComponent( mainDisplayEngine, "MainDisplayEngine" );
        mainDisplayEngine.removeAllItems();
        mainDisplayEngine.addItem("SDLContentRender");
        mainDisplayEngine.addItem("SwingContentRender");
        mainDisplayEngine.addItem("RemoteContentRender");

        registerComponent( monitorDisplayEngine, "MonitorDisplayEngine" );
        monitorDisplayEngine.removeAllItems();
        monitorDisplayEngine.addItem("SDLContentRender");
        monitorDisplayEngine.addItem("SwingContentRender");
        
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
        tabConfig = new javax.swing.JTabbedPane();
        panelGeneral = new javax.swing.JPanel();
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
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        clockMode = new javax.swing.JComboBox();
        templateText = new datasoul.templates.TemplateComboBox();
        jLabel8 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtMonitorTemplateFilter = new javax.swing.JTextField();
        txtAlertTemplateFilter = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtGeneralTemplateFilter = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        panelVideo = new javax.swing.JPanel();
        videoDebugModeLabel = new javax.swing.JLabel();
        videoDebugMode = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        videoInputLabel = new javax.swing.JLabel();
        videoInput = new javax.swing.JComboBox();
        videoModeLabel = new javax.swing.JLabel();
        videoMode = new javax.swing.JComboBox();
        videoDeintrelaceLabel = new javax.swing.JLabel();
        videoDeintrelace = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        btnApply = new javax.swing.JButton();

        tabConfig.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Monitor Output"));
        jLabel2.setText("Use monitor output:");

        monitorOutput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        monitorOutput.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel11.setText("Display Engine:");

        monitorDisplayEngine.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Position:");

        monitorOutputPositionLeft.setText("jTextField1");

        jLabel14.setText("left");

        monitorOutputPositionTop.setText("jTextField1");

        jLabel15.setText("top");

        jLabel19.setText("height");

        monitorOutputSizeHeight.setText("jTextField1");

        jLabel18.setText("width");

        monitorOutputSizeWidth.setText("jTextField1");

        jLabel5.setText("Size:");

        jLabel29.setText("Output Device:");

        btnSelectDeviceMonitor.setText("Select");
        btnSelectDeviceMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectDeviceMonitorActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Dialog", 2, 10));
        jLabel31.setText("* Using 640x480 is highly recommented for better performance");

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
                            .add(jLabel5))
                        .add(27, 27, 27)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnSelectDeviceMonitor)
                            .add(jPanel4Layout.createSequentialGroup()
                                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, monitorDisplayEngine, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(jPanel4Layout.createSequentialGroup()
                                                .add(monitorOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jLabel14))
                                            .add(jPanel4Layout.createSequentialGroup()
                                                .add(monitorOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jLabel18)))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                            .add(monitorOutputSizeHeight, 0, 0, Short.MAX_VALUE)
                                            .add(monitorOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(jLabel19)
                                            .add(jLabel15))))
                                .add(23, 23, 23)
                                .add(jLabel31))))
                    .add(jLabel4))
                .addContainerGap(133, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel2)
                    .add(monitorOutput))
                .add(25, 25, 25)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel11)
                            .add(monitorDisplayEngine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel29)
                            .add(btnSelectDeviceMonitor))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel4)
                            .add(monitorOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel14))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel5)
                            .add(monitorOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel18)))
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(monitorOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel15))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(monitorOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel19)
                            .add(jLabel31))))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Main Output"));
        jLabel6.setText("Size:");

        mainOutputSizeWidth.setText("jTextField1");

        jLabel16.setText("width");

        mainOutputSizeHeight.setText("jTextField1");

        jLabel17.setText("height");

        jLabel13.setText("top");

        mainOutputPositionTop.setText("jTextField1");

        jLabel12.setText("left");

        mainOutputPositionLeft.setText("jTextField1");

        jLabel3.setText("Position:");

        jLabel10.setText("Display Engine:");

        mainDisplayEngine.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        mainOutput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        mainOutput.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel1.setText("Use main output:");

        jLabel28.setText("Output Device:");

        btnSelectDeviceMain.setText("Select");
        btnSelectDeviceMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectDeviceMainActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Dialog", 2, 10));
        jLabel30.setText("* Using 640x480 is highly recommented for better performance");

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
                            .add(jLabel10)
                            .add(jLabel28)
                            .add(jLabel3)
                            .add(jLabel6))
                        .add(15, 15, 15)
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btnSelectDeviceMain)
                            .add(jPanel5Layout.createSequentialGroup()
                                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(mainOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(mainOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
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
                            .add(mainDisplayEngine, 0, 186, Short.MAX_VALUE))))
                .add(14, 14, 14)
                .add(jLabel30)
                .add(243, 243, 243))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(mainOutput))
                .add(17, 17, 17)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel10)
                    .add(mainDisplayEngine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel28)
                    .add(btnSelectDeviceMain))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(mainOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel12)
                    .add(mainOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel13)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(mainOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel16)
                    .add(mainOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel17)
                    .add(jLabel6)
                    .add(jLabel30))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout panelGeneralLayout = new org.jdesktop.layout.GroupLayout(panelGeneral);
        panelGeneral.setLayout(panelGeneralLayout);
        panelGeneralLayout.setHorizontalGroup(
            panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelGeneralLayout.setVerticalGroup(
            panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(262, 262, 262))
        );
        tabConfig.addTab("General", panelGeneral);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("General"));
        jLabel9.setText("Clock mode:");

        clockMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        templateText.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "monitor", "Template1", "Aviso 1", "Biblia40dias", "Musicas40Dias", "Avisos40Dias", "teste123" }));
        templateText.setOpaque(false);

        jLabel8.setText("Default template for text items:");

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel9)
                    .add(jLabel8))
                .add(14, 14, 14)
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(clockMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 207, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(templateText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 207, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(355, 355, 355))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(clockMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(templateText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel8))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Filters"));
        jLabel7.setText("For monitor templates, show only itens containing: *");

        txtMonitorTemplateFilter.setText("filter");

        txtAlertTemplateFilter.setText("filter");

        jLabel25.setText("For alert templates, show only itens containing: *");

        jLabel26.setText("For general templates, show only itens containing: *");

        txtGeneralTemplateFilter.setText("filter");

        jLabel27.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel27.setText("* leave blank for showing all templates available");

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel9Layout.createSequentialGroup()
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel7)
                            .add(jLabel25)
                            .add(jLabel26))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(txtGeneralTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtAlertTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtMonitorTemplateFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel9Layout.createSequentialGroup()
                        .add(jLabel27)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(355, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
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
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                    .add(jPanel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(430, Short.MAX_VALUE))
        );
        tabConfig.addTab("Templates", jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Main Output"));
        jLabel21.setText("Host:");

        jTextField1.setText("127.0.0.1");

        jLabel22.setText("Port:");

        jTextField2.setText("10001");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel21)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel22)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(601, 601, 601))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel21)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel22)
                    .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Monitor Output"));
        jLabel23.setText("Port:");

        jLabel32.setText("Host:");

        jTextField3.setText("127.0.0.1");

        jTextField4.setText("10002");

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel32)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField3))
                    .add(jPanel7Layout.createSequentialGroup()
                        .add(jLabel23)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(602, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel32)
                    .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel23)
                    .add(jTextField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(500, Short.MAX_VALUE))
        );
        tabConfig.addTab("Remote Renderer", jPanel1);

        videoDebugModeLabel.setText("Debug Mode:");

        videoDebugMode.setText("Debug enabled");
        videoDebugMode.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        videoDebugMode.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Video4Linux"));
        videoInputLabel.setText("Video input:");

        videoInput.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        videoInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                videoInputActionPerformed(evt);
            }
        });

        videoModeLabel.setText("Video mode:");

        videoMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        videoMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                videoModeActionPerformed(evt);
            }
        });

        videoDeintrelaceLabel.setText("Video deintrelace:");

        videoDeintrelace.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        videoDeintrelace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                videoDeintrelaceActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel20.setText("* this option depends on your hardware configuration");

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(videoDeintrelaceLabel))
                    .add(videoInputLabel)
                    .add(videoModeLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(videoMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 223, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(videoDeintrelace, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 223, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(videoInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 222, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 249, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoInputLabel)
                    .add(jLabel20)
                    .add(videoInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoModeLabel)
                    .add(videoMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoDeintrelace, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(videoDeintrelaceLabel))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout panelVideoLayout = new org.jdesktop.layout.GroupLayout(panelVideo);
        panelVideo.setLayout(panelVideoLayout);
        panelVideoLayout.setHorizontalGroup(
            panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelVideoLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                    .add(panelVideoLayout.createSequentialGroup()
                        .add(videoDebugModeLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(videoDebugMode)))
                .addContainerGap())
        );
        panelVideoLayout.setVerticalGroup(
            panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelVideoLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoDebugModeLabel)
                    .add(videoDebugMode))
                .addContainerGap(511, Short.MAX_VALUE))
        );
        tabConfig.addTab("SDL Renderer", panelVideo);

        btnApply.setText("Apply and Save Defaults");
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(tabConfig, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 781, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(btnApply)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(tabConfig, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnApply, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSelectDeviceMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectDeviceMonitorActionPerformed
        jPpMenuMonitor.show(btnSelectDeviceMonitor, btnSelectDeviceMonitor.getWidth(), 0);
    }//GEN-LAST:event_btnSelectDeviceMonitorActionPerformed

    private void btnSelectDeviceMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectDeviceMainActionPerformed
         jPpMenuMain.show(btnSelectDeviceMain, btnSelectDeviceMain.getWidth(), 0);
    }//GEN-LAST:event_btnSelectDeviceMainActionPerformed

    private void videoDeintrelaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoDeintrelaceActionPerformed

        
    }//GEN-LAST:event_videoDeintrelaceActionPerformed

    private void videoModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoModeActionPerformed
        
    }//GEN-LAST:event_videoModeActionPerformed

    private void videoInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoInputActionPerformed

    }//GEN-LAST:event_videoInputActionPerformed

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
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu jPpMenuMain;
    private javax.swing.JPopupMenu jPpMenuMonitor;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
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
    private javax.swing.JPanel panelGeneral;
    private javax.swing.JPanel panelVideo;
    private javax.swing.JTabbedPane tabConfig;
    private datasoul.templates.TemplateComboBox templateText;
    private javax.swing.JTextField txtAlertTemplateFilter;
    private javax.swing.JTextField txtGeneralTemplateFilter;
    private javax.swing.JTextField txtMonitorTemplateFilter;
    private javax.swing.JCheckBox videoDebugMode;
    private javax.swing.JLabel videoDebugModeLabel;
    private javax.swing.JComboBox videoDeintrelace;
    private javax.swing.JLabel videoDeintrelaceLabel;
    private javax.swing.JComboBox videoInput;
    private javax.swing.JLabel videoInputLabel;
    private javax.swing.JComboBox videoMode;
    private javax.swing.JLabel videoModeLabel;
    // End of variables declaration//GEN-END:variables
    
}
