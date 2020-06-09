// IPlayService.aidl
package com.example.part7_20a_aidl;

interface IPlayService {
    int currentPostion();
    int getMaxDuration();
        void start();
        void stop();
        int getMediaStatus();
}