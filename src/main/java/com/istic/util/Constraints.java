package com.istic.util;

public class Constraints {

    public static final Double VOLT = 5D;


    public static final Double MODULATION_VOLT = 10D;


    public static final Double FREQUENCE_MAX = 21000D;


    /**
     *
     * @param value frequence à vérifier
     * @return frequence vérifiée
     */
    public static double verifFrequenceMax(double value) {
        if(value > FREQUENCE_MAX) {
            return FREQUENCE_MAX;
        }
        return value;
    }


    /**
     * Verifie que l'amplitude respecte les seuils
     * Amplitude des signaux audio : −5 V à + 5V
     * @param value amplitude
     * @return la valeur initiale ou du seuil
     */
    public static double verifAmp(double value) {
        if(value > VOLT){
            return VOLT;
        }else if(value < -VOLT){
            return -VOLT;
        }
        return value;
    }

    public static double verifModFreq(double value) {
      return verifAmp(value);
    }

    /**
     * Verifie que l'amplitude respecte les seuils
     * Les signaux de modulation peuvent dépasser cette amplitude (de −10 V à +10 V)
     * @param value amplitude
     * @return la valeur initiale ou du seuil
     */
    public static double verifModAmp(double value) {
        if(value > MODULATION_VOLT){
            return MODULATION_VOLT;
        }else if(value < -MODULATION_VOLT){
            return -MODULATION_VOLT;
        }
        return value;
    }


}