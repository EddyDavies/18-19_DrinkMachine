package ac.liv.csc.comp201.control;

import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.control.*;

public class controlDrink {

    private IMachine machine;
    private controlWaterHeater heater;
        
    private static final int IDLE=1;   // waiting for customer to enter valid drink code
    private static final int PREHEATING=2;  // heating temperature to make drink
    private static final int HEATED=3;  // temperature to make drink reached and maintained
    public static final int COFFEE=0;
    public static final int MILK=1;
    public static final int SUGAR=2;
    public static final int CHOCOLATE=3;
    public static final int SMALL_CUP=1;
    public static final int MEDIUM_CUP=2;
    public static final int LARGE_CUP=3;
        
    private boolean drinkMade;
    private boolean milk;
    private boolean sugar;
    private double size;
    private double targetTemp;
    private double[] weights;
    private boolean coffeeDone;
    private boolean sugarDone;
    private boolean milkDone;
    private boolean chocolateDone;

    public controlDrink(IMachine machine, controlWaterHeater heater) {
        this.machine = machine;
        this.heater = heater;
    }
    
    public void setSize(double size){
        this.size = size;
    }   
    
    public void setWeights(double[] weights){
        this.weights = weights;
    }
    
    public void setTemp (double targetTemp){
        this.targetTemp = targetTemp;
    }
    
    public void selectDrink(int orderCode[], int codeLen){
        drinkMade = false;
        milk = false;
        sugar = false;

        if(orderCode[codeLen-3] == 1 && orderCode[codeLen-1] == 1) {
            sugar = false;
            milk = false;

        } else if (orderCode[codeLen-3] == 1 && orderCode[codeLen-1] == 2) {
            sugar = true;
            milk = false;

        } else if (orderCode[codeLen-3] == 2 && orderCode[codeLen-1] == 1) {
            sugar = false;
            milk = true;

        } else if (orderCode[codeLen-3] == 2 && orderCode[codeLen-1] == 2) {
            sugar = true;
            milk = true;

        } else if (orderCode[codeLen-3] == 3) {                
        }
    }
       
    
    public boolean makeDrink(){
        machine.getDisplay().setTextString("Making Drink");

        if (machine.getCup()!=null) {
        if (machine.getCup().getWaterLevelLitres()==0) { // got new cup, can start making drink now             
            machine.getWaterHeater().setHotWaterTap(true);
            coffeeDone = false;
            sugarDone = false;
            milkDone = false;
            chocolateDone = false;
        }
        
        if (machine.getCup().getWaterLevelLitres()>=0) {
        if(machine.getCup().getWaterLevelLitres() <= size){
            
            if (machine.getCup().getTemperatureInC() <= targetTemp){
                machine.getWaterHeater().setHotWaterTap(true);
                machine.getWaterHeater().setColdWaterTap(false);
            
            } else if(machine.getCup().getTemperatureInC() >= targetTemp){
                machine.getWaterHeater().setHotWaterTap(false);
                machine.getWaterHeater().setColdWaterTap(true);
            }
            
        } else {

            machine.getWaterHeater().setHotWaterTap(false);
            machine.getWaterHeater().setColdWaterTap(false);
        }

        if(machine.getCup().getCoffeeGrams() <= weights[COFFEE]){
            machine.getHoppers().setHopperOn(COFFEE);
        } else {
            machine.getHoppers().setHopperOff(COFFEE);
            coffeeDone = true;
        }

        if(machine.getCup().getSugarGrams() <= weights[SUGAR]){
            machine.getHoppers().setHopperOn(SUGAR);
        } else {
            machine.getHoppers().setHopperOff(SUGAR);
            sugarDone = true;
            
        }

        if(machine.getCup().getMilkGrams() <= weights[MILK]){
            machine.getHoppers().setHopperOn(MILK);
        } else {
            machine.getHoppers().setHopperOff(MILK);
            milkDone = true;
        }

        if(machine.getCup().getChocolateGrams() <= weights[CHOCOLATE]){
            machine.getHoppers().setHopperOn(CHOCOLATE);
        } else {
            machine.getHoppers().setHopperOff(CHOCOLATE);
            chocolateDone = true;            
            
        }
        }

        
        } else {
            if(size == 0.34){
                machine.vendCup(machine.getCup().SMALL_CUP);
            } else if (size == 0.45) {
                machine.vendCup(machine.getCup().MEDIUM_CUP);
            } else if(size == 0.56) {
                machine.vendCup(machine.getCup().LARGE_CUP);
                
            }
        }
        
        
        
        return drinkMade;
    }
    
    public void setDrinkMade(){
        if(coffeeDone && sugarDone && milkDone && chocolateDone){
            drinkMade=true;

        } else {
            drinkMade=false;
        }
            
    }
    
    public boolean getDrinkMade(){
        return drinkMade;
    }
}