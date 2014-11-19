package parallel.workers.w2v;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import io.sentence.BasicTreeInputStream;
import parallel.comm.ParameterMessager;
import parallel.workers.ModelParameters;
import parallel.workers.ParameterEstimator;
import tree.Tree;

public class W2vEstimator implements ParameterEstimator {

//    protected BasicTreeInputStream inputStream;
    protected BufferedReader reader;
    
    public W2vEstimator(String inputFile) {
        try {
//            inputStream = new BasicTreeInputStream(inputFile);
            reader = new BufferedReader(new FileReader(inputFile));
            System.out.println("File name: " + inputFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
//            inputStream = null;
            reader = null;
            e.printStackTrace();
        }
    }
    @Override
    public void run(ModelParameters init, ParameterMessager parameterMessager) {
        // TODO Auto-generated method stub
        long numLine = 0;
        W2vParameters parameters = new W2vParameters();
        try {
//            Tree tree = inputStream.readTree();
            String line = reader.readLine();
//            while (tree != null) {
            while (line != null) {
                numLine += 1;
                if (numLine % 10000 == 0) {
                    parameters.setValue(new Long(10000));
                    parameterMessager.sendUpdate(parameters);
                    System.out.println("numLine: " + numLine);
                }
//                tree = inputStream.readTree();
                line = reader.readLine();
            }
            parameters.setValue(new Long(numLine % 10000));
            parameterMessager.sendUpdate(parameters);
            System.out.println("numLine: " + numLine);
//            parameterMessager.sendEnd();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
