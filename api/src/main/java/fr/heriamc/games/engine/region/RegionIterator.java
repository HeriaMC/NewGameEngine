package fr.heriamc.games.engine.region;

import lombok.AllArgsConstructor;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
class RegionIterator implements Iterator<Block> {

    private final Region region;
    private final AtomicInteger index;

    public RegionIterator(Region region) {
        this.region = region;
        this.index = new AtomicInteger(0);
    }

    @Override
    public boolean hasNext() {
        return index.get() < region.getVolume();
    }

    @Override
    public Block next() {
        int cursor = index.getAndIncrement();
        int x = (int) (cursor % region.getWidth() + region.getMinX());
        int y = (int) ((double) cursor / region.getLength() % region.getHeight() + region.getMinY());
        int z = (int) ((double) cursor / region.getWidth() * region.getHeight() % region.getLength() + region.getMinZ());

        return region.getWorld().getBlockAt(x, y, z);
    }

}