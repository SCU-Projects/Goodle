package com.app.katchup.Sharding;

import com.app.katchup.Sharding.model.Database;

public class Utilities {

    public static Database getShardedDBLocation(String input){
        Integer hash = 7;
        for (Integer i = 0; i < input.length(); i++) {
            hash = hash*31 + input.charAt(i);
            hash = hash % 3;
        }
        return getDbFromHash(hash%3);
    }

    private static Database getDbFromHash(int hash){
        switch (hash){
            case 0: return Database.DB1;
            case 1: return Database.DB2;
            case 2: return Database.DB3;
            default: return getDbFromHash(hash%3);
        }
    }

}
