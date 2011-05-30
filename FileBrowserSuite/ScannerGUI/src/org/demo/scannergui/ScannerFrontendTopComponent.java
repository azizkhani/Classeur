/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demo.scannergui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.demo.core.CentralLookup;
import org.demo.fileservice.DocumentWriter;
import org.demo.fileservice.Extension;
import org.demo.fileservice.ExtensionMap;
import org.demo.scannerservice.ScannerManager;
import org.demo.scannerservice.ScannerFactory;
import org.demo.scannerservice.ScannerListener;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.demo.scannergui//ScannerFrontend//EN",
autostore = false)
public final class ScannerFrontendTopComponent extends TopComponent {

    private static ScannerFrontendTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "ScannerFrontendTopComponent";    
    
    public ScannerFrontendTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(ScannerFrontendTopComponent.class, "CTL_ScannerFrontendTopComponent"));
        setToolTipText(NbBundle.getMessage(ScannerFrontendTopComponent.class, "HINT_ScannerFrontendTopComponent"));
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                init();
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        formatsCombobox = new javax.swing.JComboBox();
        scanBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ScannerFrontendTopComponent.class, "ScannerFrontendTopComponent.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ScannerFrontendTopComponent.class, "ScannerFrontendTopComponent.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(scanBtn, org.openide.util.NbBundle.getMessage(ScannerFrontendTopComponent.class, "ScannerFrontendTopComponent.scanBtn.text")); // NOI18N
        scanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                scanBtnActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(ScannerFrontendTopComponent.class, "ScannerFrontendTopComponent.jButton2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scanBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, 148, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(formatsCombobox, 0, 148, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scanBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(formatsCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void scanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_scanBtnActionPerformed
        scanBtn.setEnabled(false);
        ScannerManager manager = ScannerFactory.getManager();
        manager.addListener(new ScannerListener() {

            @Override
            public void statusUpdated() {
                System.out.println("scanner updated");
            }

            @Override
            public void imageAcquired(final BufferedImage image) {

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            TopComponent tc = WindowManager.getDefault().findTopComponent("FolderViewerTopComponent");
                            if (tc == null) {
                                // XXX: message box?
                                return;
                            }
                            FileObject folder = tc.getLookup().lookup(FileObject.class);
                            if (folder == null) {
                                // XXX: message box?
                                return;
                            }

                            FileObject fo = folder.createData("temp."+((Extension)formatsCombobox.getSelectedItem()).getExtension());
                            DataObject f = DataObject.find(fo);

                            if(f instanceof DocumentWriter){
                                ((DocumentWriter)f).write(image);
                            }
                            
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                });


            }
        });
        manager.acquire();
    }//GEN-LAST:event_scanBtnActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox formatsCombobox;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton scanBtn;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized ScannerFrontendTopComponent getDefault() {
        if (instance == null) {
            instance = new ScannerFrontendTopComponent();
        }
        return instance;
    }    
    
    /**
     * Obtain the FileViewerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ScannerFrontendTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(ScannerFrontendTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof ScannerFrontendTopComponent) {
            return (ScannerFrontendTopComponent) win;
        }
        Logger.getLogger(ScannerFrontendTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }    
    
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private void init() {
        ExtensionMap exts = CentralLookup.getDefault().lookup(ExtensionMap.class);
        for(Extension extension:exts){
            formatsCombobox.addItem(extension);
        }
    }
}
