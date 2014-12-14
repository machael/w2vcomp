package common.classifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import common.IOUtils;
import common.exception.UnimplementedException;

public class SvmUtils {
    String svmDir;
    String[] options = {"-s","-t", "-d", "-c", "-g", "-p", "-v"} ;
    boolean[] intOption = {true, true, true, true, false, false, true};
    public SvmUtils(String svmDir) {
        this.svmDir = svmDir;
    }
    
    public static void shuffleArray(int[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
    
    public int[] range(int size) {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = i;
        }
        return result;
    }
    
    public int[][] splitFold(int[] indices, int numFold) {
        int[][] result = new int[numFold][];
        int length = indices.length;
        int div = length / numFold;
        int mod = length % numFold;
        for (int i = 0; i < numFold; i++) {
            int foldLength = div + ((i<mod)?1:0);
            int start = (i<=mod)?(i* (div+1)):(i*div + mod);
            result[i] = new int[foldLength];
            System.arraycopy(indices, start, result[i], 0, foldLength);
        }
        return result;
    }

    public int[][] getFolds(int datasize, int numFold) {
        int[] indices = range(datasize);
        shuffleArray(indices);
        int[][] splits = splitFold(indices, numFold);
        return splits;
    }
    
    public void printToStream(int[] indices, ArrayList<String> lines, BufferedWriter writer) throws IOException{
        for (int index: indices) {
            writer.write(lines.get(index));
            writer.write("\n");
        }
    }
    
    public void printFile(int[][] folds, int foldIndex, ArrayList<String> lines, String foldTrainFile, String foldTestFile) throws IOException{
        BufferedWriter trainWriter = new BufferedWriter(new FileWriter(foldTrainFile));
        BufferedWriter testWriter = new BufferedWriter(new FileWriter(foldTestFile));
        for (int i = 0; i < folds.length; i++) {
            if (i == foldIndex) {
                printToStream(folds[i], lines, testWriter);
            } else {
                printToStream(folds[i], lines, trainWriter);
            }
        }
    }
    
    public double computeAccWithCross(String trainFile, HashMap<String, Double> parameters, int numFold) {
        ArrayList<String> lines = IOUtils.readFile(trainFile);
        int[][] folds = getFolds(lines.size(), numFold);
        double sumAcc = 0;
        for (int i = 0; i < numFold; i++) {
            try {
                String foldTrainFile = File.createTempFile("train",".txt").getAbsolutePath();
                String foldTestFile = File.createTempFile("test",".txt").getAbsolutePath();
                String foldModelFile = File.createTempFile("model",".txt").getAbsolutePath();
                printFile(folds,i, lines, foldTrainFile, foldTestFile);
                train(foldTrainFile, foldModelFile, parameters);
                double accurracy = accuracy(foldTestFile, foldModelFile, null);
                sumAcc += accurracy * folds[i].length;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sumAcc/lines.size();
    }
    
    public void train(String trainFile, String modelFile, HashMap<String, Double> parameters) throws IOException{
        Runtime rt = Runtime.getRuntime();
        ArrayList<String> commandList = new ArrayList<String>();
        commandList.add(svmDir + "/svm-train");
        for (int i = 0; i < options.length; i++) {
            String option = options[i];
            if (parameters.containsKey(option)) {
                commandList.add(option);
                if (intOption[i]) {
                    commandList.add("" + Math.round(parameters.get(option)));
                } else {
                    commandList.add("" + parameters.get(option));
                }
            }
            
        }
        commandList.add(trainFile);
        commandList.add(modelFile);
        String[] commands = new String[commandList.size()];
        commands = commandList.toArray(commands);
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new 
             InputStreamReader(proc.getInputStream()));
        
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
        BufferedReader stdError = new BufferedReader(new 
                InputStreamReader(proc.getErrorStream()));

        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }
    }
    
    public double accuracy(String testFile, String modelFile, String outFile) throws IOException{
        Runtime rt = Runtime.getRuntime();
        if (outFile == null) {
            File temp = File.createTempFile("temp-file-name", ".tmp");
            outFile = temp.getAbsolutePath();
        }
        String[] commands = {svmDir + "/svm-predict",testFile, modelFile, outFile};
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new 
             InputStreamReader(proc.getInputStream()));
        
        String s = null;
        double accuracy = -1.0;
        while ((s = stdInput.readLine()) != null) {
            if (s.startsWith("Accuracy")) {
                String accuracyString = s.split(" ")[2];
                accuracy = Double.parseDouble(accuracyString.substring(0, accuracyString.length() - 1));
            }
        }
        if (accuracy >= 0) {
            return accuracy;
        }
        BufferedReader stdError = new BufferedReader(new 
                InputStreamReader(proc.getErrorStream()));

        // read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }
        throw new UnimplementedException("");
    }
    
    public static void main(String[] args) throws IOException{
        String svmDir = "/home/thenghiapham/Downloads/libsvm-3.20";
        SvmUtils utils = new SvmUtils(svmDir);
        
        String trainFile = "/home/thenghiapham/work/project/mikolov/rte/svm/addSICK_train_trial.txt";
        
        HashMap<String, Double> parameters = new HashMap<String, Double>();
        parameters.put("-s", 0.0);
        parameters.put("-t", 2.0);
        parameters.put("-d", 2.0);
        double acc = utils.computeAccWithCross(trainFile, parameters, 5);
        System.out.println("cross acc: " + acc);
    }
}
