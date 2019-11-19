
package ac.liv.csc.comp201.control;

import ac.liv.csc.comp201.control.*;
import ac.liv.csc.comp201.model.IMachine;

public class controlRequirements{
    
    
    private static final int CODE= 1;
    private static final int FUNDS= 2;
    private static final int STOCK= 3;
    public static final int COFFEE=0;
    public static final int MILK=1;
    public static final int SUGAR=2;
    public static final int CHOCOLATE=3;
    private static final int MAX_CODE = 4;
    private static final int MIN_CODE= 3;
    private static final int MEDIUM_PREFIX = 5;
    private static final int LARGE_PREFIX = 6;
    private static final double SMALL = 0.34;
    private static final double MEDIUM = 0.45;
    private static final double LARGE = 0.56;

    
    private int baseCost;
    private int sizeCost;
    private double coffeeWeight = 0;
    private double sugarWeight = 0;
    private double milkWeight = 0;
    private double chocolateWeight = 0;
    private double size;
    private double targetTemp;
    
    private IMachine machine;
    private controlBalance bal;
    
    public controlRequirements(IMachine machine) {
        this.machine = machine;
        
    }

    public void errorMessage(int type) {
        if(type == CODE) {
            machine.getDisplay().setTextString("Invalid Code");
        }
        if(type == FUNDS) {
            machine.getDisplay().setTextString("Insufficent Funds");
        }	
        if(type == STOCK) {
            machine.getDisplay().setTextString("Insufficent Stock"); // add extra specification of what stock is missing??
        }
        
        //error for machine stock at high heat
        
        //error for no ingredietns left

     }

    public int setSize(int orderCode[], int codeLen){
        if (orderCode[0] == LARGE_PREFIX) { // codes with prefix are a little longer
            codeLen = MAX_CODE;
            sizeCost = 20;
            size = LARGE;
        }
        else if (orderCode[0] == MEDIUM_PREFIX) {   // codes with prefix are a little longer
            codeLen = MAX_CODE;
            sizeCost = 25;
            size = MEDIUM;
        }
        else {
            codeLen = MIN_CODE;
            sizeCost = 0;
            size = SMALL;
        }
        return codeLen;
    }
    
    public double getSize(){
        return size;
    }
    
    public double getTemp(){
        return targetTemp;
    }
    
    public boolean correctCode(int orderCode[], int codeLen){
        targetTemp = 95.9;
        if(orderCode[codeLen-3] == 1 && orderCode[codeLen-2] == 0 && orderCode[codeLen-1] == 1) {
            baseCost = 120;
            coffeeWeight = (2/SMALL)*size;	
        }
        else if(orderCode[codeLen-3] == 1 && orderCode[codeLen-2] == 0 && orderCode[codeLen-1] == 2) {
            baseCost = 130;
            coffeeWeight = (2/SMALL)*size;
            sugarWeight = (5/SMALL)*size;
        }	
        else if(orderCode[codeLen-3] == 2 && orderCode[codeLen-2] == 0 && orderCode[codeLen-1] == 1) {
            baseCost = 120;
            coffeeWeight = (2/SMALL)*size;
            milkWeight = (3/SMALL)*size;
        }	
        else if(orderCode[codeLen-3] == 2 && orderCode[codeLen-2] == 0 && orderCode[codeLen-1] == 2) {
            baseCost = 130;
            coffeeWeight = (2/SMALL)*size;
            sugarWeight = (5/SMALL)*size;
            milkWeight = (3/SMALL)*size;
        }
        else if(orderCode[codeLen-3] == 3 && orderCode[codeLen-2] == 0 && orderCode[codeLen-1] == 0) {
            baseCost = 110;
            targetTemp = 90;
            sugarWeight = (28/SMALL)*size;
        }
        else {
            errorMessage(CODE);
            return false;
        }
        return true;
    }
    
    
    public boolean correctMoney(){
        if(machine.getBalance() >= baseCost + sizeCost) {
            return true;    
        }
        errorMessage(FUNDS);
        return false;
    }

    public boolean correctIngredients(){
        
        if( machine.getHoppers().getHopperLevelsGrams(COFFEE)>= coffeeWeight &&
            machine.getHoppers().getHopperLevelsGrams(MILK)>= milkWeight &&
            machine.getHoppers().getHopperLevelsGrams(SUGAR)>= sugarWeight &&
            machine.getHoppers().getHopperLevelsGrams(CHOCOLATE)>= chocolateWeight) {
            return true; 
        }
        errorMessage(STOCK);
        return false;
    }
    
    public double[] getWeights(){
        double weights[] = new double[4];
        
        weights[COFFEE] = coffeeWeight;
        weights[MILK] = milkWeight;
        weights[SUGAR] = sugarWeight;
        weights[CHOCOLATE] = chocolateWeight;
        
        return weights;
    }
    
    public void setSizeCost(int sizeCost){
        this.sizeCost = sizeCost;
    
    }
    
    public int getCost(){
        return sizeCost+baseCost;
    }
            
    public void resetWeights(){
        coffeeWeight = 0;
        sugarWeight = 0;
        milkWeight = 0;
        chocolateWeight = 0;
    }
            
}

// use lockCoinHandler()