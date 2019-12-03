package inferencer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

/**
 * The controller class mediating between the
 * model and the view class
 *
 * @author Hannah Lewerentz <hlewerentz@uos.de>
 */
public class Controller implements ActionListener {

    /**
     * The view; GUI
     */
    private View view;
    /**
     * The model; internal logic
     */
    private Model model;
    private int counter = 1;

    /**
     * Constructor, sets model and view
     *
     * @param m the model
     * @param v the view
     */
    public Controller(Model m, View v){

        this.model = m;
        this.view = v;

        // All clickable fields
        // File Menu
        view.itemClear.addActionListener(this);
        view.itemOpen.addActionListener(this);
        view.itemSaveModelTxt.addActionListener(this);
        view.itemSaveResultsTxt.addActionListener(this);
        view.itemSaveResultsCsv.addActionListener(this);
        // Edit Menu
        view.itemEditFactors.addActionListener(this);
        view.setFactorsStrong.addActionListener(this);
        view.setFactorsTol.addActionListener(this);
        view.setFactorsReq.addActionListener(this);
        view.setFactorsStrict.addActionListener(this);
        view.setFactorsWeak.addActionListener(this);
        view.setFactorsPars.addActionListener(this);
        view.setFactorsPenal.addActionListener(this);
        view.setFactorsPerm.addActionListener(this);

        view.inferenceButton.addActionListener(this);
        view.itemShowHelp.addActionListener(this);
    }

    /**
     * Specifies the actions to be taken in case of
     * an ActionEvent on the clickable fields
     *
     * @param ae ActionEvent within the view
     */
    public void actionPerformed(ActionEvent ae) {

        if(ae.getSource() == view.itemClear){
            view.clearModelText();
            view.clearResultText();
        }

        if(ae.getSource() == view.itemOpen){
            // Choose File, build internal model and update modelText in view
            try{
                String path = model.chooseFile();
                if(path != null){
                    view.setModelText(model.buildModelFromFile(path));
                }
            } catch (Exception e){
                view.setResultText(e.getMessage());
            }
        }

        if(ae.getSource() == view.itemSaveModelTxt){
            // Save modelText to .txt file
            try{
                model.saveToFile(view.getModelText(), "txt");
            }
            catch(IOException e){
                view.setResultText(e.getMessage());
            }
        }

        if(ae.getSource() == view.itemSaveResultsTxt){
            // Save modelText to .txt file
            try{
                model.saveToFile(view.getResultText(), "txt");
            }
            catch(IOException e){
                view.setResultText(e.getMessage());
            }
        }

        if(ae.getSource() == view.itemSaveResultsCsv){
            // Save modelText to .txt file
            try{
                model.saveToFile(view.getResultText(), "csv");
            }
            catch(IOException e){
                view.setResultText(e.getMessage());
            }
        }

        if(ae.getSource() == view.itemEditFactors){

            // Show edit frame
            view.editFactorsFrame = view.initEditFactorValues(model.getAttack(), model.getSupport());
            view.editFactorsSaveButton.addActionListener(this);
            view.editFactorsFrame.setVisible(true);
        }

        if(ae.getSource() == view.editFactorsSaveButton){
            // Check if all doubles
            double[] newAtt = new double[4];
            double[] newSup = new double[4];
            try
            {
                newAtt[0] = Double.parseDouble(view.att00.getText());
                newAtt[1] = Double.parseDouble(view.att01.getText());
                newAtt[2] = Double.parseDouble(view.att10.getText());
                newAtt[3] = Double.parseDouble(view.att11.getText());

                newSup[0] = Double.parseDouble(view.sup00.getText());
                newSup[1] = Double.parseDouble(view.sup01.getText());
                newSup[2] = Double.parseDouble(view.sup10.getText());
                newSup[3] = Double.parseDouble(view.sup11.getText());

                if(!Arrays.equals(model.getAttack(),newAtt)){
                    model.setAtt(newAtt);
                    view.setFactorsCustom.setSelected(true);
                }
                if(!Arrays.equals(model.getSupport(),newSup)){
                    model.setAtt(newSup);
                    view.setFactorsCustom.setSelected(true);
                }

                view.editFactorsFrame.setVisible(false);

            }
            catch(NumberFormatException e)
            {
                JOptionPane.showMessageDialog(view, "Please enter double values only!", null, JOptionPane.ERROR_MESSAGE);
            }
        }

        if(ae.getSource() == view.inferenceButton){

            // If there is no model, do nothing
            if(view.getModelText().equals("")){
                view.setResultText("Please enter or upload a model first!");
            }

            // If there is a model, only do inference if correctly parsed
            else{
                String modelNoWhitespace = view.getModelText().replaceAll(" ","");
                try{
                    model.parseModel(modelNoWhitespace);
                    // Check which inferencer to use
                    if(view.junctionTree.isSelected()){
                        view.setResultText(model.inference("junctionTree"));
                    }
                    if(view.gibbsSampler.isSelected()){
                        view.setResultText(model.inference("gibbsSampler"));
                    }
                }
                // Parsing error
                catch (Exception e){
                    view.setResultText(e.getMessage());
                }
            }
        }

        if(ae.getSource() == view.itemShowHelp){
            view.showHelp();
        }

        if(ae.getSource() == view.setFactorsStrong){
            model.setAtt(new double[] {1.0, 1.0, 2.0, 0.0});
            model.setSup(new double[] {1.0, 1.0, 0.0, 2.0});
        }

        if(ae.getSource() == view.setFactorsTol){
            model.setAtt(new double[] {1.0, 1.0, 2.0, 0.5});
            model.setSup(new double[] {1.0, 1.0, 0.5, 2.0});
        }

        if(ae.getSource() == view.setFactorsReq){
            model.setAtt(new double[] {1.0, 1.0, 2.0, 1.0});
            model.setSup(new double[] {1.0, 1.0, 1.0, 2.0});
        }

        if(ae.getSource() == view.setFactorsStrict){
            model.setAtt(new double[] {1.0, 1.0, 1.5, 0.0});
            model.setSup(new double[] {1.0, 1.0, 0.0, 1.5});
        }

        if(ae.getSource() == view.setFactorsWeak){
            model.setAtt(new double[] {1.0, 1.0, 1.5, 0.5});
            model.setSup(new double[] {1.0, 1.0, 0.5, 1.5});
        }

        if(ae.getSource() == view.setFactorsPars){
            model.setAtt(new double[] {1.0, 1.0, 1.5, 1.0});
            model.setSup(new double[] {1.0, 1.0, 1.0, 1.5});
        }

        if(ae.getSource() == view.setFactorsPenal){
            model.setAtt(new double[] {1.0, 1.0, 1.0, 0.0});
            model.setSup(new double[] {1.0, 1.0, 0.0, 1.0});
        }

        if(ae.getSource() == view.setFactorsPerm){
            model.setAtt(new double[] {1.0, 1.0, 1.0, 0.5});
            model.setSup(new double[] {1.0, 1.0, 0.5, 1.0});
        }
    }
}
