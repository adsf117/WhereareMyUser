package com.andres.wherearemyuser;

import java.util.Random;

/**
 * Created by andresdavid on 16/09/16.
 */
public class Utils {

    int index=0;
    int lastindex=0;


    public  int generateRamdomNumber( int arraySize){
        Random randomGenerator = new Random();
        while (index == lastindex)
        {
            index= randomGenerator.nextInt(arraySize);
        }
        lastindex=index;
        return index;
    }
}
