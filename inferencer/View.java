package inferencer;

import javax.swing.*;
import java.awt.*;

/**
 * The View class instantiating the GUI
 *
 * @author Hannah Lewerentz <hlewerentz@uos.de>
 */
class View extends JFrame {

    /**
     * The JMenuItem for File -> New
     */
    JMenuItem itemClear;
    /**
     * The JMenuItem for File -> Open
     */
    JMenuItem itemOpen;
    /**
     * Calls a popup-menu to edit factors
     */
    JMenuItem itemEditFactors;
    /**
     * The JMenuItem for File -> Save Results as Txt
     */
    JMenuItem itemSaveResultsTxt;
    /**
     * The JMenuItem for File -> Save Results as Csv
     */
    JMenuItem itemSaveResultsCsv;
    /**
     * The JMenuItem for File -> Save Model as Txt
     */
    JMenuItem itemSaveModelTxt;
    /**
     * The Inference Button on the lower right
     */
    JButton inferenceButton;
    /**
     * Shows the manual when clicked
     */
    JMenuItem itemShowHelp;

    /**
     * Attack factor value for Ai = 0, Aj = 0
     */
    JTextField att00;
    /**
     * Attack factor value for Ai = 0, Aj = 1
     */
    JTextField att01;
    /**
     * Attack factor value for Ai = 1, Aj = 0
     */
    JTextField att10;
    /**
     * Attack factor value for Ai = 1, Aj = 1
     */
    JTextField att11;
    /**
     * Support factor value for Ai = 0, Aj = 0
     */
    JTextField sup00;
    /**
     * Support factor value for Ai = 0, Aj = 1
     */
    JTextField sup01;
    /**
     * Support factor value for Ai = 1, Aj = 0
     */
    JTextField sup10;
    /**
     * Support factor value for Ai = 1, Aj = 1
     */
    JTextField sup11;

    /**
     * Saves changes in factor values
     */
    JButton editFactorsSaveButton;
    /**
     * Frame for editing the attack/support factor values
     */
    JFrame editFactorsFrame;
    /**
     * Resets factors to default
     */
    JRadioButtonMenuItem setFactorsStrong;
    /**
     * Sets factors to low reward
     */
    JRadioButtonMenuItem setFactorsTol;
    /**
     * Sets factors to high reward
     */
    JRadioButtonMenuItem setFactorsReq;
    /**
     * Sets factors to low punishment
     */
    JRadioButtonMenuItem setFactorsStrict;
    /**
     * Sets factors to high punishment
     */
    JRadioButtonMenuItem setFactorsWeak;
    /**
     * Sets factors to low rewards / high punishment
     */
    JRadioButtonMenuItem setFactorsPars;
    /**
     * Sets factors to low punishment / high reward
     */
    JRadioButtonMenuItem setFactorsPenal;
    /**
     * Sets factors to high reward / high punishment
     */
    JRadioButtonMenuItem setFactorsPerm;
    /**
     * True if custom factors are set
     */
    JRadioButtonMenuItem setFactorsCustom;

    /**
     * Chooses the junction tree inferencer
     */
    JRadioButtonMenuItem junctionTree;
    /**
     * Chooses the gibbs sampling inferencer
     */
    JRadioButtonMenuItem gibbsSampler;

    /**
     * The Font used throughout the application
     */
    private Font myFont = new Font("DialogInput", Font.PLAIN, 20);
    /**
     * The area for the model input text
     */
    private JTextArea modelText;
    /**
     * The label for the inference results
     */
    private JTextArea resultText;


    /**
     * Constructor; initializes the view
     */
    View(){

        // inferencer.Main Frame
        this.setTitle("BAF Inferencer Application");
        this.setSize(950,700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // MenuBar
        JMenuBar menuBar = new JMenuBar();

        // File Menu and submenus
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(myFont);

        itemClear = new JMenuItem("Clear all");
        itemClear.setFont(myFont);
        fileMenu.add(itemClear);
        fileMenu.add(new JSeparator());

        itemOpen = new JMenuItem("Load model");
        itemOpen.setFont(myFont);
        fileMenu.add(itemOpen);
        fileMenu.add(new JSeparator());

        itemSaveModelTxt = new JMenuItem("Save model as .txt");
        itemSaveModelTxt.setFont(myFont);
        fileMenu.add(itemSaveModelTxt);
        fileMenu.add(new JSeparator());

        itemSaveResultsTxt = new JMenuItem("Save results as .txt");
        itemSaveResultsTxt.setFont(myFont);
        fileMenu.add(itemSaveResultsTxt);
        fileMenu.add(new JSeparator());

        itemSaveResultsCsv = new JMenuItem("Save results as .csv");
        itemSaveResultsCsv.setFont(myFont);
        fileMenu.add(itemSaveResultsCsv);
        menuBar.add(fileMenu);

        // Factors Menu and submenus
        JMenu factorsMenu = new JMenu("Factors");
        factorsMenu.setFont(myFont);

        itemEditFactors = new JMenuItem("Edit factors");
        itemEditFactors.setFont(myFont);
        factorsMenu.add(itemEditFactors);
        factorsMenu.add(new JSeparator());

        setFactorsStrong = new JRadioButtonMenuItem("Set strong");
        setFactorsStrong.setFont(myFont);
        factorsMenu.add(setFactorsStrong);
        factorsMenu.add(new JSeparator());

        setFactorsTol = new JRadioButtonMenuItem("Set tolerant");
        setFactorsTol.setFont(myFont);
        factorsMenu.add(setFactorsTol);
        factorsMenu.add(new JSeparator());

        setFactorsReq = new JRadioButtonMenuItem("Set requiting");
        setFactorsReq.setFont(myFont);
        factorsMenu.add(setFactorsReq);
        factorsMenu.add(new JSeparator());

        setFactorsStrict = new JRadioButtonMenuItem("Set strict");
        setFactorsStrict.setFont(myFont);
        factorsMenu.add(setFactorsStrict);
        factorsMenu.add(new JSeparator());

        setFactorsWeak = new JRadioButtonMenuItem("Set weak", true);
        setFactorsWeak.setFont(myFont);
        factorsMenu.add(setFactorsWeak);
        factorsMenu.add(new JSeparator());

        setFactorsPars = new JRadioButtonMenuItem("Set parsimonious");
        setFactorsPars.setFont(myFont);
        factorsMenu.add(setFactorsPars);
        factorsMenu.add(new JSeparator());

        setFactorsPenal = new JRadioButtonMenuItem("Set penalizing");
        setFactorsPenal.setFont(myFont);
        factorsMenu.add(setFactorsPenal);
        factorsMenu.add(new JSeparator());

        setFactorsPerm = new JRadioButtonMenuItem("Set permissive");
        setFactorsPerm.setFont(myFont);
        factorsMenu.add(setFactorsPerm);
        factorsMenu.add(new JSeparator());

        setFactorsCustom = new JRadioButtonMenuItem("Custom");
        setFactorsCustom.setFont(myFont);
        setFactorsCustom.setEnabled(false);
        factorsMenu.add(setFactorsCustom);

        ButtonGroup fac_group = new ButtonGroup();
        fac_group.add(setFactorsStrong);
        fac_group.add(setFactorsTol);
        fac_group.add(setFactorsReq);
        fac_group.add(setFactorsStrict);
        fac_group.add(setFactorsWeak);
        fac_group.add(setFactorsPars);
        fac_group.add(setFactorsPenal);
        fac_group.add(setFactorsPerm);
        fac_group.add(setFactorsCustom);
        menuBar.add(factorsMenu);

        // Inferencer Menu and submenus
        JMenu infMenu = new JMenu("Inferencer");
        infMenu.setFont(myFont);

        junctionTree = new JRadioButtonMenuItem("Junction Tree", true);
        junctionTree.setFont(myFont);
        infMenu.add(junctionTree);
        infMenu.add(new JSeparator());

        gibbsSampler = new JRadioButtonMenuItem("Gibbs Sampler");
        gibbsSampler.setFont(myFont);
        infMenu.add(gibbsSampler);
        ButtonGroup inf_group = new ButtonGroup();
        inf_group.add(junctionTree);
        inf_group.add(gibbsSampler);
        menuBar.add(infMenu);

        // Help Menu and submenus
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(myFont);
        itemShowHelp = new JMenuItem("Show manual");
        itemShowHelp.setFont(myFont);
        helpMenu.add(itemShowHelp);
        menuBar.add(helpMenu);

        // Model Text Field
        modelText = new JTextArea();
        modelText.setFont(myFont);
        modelText.setLineWrap(true);
        modelText.setWrapStyleWord(true);
        JScrollPane scrollModelText = new JScrollPane(modelText,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Result Text Field
        resultText = new JTextArea();
        resultText.setFont(myFont);
        resultText.setLineWrap(true);
        resultText.setWrapStyleWord(true);
        resultText.setEditable(false);
        JScrollPane scrollResultText = new JScrollPane(resultText,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Headlines
        JLabel modelHeadline = new JLabel("Type in or upload a model here");
        modelHeadline.setFont(myFont);
        JLabel resultHeadline = new JLabel("Results will be displayed here");
        resultHeadline.setFont(myFont);

        // Inference Button
        inferenceButton = new JButton("Inference");
        inferenceButton.setFont(myFont);

        // GridBagLayout specifications
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;

        // - ModelHeadline, ResultHeadline
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = this.getWidth()/2;
        c.ipady = 20;
        c.weightx = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0,10,0,10);  //right padding
        this.add(modelHeadline,c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        this.add(resultHeadline,c);

        // - ModelText, ResultText
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 0;
        c.weighty = 1;
        c.anchor = GridBagConstraints.LINE_START;
        this.add(scrollModelText,c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.LINE_END;
        this.add(scrollResultText,c);

        // - InferenceButton
        c.weightx = 0;
        c.weighty = 0;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.gridx = 2;
        c.gridy = 2;
        c.ipady = 10;
        c.insets = new Insets(20,130,10,20);  //top, left, bottom, right
        this.add(inferenceButton, c);

        this.setJMenuBar(menuBar);
        this.setVisible(true);

    }

    /**
     * Opens a Message Dialog displaying a help text
     */
    void showHelp(){

        String helpText = "+----------------------------------+\n" +
                          "|    Inferencer Application for    |\n" +
                          "| Bipolar argumentation frameworks |\n" +
                          "+----------------------------------+\n\n" +
                          "This application eases inference in Bipolar Argumentation\n" +
                          "Frameworks (BAFs) by internally modelling them as a Markov\n" +
                          "Random Fields. And efficiently computing them via Junction\n" +
                          "Tree Inferencer or Gibbs Sampling.\n\n" +

                          "+------------+\n" +
                          "| How to use |\n" +
                          "+------------+\n\n" +
                          "(1) Type in or upload a .txt file with a model of the following\n" +
                          "    form:\n" +
                          "    Specify the unique arguments:\n" +
                          "        args{Ai,...,An}\n" +
                          "        ...\n" +
                          "    Specify the binary attack and support relations between the\n" +
                          "    arguments:\n" +
                          "        attack(Ai,Aj)\n" +
                          "        support(Ai,Aj)\n" +
                          "        ...\n" +
                          "    Line comments are allowed and must start with the comment\n" +
                          "     delimiter '#':\n" +
                          "       # This is a comment line.\n\n" +

                          "(2) Edit the default factor values for the attack and support\n" +
                          "    relations by choosing one of the semantics listed under\n" +
                          "    'Factors' or define custom factor values under\n" +
                          "    'Factors' –> 'Edit Factors'. You may only use numerical\n" +
                          "    values.\n" +
                          "        'Reward' refers to the increase in probability if a\n" +
                          "        strongly believed argument attacks a less believed argument\n" +
                          "        or supports an already believed argument.\n" +
                          "        'Punishment' refers to the decrease in probability if a\n" +
                          "        strongly believed argument attacks a strongly believed\n" +
                          "        argument or supports an less believed argument.\n\n" +

                          "(3) Choose between a JunctionTree inferencer (exact inference)\n" +
                          "    for sparse models or a GibbsSampler (approximate inference)\n" +
                          "    for dense models.\n\n" +

                          "(4) Press the inference button to output the probabilities for\n" +
                          "    each argument either being true (outcome = 1) or false\n" +
                          "    (outcome = 0).\n\n" +

                          "(5) Save your typed in model as a .txt file or save your inference\n" +
                          "    results either as a .txt or a .csv file.";

        JTextArea helpTextArea = new JTextArea(helpText);
        helpTextArea.setFont(myFont);
        helpTextArea.setLineWrap(true);
        helpTextArea.setWrapStyleWord(true);
        helpTextArea.setEditable(false);
        JScrollPane scrollHelpText = new JScrollPane(helpTextArea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Popup frame
        JFrame helpFrame = new JFrame("User Manual");
        helpFrame.add(scrollHelpText);
        helpFrame.setSize(850,800);
        helpFrame.setLocation(this.getX(), this.getY());
        helpFrame.setVisible(true);
    }

    /**
     * Displays the popup window for editing attack/support
     * factor values.
     *
     * @param attack current attack values
     * @param support current support values
     * @return the popup window to be made visible
     */
    JFrame initEditFactorValues(double[] attack, double[] support){

        // Frame + Layout
        editFactorsFrame = new JFrame("Factor Values");
        editFactorsFrame.setResizable(false);
        editFactorsFrame.setLocation(this.getX()+getWidth()/3, this.getY()+getHeight()/3);
        editFactorsFrame.setSize(550,380);
        editFactorsFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 30;
        c.ipady = 30;

        // Labels
        JLabel Ai = new JLabel("Ai");
        Ai.setFont(myFont);
        JLabel Aj = new JLabel("Aj");
        Aj.setFont(myFont);
        JLabel att = new JLabel("attack(Ai,Aj)");
        att.setFont(myFont);
        JLabel sup = new JLabel("support(Ai,Aj)");
        sup.setFont(myFont);
        // A bit cumbersome, but one Label can't be used twice
        JLabel zero1 = new JLabel("0");
        zero1.setFont(myFont);
        JLabel zero2 = new JLabel("0");
        zero2.setFont(myFont);
        JLabel zero3 = new JLabel("0");
        zero3.setFont(myFont);
        JLabel zero4 = new JLabel("0");
        zero4.setFont(myFont);
        JLabel one1 = new JLabel("1");
        one1.setFont(myFont);
        JLabel one2 = new JLabel("1");
        one2.setFont(myFont);
        JLabel one3 = new JLabel("1");
        one3.setFont(myFont);
        JLabel one4 = new JLabel("1");
        one4.setFont(myFont);

        // TextFields from which inputs are extracted
        // Initialize with current factor values
        att00 = new JTextField(Double.toString(attack[0]));
        att00.setFont(myFont);
        att01 = new JTextField(Double.toString(attack[1]));
        att01.setFont(myFont);
        att10 = new JTextField(Double.toString(attack[2]));
        att10.setFont(myFont);
        att11 = new JTextField(Double.toString(attack[3]));
        att11.setFont(myFont);

        sup00 = new JTextField(Double.toString(support[0]));
        sup00.setFont(myFont);
        sup01 = new JTextField(Double.toString(support[1]));
        sup01.setFont(myFont);
        sup10 = new JTextField(Double.toString(support[2]));
        sup10.setFont(myFont);
        sup11 = new JTextField(Double.toString(support[3]));
        sup11.setFont(myFont);

        // Save Button
        editFactorsSaveButton = new JButton("Save changes");

        // GridBagLayout specifications
        // Row 0
        c.gridy = 0;
        c.gridx = 0;
        editFactorsFrame.add(Ai,c);
        c.gridx = 1;
        editFactorsFrame.add(Aj,c);
        c.gridx = 2;
        editFactorsFrame.add(att,c);
        c.gridx = 3;
        editFactorsFrame.add(sup,c);

        // Row 1
        c.gridy = 1;
        c.gridx = 0;
        editFactorsFrame.add(zero1,c);
        c.gridx = 1;
        editFactorsFrame.add(zero2,c);
        c.gridx = 2;
        editFactorsFrame.add(att00,c);
        c.gridx = 3;
        editFactorsFrame.add(sup00,c);

        // Row 2
        c.gridy = 2;
        c.gridx = 0;
        editFactorsFrame.add(zero3,c);
        c.gridx = 1;
        editFactorsFrame.add(one1,c);
        c.gridx = 2;
        editFactorsFrame.add(att01,c);
        c.gridx = 3;
        editFactorsFrame.add(sup01,c);

        // Row 3
        c.gridy = 3;
        c.gridx = 0;
        editFactorsFrame.add(one2,c);
        c.gridx = 1;
        editFactorsFrame.add(zero4,c);
        c.gridx = 2;
        editFactorsFrame.add(att10,c);
        c.gridx = 3;
        editFactorsFrame.add(sup10,c);

        // Row 4
        c.gridy = 4;
        c.gridx = 0;
        editFactorsFrame.add(one3,c);
        c.gridx = 1;
        editFactorsFrame.add(one4,c);
        c.gridx = 2;
        editFactorsFrame.add(att11,c);
        c.gridx = 3;
        editFactorsFrame.add(sup11,c);

        // Row 5
        c.gridy = 5;
        c.gridx = 3;
        c.ipady = 10;
        c.ipadx = 10;
        c.insets = (new Insets(20,0,0,0));
        editFactorsFrame.add(editFactorsSaveButton, c);

        return editFactorsFrame;
    }

    /**
     * Clears the text in JTextArea modelText
     */
    void clearModelText(){
        setModelText("");
    }

    /**
     * Clears the text in JTextArea resultText
     */
    void clearResultText() { setResultText(""); }

    /**
     * Returns the text in JTextArea modelText
     *
     * @return the text in JTextArea modelText
     */
    String getModelText(){
        return modelText.getText();
    }

    /**
     * Sets the text in JTextArea modelText
     *
     * @param text Text to be set in JTextArea modelText
     */
    void setModelText(String text){
        modelText.setText(text);
    }

    /**
     * Sets the text in JLabel resultText
     *
     * @param text Text to be set in JTextArea resultText
     */
    void setResultText(String text){
        resultText.setText(text);
    }

    /**
     * Returns the text in JLabel resultText
     *
     * @return text in JLabel resultText
     */
    String getResultText(){
        return resultText.getText();
    }
}
