package naiveProbabilities;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class JointProbabilityCalculator {

    private static int[][] create_assignment_table(int vars){

        int assignments = (int)Math.pow(2, vars);
        int[][] assignment_table = new int[assignments][vars];

        for(int i = 0; i < assignments; i++){
            String bin = Integer.toBinaryString(i);
            // Fill binary to var number
            while(bin.length() < vars){
                bin = "0" + bin;
            }
            // Fill assignment table
            for(int j = 0; j < bin.length(); j++){
                assignment_table[i][j] = Character.getNumericValue(bin.charAt(j));
            }
        }

        return assignment_table;
    }

    private static boolean contains_dupes(String[] vars){
        for (int i = 0; i < vars.length; i++) {
            for (int j = i+1; j < vars.length; j++) {
                if (vars[i].equals(vars[j])){
                    return true;
                }
            }
        }
        return false;
    }

    private static double findFactorValue(int[] assignment, String factor){
        // Factor definitions
        double[] definition = new double[4];
        double[] attack = {
                    1.0,
                    1.0,
                    1.5,
                    0.5};
        double[] support = {
                    1.0,
                    1.0,
                    0.5,
                    1.5};

        if(factor.equals("attack")){ definition = attack; }
        else if(factor.equals("support")){ definition = support; }
        else{ System.out.println("Second argument must be 'attack' or 'support'!"); }

        // Possible assignments
        int [] a0 = {0,0};
        int [] a1 = {0,1};
        int [] a2 = {1,0};
        int [] a3 = {1,1};

        if(Arrays.equals(assignment,a0)){
            return definition[0];
        }
        else if(Arrays.equals(assignment,a1)){
            return definition[1];
        }
        else if(Arrays.equals(assignment,a2)){
            return definition[2];
        }
        else if(Arrays.equals(assignment,a3)){
            return definition[3];
        }
        else {
            return 0;
        }
    }

    private static void printTable(int[][] assignment_table, double[] joint_probabilities){
        for(int i = 0; i < assignment_table.length; i++){
            System.out.print("[");
            for(int j = 0; j < assignment_table[0].length; j++){
                System.out.print(" " + assignment_table[i][j]);
            }

            System.out.println(String.format(" ]: %11.4f |\n", joint_probabilities[i]));
        }
    }

    public static void main(String[] args) {

        /**
         * args{T,A1,A2,A3,A4,A5,A6,R1,R2,R3}
         * attack(A2,T)
         * attack(A6,A1)
         * attack(A5,A2)
         * attack(R1,A6)
         * attack(A6,A4)
         * support(A1,T)
         * support(A3,A1)
         * support(A4,T)
         * support(R2,A5)
         * support(R3,A5)
         */

        // This could be changeable via command line
        String[] vars = {"T","A1","A2","A3","A4","A5","A6","R1","R2","R3"};
        if(contains_dupes(vars)){
            System.out.println("Variable list contains duplicates!");
        }

        // Variable assignment table
        int[][] assignment_table = create_assignment_table(vars.length);
        double[] joint_probabilities = new double[assignment_table.length];

        // Compute joint probability of each assignment
        for(int i = 0; i < assignment_table.length; i++){
            // attack(A2,T)
            int[] factor1 = {assignment_table[i][2],assignment_table[i][0]};
            double f1 = findFactorValue(factor1, "attack");
            // attack(A6,A1)
            int[] factor2 = {assignment_table[i][6],assignment_table[i][1]};
            double f2 = findFactorValue(factor2, "attack");
            // attack(A5,A2)
            int[] factor3 = {assignment_table[i][5],assignment_table[i][2]};
            double f3 = findFactorValue(factor3, "attack");
            // attack(R1,A6)
            int[] factor4 = {assignment_table[i][7],assignment_table[i][6]};
            double f4 = findFactorValue(factor4, "attack");
            // attack(A6,A4)
            int[] factor5 = {assignment_table[i][6],assignment_table[i][4]};
            double f5 = findFactorValue(factor5, "attack");
            // support(A1,T)
            int[] factor6 = {assignment_table[i][1],assignment_table[i][0]};
            double f6 = findFactorValue(factor6, "support");
            // support(A3,A1)
            int[] factor7 = {assignment_table[i][3],assignment_table[i][1]};
            double f7 = findFactorValue(factor7, "support");
            // support(A4,T)
            int[] factor8 = {assignment_table[i][4],assignment_table[i][0]};
            double f8 = findFactorValue(factor8, "support");
            // support(R2,A5)
            int[] factor9 = {assignment_table[i][8],assignment_table[i][5]};
            double f9 = findFactorValue(factor9, "support");
            // support(R3,A5)
            int[] factor10 = {assignment_table[i][9],assignment_table[i][5]};
            double f10 = findFactorValue(factor10, "support");

            joint_probabilities[i] = f1*f2*f3*f4*f5*f6*f7*f8*f9*f10;
        }

        // Normalize
        double norm_constant = DoubleStream.of(joint_probabilities).sum();
        for(int i = 0; i < joint_probabilities.length; i++){
            joint_probabilities[i] = joint_probabilities[i]/norm_constant;
        }

        // Marginal for T
        double t = 0.0;
        for(int i = 0; i < assignment_table.length; i++){
            // T is the 0th variable entry
            if(assignment_table[i][0] == 1){
                t += joint_probabilities[i];
            }
        }
        System.out.println("P(T = 1) = " + t);

        //printTable(assignment_table, joint_probabilities);

    } // main
} // class
