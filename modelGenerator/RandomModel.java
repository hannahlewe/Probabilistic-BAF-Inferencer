package modelGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class generating a random graph model (not necessarily
 * connected) of a Bipolar Argumentation Framework in a .txt format
 * which can be uploaded in the BAF-Inferencer application.
 *
 * @author Hannah Lewerentz <hlewerentz@uos.de>
 */
public class RandomModel {
    /**
     * The available relations in the BAF
     */
    private static String[] relations = {"attack(", "support("};
    /**
     * The generated arguments' labels
     */
    private String[] arguments;
    /**
     * The number of nodes, i.e. arguments, in the argumentation graph
     */
    private static int num_args;
    /**
     * The number of relations, dependent on chosen density
     */
    private static int num_relations;
    /**
     * The chosen density; can be "sparse" or "dense"
     */
    private String density;

    // Ctor
    RandomModel(int nodes, String density){
        num_args = nodes;
        if(density.equals("dense")){
            // NOT TOO DENSE!!!!
            num_relations = (int)(num_args*0.8);
        }
        else if (density.equals("sparse")){
            num_relations = num_args/2;
        }
        this.density = density;
    }

    /**
     * Helper function that generates random relations
     * "support" or "attack" given the arguments of the model.
     *
     * @return String the new relation
     */
    private String generateNewRel(){
        Random r = new Random();
        String a1 = arguments[r.nextInt(num_args)];
        String a2 = arguments[r.nextInt(num_args)];

        // Sanity check, no self-referring relations
        while(a1.equals(a2)){
            a2 = arguments[r.nextInt(num_args)];
        }

        return relations[r.nextInt(2)] +
                a1 + "," + a2 + ")\n";
    }

    /**
     * Generates the model string of the given model.
     * Iteratively picks random nodes and connecting them.
     * Connectivity is not ensured.
     *
     * @return String generated model
     */
    String generateModel(){

        // Arg-line
        String model = "args{";
        arguments = new String[num_args];
        for(int i = 0; i < num_args; i++){
            arguments[i] = "A" + i;
            model += "A" + i;
            if(i != num_args-1) model += ",";
            else model += "}\n";
        }

        // Generate random relations
        List<String> cropped_curr_rels = new ArrayList<String>();
        List<String> full_curr_rels = new ArrayList<String>();

        for(int i = 0; i < num_relations; i++){
            String cropped_rel = "";
            String reverse_rel = "";
            String new_rel = "";

            do{
                new_rel = generateNewRel();

                // Check if exact same relation already exists
                if(full_curr_rels.contains(new_rel)){
                    new_rel = generateNewRel();
                }

                if(new_rel.startsWith("support")){
                    cropped_rel = new_rel.substring(7);
                    String[] args = cropped_rel.split(",");
                    reverse_rel = ("attack(" + args[1] + "," + args[0] + ")");
                }
                else if(new_rel.startsWith("attack")){
                    cropped_rel = new_rel.substring(6);
                    String[] args = cropped_rel.split(",");
                    reverse_rel = ("support(" + args[1] + "," + args[0] + ")");
                }
            // Check if there is no relation A->B
            // Check if there is no perpendicular relation B->A
            } while(cropped_curr_rels.contains(cropped_rel) ||
                    full_curr_rels.contains(reverse_rel));

            cropped_curr_rels.add(cropped_rel);
            full_curr_rels.add(new_rel);
            model += new_rel;
        }
        return model;
    }
}
