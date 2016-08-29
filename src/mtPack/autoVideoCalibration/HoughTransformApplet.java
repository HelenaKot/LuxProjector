package mtPack.autoVideoCalibration;

import houghtransformation.EHTProcessStep;
import houghtransformation.HTEngine;
import houghtransformation.HTImage;
import houghtransformation.HTImagePanel;
import houghtransformation.HTImagePanelEx;
import houghtransformation.HTEngine.HoughCoordinate;
import houghtransformation.HTImagePanelEx.DISPLAY_MODE;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public 
	class HoughTransformApplet extends javax.swing.JApplet {

    private HTEngine htEngine;
    
    private HTImagePanelEx originalImagePanel;
    
    private HTImagePanel transformedImagePanel;
    
    private JComboBox stepComboBox;
       
    private static final Dimension IMAGE_PANEL_DIMENSION = new Dimension(300,300);
    
    private URL defaultImageUrl;
    
    private BufferedImage defaultImageBuf;
    
    private String lastLoadedImageFilename;
    
    private boolean lastLoadedImageIsDefaultImage;
    
    private final Object[] stepComboBoxItems = {
                    "Step0: Original Image",
                    "Step1: Gray-scale image", 
                    "Step2: Edge Detection using Sobel operator",
                    "Step3: Edge Detection with treshold",
                    "Step4: Hough space (Origin @Top)",
                    "Step4: Hough space (Origin @Center)",
                    "Step5: Hough space filtered",
                    "Step6: Original image with overlayed images"};   
    
    private static final String HELP_TEXT =
            "Hough Transform example applet" + System.getProperty("line.separator") + 
            "-------------------------------------------------------" + System.getProperty("line.separator") + 
            "www.sunshine2k.de" + System.getProperty("line.separator") + 
            "December 2012" + System.getProperty("line.separator") + System.getProperty("line.separator") + 
            "Instructions:" + System.getProperty("line.separator") + 
            "On the left a source image can be loaded. The right side shows the visualization of the currently " + 
            "chosen hough transform step." + System.getProperty("line.separator") +
            "The image of the current step can also be saved to an image file." + System.getProperty("line.separator") +
            "When chosing the interactive mode, instead of loading an image a line is drawn to the panel " + System.getProperty("line.separator") +
            "which endpoints can be freely moved. To update the right side with the current step,"  + System.getProperty("line.separator") +
            "the update button has to be pressed." + System.getProperty("line.separator") +
            "The hough transform process can be modified by freely choosing the edge treshold and the relative maximum value."+ System.getProperty("line.separator") +
            "The brightness factor does not influence the process, it just affects the visual appearance of the hough space.";
    
    /** Initializes the applet HoughTransformApplet */
    @Override
    public void init() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HoughTransformApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HoughTransformApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HoughTransformApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HoughTransformApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the applet */
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        setSize(650, 550);
        
        /* init hough transform engine */
        htEngine = new HTEngine();
        
        /* get path to standard image */
        //defaultImageUrl = getClass().getResource("/tomaszew2/Biurko/room.png");
        try {
			defaultImageUrl = new URL("file:Desktop/room1.png");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        try {
        File f = new File("../room.png");
        if(!f.exists())
        	System.out.println("not exist");

           defaultImageBuf = ImageIO.read(f); 
        		   //ImageIO.read(defaultImageUrl);
        } catch (IOException e) {
            String str = "Image could not be loaded: " + defaultImageUrl + "\n" + e.getStackTrace();
            JOptionPane.showMessageDialog(null, str, "Fatal Error", JOptionPane.ERROR_MESSAGE);
        }
        lastLoadedImageIsDefaultImage = true;
        htEngine.setSourceImage(defaultImageBuf);
        
        createPanels();
        createStepCombobox();
        
        /* init state */
        imageModeRadioButton.setSelected(true);
        updateIntActModeButton.setEnabled(false);
        thresEdgeValLabel.setVisible(false);
        relMaxValLabel.setVisible(false);
        brightFactorSlider.setEnabled(false);
        edgeTresholdSlider.setEnabled(false);
        relMaxSlider.setEnabled(false);
        
        transformedImagePanel.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent me) {
                /* nothing to do */
            }

            @Override
            public void mouseMoved(MouseEvent me) {
                int x = (int)me.getPoint().getX();
		int y = (int)me.getPoint().getY();

		if (!(htEngine.getMaxCalculatedStep().isLowerStep(EHTProcessStep.STEP_HOUGH_SPACE_TOP)))
		{
                    if (stepComboBox.getSelectedIndex() == 4)
                    {
                        HoughCoordinate houghCoordinate = htEngine.getHoughCoordinateTop(x, y, 
                                    transformedImagePanel.getWidth(), transformedImagePanel.getHeight());
                        DecimalFormat df = new DecimalFormat("#.#");
                        houghSpaceAngleLabel.setText("angle: " + df.format(houghCoordinate.getAngle()));
                        houghSpaceDistLabel.setText("r: " + df.format(houghCoordinate.getDistance()));
                    }
                    else if (stepComboBox.getSelectedIndex() == 5)
                    {
                        HoughCoordinate houghCoordinate = htEngine.getHoughCoordinateCenter(x, y, 
                                            transformedImagePanel.getWidth(), transformedImagePanel.getHeight());
                        DecimalFormat df = new DecimalFormat("#.#");
                        houghSpaceAngleLabel.setText("angle: " + df.format(houghCoordinate.getAngle()));
                        houghSpaceDistLabel.setText("r: " + df.format(houghCoordinate.getDistance()));
                    }
                    else
                    {
                        houghSpaceAngleLabel.setText("-");
                        houghSpaceDistLabel.setText("-");
                    }
		}
            }
        });
        
    }
    
    private void createPanels()
    {
        /* init panel for the original image */   
        originalImagePanel = new HTImagePanelEx();
	originalImagePanel.setBounds(10, 32, IMAGE_PANEL_DIMENSION.width, IMAGE_PANEL_DIMENSION.height);
	originalImagePanel.setImage(htEngine.getOriginalImage());
	getContentPane().add(originalImagePanel);
        /* init panel for the original image */   
	transformedImagePanel = new HTImagePanelEx();
	transformedImagePanel.setBounds(IMAGE_PANEL_DIMENSION.width + 30, 
			32, IMAGE_PANEL_DIMENSION.width, IMAGE_PANEL_DIMENSION.height);
	transformedImagePanel.setImage(htEngine.getOriginalImage());
	getContentPane().add(transformedImagePanel);        
    }
    
    private void createStepCombobox()
    {
        /* init combobox for selecting the transformation step to show */
        stepComboBox = new JComboBox(stepComboBoxItems); 
        stepComboBox.setBounds(IMAGE_PANEL_DIMENSION.width + 30, 
                               40 + IMAGE_PANEL_DIMENSION.height,
                               IMAGE_PANEL_DIMENSION.width, 25);
        stepComboBox.addItemListener(
        	new ItemListener() {
	            @Override
	            public void itemStateChanged(ItemEvent arg0) {
	                Object source = arg0.getSource();
	                if (source instanceof JComboBox) 
	                {
	                    JComboBox selectedChoice = (JComboBox)source;
	                    if (selectedChoice != null)
	                    {
	                        loadImageForSelectedImage(selectedChoice.getSelectedIndex());
	                        transformedImagePanel.repaint();
	                    }
	                }
	            }
        	}
        );
        getContentPane().add(stepComboBox);
    }
    
    public void loadImageForSelectedImage(int index)
    {
        switch (index)
        {
            case 0: 
                transformedImagePanel.setImage(htEngine.getHTProcessStep(EHTProcessStep.STEP_ORIGINAL));
                brightFactorSlider.setEnabled(false);
                edgeTresholdSlider.setEnabled(false);
                relMaxSlider.setEnabled(false);
                break;

            case 1:
                transformedImagePanel.setImage(htEngine.getHTProcessStep(EHTProcessStep.STEP_GRAYSCALE));
                brightFactorSlider.setEnabled(false);
                edgeTresholdSlider.setEnabled(false);
                relMaxSlider.setEnabled(false);
                break;

            case 2:
                transformedImagePanel.setImage(htEngine.getHTProcessStep(EHTProcessStep.STEP_EDGE_DETECTION));
                brightFactorSlider.setEnabled(false);
                edgeTresholdSlider.setEnabled(false);
                relMaxSlider.setEnabled(false);
                break;

            case 3:
                transformedImagePanel.setImage(htEngine.getHTProcessStep(EHTProcessStep.STEP_EDGE_TRESHOLD));
                brightFactorSlider.setEnabled(false);
                edgeTresholdSlider.setEnabled(true);
                relMaxSlider.setEnabled(false);
                break;

            case 4:
                transformedImagePanel.setImage(htEngine.getHTProcessStep(EHTProcessStep.STEP_HOUGH_SPACE_TOP));
                brightFactorSlider.setEnabled(true);
                edgeTresholdSlider.setEnabled(true);
                relMaxSlider.setEnabled(false);
                break;

            case 5:
                transformedImagePanel.setImage(htEngine.getHTProcessStep(EHTProcessStep.STEP_HOUGH_SPACE_CENTER));
                brightFactorSlider.setEnabled(true);
                edgeTresholdSlider.setEnabled(true);
                relMaxSlider.setEnabled(false);
                break;

            case 6:
                transformedImagePanel.setImage(htEngine.getHTProcessStep(EHTProcessStep.STEP_HOUGH_SPACE_FILTERED));
                brightFactorSlider.setEnabled(false);
                edgeTresholdSlider.setEnabled(true);
                relMaxSlider.setEnabled(true);
                break;

            case 7:
                transformedImagePanel.setImage(htEngine.getHTProcessStep(EHTProcessStep.STEP_ORIGINAL_LINES_OVERLAYED));
                brightFactorSlider.setEnabled(false);
                edgeTresholdSlider.setEnabled(true);
                relMaxSlider.setEnabled(true);
                break;

                default:break;
        }
    }

    /** This method is called from within the init() method to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        brightFactorSlider = new javax.swing.JSlider();
        thresEdgeValLabel = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        edgeTresholdSlider = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        relMaxSlider = new javax.swing.JSlider();
        relMaxValLabel = new javax.swing.JLabel();
        loadImageButton = new javax.swing.JButton();
        imageModeRadioButton = new javax.swing.JRadioButton();
        interactModeRadioButton = new javax.swing.JRadioButton();
        updateIntActModeButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        saveImageButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();
        loadDefaultImageButton = new javax.swing.JButton();
        houghSpaceAngleLabel = new javax.swing.JLabel();
        houghSpaceDistLabel = new javax.swing.JLabel();

        getContentPane().setLayout(null);

        jLabel1.setText("Source Image:");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 11, 90, 14);

        jLabel2.setText("Relative maximum (%):");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(330, 455, 140, 14);

        jLabel3.setText("Transformed Image:");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(330, 10, 140, 14);

        brightFactorSlider.setMaximum(420);
        brightFactorSlider.setMinimum(1);
        brightFactorSlider.setValue(200);
        brightFactorSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                brightFactorSliderStateChanged(evt);
            }
        });
        getContentPane().add(brightFactorSlider);
        brightFactorSlider.setBounds(330, 430, 300, 23);

        thresEdgeValLabel.setText("100");
        getContentPane().add(thresEdgeValLabel);
        thresEdgeValLabel.setBounds(580, 375, 50, 14);

        jLabel4.setText("Edge Threshold:");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(330, 375, 140, 14);

        edgeTresholdSlider.setMaximum(480);
        edgeTresholdSlider.setMinimum(1);
        edgeTresholdSlider.setValue(100);
        edgeTresholdSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                edgeTresholdSliderStateChanged(evt);
            }
        });
        getContentPane().add(edgeTresholdSlider);
        edgeTresholdSlider.setBounds(330, 390, 300, 23);

        jLabel5.setText("Brightness:");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(330, 415, 140, 14);

        relMaxSlider.setValue(80);
        relMaxSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                relMaxSliderStateChanged(evt);
            }
        });
        getContentPane().add(relMaxSlider);
        relMaxSlider.setBounds(330, 470, 300, 23);

        relMaxValLabel.setText("100");
        getContentPane().add(relMaxValLabel);
        relMaxValLabel.setBounds(580, 455, 50, 14);

        loadImageButton.setText("Load new image");
        loadImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadImageButtonActionPerformed(evt);
            }
        });
        getContentPane().add(loadImageButton);
        loadImageButton.setBounds(10, 340, 120, 23);

        buttonGroup1.add(imageModeRadioButton);
        imageModeRadioButton.setText("Image Mode");
        imageModeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageModeRadioButtonActionPerformed(evt);
            }
        });
        getContentPane().add(imageModeRadioButton);
        imageModeRadioButton.setBounds(10, 390, 130, 23);

        buttonGroup1.add(interactModeRadioButton);
        interactModeRadioButton.setText("Interactive Mode");
        interactModeRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                interactModeRadioButtonActionPerformed(evt);
            }
        });
        getContentPane().add(interactModeRadioButton);
        interactModeRadioButton.setBounds(10, 420, 130, 23);

        updateIntActModeButton.setText("Update");
        updateIntActModeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateIntActModeButtonActionPerformed(evt);
            }
        });
        getContentPane().add(updateIntActModeButton);
        updateIntActModeButton.setBounds(140, 420, 80, 23);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(320, 340, 1, 180);

        saveImageButton.setText("Save transformed image");
        saveImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveImageButtonActionPerformed(evt);
            }
        });
        getContentPane().add(saveImageButton);
        saveImageButton.setBounds(330, 500, 170, 23);

        helpButton.setText("Help");
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButtonActionPerformed(evt);
            }
        });
        getContentPane().add(helpButton);
        helpButton.setBounds(10, 500, 90, 23);

        loadDefaultImageButton.setText("Load default image");
        loadDefaultImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDefaultImageButtonActionPerformed(evt);
            }
        });
        getContentPane().add(loadDefaultImageButton);
        loadDefaultImageButton.setBounds(170, 340, 140, 23);

        houghSpaceAngleLabel.setText("-");
        getContentPane().add(houghSpaceAngleLabel);
        houghSpaceAngleLabel.setBounds(510, 500, 120, 14);

        houghSpaceDistLabel.setText("-");
        getContentPane().add(houghSpaceDistLabel);
        houghSpaceDistLabel.setBounds(510, 520, 110, 14);
    }// </editor-fold>//GEN-END:initComponents

    private void edgeTresholdSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_edgeTresholdSliderStateChanged
       htEngine.setEdgeTreshold(edgeTresholdSlider.getValue());
       loadImageForSelectedImage(stepComboBox.getSelectedIndex());
       transformedImagePanel.invalidate();
       transformedImagePanel.repaint();
       //thresEdgeValLabel.setText(Integer.toString(edgeTresholdSlider.getValue()));
    }//GEN-LAST:event_edgeTresholdSliderStateChanged

    private void brightFactorSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_brightFactorSliderStateChanged
        htEngine.setBrightnessFactor((double)brightFactorSlider.getValue() / 100.0);
	loadImageForSelectedImage(stepComboBox.getSelectedIndex());
	transformedImagePanel.invalidate();
	transformedImagePanel.repaint();
    }//GEN-LAST:event_brightFactorSliderStateChanged

    private void relMaxSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_relMaxSliderStateChanged
        htEngine.setRelativeMaximum(relMaxSlider.getValue());
	loadImageForSelectedImage(stepComboBox.getSelectedIndex());
	transformedImagePanel.invalidate();
	transformedImagePanel.repaint();
        //relMaxValLabel.setText(Integer.toString(relMaxSlider.getValue()));
    }//GEN-LAST:event_relMaxSliderStateChanged

    private void loadImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadImageButtonActionPerformed
        if (evt.getSource() == loadImageButton)
        {
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(HoughTransformApplet.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) 
            {
                File file = fc.getSelectedFile();
                lastLoadedImageFilename = file.getAbsolutePath();
                lastLoadedImageIsDefaultImage = false;
                htEngine.setSourceImage(file.getAbsolutePath());
                originalImagePanel.setImage(htEngine.getOriginalImage());
                originalImagePanel.repaint();
                int oldIndex = stepComboBox.getSelectedIndex();
                stepComboBox.setSelectedIndex(0);
                stepComboBox.setSelectedIndex(oldIndex);
            }
        }			
    }//GEN-LAST:event_loadImageButtonActionPerformed

    private void imageModeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageModeRadioButtonActionPerformed
        originalImagePanel.setDisplayMode(DISPLAY_MODE.NORMAL);	
        if (lastLoadedImageIsDefaultImage)
        {
            htEngine.reloadNewImage(defaultImageBuf);
        }
        else
        {
            htEngine.reloadNewImage(lastLoadedImageFilename);
        }
        
        loadImageForSelectedImage(stepComboBox.getSelectedIndex());
        transformedImagePanel.invalidate();
        transformedImagePanel.repaint();
        updateIntActModeButton.setEnabled(false);
        loadImageButton.setEnabled(true);
        loadDefaultImageButton.setEnabled(true);
    }//GEN-LAST:event_imageModeRadioButtonActionPerformed

    private void interactModeRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_interactModeRadioButtonActionPerformed
        originalImagePanel.setDisplayMode(DISPLAY_MODE.INTERACTIVE);
        HTImage htImg = originalImagePanel.getInteractiveImage();
        htEngine.reloadNewImage(htImg);
        loadImageForSelectedImage(stepComboBox.getSelectedIndex());
        transformedImagePanel.invalidate();
        transformedImagePanel.repaint();
        updateIntActModeButton.setEnabled(true);
        loadImageButton.setEnabled(false);
        loadDefaultImageButton.setEnabled(false);
    }//GEN-LAST:event_interactModeRadioButtonActionPerformed

    private void updateIntActModeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateIntActModeButtonActionPerformed
        HTImage htImg = originalImagePanel.getInteractiveImage();			
        htEngine.reloadNewImage(htImg);
        loadImageForSelectedImage(stepComboBox.getSelectedIndex());
        transformedImagePanel.invalidate();
        transformedImagePanel.repaint();
    }//GEN-LAST:event_updateIntActModeButtonActionPerformed

    private void saveImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveImageButtonActionPerformed
        if (evt.getSource() == saveImageButton)
        {
            final JFileChooser fc = new JFileChooser();
           /*
            fc.setFileFilter(
            	new FileFilter() {
	                @Override
	                public boolean accept(File file) {
	                    return file.getName().toLowerCase().endsWith(".png");
	                }
	
	                public String getDescription() {
	                    return "PNG images (*.png)";
	                }
	            }
            );
            */
            int returnVal = fc.showSaveDialog(HoughTransformApplet.this);
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fc.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".png"))
                {
                    file = new File(file.getAbsoluteFile() + ".png");
                }
                RenderedImage renderedImage = transformedImagePanel.getDrawnImage();
                try {
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException e) {
                    String str = "Image could not be saved: " + e.getStackTrace();
                    JOptionPane.showConfirmDialog(null, str );
                }
            }
        }
    }//GEN-LAST:event_saveImageButtonActionPerformed

    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
        JOptionPane.showMessageDialog(null, HELP_TEXT, "Help", JOptionPane.OK_CANCEL_OPTION);
    }//GEN-LAST:event_helpButtonActionPerformed

    private void loadDefaultImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadDefaultImageButtonActionPerformed
        htEngine.setSourceImage(defaultImageBuf);
        lastLoadedImageIsDefaultImage = true;
        originalImagePanel.setImage(htEngine.getOriginalImage());
        originalImagePanel.repaint();
        int oldIndex = stepComboBox.getSelectedIndex();
        stepComboBox.setSelectedIndex(0);
        stepComboBox.setSelectedIndex(oldIndex);
    }//GEN-LAST:event_loadDefaultImageButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSlider brightFactorSlider;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JSlider edgeTresholdSlider;
    private javax.swing.JButton helpButton;
    private javax.swing.JLabel houghSpaceAngleLabel;
    private javax.swing.JLabel houghSpaceDistLabel;
    private javax.swing.JRadioButton imageModeRadioButton;
    private javax.swing.JRadioButton interactModeRadioButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton loadDefaultImageButton;
    private javax.swing.JButton loadImageButton;
    private javax.swing.JSlider relMaxSlider;
    private javax.swing.JLabel relMaxValLabel;
    private javax.swing.JButton saveImageButton;
    private javax.swing.JLabel thresEdgeValLabel;
    private javax.swing.JButton updateIntActModeButton;
    // End of variables declaration//GEN-END:variables
}
