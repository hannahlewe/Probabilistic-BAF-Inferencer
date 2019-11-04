package modelGenerator;

import java.io.*;
import java.util.*;

public class PolyTreeModel {
    private static String[] relations = {"attack(", "support("};
    private static String[] arguments;
    private static int num_args;
    private static int num_relations;
    private String density;
    private static boolean[][] adj;

    PolyTreeModel(int nodes, String den){
        num_args = nodes;
        density = den;
        if(den.equals("dense")){
            // 80% density, since no cycles up to n-1
            num_relations = (int)(num_args*0.8);
        }
        else if (den.equals("sparse")){
            num_relations = num_args/2;
        }
        adj = new boolean[num_args][num_args];
    }

    private boolean recursiveCyclic(int current_node, int last_node, boolean visited[]){
        visited[current_node] = true;
        for(int i = 0; i < num_args; i++){
            if(i != last_node && adj[current_node][i]){
                if(recursiveCyclic(i, current_node, visited)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCyclic(int start_node){
        boolean[] visited = new boolean[num_args];
        return recursiveCyclic(start_node, -1, visited);
    }

    private static void writeToFile(String text, String filename){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("/home/hannahlewerentz/Schreibtisch/Thesis/"+ filename +".txt")))) {
            writer.write(text);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private String generateNewRel(){

        Random r = new Random();
        int i = r.nextInt(num_args);
        int j = r.nextInt(num_args);

        String a1 = arguments[i];
        String a2 = arguments[j];

        while(a1.equals(a2)){
            j = r.nextInt(num_args);
            a2 = arguments[j];
        }

        adj[i][j] = adj[j][i] = true;

        // CYCLE CHECK
        if(isCyclic(i)){
            adj[i][j] = adj[j][i] = false;
            return generateNewRel();
        }
        else {
            return relations[r.nextInt(2)] +
                    a1 + "," + a2 + ")\n";
        }
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
        // fdf df
    }
}
