package word2vec;

import common.exception.ValueException;

import space.SemanticSpace;
import vocab.Vocab;
import vocab.VocabEntry;
import io.word.Phrase;

public class MMSkipNgramWord2Vec extends SingleThreadWord2Vec {
    public MMSkipNgramWord2Vec(int projectionLayerSize, int windowSize,
            boolean hierarchicalSoftmax, int negativeSamples, int negativeSamplesImages, double subSample) {
        super(projectionLayerSize, windowSize, hierarchicalSoftmax,
                negativeSamples, negativeSamplesImages, subSample);
    }
    
    public MMSkipNgramWord2Vec(int projectionLayerSize, int windowSize,
            boolean hierarchicalSoftmax, int negativeSamples, int negativeSamplesImages , double subSample,  String menFile) {
        super(projectionLayerSize, windowSize, hierarchicalSoftmax,
                negativeSamples, negativeSamplesImages, subSample, menFile);
    }

    public void trainSentence(int[] sentence) {
        // train with the sentence
        double[] a1 = new double[projectionLayerSize];
        double[] a1error = new double[projectionLayerSize];
        int sentenceLength = sentence.length;
        int iWordIndex = 0;
        
        
        boolean updateAtTheEnd=false;
        
        for (int wordPosition = 0; wordPosition < sentence.length; wordPosition++) {

            int wordIndex = sentence[wordPosition];

            // no way it will go here
            if (wordIndex == -1)
                continue;

            for (int i = 0; i < projectionLayerSize; i++) {
                a1[i] = 0;
                a1error[i] = 0;
            }

            // random actual window size
            int start = rand.nextInt(windowSize);

            VocabEntry targetWord = vocab.getEntry(wordIndex);
            //modality 1
            for (int i = start; i < windowSize * 2 + 1 - start; i++) {
                if (i != windowSize) {
                    int iPos = wordPosition - windowSize + i;
                    if (iPos < 0 || iPos >= sentenceLength)
                        continue;
                    iWordIndex = sentence[iPos];
                    if (iWordIndex == -1)
                        continue;

                    //for (int j = 0; j < projectionLayerSize; j++)
                     //   a1error[j] = 0;
                    VocabEntry context = vocab.getEntry(iWordIndex);
                    // HIERARCHICAL SOFTMAX
                    if (hierarchicalSoftmax) {
                        for (int bit = 0; bit < context.code.length(); bit++) {
                            double z2 = 0;
                            int iParentIndex = context.ancestors[bit];
                            // Propagate hidden -> output
                            for (int j = 0; j < projectionLayerSize; j++) {
                                z2 += weights0[wordIndex][j]
                                        * weights1[iParentIndex][j];
                            }

                            double a2 = sigmoidTable.getSigmoid(z2);
                            if (a2 == 0 || a2 == 1)
                                continue;
                            // 'g' is the gradient multiplied by the learning
                            // rate
                            double gradient = (double) ((1 - (context.code
                                    .charAt(bit) - 48) - a2) * alpha);
                            // Propagate errors output -> hidden
                            for (int j = 0; j < projectionLayerSize; j++) {
                                a1error[j] += gradient
                                        * weights1[iParentIndex][j];
                            }
                            // Learn weights hidden -> output
                            for (int j = 0; j < projectionLayerSize; j++) {
                                weights1[iParentIndex][j] += gradient
                                        * weights0[wordIndex][j];
                            }
                        }
                    }
                    

//                    // NEGATIVE SAMPLING
//                    if (negativeSamples > 0) {
//                        for (int l = 0; l < negativeSamples + 1; l++) {
//                            int target;
//                            int label;
//
//                            if (l == 0) {
//                                target = wordIndex;
//                                label = 1;
//                            } else {
//                                target = unigram.randomWordIndex();    
//                                if (target == 0) {
//                                    target = rand
//                                            .nextInt(vocab.getVocabSize() - 1) + 1;
//                                }
//                                if (target == wordIndex)
//                                    continue;
//                                label = 0;
//                            }
//                            double z2 = 0;
//                            double gradient;
//                            for (int j = 0; j < projectionLayerSize; j++) {
//                                z2 += weights0[iWordIndex][j]
//                                        * negativeWeights1[target][j];
//                            }
//                            double a2 = sigmoidTable.getSigmoid(z2);
//                            gradient = (double) ((label - a2) * alpha);
//                            for (int j = 0; j < projectionLayerSize; j++) {
//                                a1error[j] += gradient
//                                        * negativeWeights1[target][j];
//                            }
//                            for (int j = 0; j < projectionLayerSize; j++) {
//                                negativeWeights1[target][j] += gradient
//                                        * weights0[iWordIndex][j];
//                            }
//                        }
                }
                    
             }
            // Learn weights input -> hidden
            if (!updateAtTheEnd){
                for (int j = 0; j < projectionLayerSize; j++) {
                    weights0[wordIndex][j] += a1error[j];
                }
            }
        
                /*************    FOR SECOND MODALITY   ****************/
            if (!updateAtTheEnd){
                for (int i = 0; i < projectionLayerSize; i++) {
                    a1error[i] = 0;
                }
            }
        
        
            String percept =    targetWord.word;      
            int jPerceptIndex = images.getIndex(percept);
       
            // NEGATIVE SAMPLING  
            if (negativeSamplesImages > 0 && jPerceptIndex!=-1) {
            
                for (int l = 0; l < negativeSamplesImages + 1; l++) {
                    int target;
                    int label;

                    if (l == 0) {
                        target = jPerceptIndex;
                        label = 1;
                    } else {
                        target = images.randomWordIndex();       //random sampling and then based on neighboorhood
                        if (target == jPerceptIndex)
                            continue;
                        label = 0;
                    }
                    double z2 = 0;
                    double gradient;
                    for (int j = 0; j < projectionLayerSize; j++) {
                        z2 += weights0[wordIndex][j]
                            * negativeWeights1Images[target][j];
                    }
                    double a2 = sigmoidTable.getSigmoid(z2);
                    gradient = (double) ((label - a2) * alpha);
                    for (int j = 0; j < projectionLayerSize; j++) {
                        a1error[j] += gradient
                            * negativeWeights1Images[target][j];
                    }
                    /***************     This is for updating also image vectors
                    //  Learn weights hidden -> output
                    for (int j = 0; j < projectionLayerSize; j++) {
                        negativeWeights1[target][j] += gradient
                            * weights0[jPerceptIndex][j];
                    }
                     ****************/
                }
            }
            // Learn weights input -> hidden
            for (int j = 0; j < projectionLayerSize; j++) {
                weights0[wordIndex][j] += a1error[j];
            }
        
        }

    }

    public double[] getCors() throws ValueException{
        return images.pairwise_cor(new SemanticSpace(vocab, weights0,false));
    }

    @Override
    public void trainSinglePhrase(Phrase phrase, int[] pseudoSentence) {
        // TODO Auto-generated method stub

    }
    
    
}

