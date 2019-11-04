package inferencer;

import edu.umass.cs.mallet.grmm.inference.GibbsSampler;
import edu.umass.cs.mallet.grmm.inference.Inferencer;
import edu.umass.cs.mallet.grmm.inference.JunctionTreeInferencer;
import edu.umass.cs.mallet.grmm.inference.SamplingInferencer;
import edu.umass.cs.mallet.grmm.types.*;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The model class; handles the internal model
 * (the FactorGraph representing the Bipolar
 * Argumentation Framework (BAF)), the inference, the
 * internal logic and is responsible for storing
 * and retrieving data.
 *
 * @author Hannah Lewerentz <hlewerentz@uos.de>
 */
public class Model {

    /**
     * Attack factor specification (default = weak)
     */
    private double[] att = {1.0, 1.0, 1.5, 0.5};
    /**
     * Support factor specification (default = weak)
     */
    private double[] sup = {1.0, 1.0, 0.5, 1.5};
    /**
     * The internal model representing the BAF
     */
    private FactorGraph fg;
    /**
     * The variables, i.e. the arguments, used in the BAF
     */
    private Variable[] vars;
    long times_sum = 0;

    /**
     * Opens a JFileChooser to pick a .txt file
     * containing the model description
     *
     * @return absolute path to chosen file
     *          (null if no file was chosen)
     * @throws IOException if chosen file is not a
     *          .txt file
     */
    String chooseFile() throws IOException {

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setPreferredSize(new Dimension(600,400));
        int returnValue = jfc.showOpenDialog(null);
        String absPath;

        // Only go on if a file is chosen at all
        if(returnValue == JFileChooser.APPROVE_OPTION){

            File selectedFile = jfc.getSelectedFile();
            absPath = selectedFile.getAbsolutePath();

            // Check if txt
            String extension = "";
            int i = absPath.lastIndexOf(".");
            // Cover cases like dots in directory names
            int p = Math.max(absPath.lastIndexOf("/"), absPath.lastIndexOf("\\"));
            if(i > p){
                extension = absPath.substring(i+1);
            }
            if(extension.equals("txt")){
                return absPath;
            }
            else{
                throw new IOException("Wrong file extension! Try uploading a .txt file!");
            }
        }
        // No error, just go back to view
        else return null;
    }

    /**
     * Opens a JFileChooser to save the results to a .txt file
     *
     * @param results the result String to be saved
     * @param ext specifies the file extension, either txt or csv
     * @throws IOException writing to file error
     */
    void saveToFile(String results, String ext) throws IOException{

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setPreferredSize(new Dimension(600, 400));
        int returnValue = jfc.showSaveDialog(null);

        // Only act if Save button is pressed at all
        if (returnValue == JFileChooser.APPROVE_OPTION) {

            // TXT
            if (ext.equals("txt")) {
                try (FileWriter fw = new FileWriter(jfc.getSelectedFile() + ".txt")) {
                    fw.write(results);
                } catch (IOException e) {
                    throw new IOException("IOException when trying to save file!");
                }
            }

            // CSV
            if (ext.equals("csv")) {
                try (FileWriter fw = new FileWriter(jfc.getSelectedFile() + ".csv")) {
                    // Remove separators + whitespace
                    String noSep = results.replaceAll("\\+----------\\+---------\\+-------------\\+\n", "");
                    String noWS = noSep.replaceAll(" ", "");
                    // Split into lines
                    String[] split = noWS.split("\n");

                    String csvResult = "";
                    for(String line : split){
                        // Remove starting and ending "|"
                        String cropped = line.substring(1, line.length() - 1);
                        // Insert commas
                        csvResult += cropped.replaceAll("\\|", ",") + "\n";
                    }
                    fw.write(csvResult);
                } catch (IOException e) {
                    throw new IOException("IOException when trying to save file!");
                }
            }
        }
    }

    /**
     * Parses a model and checks its correctness
     *
     * @param model The String to be parsed
     * @throws Exception error message when a parsing error occurs
     */
    void parseModel(String model) throws Exception {

        resetModel();

        // Display hint for empty model
        if(model.equals("")){
            throw new Exception("Please enter or upload a model first!");
        }

        // Split to get different lines
        String[] splitModel = model.split("\n");

        // Extract arguments
        List<String> varLabels = new ArrayList<String>();
        int lineCount = 1;
        for(String line : splitModel){
            // Empty lines and comments allowed
            // Skip Supports and Attacks for now
            if(line.equals("") || isComment(line) || isSupLine(line) || isAttLine(line)){
                lineCount++;
                continue;
            }

            else if(isArgLine(line)){
                String content = line.substring(5, line.length()-1);
                for(String label : content.split(",")){
                    if(varLabels.contains(label)){
                        throw new Exception("Error in line " + lineCount + ":\n Duplicate Variable!");
                    }
                    varLabels.add(label);
                }
                lineCount++;
            }

            else {
                throw new Exception("Error in line " + lineCount + ":\n Illegal start of expression!");
            }
        }

        // Create Variables and FactorGraph
        vars = new Variable[varLabels.size()];
        for(int i = 0; i < varLabels.size(); i++){
            vars[i] = new Variable(2);
            vars[i].setLabel(varLabels.get(i));
        }
        fg = new FactorGraph(vars);

        // Extract Supports and Attacks
        lineCount = 1;
        for(String line : splitModel){

            // Empty lines and comments allowed
            if(line.equals("") || isComment(line) || isArgLine(line)){
                lineCount++;
                continue;
            }

            // ATT
            else if(isAttLine(line)){
                String content = line.substring(7, line.length()-1);
                String [] stringVars = content.split(",");
                if(stringVars.length > 2 || stringVars.length == 1){
                    throw new Exception("Error in line " + lineCount + ":\n" +
                            "Attack relation can only be binary! " +
                            "Attack relations need to be of the form attack(Ai,Aj)");
                }
                if(!varLabels.contains(stringVars[0]) ||
                        !varLabels.contains(stringVars[1])){
                    throw new Exception("Error in line " + lineCount + ":\n" +
                            "Variable not found!");
                }
                else {
                    int x = varLabels.indexOf(stringVars[0]);
                    int y = varLabels.indexOf(stringVars[1]);
                    fg.addFactor(vars[x], vars[y], att);
                }
            }

            // SUP
            else if(isSupLine(line)){
                String content = line.substring(8, line.length()-1);
                String [] stringVars = content.split(",");
                if(stringVars.length > 2 || stringVars.length == 1){
                    throw new Exception("Error in line " + lineCount + ":\n" +
                            "Support relation can only be binary! " +
                            "Support relations need to be of the form support(Ai,Aj).");
                }
                else if(!varLabels.contains(stringVars[0]) ||
                        !varLabels.contains(stringVars[1])){
                    throw new Exception("Error in line " + lineCount + ":\n" +
                            "Variable not found!");
                }
                else {
                    int x = varLabels.indexOf(stringVars[0]);
                    int y = varLabels.indexOf(stringVars[1]);
                    fg.addFactor(vars[x], vars[y], sup);
                }
            }

            // Parsing error
            else {
                throw new Exception("Error in line " + lineCount + ":\n Illegal start of expression!");
            }
        }
    }

    /**
     * Builds the internal model from a .txt file,
     * given its absolute path
     *
     * @param absPath absolute path to a .txt file
     * @return model String if no exception occurs
     * @throws Exception FileNotFoundException or IOException
     */
    String buildModelFromFile(String absPath) throws Exception {

        String modelTextString = "";

        try(BufferedReader br = new BufferedReader(new FileReader(absPath))){
            // Ignore whitespace
            String line = br.readLine();

            while(line != null){
                modelTextString += line + "\n";
                // Again, ignore whitespace
                line = br.readLine();
            }
        } catch(Exception e){
            // FileNotFoundException or IOException
            throw e;
        }
        return modelTextString;
    }

    /**
     * Does the inference on a given internal model
     * and returns the results as a String
     *
     * @param inferencer the inferencer to use
     * @return String containing the results
     */
    String inference(String inferencer){

        String results = "+----------+---------+-------------+\n" +
                         "| Variable | Outcome | Probability |\n" +
                         "+----------+---------+-------------+\n";

        // Set the inferencer
        Inferencer inf = null;
        if(inferencer.equals("junctionTree")){
            inf = new JunctionTreeInferencer();
        }
        if(inferencer.equals("gibbsSampler")){
            inf = new SamplingInferencer(new GibbsSampler(10000), 50000);
        }

        // TIME THIS
        long startTime = System.currentTimeMillis();

        inf.computeMarginals(fg);

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        times_sum += elapsedTime;
        System.out.println("Inference time: " + elapsedTime + " ms");

        // Collect marginals
        for(Variable var : vars){
            Factor f = inf.lookupMarginal(var);
            AssignmentIterator it = f.assignmentIterator();
            while(it.hasNext()){
                int outcome = it.indexOfCurrentAssn();
                double prob = f.value(it);
                results += String.format("| %8s | %7d | %11.4f |\n", var, outcome, prob);
                it.next();
            }
            results += "+----------+---------+-------------+\n";
        }
        return results;
    }

    /**
     * Resets the internal FactorGraph model
     */
    private void resetModel(){
        // Due to GRMM internals, the clear() method
        // doesn't work here
        fg = new FactorGraph();
        vars = new Variable[] {};
    }

    /**
     * Returns current attack factor values
     *
     * @return current attack factor values
     */
    double[] getAttack(){ return this.att; }

    /**
     * Sets new attack factor values
     *
     * @param attack new factor values to set
     */
    void setAtt(double[] attack){ this.att = attack; }

    /**
     * Returns current support factor
     *
     * @return current support factor
     */
    double[] getSupport(){ return this.sup; }

    /**
     * Sets new support factor values
     *
     * @param support new factor values to set
     */
    void setSup(double[] support){ this.sup = support;}

    /**
     * Checks whether a line is an argument line
     * @param line Line to be checked
     * @return boolean true if argument line, else false
     */
    private boolean isArgLine(String line){
        return line.startsWith("args{") & line.endsWith("}");
    }

    /**
     * Checks whether a line is a support line
     * @param line Line to be checked
     * @return boolean true if support line, else false
     */
    private boolean isSupLine(String line) {
        return line.startsWith("support(") & line.endsWith(")");
    }

    /**
     * Checks whether a line is an attack line
     * @param line Line to be checked
     * @return boolean true if attack line, else false
     */
    private boolean isAttLine(String line) {
        return line.startsWith("attack(") & line.endsWith(")");
    }

    /**
     * Checks whether a line is a comment line
     * @param line Line to be checked
     * @return boolean true if comment line, else false
     */
    private boolean isComment(String line) {
        return line.startsWith("#");
    }
}
