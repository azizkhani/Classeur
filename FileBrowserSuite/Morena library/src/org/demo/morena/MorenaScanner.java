/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.demo.morena;

import SK.gnome.morena.MorenaSource;
import SK.gnome.sane.SaneSource;
import SK.gnome.twain.TwainException;
import SK.gnome.twain.TwainSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.demo.scannerservice.Scanner;

/**
 *
 * @author ECO
 */
public class MorenaScanner implements Scanner {

    private MorenaSource src;
    private String name;
    private List<Double> supportedResolutions=null;
    private List<Integer> supportedBitDepth=null;
    private List<String> supportedModes=null;
    
    public MorenaScanner(MorenaSource src) {
        this.src=src;
        name=src.toString();
        
        if(src instanceof TwainSource){
            TwainSource twainsrc=(TwainSource) src;
            try {
                supportedResolutions=new ArrayList<Double>();
                double[] supportedXResolution = twainsrc.getSupportedXResolution();
                for (double d : supportedXResolution) {
                    supportedResolutions.add(d);
                }
                
                supportedBitDepth=new ArrayList<Integer>();
                int[] supportedBitDepth1 = twainsrc.getSupportedBitDepth();
                for (int i : supportedBitDepth1) {
                    supportedBitDepth.add(i);
                }
                
                supportedModes=new ArrayList<String>();
            } catch (TwainException ex) {
                Logger.getLogger(MorenaScanner.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else if(src instanceof SaneSource){
            SaneSource sanesrc=(SaneSource) src;
            
        }
        
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Double> getSupportedResolutions() {
        return supportedResolutions;
    }

    public List<Integer> getSupportedBitDepth() {
        return supportedBitDepth;
    }

    public List<String> getSupportedModes() {
        return supportedModes;
    }
    
}
