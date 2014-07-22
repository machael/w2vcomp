package word2vec;

import java.util.ArrayList;

import org.ejml.simple.SimpleMatrix;

import tree.CcgTree;
import vocab.VocabEntry;

import common.AdjNounCorrelation;
import common.GradientUtils;
import common.IOUtils;
import common.SimpleMatrixUtils;
import common.ValueGradient;
import composition.FullAdditive;
import composition.WeightedAdditive;

import edu.stanford.nlp.neural.NeuralUtils;
import io.word.Phrase;

public class NeuralLanguageModel extends SingleThreadWord2Vec{

    // TODO: adding tanh
    // TODO: check gradient negative sampling
    // TODO: adding adj-noun composition
    SimpleMatrix compositionMatrix;
    double weightDecay = 1e-4;
    boolean useTanh = false;
    AdjNounCorrelation anCorrelation;
    
    public NeuralLanguageModel(int projectionLayerSize, int windowSize,
            boolean hierarchicalSoftmax, int negativeSamples, double subSample) {
        super(projectionLayerSize, windowSize, hierarchicalSoftmax, negativeSamples,
                subSample);
        // TODO Auto-generated constructor stub
    }
    
    public NeuralLanguageModel(int projectionLayerSize, int windowSize,
            boolean hierarchicalSoftmax, int negativeSamples, double subSample,
            String menFile) {
        super(projectionLayerSize, windowSize, hierarchicalSoftmax, negativeSamples,
                subSample, menFile);
        // TODO Auto-generated constructor stub
    }

    public NeuralLanguageModel(int projectionLayerSize, int windowSize,
            boolean hierarchialSoftmax, int negativeSamples,
            double subSample, String menFile, String anFile) {
        // TODO Auto-generated constructor stub
        super(projectionLayerSize, windowSize, hierarchialSoftmax, negativeSamples,
                subSample, menFile);
        anCorrelation = new AdjNounCorrelation(anFile);
    }

    protected void initBare() {
        super.initBare();
//        randomInitializeComposeMatrices();
        initalizeComposeMatricesAsIdentity();
    }
  
    protected void randomInitializeComposeMatrices(){
        double[][] randomMatrix = new double[projectionLayerSize][2 * projectionLayerSize];
        for (int i = 0; i < projectionLayerSize; i++) {
            for (int j = 0; j < 2 * projectionLayerSize; j++) {
                randomMatrix[i][j] = (double) (rand.nextFloat() - 0.5)
                        / projectionLayerSize;
            }
        }
        compositionMatrix = new SimpleMatrix(randomMatrix);
    }
    
    protected void initalizeComposeMatricesAsIdentity() {
        SimpleMatrix identity = SimpleMatrix.identity(projectionLayerSize);
        compositionMatrix = SimpleMatrixUtils.hStack(identity, identity);
    }
  
    public void checkingGradientsSoftmax(SimpleMatrix inputPhrase, SimpleMatrix compositionMatrix,
              SimpleMatrix softmaxWeight, SimpleMatrix softmaxValue) {
        ValueGradient valueGrad = computeGradientSoftmax(inputPhrase, compositionMatrix, softmaxWeight, softmaxValue);
        ArrayList<SimpleMatrix> gradients = valueGrad.gradients;
        ArrayList<SimpleMatrix> numGradients = computeNumericGradientsSoftmax(inputPhrase, compositionMatrix, softmaxWeight, softmaxValue);
        for (int i = 0; i < gradients.size(); i++) {
          
            SimpleMatrix component = gradients.get(i);
            SimpleMatrix numComponent = numGradients.get(i);
            
            double squareError = component.minus(numComponent).normF();
            squareError = squareError * squareError;
            if (squareError / (component.numCols() * component.numRows()) > 1e-4) {
                System.out.println("Big error "+ i + ": " + squareError / (component.numCols() * component.numRows()));
            } else {
//              System.out.println("Good error");
            }
        }
    }
  
    public void checkingGradientsNegativeSampling(SimpleMatrix inputPhrase, SimpleMatrix compositionMatrix,
            SimpleMatrix negativeWeight, SimpleMatrix target) {
        ValueGradient valueGrad = computeGradientNegSampling(inputPhrase, compositionMatrix, negativeWeight, target);
        ArrayList<SimpleMatrix> gradients = valueGrad.gradients;
        ArrayList<SimpleMatrix> numGradients = computeNumericGradientsNegativeSampling(inputPhrase, compositionMatrix, negativeWeight, target);
        for (int i = 0; i < gradients.size(); i++) {
            
            SimpleMatrix component = gradients.get(i);
            SimpleMatrix numComponent = numGradients.get(i);
            
            double squareError = component.minus(numComponent).normF();
            squareError = squareError * squareError;
            if (squareError / (component.numCols() * component.numRows()) > 1e-4) {
                System.out.println("Big error");
            } else {
                //          System.out.println("Good error");
            }
        }
    }
  
    public ArrayList<SimpleMatrix> computeNumericGradientsSoftmax(SimpleMatrix inputPhrase, SimpleMatrix compositionMatrix,
            SimpleMatrix softmaxWeight, SimpleMatrix softmaxValue) {
        ArrayList<SimpleMatrix> numGradients = new ArrayList<>();
        SimpleMatrix theta[] = new SimpleMatrix[]{inputPhrase, compositionMatrix, softmaxWeight};
        double e = 1e-4;
        for (int i = 0; i < 3; i++) {
            SimpleMatrix component = theta[i];
            int rowNum = component.numRows();
            int colNum = component.numCols();
            SimpleMatrix componentDelta = new SimpleMatrix(rowNum, colNum);
            SimpleMatrix componentGrad = new SimpleMatrix(rowNum, colNum);
            for (int x = 0; x < rowNum; x++) {
                for (int y = 0; y < colNum; y++) {
                    componentDelta.set(x, y, e);
                    theta[i] = component.plus(componentDelta);
                    double loss1 = computeGradientSoftmax(theta[0], theta[1], theta[2], softmaxValue).value;
                    theta[i] = component.minus(componentDelta);
                    double loss2 = computeGradientSoftmax(theta[0], theta[1], theta[2], softmaxValue).value;
                    componentGrad.set(x, y, (loss1 - loss2) / (2 * e));
                    componentDelta.set(x, y, 0);
                }
            }
            theta[i] = component;
            numGradients.add(componentGrad);
        }
        return numGradients;
    }
  
    public ArrayList<SimpleMatrix> computeNumericGradientsNegativeSampling(SimpleMatrix inputPhrase, SimpleMatrix compositionMatrix,
            SimpleMatrix softmaxWeight, SimpleMatrix softmaxValue) {
        ArrayList<SimpleMatrix> numGradients = new ArrayList<>();
        SimpleMatrix theta[] = new SimpleMatrix[]{inputPhrase, compositionMatrix, softmaxWeight};
        double e = 1e-4;
        for (int i = 0; i < 3; i++) {
            SimpleMatrix component = theta[i];
            int rowNum = component.numRows();
            int colNum = component.numCols();
            SimpleMatrix componentDelta = new SimpleMatrix(rowNum, colNum);
            SimpleMatrix componentGrad = new SimpleMatrix(rowNum, colNum);
            for (int x = 0; x < rowNum; x++) {
                for (int y = 0; y < colNum; y++) {
                    componentDelta.set(x, y, e);
                    theta[i] = component.plus(componentDelta);
                    double loss1 = computeGradientNegSampling(theta[0], theta[1], theta[2], softmaxValue).value;
                    theta[i] = component.minus(componentDelta);
                    double loss2 = computeGradientNegSampling(theta[0], theta[1], theta[2], softmaxValue).value;
                    componentGrad.set(x, y, (loss1 - loss2) / (2 * e));
                    componentDelta.set(x, y, 0);
                }
            }
            theta[i] = component;
            numGradients.add(componentGrad);
        }
        return numGradients;
    }
  
  
  
    public ValueGradient computeGradientSoftmax(SimpleMatrix inputPhrase, SimpleMatrix compositionMatrix,
            SimpleMatrix softmaxWeight, SimpleMatrix softmaxValue) {
      
        /*
         * Forward pass
         */
        SimpleMatrix a0 = inputPhrase;
        SimpleMatrix z1 = compositionMatrix.mult(a0);
        SimpleMatrix a1;
        if (useTanh) {
            a1 = NeuralUtils.elementwiseApplyTanh(z1);
        } else {
            a1 = z1;
        }
//        System.out.println("norm a1: " + a1.normF());
        SimpleMatrix z2 = softmaxWeight.mult(a1);
//        System.out.println("norm w1: " + softmaxWeight.normF());
//        System.out.println("norm z2: " + z2.normF());
        SimpleMatrix a2 = SimpleMatrixUtils.elementwiseApplySigmoid(z2, sigmoidTable);
        //TODO: compute value function here
        double value = 0.0;
        for (int i = 0; i < softmaxValue.numRows(); i++) {
            double a2ithElement = a2.get(i, 0);
          
            double bit = softmaxValue.get(i);
            /* 
             * if sigmoid (probability) ~ 1, gradient = 0;
             * if sigmoid (probability) ~ 0, gradient too big,
             * -> cut down to 0; still I don't know why we don't cut
             * it down to value f'(x) ~ f'(-6) (in that case make f(x) = f(-6) 
             * instead of removing it
             * anyway, look at Mikolov's code again
             */
            if (a2ithElement == 0 || a2ithElement == 1) {
//                System.out.println("blah" +  a2ithElement);
                continue;
            }
            else {
//                System.out.println("blah" +  a2ithElement);
                if (bit == 0) {
                    value += Math.log(a2ithElement);
                } else {
                    value += Math.log(1 - a2ithElement);
                }
            }
        }
        if (weightDecay != 0.0) {
            double normF = compositionMatrix.normF(); 
            value -= (weightDecay / 2) * normF * normF;
        }
        
        ArrayList<SimpleMatrix> gradients = new ArrayList<>();
        SimpleMatrix softmaxWeightGradient = null;
        SimpleMatrix compositionMatrixGradient = null;
        SimpleMatrix inputPhraseGradient = null;
      
        /*
         * Backward pass
         */
        double[] rawd2 = new double[softmaxValue.numRows()];
        double[] rawSoftmaxValue = softmaxValue.getMatrix().getData();
        for (int i = 0; i < softmaxValue.numRows(); i++) {
            double a2ithElement = a2.get(i, 0);
            if (a2ithElement == 0 || a2ithElement == 1) {
                rawd2[i] = 0;
            } else {
                rawd2[i] = (1 - rawSoftmaxValue[i] - a2ithElement); //a2ithElement * 
            }
        }
        SimpleMatrix d2 = new SimpleMatrix(rawd2.length, 1, true, rawd2);
        // W2's gradient
        softmaxWeightGradient = d2.mult(a1.transpose());
      
        SimpleMatrix d1 = softmaxWeight.transpose().mult(d2);
        if (useTanh) {
            d1 = d1.elementMult(NeuralUtils.elementwiseApplyTanhDerivative(z1));
        }
        // W1's gradient
        compositionMatrixGradient = d1.mult(a0.transpose());
        
        // input's gradient
        // it's d0
        inputPhraseGradient = compositionMatrix.transpose().mult(d1);
      
        if (weightDecay != 0.0) {
            compositionMatrixGradient = compositionMatrixGradient.minus(compositionMatrix.scale(weightDecay));
        }
        gradients.add(inputPhraseGradient);
        gradients.add(compositionMatrixGradient);
        gradients.add(softmaxWeightGradient);
        return new ValueGradient(value, gradients);
    }
  
  
    public ValueGradient computeGradientNegSampling(SimpleMatrix inputPhrase, SimpleMatrix compositionMatrix,
            SimpleMatrix negativeWeight, SimpleMatrix target) {
      
        /*
         * Forward pass
         */
        SimpleMatrix a0 = inputPhrase;
        SimpleMatrix z1 = compositionMatrix.mult(a0);
        SimpleMatrix a1;
        if (useTanh) {
            a1 = NeuralUtils.elementwiseApplyTanh(z1);
        } else {
            a1 = z1;
        }
        SimpleMatrix z2 = negativeWeight.mult(a1);
        SimpleMatrix a2 = SimpleMatrixUtils.elementwiseApplySigmoid(z2, sigmoidTable);
        
        // TODO: compute value function here
        double value = 0.0;
        for (int i = 0; i < target.numRows(); i++) {
            double a2ithElement = a2.get(i, 0);
            double bit = target.get(i);
            /* 
             * if sigmoid (probability) ~ 1, log = 0;
             * if sigmoid (probability) ~ 0, log too big,
             * -> cut down to 0; still I don't know why we don't cut
             * it down to value f'(x) ~ f'(-6) (in that case make f(x) = f(-6) 
             * instead of removing it
             * anyway, look at Mikolov's code again
             */
          
            if (a2ithElement == 0 || a2ithElement == 1) continue;
          
            if (bit == 1) {
                value += Math.log(a2ithElement);
            } else {
                value += Math.log(1 - a2ithElement);
            }
        }
        if (weightDecay != 0.0) {
            double normF = compositionMatrix.normF(); 
            value -= (weightDecay / 2) * normF * normF;
        }
      
        ArrayList<SimpleMatrix> gradients = new ArrayList<>();
        SimpleMatrix negWeightGradient = null;
        SimpleMatrix compositionMatrixGradient = null;
        SimpleMatrix inputPhraseGradient = null;
      
        /*
         * Backward pass
         */
        double[] rawd2 = new double[target.numRows()];
        double[] rawTargetValue = target.getMatrix().getData();
        for (int i = 0; i < target.numRows(); i++) {
            double a2ithElement = a2.get(i, 0);
            double bit = target.get(i);
            if (a2ithElement == 0) {
                rawd2[i] = bit - 0; 
            } else if (a2ithElement == 1) {
                rawd2[i] = bit - 1;
            } else {
                rawd2[i] = (rawTargetValue[i] - a2ithElement); //a2ithElement * 
            }
        }
        SimpleMatrix d2 = new SimpleMatrix(rawd2.length, 1, true, rawd2);
        // W2's gradient
        negWeightGradient = d2.mult(a1.transpose());
      
        SimpleMatrix d1 = negativeWeight.transpose().mult(d2);
        if (useTanh) {
            d1 = d1.elementMult(NeuralUtils.elementwiseApplyTanhDerivative(z1));
        }
        // W1's gradient
        compositionMatrixGradient = d1.mult(a0.transpose());
      
        // input's gradient
        // it's d0
        inputPhraseGradient = compositionMatrix.transpose().mult(d1);
      
        if (weightDecay != 0.0) {
            compositionMatrixGradient = compositionMatrixGradient.minus(compositionMatrix.scale(weightDecay));
        }
        gradients.add(inputPhraseGradient);
        gradients.add(compositionMatrixGradient);
        gradients.add(negWeightGradient);
        return new ValueGradient(value, gradients);
    }
  


    @Override
    public void trainSinglePhrase(Phrase phrase, int[] sentence) {
     // train with the sentence
        
//        System.out.println(phrase.tree.getSurfaceString());
        // TODO: adding a HashTable or something here to use more matrices for
        // different constructions
//        if (phrase.phraseType != CcgTree.AN) return;
//        System.out.println("AN");
        
        
        int sentenceLength = sentence.length;
        int iWordIndex = 0;

        // TODO: change this
        int word1Index = ((CcgTree) phrase.tree.getChildren().get(0).getChildren().get(0)).getWordIndex();
        int word2Index = ((CcgTree) phrase.tree.getChildren().get(1).getChildren().get(0)).getWordIndex();
        
        // random actual window size
        int start = rand.nextInt(windowSize);

        for (int iPos = phrase.startPosition - start - 1; iPos <= phrase.endPosition + start + 1; iPos++) {
            if (iPos < 0 || iPos >= sentenceLength || (iPos >= phrase.startPosition && iPos <= phrase.endPosition))
                continue;
            iWordIndex = sentence[iPos];
            if (iWordIndex == -1)
                continue;
            
            double[] a0 = new double[2 * projectionLayerSize];
            System.arraycopy(weights0[word1Index], 0, a0, 0, projectionLayerSize);
            System.arraycopy(weights0[word2Index], 0, a0, projectionLayerSize, projectionLayerSize);
            SimpleMatrix inputPhrase = new SimpleMatrix(2 * projectionLayerSize, 1, true, a0);
            
            VocabEntry contextWord = vocab.getEntry(iWordIndex);
            // HIERARCHICAL SOFTMAX
            if (hierarchicalSoftmax) {
                int codeLength = contextWord.code.length();
                double[][] rawSoftmaxWeight = new double[codeLength][projectionLayerSize];
                double[] rawSoftmaxValue = new double[codeLength];
                int[] parentIds = contextWord.ancestors;
                
                for (int bit = 0; bit < codeLength; bit++) {
                    int iParentIndex = contextWord.ancestors[bit];
//                    System.out.println("" + bit + ": " + iParentIndex);
                    System.arraycopy(weights1[iParentIndex], 0, rawSoftmaxWeight[bit], 0, projectionLayerSize);
                    rawSoftmaxValue[bit] = contextWord.code.charAt(bit) - 48;
                }
//                System.out.println("before updating:");
//                IOUtils.printDoubles(weights1[parentIds[0]]);
                
//                IOUtils.printDoubles(rawSoftmaxWeight[0]);
                SimpleMatrix softmaxWeight = new SimpleMatrix(rawSoftmaxWeight);
                
//                System.out.println("norm before:" + softmaxWeight.normF());
                SimpleMatrix softmaxValue = new SimpleMatrix(codeLength, 1, true, rawSoftmaxValue);
                
                boolean checked = (rand.nextInt(10000000) == 0);
                if (checked) checkingGradientsSoftmax(inputPhrase, compositionMatrix, softmaxWeight, softmaxValue);
                
                ValueGradient valueGrad = computeGradientSoftmax(inputPhrase, compositionMatrix, softmaxWeight, softmaxValue);
                
                SimpleMatrix inputPhraseGrad = valueGrad.gradients.get(0).scale(alpha / 2);
                compositionMatrix = compositionMatrix.plus(valueGrad.gradients.get(1).scale(alpha / 2));
                SimpleMatrix softmaxGrad = valueGrad.gradients.get(2).scale(alpha / 2);
                
                inputPhraseGrad.reshape(2, projectionLayerSize);
//                System.out.println("value: " + valueGrad.value);
//                System.out.println("grad norm: " + valueGrad.gradients.get(0).normF());
//                System.out.println("soft grad norm: " + valueGrad.gradients.get(2).normF());
//                System.out.println("soft grad norm: " + valueGrad.gradients.get(2).scale(alpha / 2).normF());
                GradientUtils.updateWeights(weights0,new int[]{word1Index, word2Index},inputPhraseGrad);
//                IOUtils.printInts(parentIds);
                GradientUtils.updateWeights(weights1, parentIds, softmaxGrad);
//                System.out.println("after updating:");
//                IOUtils.printDoubles(weights1[parentIds[0]]);
//                System.out.println(softmaxGrad.normF());
                
            }
            // NEGATIVE SAMPLING
            if (negativeSamples > 0) {
                double[][] rawNegWeights = new double[negativeSamples + 1][projectionLayerSize]; 
                double[] targets = new double[negativeSamples + 1];
                int[] targetWordIds = new int[negativeSamples + 1];
                targets[0] = 1;
                targetWordIds[0] = iWordIndex;
                System.arraycopy(negativeWeights1[iWordIndex], 0, rawNegWeights[0], 0, projectionLayerSize);
                
                for (int k = 0; k < negativeSamples; k++) {
                    targets[k+1] = 0;
                    int targetWordIndex = unigram.randomWordIndex();
                    // when targetWordIndex == iWordIndex, mikolov ignores 
                    // the training sample but blah, we'll see
                    // TODO: check the sample with freq(</s> = 0)
                    while (targetWordIndex == 0 || targetWordIndex == iWordIndex) {
                        targetWordIndex = rand
                                .nextInt(vocab.getVocabSize() - 1) + 1;
                    }
                    targetWordIds[k+1] = targetWordIndex;
                    System.arraycopy(negativeWeights1[targetWordIndex], 0, rawNegWeights[k + 1], 0, projectionLayerSize);
                }
                SimpleMatrix negWeight = new SimpleMatrix(rawNegWeights);
                SimpleMatrix targetValue = new SimpleMatrix(negativeSamples + 1, 1, true, targets);
                
                boolean checked = (rand.nextInt(1000000) == 0);
                if (checked) checkingGradientsNegativeSampling(inputPhrase, compositionMatrix, negWeight, targetValue);
                
                ValueGradient valueGrad = computeGradientNegSampling(inputPhrase, compositionMatrix, negWeight, targetValue);
                
                SimpleMatrix inputPhraseGrad = valueGrad.gradients.get(0).scale(alpha);
                compositionMatrix = compositionMatrix.plus(valueGrad.gradients.get(1).scale(alpha));
                SimpleMatrix negGrad = valueGrad.gradients.get(2).scale(alpha);
                
                inputPhraseGrad.reshape(2, projectionLayerSize);
                GradientUtils.updateWeights(weights0,new int[]{word1Index, word2Index},inputPhraseGrad);
                GradientUtils.updateWeights(negativeWeights1, targetWordIds, negGrad);
                
                
            }
            
        }
    }
            
    
    @Override
    public void printStatistics() {
        if (anCorrelation != null) {
            FullAdditive anComposition = new FullAdditive(compositionMatrix);
            WeightedAdditive additive = new WeightedAdditive();
            if (outputSpace != null) {
                System.out.println("an:\t" + anCorrelation.evaluateSpacePearson(outputSpace, anComposition));
                System.out.println("an add:\t" + anCorrelation.evaluateSpacePearson(outputSpace, additive));
            }
        }

        System.out.println("L2 comp: " + compositionMatrix.normF());
        if (hierarchicalSoftmax) {
            System.out.println("L2 soft: " + new SimpleMatrix(weights1).normF());
        }
        if (negativeSamples > 0) {
            System.out.println("L2 neg: " + new SimpleMatrix(negativeWeights1).normF());
        }
        System.out.println("L2 vectors: " + new SimpleMatrix(weights0).normF());
        System.out.println("*********");
    }
    
    public double getWeightDecay() {
        return weightDecay;
    }
    
    public void saveMatrix(String matrixFile, boolean binary) {
        IOUtils.saveMatrix(matrixFile, SimpleMatrixUtils.to2DArray(compositionMatrix), binary);
    }
    
    public void readMatrixFromBinaryFile(String matrixFile) {
        
    }

    @Override
    public void trainSentence(int[] sentence) {
        // Do nothing
    }

}
