package demo;

public class TestConstants {

    // public static final String TRAIN_FILE = "/home/pham/svn/word2vec/text8";
    // public static final String VECTOR_FILE =
    // "/home/pham/svn/word2vec/out.bin";
    // public static final String VOCABULARY_FILE =
    // "/home/pham/svn/word2vec/out.voc";
    // public static final String INITIALIZATION_FILE =
    // "/home/pham/svn/word2vec/out.ini";

    // public static final String GZIP_TRAIN_FILES =
    // "/home/pham/svn/word2vec/bnc.utf8.xml.gz;"
    // + "/home/pham/svn/word2vec/wikipedia_sample.xml.gz";
    // public static final String GZIP_VECTOR_FILE =
    // "/home/pham/svn/word2vec/malt.bin";
    // public static final String GZIP_VOCABULARY_FILE =
    // "/home/pham/svn/word2vec/malt.voc";
    // public static final String GZIP_INITIALIZATION_FILE =
    // "/home/pham/svn/word2vec/malt.ini";

    
    public static final String TRAIN_FILE               = "/home/angeliki/Documents/mikolov_composition/corpora_simple/enwik8";
    //public static final String VISION_FILE              = "//home/angeliki/masterclic4/visLang/caption_generation/spaces/train_dataset.Nouns/matrix.simple.txt.aggr";
    public static final String VISION_FILE              = "/home/angeliki/masterclic4/visLang/fast-mapping/feature_extraction/hists/pmisvd/pmisvd.aggr.dm";
    //public static final String VISION_FILE              = "/home/angeliki/masterclic4/visLang/fast-mapping/feature_extraction/cnn/dim_300_pmi/pmisvd.aggr.dm";
    public static final String VECTOR_FILE              = "/home/angeliki/Documents/mikolov_composition/out/multimodal/out_enwik8.bin";
    public static final String VOCABULARY_FILE          = "/home/angeliki/Documents/mikolov_composition/out/multimodal/out_enwik8.voc";
    public static final String INITIALIZATION_FILE      = "/home/angeliki/Documents/mikolov_composition/out/multimodal/out_enwik8.ini";
    //public static final String LOG_FILE               = "/home/angeliki/workspace/w2vcomp/w2vcomp/logs/multi_enwiki8_5_rand2.log";
    public static final String LOG_FILE               = "/home/angeliki/workspace/w2vcomp/w2vcomp/logs/men-en-wiki8_5rand.log";

    public static final String LOG_DIR               = "/home/angeliki/workspace/w2vcomp/w2vcomp/logs";

    // public static final String GZIP_TRAIN_FILES =
    // "/home/thenghiapham/svn/word2vec/bnc.utf8.xml.gz";
    // // public static final String GZIP_TRAIN_FILE =
    // "/home/thenghiapham/svn/word2vec/wikipedia_sample.xml.gz";
    // public static final String GZIP_VECTOR_FILE =
    // "/home/thenghiapham/svn/word2vec/malt.bin";
    // public static final String GZIP_VOCABULARY_FILE =
    // "/home/thenghiapham/svn/word2vec/malt.voc";
    // public static final String GZIP_INITIALIZATION_FILE =
    // "/home/thenghiapham/svn/word2vec/malt.ini";

    // on the cluster
    public static final String PATH                     = "/mnt/cimec-storage-sata/users/marco.baroni/share/ukwac-maltparsing/data/";
    public static final String GZIP_TRAIN_FILES         = PATH
                                                                + "bnc.xml.gz;"
                                                                + PATH
                                                                + "ukwac1.xml.gz;"
                                                                + PATH
                                                                + "ukwac2.xml.gz;"
                                                                + PATH
                                                                + "ukwac3.xml.gz;"
                                                                + PATH
                                                                + "ukwac4.xml.gz;"
                                                                + PATH
                                                                + "ukwac5.xml.gz;"
                                                                + PATH
                                                                + "wikipedia-1.xml.gz;"
                                                                + PATH
                                                                + "wikipedia-2.xml.gz;"
                                                                + PATH
                                                                + "wikipedia-3.xml.gz;"
                                                                + PATH
                                                                + "wikipedia-4.xml.gz";
    // public static final String OUT_PATH =
    // "/mnt/cimec-storage-sata/users/thenghia.pham/data/project/mikolov/output/";
    public static final String OUT_PATH                 = "/home/pham/work/project/mikolov/output/";
    public static final String GZIP_VECTOR_FILE         = OUT_PATH
                                                                + "testVector.bin";
    public static final String GZIP_VOCABULARY_FILE     = OUT_PATH
                                                                + "testVector.voc";
    public static final String GZIP_INITIALIZATION_FILE = OUT_PATH
                                                                + "testVector.ini";
    
    
    public static final String CCG_MEN_FILE             = "/home/angeliki/Documents/mikolov_composition/misc/MEN_dataset_lemma_nopos_form_full";
    public static final String CCG_AN_FILE              = "/home/thenghiapham/work/project/mikolov/an_ml/an_ml_lemmapos.txt";
    
    public static final String CCG_TRAIN_FILE           = "/home/thenghiapham/work/project/mikolov/ccg_text/wikiA.txt";
    public static final String CCG_VECTOR_FILE          = "/home/thenghiapham/work/project/mikolov/output/phrase1.bin";
    public static final String CCG_VOCABULARY_FILE      = "/home/thenghiapham/work/project/mikolov/output/phrase1.voc";
    public static final String CCG_INITIALIZATION_FILE  = "/home/thenghiapham/work/project/mikolov/output/phrase1.ini";
    public static final String CCG_MATRIX_FILE          = "/home/thenghiapham/work/project/mikolov/output/phrase1.comp.mat";
}
