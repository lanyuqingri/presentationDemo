package com.rokid.glass.libbase.faceid.wrapper;

import com.rokid.glass.pcassistant.bean.FeatInfo;

import java.util.List;

/**
 * Author: Zack
 * Email:  newzzack@gmail.com
 * Date:   2020.02.24 11:28
 */
public class FaceMappingWrapper {

    private String mCoverId;
    private List<FeatInfo> mFeatInfos;

    public String getCoverId() {
        return mCoverId;
    }

    public void setCoverId(String coverId) {
        mCoverId = coverId;
    }

    public List<FeatInfo> getFeatInfos() {
        return mFeatInfos;
    }

    public void setFeatInfos(List<FeatInfo> featInfos) {
        mFeatInfos = featInfos;
    }
}
