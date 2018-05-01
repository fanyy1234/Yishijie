package com.sunday.common.update;

/**
 * 事件的监听回调
 * Created by admin
 */
public interface UpdateDownloadListener {

    /**
     * 下载请求开始回调
     */
    public void onStarted();

    /**
     * 进度更新回调
     *
     * @param progress
     * @param downloadUrl
     */
    public void onProgressChanged(int progress, String downloadUrl);

    /**
     * 下载完成回调
     *
     * @param completeSize
     * @param downloadUrl
     */
    public void onFinished(int completeSize, String downloadUrl);

    /**
     * 下载失败回调
     */
    public void onFailure();
}
