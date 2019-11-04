package modelGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NonPolyTreeModel {
    private static String[] relations = {"attack(", "support("};
    private static String[] arguments;
    private static int num_args;
    private static int num_relations;
    private static String density;

    NonPolyTreeModel(int nodes, String density){
        num_args = nodes;
        if(density.equals("dense")){
            num_relations = num_args;
        }
        else if (density.equals("sparse")){
            num_relations = num_args/2;
        }
        this.density = density;
    }

    private static void writeToFile(String text, String filename){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("/home/hannahlewerentz/Schreibtisch/Thesis/"+ filename +".txt")))) {
            writer.write(text);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static String generateNewRel(){
        Random r = new Random();
        String a1 = arguments[r.nextInt(num_args)];
        String a2 = arguments[r.nextInt(num_args)];

        while(a1.equals(a2)){
            a2 = arguments[r.nextInt(num_args)];
        }

        return relations[r.nextInt(2)] +
                a1 + "," + a2 + ")\n";
    }

    void generateModel(){

        String model = "args{";
        arguments = new String[num_args];

        // Generate arguments
        for(int i = 0; i < num_args; i++){
            arguments[i] = "A" + i;
            model += "A" + i;
            if(i != num_args-1) model += ",";
            else model += "}\n";
        }

        // Generate random relations
        List<String> curr_rels = new ArrayList<String>();

        for(int i = 0; i < num_relations; i++){
            String new_rel = generateNewRel();
            while(curr_rels.contains(new_rel)){
                new_rel = generateNewRel();
            }
            curr_rels.add(new_rel);
            model += new_rel;
        }

        writeToFile(model, num_args + "args_" + density);
    }
}
