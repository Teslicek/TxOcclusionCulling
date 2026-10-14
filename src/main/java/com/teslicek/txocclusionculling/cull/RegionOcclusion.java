package com.teslicek.txocclusionculling.cull;

public interface RegionOcclusion {

    long[] txocclusionculling$getHiddenSections();

    long[] txocclusionculling$getTestedSections();

    int txocclusionculling$getTestedGeneration();

    void txocclusionculling$setTestedGeneration(int generation);
}
