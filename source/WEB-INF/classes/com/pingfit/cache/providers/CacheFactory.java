package com.pingfit.cache.providers;

import com.pingfit.cache.providers.ehcache.EhcacheProvider;
import com.pingfit.cache.providers.jboss.JbossTreeCacheAOPProvider;
import com.pingfit.cache.providers.oscache.OsCacheProvider;
import com.pingfit.cache.providers.oscache.OsCacheClusteredProvider;

/**
 * Factory class to get a cache provider
 */
public class CacheFactory {

    public static CacheProvider getCacheProvider(){
        return getCacheProvider("EhcacheProvider");
    }

    public static CacheProvider getCacheProvider(String providername){
        if (providername.equals("EhcacheProvider")){
            return new EhcacheProvider();
        } else if (providername.equals("JbossTreeCacheAOPProvider")){
            return new JbossTreeCacheAOPProvider();
        } else if (providername.equals("OsCacheProvider")){
            return new OsCacheProvider();
        } else if (providername.equals("OsCacheClusteredProvider")){
            return new OsCacheClusteredProvider();
        } else {
            return getCacheProvider();
        }
    }

}
