/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demo.fileViewer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.demo.fileservice.Thumbnail;
import org.openide.util.Exceptions;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Lookup;
import org.openide.util.LookupListener;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.demo.fileViewer//FileViewer//EN",
autostore = false)
public final class FileViewerTopComponent extends TopComponent implements LookupListener {

    private static FileViewerTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "FileViewerTopComponent";
    private Lookup.Result result = null;
    private FileObject currentFile = null;
    private final InstanceContent content;

    public FileViewerTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(FileViewerTopComponent.class, "CTL_FileViewerTopComponent"));
        setToolTipText(NbBundle.getMessage(FileViewerTopComponent.class, "HINT_FileViewerTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        content = new InstanceContent();
        associateLookup(new AbstractLookup(content));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        view = new JImagePreviewPanel();

        setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout viewLayout = new javax.swing.GroupLayout(view);
        view.setLayout(viewLayout);
        viewLayout.setHorizontalGroup(
            viewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        viewLayout.setVerticalGroup(
            viewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(view, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel view;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized FileViewerTopComponent getDefault() {
        if (instance == null) {
            instance = new FileViewerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the FileViewerTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized FileViewerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(FileViewerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof FileViewerTopComponent) {
            return (FileViewerTopComponent) win;
        }
        Logger.getLogger(FileViewerTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                TopComponent tc = WindowManager.getDefault().findTopComponent("FileBrowserTopComponent");
                if (tc == null) {
                    // XXX: message box?
                    return;
                }
                result = tc.getLookup().lookupResult(FileObject.class);
                result.addLookupListener(FileViewerTopComponent.this);
                resultChanged(new LookupEvent(result));
            }
        });
    }

    @Override
    public void componentClosed() {
        result.removeLookupListener(this);
        result = null;
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result r = (Lookup.Result) ev.getSource();
        Collection<FileObject> coll = r.allInstances();
        if (!coll.isEmpty()) {

            currentFile = coll.iterator().next();
            BufferedImage loadImage = null;
            try {
                DataObject document = DataObject.find(currentFile);
                if (document instanceof Thumbnail) {
                    loadImage = ((Thumbnail) document).getThumbnail();
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            ((JImagePreviewPanel) view).setImage(loadImage);
        } else {
            currentFile = null;
        }

    }
}
