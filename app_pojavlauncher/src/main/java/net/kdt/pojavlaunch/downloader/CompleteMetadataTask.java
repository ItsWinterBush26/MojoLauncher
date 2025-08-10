package net.kdt.pojavlaunch.downloader;

import android.util.Log;

import net.kdt.pojavlaunch.mirrors.DownloadMirror;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class CompleteMetadataTask extends DownloaderTask {
    CompleteMetadataTask(TaskMetadata mMetadata, Downloader mHostDownloader) {
        super(mMetadata, mHostDownloader);
    }

    @Override
    protected void performTask() throws IOException {
        if(mMetadata.sha1Hash == null && mMetadata.mirrorType == DownloadMirror.DOWNLOAD_CLASS_LIBRARIES) {
            try {
                mMetadata.sha1Hash = mDownloader.downloadString(new URL(mMetadata.url + ".sha1"));
            }catch (FileNotFoundException e) {
                Log.i("CompleteMetadataTask", "No server hash for file "+mMetadata.path.getName());
            }
        }
        if(mMetadata.size == -1) {
            mMetadata.size = mDownloader.getFileContentLength(mMetadata.url);
            Log.i("CompleteMetadataTask", "Got size: "+mMetadata.size +" for "+mMetadata.path.getName());
        }
        if(mMetadata.size == -1) {
            mDownloader.disableSizeCounter();
        }
        mDownloader.fileComplete();
    }

    protected static boolean shouldCompleteMetadata(TaskMetadata metadata) {
        return (metadata.sha1Hash == null && metadata.mirrorType == DownloadMirror.DOWNLOAD_CLASS_LIBRARIES) || metadata.size == -1;
    }
}
