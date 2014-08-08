package common;

import java.util.ArrayList;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import demo.TestConstants;

import space.NewSemanticSpace;
import space.SemanticSpace;

/**
 * This class can be used to evaluate a word vector space by computing the
 * correlation between the cosine of the words' vectors and the gold-standard
 * similarities of them (typically based on human judgment)
 * The name is kind of misleading since we can use other dataset than MEN
 * @author thenghiapham
 *
 */

public class MenCorrelation {
	String[][] wordPairs;
	double[] golds;
	PearsonsCorrelation pearson;
	SpearmansCorrelation spearman;
	
	/**
	 * Initialize with the path to the dataset file
	 * @param dataset
	 */
	public MenCorrelation(String dataset) {
	    pearson = new PearsonsCorrelation();
	    spearman = new SpearmansCorrelation();
		readDataset(dataset);
	}
	
	
	public MenCorrelation(String[][] wordPairs, double[] golds) {
	    pearson = new PearsonsCorrelation();
        spearman = new SpearmansCorrelation();
        this.wordPairs = wordPairs;
        this.golds = golds;
	}
	

    /**
     * Read the word pairs and the gold standard from the dataset
     * @param dataset
     */
	public void readDataset(String dataset) {
		ArrayList<String> data = IOUtils.readFile(dataset);
		golds = new double[data.size()];
		wordPairs = new String[data.size()][2];
		for (int i = 0; i < data.size(); i++) {
			String dataPiece = data.get(i);
			String elements[] = dataPiece.split(" ");
			wordPairs[i][0] = elements[0];
			wordPairs[i][1] = elements[1];
			golds[i] = Double.parseDouble(elements[2]);
		}
	}
	
	/**
	 * Compute the pearson correlation of the predicted values against the gold
	 * standard
	 * @param predicts
	 * @return
	 */
	public double pearsonCorrelation(double[] predicts) {
	    return pearson.correlation(golds, predicts);
	}
	
	/**
	 * Compute the spearman correlation of the predicted values against the gold
     * standard 
	 * @param predicts
	 * @return
	 */
	public double spearmanCorrelation(double[] predicts) {
        return spearman.correlation(golds, predicts);
    }
	
	
	/**
	 * Evaluate the space using the pearson correlation
	 * @param space
	 * @return
	 */
	public double evaluateSpacePearson(SemanticSpace space) {
	    double[] predicts = new double[golds.length];
	    for (int i = 0; i < golds.length; i++) {
	        predicts[i] = space.getSim(wordPairs[i][0], wordPairs[i][1]);
	    }
	    return pearson.correlation(golds, predicts);
	}
	
	/**
     * Evaluate the space using the pearson correlation
     * @param space
     * @return
     */
    public double evaluateSpacePearson(NewSemanticSpace space) {
        double[] predicts = new double[golds.length];
        for (int i = 0; i < golds.length; i++) {
            predicts[i] = space.getSim(wordPairs[i][0], wordPairs[i][1]);
        }
        return pearson.correlation(golds, predicts);
    }
	
	/**
     * Evaluate the space using the spearman correlation
     * @param space
     * @return
     */
	public double evaluateSpaceSpearman(SemanticSpace space) {
        double[] predicts = new double[golds.length];
        for (int i = 0; i < golds.length; i++) {
            predicts[i] = space.getSim(wordPairs[i][0], wordPairs[i][1]);
        }
        return spearman.correlation(golds, predicts);
    }
	
	/**
     * Evaluate the space using the spearman correlation
     * @param space
     * @return
     */
    public double evaluateSpaceSpearman(NewSemanticSpace space) {
        double[] predicts = new double[golds.length];
        for (int i = 0; i < golds.length; i++) {
            predicts[i] = space.getSim(wordPairs[i][0], wordPairs[i][1]);
        }
        return spearman.correlation(golds, predicts);
    }
	
	/**
	 * @return the gold standard (human's judgment on the similarities)
	 */
	public double[] getGolds() {
	    return golds;
	}
	
	public static void main(String[] args) {
//	    SemanticSpace space = SemanticSpace.readSpace("/home/thenghiapham/svn/w2v-unmodified/vectors.bin");
	    SemanticSpace space = SemanticSpace.readSpace(TestConstants.CCG_VECTOR_FILE);
	    MenCorrelation men = new MenCorrelation(TestConstants.CCG_MEN_FILE);
	    System.out.println("men: " + men.evaluateSpacePearson(space));
	}
}
