package modelGenerator;

import java.io.*;

/**
 * Helping class to generate random Bipolar Argumentation
 * Frameworks as .txt model files to be used in the
 * BAF-Inferencer application.
 *
 * usage: Generator -n <nodes> {-d|-s|-p}
 *          -n,--nodes      number of nodes in model
 *          -d,--dense      creates random model with 80% density (default)
 *          -s,--sparse     creates random model with 50% density
 *          -p,--polytree   creates connected polytree model
 *
 * @author Hannah Lewerentz <hlewerentz@uos.de>
 */
public class Generator {

    /**
     * Number of nodes passed to model generator class
     */
    private static int nodes;
    /**
     * True if sparse random model shall be generated
     */
    private static boolean sparse = false;
    /**
     * True if dense random model shall be generated (default)
     */
    private static boolean dense = true;
    /**
     * True if polytree modes shall be generated
     */
    private static boolean polytree = false;

    /**
     * Returns a usage message to be printed to the command line
     *
     * @return String usage message
     */
    private static String usage(){
        return "usage: Generator -n <nodes> {-d|-s|-p}\n" +
                        " -n,--nodes    number of nodes in model\n" +
                        " -d,--dense   creates random model with 80% density (default)\n" +
                        " -s,--sparse   creates random model with 50% density\n" +
                        " -p,--polytree   creates connected polytree model\n";
    }

    /**
     * Parses command line arguments
     *
     * @param args command line arguments, passed from main method
     * @throws Exception if a parsing error occurs
     */
    private static void parseArgs(String[] args) throws Exception{
        // Case -h or --help
        if (args.length == 1){
            if(args[0].equals("-h") || args[0].equals("--help")){
                System.out.println(usage());
            }
            else{
                throw new Exception("Arguments entered incorrectly!\n"+usage());
            }
        }

        // Case -n <INT>
        else if (args.length == 2){
            if(args[0].equals("-n") || args[0].equals("--nodes")){
                try{
                    nodes = Integer.parseInt(args[1]);
                }catch(NumberFormatException e){
                    throw new Exception("Arguments entered incorrectly!\n"+usage());
                }
            }
            else{
                throw new Exception("Arguments entered incorrectly!\n"+usage());
            }
        }

        // Case -n <INT> {-d|-s|-p}
        else if(args.length == 3){
            if(args[0].equals("-n") || args[0].equals("--nodes")){
                try{
                    nodes = Integer.parseInt(args[1]);
                }catch(NumberFormatException e){
                    throw new Exception("Arguments entered incorrectly!\n"+usage());
                }
            }
            switch (args[2]) {
                case "-d":
                case "--dense":
                    dense = true;
                    break;
                case "-s":
                case "--sparse":
                    sparse = true;
                    break;
                case "-p":
                case "--polytree":
                    polytree = true;
                    break;
                default:
                    throw new Exception("Arguments entered incorrectly!\n" + usage());
            }
        }

        else{
            throw new Exception("Arguments entered incorrectly!\n"+usage());
        }
    }

    /**
     * Writes the generated model string into an output .txt file
     * @param text the generated model string
     * @param filename the output filename destination
     */
    private static void writeToFile(String text, String filename){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(System.getProperty("user.home") + File.separator + filename +".txt")))) {
            writer.write(text);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        try{
            parseArgs(args);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }

        // Case random model
        if(!polytree){
            if(sparse){
                RandomModel p = new RandomModel(nodes, "sparse");
                writeToFile(p.generateModel(), nodes + "args_sparse");
            }
            else{
                RandomModel p = new RandomModel(nodes, "dense");
                writeToFile(p.generateModel(), nodes + "args_dense");
            }
        }

        // Case polytree model
        else {
            PolyTreeModel p = new PolyTreeModel(nodes);
            writeToFile(p.generateModel(), nodes + "args_polytree");
        }
    }
}
