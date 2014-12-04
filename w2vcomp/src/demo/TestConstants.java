package demo;

public class TestConstants {
    
    public static final String TRAIN_FILE               = "/home/thenghiapham/work/project/mikolov/text/wikiA_sample.txt";
    public static final String TRAIN_DIR               = "/home/thenghiapham/work/project/mikolov/text/tmp";
    public static final String VECTOR_FILE              = "/home/thenghiapham/work/project/mikolov/text/wikiA.bin";
    public static final String VOCABULARY_FILE          = "/home/thenghiapham/work/project/mikolov/text/wikiA.voc";
    public static final String INITIALIZATION_FILE      = "/home/thenghiapham/work/project/mikolov/text/wikiA.ini";
    
   
    
    public static final String CCG_MEN_FILE             = "/home/thenghiapham/work/project/mikolov/men/subMen.txt";
    public static final String CCG_AN_FILE              = "/home/thenghiapham/work/dataset/lapata/nn_lemmapos.txt";
    public static final String CCG_TRAIN_FILE           = "/home/thenghiapham/work/project/mikolov/ccg_text/wikiA.txt";
    public static final String CCG_VECTOR_FILE          = "/home/thenghiapham/work/project/mikolov/output/phrase1.bin";
    public static final String CCG_VOCABULARY_FILE      = "/home/thenghiapham/work/project/mikolov/output/phrase1.voc";
    public static final String CCG_INITIALIZATION_FILE  = "/home/thenghiapham/work/project/mikolov/output/phrase1.ini";
    public static final String CCG_MATRIX_FILE          = "/home/thenghiapham/work/project/mikolov/output/phrase1.comp.mat";
    
    
    
    public static final String S_PROJECT_DIR              = "/home/thenghiapham/work/project/mikolov/";
//  public static final String S_PROJECT_DIR              = "/mnt/cimec-storage-sata/users/thenghia.pham/data/project/mikcom/";
    public static final String S_CONSTRUCTION_FILE        = S_PROJECT_DIR + "selected-constructions1.txt";
//  public static final String S_CONSTRUCTION_FILE        = S_PROJECT_DIR + "tmp-constructions.txt";
//  public static final String S_TRAIN_DIR                = S_PROJECT_DIR + "split_parse/wiki";
    public static final String S_TRAIN_DIR                = S_PROJECT_DIR + "split_parse/bnc";
//    public static final String S_TRAIN_DIR                = S_PROJECT_DIR + "tmp_parsed/bnc";
    public static final String S_TRAIN_FILE               = S_PROJECT_DIR + "tmp_parsed/bnc/bnc.txt";
    public static final int S_MIN_FREQUENCY               = 5;
    public static final String S_OUT_DIR                  = S_PROJECT_DIR + "output/";
//  public static final String S_VECTOR_FILE              = S_OUT_DIR + "wwiki.bin";
//  public static final String S_COMPOSITION_FILE         = S_OUT_DIR + "wwiki.cmp";
//  public static final String S_VOCABULARY_FILE          = S_OUT_DIR + "wwiki.voc";
//  public static final String S_INITIALIZATION_FILE      = S_OUT_DIR + "wwiki.ini";
    public static final String S_VECTOR_FILE              = S_OUT_DIR + "wbnc.bin";
    public static final String S_COMPOSITION_FILE         = S_OUT_DIR + "wbnc.cmp";
    public static final String S_MODEL_FILE               = S_OUT_DIR + "wbnc.mdl";
    public static final String S_VOCABULARY_FILE          = S_OUT_DIR + "wbnc.voc";
    public static final String S_INITIALIZATION_FILE      = S_OUT_DIR + "skipgram.mdl";
    
//    public static final String S_VECTOR_FILE              = S_OUT_DIR + "skip_bnc40.bin";
//    public static final String S_COMPOSITION_FILE         = S_OUT_DIR + "skip_bnc40.cmp";
//    public static final String S_VOCABULARY_FILE          = S_OUT_DIR + "skip_bnc.voc";
//    public static final String S_MODEL_FILE               = S_OUT_DIR + "skip_bnc40.mdl";
    
//    public static final String S_VECTOR_FILE              = S_OUT_DIR + "cbow_bnc300.bin";
//    public static final String S_COMPOSITION_FILE         = S_OUT_DIR + "cbow_bnc300.cmp";
//    public static final String S_VOCABULARY_FILE          = S_OUT_DIR + "cbow_bnc300.voc";
//    public static final String S_MODEL_FILE               = S_OUT_DIR + "cbow_bnc300.mdl";
    
    
    public static final String S_RTE_DIR                  = S_PROJECT_DIR + "rte/";
    public static final String S_RTE_FILE                 = S_RTE_DIR + "";
  
    public static final String S_LOG_DIR                  = S_PROJECT_DIR + "log/";
//  public static final String S_LOG_FILE                 = S_LOG_DIR + "wiki_sentence.log";
    public static final String S_LOG_FILE                 = S_LOG_DIR + "bnc_sentence.log";
  
//  public static final String S_LOG_DIR
    public static final String S_MEN_FILE                 = S_PROJECT_DIR + "men/MEN_dataset_lemma.txt";
//  public static final String S_MEN_FILE                 = S_PROJECT_DIR + "men/MEN_dataset_lemma_form_full";
  
    public static final String S_SICK_FILE                = S_PROJECT_DIR + "sick/postprocessed/SICK_train_trial.txt";
    public static final String S_DATASET_DIR              = "/home/thenghiapham/work/dataset/lapata/";
    public static final String S_AN_FILE                  = S_DATASET_DIR + "an_lemma.txt";
    public static final String S_NN_FILE                  = S_DATASET_DIR + "nn_lemma.txt";
    public static final String S_VO_FILE                  = S_DATASET_DIR + "vo_lemma.txt";
    public static final String S_SV_FILE                  = S_DATASET_DIR + "sv_lemma.txt";
    
    public static final String S_VALIDATION_FILE          = S_PROJECT_DIR + "tmp_parsed/test_bnc.txt";
    
    
    
    
//    public static final String S_PROJECT_DIR              = "/mnt/cimec-storage-sata/users/thenghia.pham/data/project/mikcom/";
//    public static final String S_CONSTRUCTION_FILE        = S_PROJECT_DIR + "constructions/selected-constructions1.txt";
//    public static final String S_TRAIN_DIR                = S_PROJECT_DIR + "corpus/split/bnc";
////    public static final String S_TRAIN_DIR                = S_PROJECT_DIR + "corpus/split/wiki";
//    public static final String S_TRAIN_FILE               = S_PROJECT_DIR + "corpus/bnc.txt";
////    public static final String S_TRAIN_FILE               = S_PROJECT_DIR + "corpus/wiki.parse";
//    public static final String S_OUT_DIR                  = S_PROJECT_DIR + "output/";
//    public static final String S_VECTOR_FILE              = S_OUT_DIR + "bnc.bin";
//    public static final String S_COMPOSITION_FILE         = S_OUT_DIR + "bnc.cmp";
//    public static final String S_VOCABULARY_FILE          = S_OUT_DIR + "bnc.voc";
//    public static final String S_INITIALIZATION_FILE      = S_OUT_DIR + "bnc.ini";
//    
////    public static final String S_VECTOR_FILE              = S_OUT_DIR + "wiki.bin";
////    public static final String S_COMPOSITION_FILE         = S_OUT_DIR + "wiki.cmp";
////    public static final String S_VOCABULARY_FILE          = S_OUT_DIR + "wiki.voc";
////    public static final String S_INITIALIZATION_FILE      = S_OUT_DIR + "wiki.ini";
//    
////    public static final String S_VECTOR_FILE              = S_OUT_DIR + "skip_wiki300_ss.bin";
////    public static final String S_COMPOSITION_FILE         = S_OUT_DIR + "skip_wiki300_ss.cmp";
////    public static final String S_VOCABULARY_FILE          = S_OUT_DIR + "skip_wiki300_ss.voc";
////    public static final String S_INITIALIZATION_FILE      = S_OUT_DIR + "skip_wiki300_ss.ini";
//    
//    public static final String S_LOG_DIR                  = S_PROJECT_DIR  + "log/";
//    public static final String S_LOG_FILE                 = S_LOG_DIR + "bnc.log";
////    public static final String S_LOG_FILE                 = S_LOG_DIR + "wiki.log";
//    
//    public static final int S_MIN_FREQUENCY               = 5;
////    public static final int S_MIN_FREQUENCY               = 100;
//  
//    public static final String S_MEN_FILE                 = S_PROJECT_DIR + "men/MEN_dataset_lemma.txt";
//    public static final String S_AN_FILE                  = S_PROJECT_DIR + "lapata/an_lemma.txt";
//    public static final String S_NN_FILE                  = S_PROJECT_DIR + "lapata/nn_lemma.txt";
//    public static final String S_VO_FILE                  = S_PROJECT_DIR + "lapata/vo_lemma.txt";
//    public static final String S_SV_FILE                  = S_PROJECT_DIR + "lapata/sv_lemma.txt";
//    public static final String S_SICK_FILE                = S_PROJECT_DIR + "sick/parsed/SICK_train_trial.txt";
    
}


