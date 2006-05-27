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
        registerComponent(mainOutput,"MainOutput");
        registerComponent(mainOutputPositionLeft,"MainOutputPositionLeft");
        registerComponent(mainOutputPositionTop,"MainOutputPositionTop");
        registerComponent(mainOutputSizeHeight,"MainOutputSizeHeight");
        registerComponent(mainOutputSizeWidth,"MainOutputSizeWidth");
        registerComponent(monitorOutput,"MonitorOutput");
        registerComponent(monitorOutputPositionLeft,"MonitorOutputPositionLeft");
        registerComponent(monitorOutputPositionTop,"MonitorOutputPositionTop");
        registerComponent(monitorOutputSizeHeight,"MonitorOutputSizeHeight");
        registerComponent(monitorOutputSizeWidth,"MonitorOutputSizeWidth");
        registerComponent(templateMonitor,"TemplateMonitor");
        registerComponent(templateText,"TemplateText");
        
        registerComponent(videoInput,"VideoInput");
        videoInput.removeAllItems();
        videoInput.addItem("Tuner");
        videoInput.addItem("S-Video");
        videoInput.addItem("Composite");
        
        registerComponent(videoDeintrelace,"VideoDeintrelace");
        videoDeintrelace.removeAllItems();        
        videoDeintrelace.addItem("None");
        videoDeintrelace.addItem("Blend");
        videoDeintrelace.addItem("Smart blend");
        videoDeintrelace.addItem("Smooth blend");
        
        registerComponent(videoMode,"VideoMode");
        videoMode.removeAllItems();
        videoMode.addItem("PAL");
        videoMode.addItem("NTSC");
        videoMode.addItem("SECAM");

        registerComponent(clockMode,"ClockMode");
        clockMode.removeAllItems();
        clockMode.addItem("24 Hours with Seconds");
        clockMode.addItem("24 Hours without Seconds");
        clockMode.addItem("AM/PM with Seconds");
        clockMode.addItem("AM/PM without Seconds");
        
        registerComponent(videoDebugMode, "VideoDebugMode");
        videoDebugMode.removeAllItems();
        videoDebugMode.addItem("Off");
        videoDebugMode.addItem("On");
        
        registerComponent( mainDisplayEngine, "MainDisplayEngine" );
        mainDisplayEngine.removeAllItems();
        mainDisplayEngine.addItem("SDLContentRender");
        mainDisplayEngine.addItem("SwingContentRender");

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
            if(string != null && string.compareToIgnoreCase("TRUE")==0){
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
                return "TRUE";
            }else{
                return "FALSE";
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
        panelVideo = new javax.swing.JPanel();
        videoInputLabel = new javax.swing.JLabel();
        videoInput = new javax.swing.JComboBox();
        videoModeLabel = new javax.swing.JLabel();
        videoMode = new javax.swing.JComboBox();
        videoDeintrelaceLabel = new javax.swing.JLabel();
        videoDeintrelace = new javax.swing.JComboBox();
        videoDebugMode = new javax.swing.JComboBox();
        videoDebugModeLabel = new javax.swing.JLabel();
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
        btnCancel = new javax.swing.JButton();
        btnApply = new javax.swing.JButton();

        tabConfig.setFont(new java.awt.Font("Arial", 1, 11));
        tabConfig.setMinimumSize(new java.awt.Dimension(0, 0));
        videoInputLabel.setFont(new java.awt.Font("Arial", 1, 11));
        videoInputLabel.setText("Video input:");

        videoInput.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        videoInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                videoInputActionPerformed(evt);
            }
        });

        videoModeLabel.setFont(new java.awt.Font("Arial", 1, 11));
        videoModeLabel.setText("Video mode:");

        videoMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        videoMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                videoModeActionPerformed(evt);
            }
        });

        videoDeintrelaceLabel.setFont(new java.awt.Font("Arial", 1, 11));
        videoDeintrelaceLabel.setText("Video deintrelace:");

        videoDeintrelace.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        videoDeintrelace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                videoDeintrelaceActionPerformed(evt);
            }
        });

        videoDebugMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        videoDebugModeLabel.setText("Debug Mode:");

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
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, videoDebugMode, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, videoInput, 0, 137, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, videoMode, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, videoDeintrelace, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(331, Short.MAX_VALUE))
        );
        panelVideoLayout.setVerticalGroup(
            panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelVideoLayout.createSequentialGroup()
                .add(25, 25, 25)
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoInputLabel)
                    .add(videoInput, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoModeLabel)
                    .add(videoMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoDeintrelaceLabel)
                    .add(videoDeintrelace, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(panelVideoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(videoDebugMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(videoDebugModeLabel))
                .addContainerGap(244, Short.MAX_VALUE))
        );
        tabConfig.addTab("Video", panelVideo);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel1.setText("Use main output:");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel2.setText("Use monitor output:");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel3.setText("Main output position:");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel4.setText("Monitor output position:");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel5.setText("Monitor output size:");

        mainOutput.setFont(new java.awt.Font("Arial", 1, 11));
        mainOutput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        mainOutput.setMargin(new java.awt.Insets(0, 0, 0, 0));

        monitorOutput.setFont(new java.awt.Font("Arial", 1, 11));
        monitorOutput.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        monitorOutput.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel6.setText("Main output size:");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel7.setText("Template to monitor output:");

        jLabel8.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel8.setText("Template default to text:");

        mainOutputPositionLeft.setText("jTextField1");

        jLabel12.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel12.setText("left");

        mainOutputPositionTop.setText("jTextField1");

        jLabel13.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel13.setText("top");

        monitorOutputPositionTop.setText("jTextField1");

        jLabel14.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel14.setText("left");

        monitorOutputPositionLeft.setText("jTextField1");

        jLabel15.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel15.setText("top");

        mainOutputSizeWidth.setText("jTextField1");

        jLabel16.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel16.setText("width");

        mainOutputSizeHeight.setText("jTextField1");

        jLabel17.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel17.setText("height");

        monitorOutputSizeWidth.setText("jTextField1");

        monitorOutputSizeHeight.setText("jTextField1");

        jLabel18.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel18.setText("width");

        jLabel19.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel19.setText("height");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel9.setText("Clock mode:");

        clockMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 11));
        jLabel10.setText("Main output engine:");

        mainDisplayEngine.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 11));
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
                            .add(templateMonitor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(panelGeneralLayout.createSequentialGroup()
                                .add(panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelGeneralLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, clockMode, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(org.jdesktop.layout.GroupLayout.LEADING, templateText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, Short.MAX_VALUE))
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelGeneralLayout.createSequentialGroup()
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
                                            .add(jLabel18))))
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
                                            .add(jLabel19))))))))
                .addContainerGap(178, Short.MAX_VALUE))
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

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

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
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(btnApply)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnCancel)
                        .add(12, 12, 12))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(tabConfig, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(tabConfig, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnCancel)
                    .add(btnApply))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void videoDeintrelaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoDeintrelaceActionPerformed

        
    }//GEN-LAST:event_videoDeintrelaceActionPerformed

    private void videoModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoModeActionPerformed
        
    }//GEN-LAST:event_videoModeActionPerformed

    private void videoInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_videoInputActionPerformed

    }//GEN-LAST:event_videoInputActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.refreshScreenValues();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
        refreshObjectValues();
        configObj.save();
    }//GEN-LAST:event_btnApplyActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton btnCancel;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JComboBox videoDebugMode;
    private javax.swing.JLabel videoDebugModeLabel;
    private javax.swing.JComboBox videoDeintrelace;
    private javax.swing.JLabel videoDeintrelaceLabel;
    private javax.swing.JComboBox videoInput;
    private javax.swing.JLabel videoInputLabel;
    private javax.swing.JComboBox videoMode;
    private javax.swing.JLabel videoModeLabel;
    // End of variables declaration//GEN-END:variables
    
}
