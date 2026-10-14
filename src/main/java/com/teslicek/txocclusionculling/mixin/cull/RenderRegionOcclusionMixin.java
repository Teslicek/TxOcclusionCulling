package com.teslicek.txocclusionculling.mixin.cull;

import com.teslicek.txocclusionculling.cull.RegionOcclusion;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(RenderRegion.class)
public abstract class RenderRegionOcclusionMixin implements RegionOcclusion {

    @Unique
    private final long[] txocclusionculling$hiddenSections = new long[RenderRegion.REGION_SIZE / Long.SIZE];

    @Unique
    private final long[] txocclusionculling$testedSections = new long[RenderRegion.REGION_SIZE / Long.SIZE];

    @Unique
    private int txocclusionculling$testedGeneration;

    @Override
    public long[] txocclusionculling$getHiddenSections() {
        return this.txocclusionculling$hiddenSections;
    }

    @Override
    public long[] txocclusionculling$getTestedSections() {
        return this.txocclusionculling$testedSections;
    }

    @Override
    public int txocclusionculling$getTestedGeneration() {
        return this.txocclusionculling$testedGeneration;
    }

    @Override
    public void txocclusionculling$setTestedGeneration(int generation) {
        this.txocclusionculling$testedGeneration = generation;
    }
}
