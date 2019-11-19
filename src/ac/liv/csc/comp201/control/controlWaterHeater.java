package ac.liv.csc.comp201.control;

import ac.liv.csc.comp201.model.IMachine;

public class controlWaterHeater {

	private static final int IDLE=1;   // waiting for customer to enter valid drink code
	private static final int PREHEATING=2;  // heating temperature to make drink
	private static final int HEATED=3;  // temperature to make drink reached and maintained

	private IMachine machine;
	private int mode;
	private controlDrink drink;
	
	public controlWaterHeater(IMachine machine, int mode, controlDrink drink) {
		this.machine=machine;
		this.mode=mode;
		this.drink=drink;
	}
	
	public void changeHeaterMode(int mode) {
		this.mode=mode;
	}	
        
		
	public void runHeater() {

		switch (mode){
			case IDLE:
				if (machine.getWaterHeater().getTemperatureDegreesC()>=80)    {
					machine.getWaterHeater().setHeaterOff(); 	// turn water heater off
				}
				else {
                                    machine.getWaterHeater().setHeaterOn();		//turn water heater on
				}
                                break;
			case PREHEATING:
				if (machine.getWaterHeater().getTemperatureDegreesC()>=95.9)    {
                                    machine.getWaterHeater().setHeaterOff(); 	// turn water heater off
                                    mode = HEATED;
				}
				else {
                                    machine.getWaterHeater().setHeaterOn();		//turn water heater on
				}
                                break;
			case HEATED:
				if (machine.getWaterHeater().getTemperatureDegreesC()>=95.9)    {
                                    machine.getWaterHeater().setHeaterOff(); 	// turn water heater off
				}
				else if (drink.getDrinkMade()){
                                    mode=IDLE;		//turn water heater on
				}
				else {
                                    machine.getWaterHeater().setHeaterOn();		//turn water heater on
				}
                                break;
		}
	}
	
        public int getMode(){
            return mode;
        }
	
}


