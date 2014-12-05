package demo;

import io.sentence.BasicTreeInputStream;
import io.sentence.ParseSentenceInputStream;
import io.sentence.SentenceInputStream;
import io.word.CombinedWordInputStream;
import io.word.TreeWordInputStream;
import io.word.WordInputStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import vocab.Vocab;
import word2vec.CBowWord2Vec;
import word2vec.MultiThreadCBow;
import word2vec.MultiThreadHybridWord2Vec;
import word2vec.MultiThreadSkipGram;
//import word2vec.CBowWord2Vec;

import demo.TestConstants;

public class WordVectorLearning2 {
    public static void main(String[] args) throws IOException{
//        MultiThreadSkipGram word2vec = new MultiThreadSkipGram(40, 5, true, 0, 0, TestConstants.S_MEN_FILE);
//        MultiThreadCBow word2vec = new MultiThreadCBow(100, 5, true, 0, 1e-3, TestConstants.S_MEN_FILE);
        MultiThreadHybridWord2Vec word2vec = new MultiThreadHybridWord2Vec(300, 5, true, 0, 0, TestConstants.S_MEN_FILE);
        String suffix = "";
        String trainDirPath = TestConstants.S_TRAIN_DIR;
        String outputFile = TestConstants.S_VECTOR_FILE.replace(".bin", suffix + ".bin");
        String vocabFile = TestConstants.S_VOCABULARY_FILE;
        String modelFile = TestConstants.S_MODEL_FILE.replace(".mdl", suffix + ".mdl");;
        System.out.println("Starting training using files in " + trainDirPath);

        boolean learnVocab = !(new File(vocabFile)).exists();
        File trainDir = new File(trainDirPath);
        File[] trainFiles = trainDir.listFiles();
        Vocab vocab = new Vocab(5);
        if (!learnVocab)
            vocab.loadVocab(vocabFile);// ,minFrequency);
        else {
            ArrayList<WordInputStream> wordStreamList = new ArrayList<>();
            for (File trainFile: trainFiles) {
                TreeWordInputStream wordStream = new TreeWordInputStream(new BasicTreeInputStream(trainFile));
                wordStreamList.add(wordStream);
            }
          
            CombinedWordInputStream wordStream = new CombinedWordInputStream(wordStreamList);
            vocab.learnVocabFromTrainStream(wordStream);
            vocab.saveVocab(vocabFile);
        }

        word2vec.setVocab(vocab);

        word2vec.initNetwork();

        // single threaded instead of multithreading
        System.out.println("Start training");
        try {
            ArrayList<SentenceInputStream> inputStreams = new ArrayList<SentenceInputStream>();
            for (File trainFile: trainFiles) {
                SentenceInputStream sentenceInputStream = new ParseSentenceInputStream(new BasicTreeInputStream(trainFile));
                inputStreams.add(sentenceInputStream);
            }
            word2vec.trainModel(inputStreams);
            word2vec.saveVector(outputFile, true);
            word2vec.saveNetwork(modelFile, true);
        } catch (IOException e) {
            System.exit(1);
        }


    }
}
