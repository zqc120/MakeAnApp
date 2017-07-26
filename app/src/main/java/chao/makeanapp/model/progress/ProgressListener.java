package chao.makeanapp.model.progress;

public interface ProgressListener {

    void progress(long bytesRead, long contentLength, boolean done);

}
