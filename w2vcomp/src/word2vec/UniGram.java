package word2vec;

import java.util.Random;

import vocab.Vocab;

public class UniGram {
    public static final int DEFAULT_TABLE_SIZE = 100000000;
    protected int           randomTablesize;
    protected int[]         randomTable;
    private Random          random;

    public UniGram(Vocab vocab, int tableSize) {
        this.randomTablesize = tableSize;
        initUnigramTable(vocab);
        random = new Random();
    }

    public UniGram(Vocab vocab) {
        this(vocab, DEFAULT_TABLE_SIZE);
    }

    /**
     * Create an unigram table to randomly generate a word. The probability of
     * generating a word corresponds to its frequency^3/4
     */
    protected void initUnigramTable(Vocab vocab) {
        long trainWordsPow = 0;
        float sumPow;
        float power = (float) 0.75;
        int vocabSize = vocab.getVocabSize();
        randomTable = new int[randomTablesize];
        for (int i = 0; i < vocabSize; i++) {
            trainWordsPow += Math.pow(vocab.getEntry(i).frequency, power);
        }
        int index = 0;
        sumPow = (float) Math.pow(vocab.getEntry(index).frequency, power)
                / trainWordsPow;
        for (int i = 0; i < randomTablesize; i++) {
            randomTable[i] = index;
            if (i / (float) randomTablesize > sumPow) {
                index++;
                if (index < vocabSize) {
                    sumPow += Math.pow(vocab.getEntry(index).frequency, power)
                            / trainWordsPow;
                } else {
                    System.out.println("what does it mean here");
                }
            }
            if (index >= vocabSize)
                index = vocabSize - 1;
        }
    }

    public int randomWordIndex() {
        int randomInt = random.nextInt(randomTablesize);
        return randomTable[randomInt];
    }

}
