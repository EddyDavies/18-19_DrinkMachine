package ac.liv.csc.comp201.control;

import ac.liv.csc.comp201.model.IMachine;

public class controlBalance{

	private IMachine machine;
        private double formatedBalance;

	public controlBalance(IMachine machine){
		this.machine = machine;
	}
        
        public void payDrink(int cost){
            machine.setBalance(machine.getBalance() - cost);

        }
        
	public void returnChangeBalance(){
            
        }
	
	public int getBalance() {
            return machine.getBalance();
	}
	
        
        public String runBalance(String coinCode){
            //coinCodes[]={"ab", "ac","ba","bc", "bd", "ef" };
            //coinNames[]={"1p","5p","10p","20p","50p","100p" };
            /*switch(coinCode){
                case "ab":
                    machine.setBalance(machine.getBalance() + 1);
                    break;
                case "ac":
                    machine.setBalance(machine.getBalance() + 5);                
                    break;
                case "ba":
                    machine.setBalance(machine.getBalance() + 10);
                    break;
                case "bc":
                    machine.setBalance(machine.getBalance() + 20);
                    break;
                case "bd":
                    machine.setBalance(machine.getBalance() + 50);
                    break;
                case "ef":
                    machine.setBalance(machine.getBalance() + 100);
                    break;
                case null:
                    break;
            }
            */
            //return "1.00";
            return convertToMoneyDisplay();
        }
        
        
        private String convertToMoneyDisplay() { 
            int cashAmount = machine.getBalance();
            
            String returnString =""+cashAmount/100+"."; // first compute the pounds (whole units) 

            int penceAmount=cashAmount % 100; // get the pence amount 

            if (penceAmount<10) {
                returnString=returnString+"0"; // leading zero to allow 2dp display
            } 

        return(returnString+penceAmount); } 
}