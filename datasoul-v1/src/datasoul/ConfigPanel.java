/*
 * ConfigForm.java
 *
 * Created on 22 de Marco de 2006, 21:22
 */

package datasoul;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
        registerComponent(templateMonitor,"TemplateMonitor");
        registerComponent(templateText,"TemplateText");
        
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
        tabConfig = new javax.swing.JTabbedPane();
        panelGeneral = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        mainOutput = new javax.swing.JCheckBox();
        monitorOutput = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        mainOutputPositionLeft = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        mainOutputPositionTop = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        monitorOutputPositionTop = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        monitorOutputPositionLeft = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        mainOutputSizeWidth = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        mainOutputSizeHeight = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        monitorOutputSizeWidth = new javax.swing.JTextField();
        monitorOutputSizeHeight = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        clockMode = new javax.swing.JComboBox();
        templateMonitor = new datasoul.templates.TemplateComboBox();
        templateText = new datasoul.templates.TemplateComboBox();
        jLabel10 = new javax.swing.JLabel();
        mainDisplayEngine = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        monitorDisplayEngine = new javax.swing.JComboBox();
        panelVideo = new javax.swing.JPanel();
        videoInputLabel = new javax.swing.JLabel();
        videoInput = new javax.swing.JComboBox();
        videoModeLabel = new javax.swing.JLabel();
        videoMode = new javax.swing.JComboBox();
        videoDeintrelaceLabel = new javax.swing.JLabel();
        videoDeintrelace = new javax.swing.JComboBox();
        videoDebugModeLabel = new javax.swing.JLabel();
        videoDebugMode = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        btnApply = new javax.swing.JButton();

        tabConfig.setMinimumSize(new java.awt.Dimension(0, 0));
        jLabel1.setText("Use main output:");

        jLabel2.setText("Use monitor output:");

        jLabel3.setText("Main output position:");

        jLabel4.setText("Monitor output position:");

        jLabel5.setText("Monitor output size:");

        mainOutput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        mainOutput.setMargin(new java.awt.Insets(0, 0, 0, 0));

        monitorOutput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        monitorOutput.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel6.setText("Main output size:");

        jLabel7.setText("Template to monitor output:");

        jLabel8.setText("Template default to text:");

        mainOutputPositionLeft.setText("jTextField1");

        jLabel12.setText("left");

        mainOutputPositionTop.setText("jTextField1");

        jLabel13.setText("top");

        monitorOutputPositionTop.setText("jTextField1");

        jLabel14.setText("left");

        monitorOutputPositionLeft.setText("jTextField1");

        jLabel15.setText("top");

        mainOutputSizeWidth.setText("jTextField1");

        jLabel16.setText("width");

        mainOutputSizeHeight.setText("jTextField1");

        jLabel17.setText("height");

        monitorOutputSizeWidth.setText("jTextField1");

        monitorOutputSizeHeight.setText("jTextField1");

        jLabel18.setText("width");

        jLabel19.setText("height");

        jLabel9.setText("Clock mode:");

        clockMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        templateMonitor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "monitor", "Template1", "BigClock", "Aviso 1", "Biblia40dias", "Musicas40Dias", "Avisos40Dias" }));

        templateText.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "monitor", "Template1", "BigClock", "Aviso 1", "Biblia40dias", "Musicas40Dias", "Avisos40Dias" }));

        jLabel10.setText("Main output engine:");

        mainDisplayEngine.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel11.setText("Monitor output engine:");

        monitorDisplayEngine.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        org.jdesktop.layout.GroupLayout panelGeneralLayout = new org.jdesktop.layout.GroupLayout(panelGeneral);
        panelGeneral.setLayout(panelGeneralLayout);
        panelGeneralLayout.setHorizontalGroup(
            panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel5)
                    .add(panelGeneralLayout.createSequentialGroup()
                        .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel7)
                            .add(jLabel8)
                            .add(jLabel3)
                            .add(jLabel6)
                            .add(jLabel4)
                            .add(jLabel11)
                            .add(jLabel10)
                            .add(jLabel2)
                            .add(jLabel1)
                            .add(jLabel9))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(monitorDisplayEngine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(monitorOutput)
                            .add(mainDisplayEngine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(mainOutput)
                            .add(panelGeneralLayout.createSequentialGroup()
                                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(mainOutputSizeWidth, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                                    .add(monitorOutputPositionLeft, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                                    .add(monitorOutputSizeWidth, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                                    .add(mainOutputPositionLeft, 0, 0, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jLabel16)
                                    .add(jLabel12)
                                    .add(jLabel14)
                                    .add(jLabel18))
                                .add(61, 61, 61)
                                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(panelGeneralLayout.createSequentialGroup()
                                        .add(mainOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(jLabel13))
                                    .add(panelGeneralLayout.createSequentialGroup()
                                        .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                            .add(monitorOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(monitorOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(mainOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                            .add(jLabel15)
                                            .add(jLabel17)
                                            .add(jLabel19)))))
                            .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, clockMode, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, templateText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, templateMonitor, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)))))
                .addContainerGap(149, Short.MAX_VALUE))
        );
        panelGeneralLayout.setVerticalGroup(
            panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelGeneralLayout.createSequentialGroup()
                .add(23, 23, 23)
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(mainOutput)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel10)
                    .add(mainDisplayEngine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(mainOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel13)
                    .add(jLabel12)
                    .add(mainOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(panelGeneralLayout.createSequentialGroup()
                        .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(mainOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel17)
                            .add(jLabel6)
                            .add(mainOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel16))
                        .add(18, 18, 18)
                        .add(jLabel2))
                    .add(monitorOutput))
                .add(11, 11, 11)
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel11)
                    .add(monitorDisplayEngine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jLabel15)
                    .add(monitorOutputPositionTop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(monitorOutputPositionLeft, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel14))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(jLabel19)
                    .add(monitorOutputSizeHeight, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(monitorOutputSizeWidth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel18))
                .add(39, 39, 39)
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(templateMonitor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(templateText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(clockMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(38, 38, 38))
        );
        tabConfig.addTab("General", panelGeneral);

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

        videoDebugModeLabel.setText("Debug Mode:");

        videoDebugMode.setText("Debug enabled");
        videoDebugMode.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        videoDebugMode.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel20.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel20.setText("* this option depends on your hardware configuration");

        org.jdesktop.layout.GroupLayout panelVideoLayout = new org.jdesktop.layout.GroupLayout(panelVideo);
        panelVideo.setLayout(panelVideoLayout);
        panelVideoLayout.setHorizontalGroup(
            panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelVideoLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(videoInputLabel)
                    .add(videoModeLabel)
                    .add(videoDeintrelaceLabel)
                    .add(videoDebugModeLabel))
                .add(46, 46, 46)
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelVideoLayout.createSequentialGroup()
                        .add(videoDebugMode)
                        .addContainerGap())
                    .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(panelVideoLayout.createSequentialGroup()
                            .add(videoDeintrelace, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(390, Short.MAX_VALUE))
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, panelVideoLayout.createSequentialGroup()
                            .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, videoInput, 0, 68, Short.MAX_VALUE)
                                .add(videoMode, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(jLabel20)
                            .add(118, 118, 118)))))
        );
        panelVideoLayout.setVerticalGroup(
            panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelVideoLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoInputLabel)
                    .add(videoInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoModeLabel)
                    .add(videoMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel20))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoDeintrelaceLabel)
                    .add(videoDeintrelace, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoDebugMode)
                    .add(videoDebugModeLabel))
                .addContainerGap(265, Short.MAX_VALUE))
        );
        tabConfig.addTab("Video4Linux", panelVideo);

        jLabel21.setText("Main Output Host:");

        jTextField1.setText("127.0.0.1");

        jLabel22.setText("Main Output Port:");

        jLabel23.setText("Monitor Ouput Host:");

        jLabel24.setText("Monitor Ouput Port:");

        jTextField2.setText("10001");

        jTextField3.setText("127.0.0.1");

        jTextField4.setText("10002");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel21)
                    .add(jLabel22)
                    .add(jLabel24)
                    .add(jLabel23))
                .add(26, 26, 26)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jTextField3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(jTextField1)
                        .add(jTextField2)
                        .add(jTextField4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)))
                .addContainerGap(325, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel21)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel22)
                    .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(29, 29, 29)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel23)
                    .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel24)
                    .add(jTextField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(253, Short.MAX_VALUE))
        );
        tabConfig.addTab("Remote Renderer", jPanel1);

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
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, btnApply)
                    .add(tabConfig, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(tabConfig, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnApply)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void videoDeintrelaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoDeintrelaceActionPerformed

        
    }//GEN-LAST:event_videoDeintrelaceActionPerformed

    private void videoModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoModeActionPerformed
        
    }//GEN-LAST:event_videoModeActionPerformed

    private void videoInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoInputActionPerformed

    }//GEN-LAST:event_videoInputActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
        refreshObjectValues();
        configObj.save();
    }//GEN-LAST:event_btnApplyActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
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
    private datasoul.templates.TemplateComboBox templateMonitor;
    private datasoul.templates.TemplateComboBox templateText;
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
