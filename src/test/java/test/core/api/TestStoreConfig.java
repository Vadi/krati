package test.core.api;

import java.io.File;
import java.io.IOException;

import test.util.FileUtils;
import junit.framework.TestCase;
import krati.core.StoreConfig;
import krati.core.StoreParams;

/**
 * TestStoreConfig
 * 
 * @author jwu
 * 06/23, 2011
 * 
 */
public class TestStoreConfig extends TestCase {
    
    protected int getInitialCapacity() {
        return 10000;
    }
    
    protected File getHomeDir() {
        return FileUtils.getTestDir(getClass().getSimpleName());
    }
    
    @Override
    protected void tearDown() {
        try {
           FileUtils.deleteDirectory(getHomeDir());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void testApiBasics() throws IOException {
        StoreConfig config = new StoreConfig(getHomeDir(), getInitialCapacity());
        
        assertEquals(StoreParams.INDEXES_CACHED_DEFAULT, config.isIndexesCached());
        assertEquals(StoreParams.INDEXES_CACHED_DEFAULT, config.getIndexesCached());
        
        assertEquals(StoreParams.BATCH_SIZE_DEFAULT, config.getBatchSize());
        assertEquals(StoreParams.NUM_SYNC_BATCHES_DEFAULT, config.getNumSyncBatches());
        
        assertEquals(StoreParams.SEGMENT_FILE_SIZE_MB_DEFAULT, config.getSegmentFileSizeMB());
        assertEquals(StoreParams.SEGMENT_COMPACT_FACTOR_DEFAULT, config.getSegmentCompactFactor());
        assertEquals(StoreParams.HASH_LOAD_FACTOR_DEFAULT, config.getHashLoadFactor());
        
        boolean indexesCached = false; 
        config.setIndexesCached(indexesCached);
        assertEquals(indexesCached, config.isIndexesCached());
        assertEquals(indexesCached, config.getIndexesCached());
        
        int batchSize = StoreParams.BATCH_SIZE_DEFAULT + 100;
        config.setBatchSize(batchSize);
        assertEquals(batchSize, config.getBatchSize());
        
        int numSyncBatches = StoreParams.NUM_SYNC_BATCHES_DEFAULT + 5;
        config.setNumSyncBatches(numSyncBatches);
        assertEquals(numSyncBatches, config.getNumSyncBatches());
        
        int segmentFileSizeMB = StoreParams.SEGMENT_FILE_SIZE_MB_DEFAULT - 10;
        config.setSegmentFileSizeMB(segmentFileSizeMB);
        assertEquals(segmentFileSizeMB, config.getSegmentFileSizeMB());
        
        double segmentCompactFactor = StoreParams.SEGMENT_COMPACT_FACTOR_DEFAULT + 0.07;
        config.setSegmentCompactFactor(segmentCompactFactor);
        assertEquals(segmentCompactFactor, config.getSegmentCompactFactor());
        
        double hashLoadFactor = StoreParams.HASH_LOAD_FACTOR_DEFAULT - 0.07;
        config.setHashLoadFactor(hashLoadFactor);
        assertEquals(hashLoadFactor, config.getHashLoadFactor());
        
        config.validate();
        config.store();
        
        StoreConfig config2 = new StoreConfig(getHomeDir(), getInitialCapacity());
        
        assertEquals(config.isIndexesCached(), config2.isIndexesCached());
        assertEquals(config.getIndexesCached(), config2.getIndexesCached());
        
        assertEquals(config.getBatchSize(), config2.getBatchSize());
        assertEquals(config.getNumSyncBatches(), config2.getNumSyncBatches());
        
        assertEquals(config.getSegmentFileSizeMB(), config.getSegmentFileSizeMB());
        assertEquals(config.getSegmentCompactFactor(), config.getSegmentCompactFactor());
        assertEquals(config.getHashLoadFactor(), config.getHashLoadFactor());
        
        File propertiesFile = new File(getHomeDir(), StoreConfig.CONFIG_PROPERTIES_FILE+".new");
        
        config.setSegmentFileSizeMB(StoreParams.SEGMENT_FILE_SIZE_MB_MIN);
        config.setNumSyncBatches(StoreParams.BATCH_SIZE_MIN);
        config.setBatchSize(StoreParams.BATCH_SIZE_MIN);
        config.store(propertiesFile, null);
        
        config2.load(propertiesFile);
        assertEquals(StoreParams.SEGMENT_FILE_SIZE_MB_MIN, config2.getSegmentFileSizeMB());
        assertEquals(StoreParams.NUM_SYNC_BATCHES_MIN, config2.getNumSyncBatches());
        assertEquals(StoreParams.BATCH_SIZE_MIN, config2.getBatchSize());
        
        config2.validate();
    }
}
