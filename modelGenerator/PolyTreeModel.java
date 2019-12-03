package modelGenerator;

import java.util.*;

/**
 * Class generating a polytree (acyclic tree) graph model
 * of a Bipolar Argumentation Framework in a .txt format
 * which can be uploaded in the BAF-Inferencer application.
 *
 * @author Hannah Lewerentz <hlewerentz@uos.de>
 */
public class PolyTreeModel {
    /**
     * The available relations in the BAF
     */
    private static String[] relations = {"attack(", "support("};
    /**
     * The number of nodes, i.e. arguments, in the argumentation graph
     */
    private static int num_args;

    // Ctor
    PolyTreeModel(int nodes){
        num_args = nodes;
    }

    /**
     * Generates the model string of the given model.
     * Iteratively picks new nodes and connecting them
     * to already connected nodes, thus generating a tree.
     *
     * @return String generated model
     */
    String generateModel(){

        // Arg-line
        String model = "args{";
        String[] arguments = new String[num_args];
        for(int i = 0; i < num_args; i++){
            arguments[i] = "A" + i;
            model += "A" + i;
            if(i != num_args-1) model += ",";
            else model += "}\n";
        }

        // Generate random relations
        List<String> nodes = new ArrayList<>(Arrays.asList(arguments));
        List<String> picked = new ArrayList<>();

        // Up to n-1, to ensure acyclicity and connectedness
        for(int i = 0; i < num_args-1; i++){
            Random r = new Random();
            String rel = relations[r.nextInt(2)];
            String a1 = "";
            String a2 = "";

            // Root node case
            if(picked.isEmpty()){
                a1 = nodes.get(r.nextInt(nodes.size()));
                a2 = nodes.get(r.nextInt(nodes.size()));
                while(a2.equals(a1)){
                    a2 = nodes.get(r.nextInt(nodes.size()));
                }
                picked.add(a1);
                picked.add(a2);
                nodes.remove(a1);
                nodes.remove(a2);
            }

            else {
                // From picked...
                a1 = picked.get(r.nextInt(picked.size()));
                // Connect to a new node!
                a2 = nodes.get(r.nextInt(nodes.size()));
                // They can (and should) not be the same
                // - no check necessary
                picked.add(a2);
                nodes.remove(a2);
            }

            model += rel + a1 + "," + a2 + ")\n";
        }
        return model;
    }
}
