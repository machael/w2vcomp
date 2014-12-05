package utils;

import space.RawSemanticSpace;
import common.correlation.ANCorrelation;
import common.correlation.MenCorrelation;
import common.correlation.NNCorrelation;
import common.correlation.ParsedPhraseCorrelation;
import common.correlation.SVCorrelation;
import common.correlation.VOCorrelation;
import composition.WeightedAdditive;
import demo.TestConstants;

public class AddEvaluation {
    public static void main(String[] args) {
        String vectorFile = TestConstants.S_VECTOR_FILE.replace(".bin", "_cbow.bin");
//        String vectorFile = TestConstants.S_VECTOR_FILE;
        if (args.length > 0) {
            vectorFile = args[0];
        }
        WeightedAdditive add = new WeightedAdditive();
        RawSemanticSpace space = RawSemanticSpace.readSpace(vectorFile);
//        space.
        MenCorrelation men = new MenCorrelation(TestConstants.S_MEN_FILE);
        ANCorrelation an = new ANCorrelation(TestConstants.S_AN_FILE);
        NNCorrelation nn = new NNCorrelation(TestConstants.S_NN_FILE);
//        SVCorrelation sv = new SVCorrelation(TestConstants.S_SV_FILE);
        VOCorrelation vo = new VOCorrelation(TestConstants.S_VO_FILE);
        ParsedPhraseCorrelation sick = new ParsedPhraseCorrelation(TestConstants.S_SICK_FILE);
        
        System.out.println("men: " + men.evaluateSpacePearson(space));
        System.out.println("an add: " + an.evaluateSpacePearson(space, add));
        System.out.println("nn add: " + nn.evaluateSpacePearson(space, add));
//        System.out.println("sv add: " + sv.evaluateSpacePearson(space, add));
        System.out.println("vo add: " + vo.evaluateSpacePearson(space, add));
        System.out.println("sick add: " + sick.evaluateSpacePearson(space, add));
        
    }
}
