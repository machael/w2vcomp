package parallel.workers.w2v;

import java.io.Serializable;

import parallel.workers.ModelParameters;

public class W2vParameters implements ModelParameters {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected long numLines;
    @Override
    public Long getValue() {
        // TODO Auto-generated method stub
        return numLines;
    }

    @Override
    public void setValue(Serializable value) {
        // TODO Auto-generated method stub
        numLines = (Long) value;
    }

}
