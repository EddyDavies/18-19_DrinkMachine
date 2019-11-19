package ac.liv.csc.comp201;

import ac.liv.csc.comp201.model.IMachine;
import ac.liv.csc.comp201.model.IMachineController;
import ac.liv.csc.comp201.simulate.Cup;
import ac.liv.csc.comp201.simulate.Hoppers;
import ac.liv.csc.comp201.control.*;

public class MachineController  extends Thread implements IMachineController {
	
    private boolean running=true;

    private IMachine machine;
    private controlBalance bal;
    private controlDrink drink;
    private controlWaterHeater heater;
    private controlRequirements req;


    private static final String version="1.22";
    private static final int IDLE=1;   // waiting for customer to enter valid drink code
    private static final int PREHEATING=2;  // heating temperature to make drink
    private static final int HEATED=3;  // temperature to make drink reached and maintained
    private static final int MAX_CODE = 4;
    private static final int MIN_CODE= 3;
    public static final int COFFEE=0;
    public static final int MILK=1;
    public static final int SUGAR=2;
    public static final int CHOCOLATE=3;
    private static final int CHANGE_CODE = 9;
    private static final double SMALL = 0.34;
    private static final double MEDIUM = 0.45;
    private static final double LARGE = 0.56;
    
    private String balance;
    
    private int orderCode[] = new int[MAX_CODE];
    private int keyCodeCount = 0;
    private int codeLen;
    private boolean drinkMade;


    public void startController(IMachine machine) {
            this.machine=machine;

            drink = new controlDrink(machine, heater);
            heater = new controlWaterHeater(machine, IDLE, drink);
            req = new controlRequirements(machine);
            bal = new controlBalance(machine);
            

            machine.getKeyPad().setCaption(7,"");
            machine.getKeyPad().setCaption(8,"");
            machine.getKeyPad().setCaption(9,"Return Change");

            super.start();
    }


    public MachineController() {

    }


    private synchronized void runStateMachine() {

        String coinCodes = machine.getCoinHandler().getCoinKeyCode();
     
        heater.runHeater();
        balance = bal.runBalance(coinCodes);
        drink.setDrinkMade();
        
        
        int keyCode=machine.getKeyPad().getNextKeyCode();
        
        
        if (keyCode >= 0 && keyCode <= 9) {
        System.out.print("\nCode " + keyCode);
        if (keyCode == CHANGE_CODE) {
            bal.returnChangeBalance();
        } else {
            codeLen = req.setSize(orderCode, codeLen);

            if (keyCodeCount < codeLen) {  // only store up maximum code length
                orderCode[keyCodeCount++] = keyCode;
            }
        }

        if (keyCodeCount >= codeLen) {  // we have got a key code of target length
            System.out.print("\nCode is ");
            for (int idx = 0; idx < codeLen; idx++) {
                System.out.print(orderCode[idx]);
            }

            if (req.correctCode(orderCode, codeLen) && req.correctMoney() && req.correctIngredients()){
                bal.payDrink(req.getCost());
                heater.changeHeaterMode(PREHEATING);
                drink.setSize(req.getSize());
                drink.setWeights(req.getWeights());
                drink.setTemp(req.getTemp());
                drink.selectDrink(orderCode, codeLen);
                req.resetWeights();
            }
            keyCodeCount = 0;		// used up this code
        }
        }

        if(heater.getMode()== HEATED && !drinkMade) {
            drinkMade = drink.makeDrink();
        }
        if (drinkMade){
            machine.getDisplay().setTextString("Machine Off");
            System.out.println("Machine Off");

            heater.changeHeaterMode(IDLE);
        }
        
        
        machine.getDisplay().setTextString("Balance: " + balance);

    }
        

	
    public void run() {
            // Controlling thread for coffee machine
            int counter=1;
            while (running) {
                    //machine.getDisplay().setTextString("Running drink machine controller "+counter);
                    counter++;
                    try {
                            Thread.sleep(10);		// Set this delay time to lower rate if you want to increase the rate
                    } catch (InterruptedException e) {
                            e.printStackTrace();
                    }
                    runStateMachine();
            }		
    }

    public void updateController() {
            //runStateMachine();
    }

    public void stopController() {
            running=false;
    }
}
