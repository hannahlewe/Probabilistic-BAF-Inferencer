package modelGenerator;

public class Generator {

    private static int nodes;
    private static boolean sparse;
    private static boolean dense;
    private static boolean polytree;

    private static String usage(){
        /**
         * required <>
         * optional []
         * choice {-t|-u}
         */
        return "usage: Generator -n <nodes> {-d|-s} [-p]\n" +
                        " -n,--nodes    number of nodes in model\n" +
                        " -d,--dense   creates model with as many edges as nodes (default)\n" +
                        " -s,--sparse   creates model with half as many edges as nodes\n" +
                        " -p,--polytree   makes model a polytree (optional, default=false)\n";
    }

    private static void parseArgs(String[] args) throws Exception{
        /**
         * -h
         */
        if (args.length == 1){
            if(args[0].equals("-h") || args[0].equals("--help")){
                System.out.println(usage());
            }
            else{
                throw new Exception("Arguments entered incorrectly!\n"+usage());
            }
        }

        /**
         * -n <INT>
         */
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

        /**
         * -n <INT> -d
         * -n <INT> -s
         * -n <INT> -p
         */
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

        /**
         * -n <INT> -d -p
         * -n <INT> -s -p
         */
        else if(args.length == 4){
            if(args[0].equals("-n") || args[0].equals("--nodes")){
                try{
                    nodes = Integer.parseInt(args[1]);
                }catch(NumberFormatException e){
                    throw new Exception("Arguments entered incorrectly!\n"+usage());
                }
            }
            if(args[2].equals("-d") || args[2].equals("--dense")){
                dense = true;
                if(args[3].equals("-p") || args[3].equals("--polytree")){
                    polytree = true;
                } else throw new Exception("Arguments entered incorrectly!\n"+usage());
            }
            else if(args[2].equals("-s") || args[2].equals("--sparse")){
                sparse = true;
                if(args[3].equals("-p") || args[3].equals("--polytree")){
                    polytree = true;
                } else throw new Exception("Arguments entered incorrectly!\n"+usage());
            }
            else{
                throw new Exception("Arguments entered incorrectly!\n"+usage());
            }
        }

        else{
            throw new Exception("Arguments entered incorrectly!\n"+usage());
        }
    }

    public static void main(String[] args) throws Exception {
        // Parse command line args
        try{
            parseArgs(args);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }

        // Generate model
        if(!polytree){
            if(sparse){
                NonPolyTreeModel p = new NonPolyTreeModel(nodes, "sparse");
                p.generateModel();
            }
            else if(dense){
                NonPolyTreeModel p = new NonPolyTreeModel(nodes, "dense");
                p.generateModel();
            }
        }


    }

}
