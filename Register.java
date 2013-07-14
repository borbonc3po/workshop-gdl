public class Register
{
  private String RFC = null;
  private String Bank_Code = null;
  private String Name_Client = null;
  private String Address = null;
  private float Loan_Amount = (float) 0.0;
  private String Date_Hired = null;
  private String Qualification = null;
  private String Active = null;
  
  public Register(String RFC, String Bank_Code, String Name_Client, String Address, float Loan_Amount, String Date_Hired, String Qualification, String Active)
  {
    this.RFC = RFC;
    this.Bank_Code = Bank_Code;
    this.Name_Client = Name_Client;
    this.Address = Address;
    this.Loan_Amount = Loan_Amount;
    this.Date_Hired = Date_Hired;
    this.Qualification = Qualification;
    this.Active = Active;
  }
  
  public Register(String RFC, String Date_Hired)
  {
    this.RFC = RFC;
    this.Date_Hired = Date_Hired;
  }
  
  public Register(String RFC)
  {
    this.RFC = RFC;
  }
  
  public static String packUpMessage(Register object, int task)
  {
    switch(task)
    {
      case 1:
	return "1#" + object.RFC;
      
      case 2:
	return "2#" + object.RFC + "|" + object.Bank_Code + "|" + object.Name_Client + "|" + object.Address + "|" + object.Loan_Amount + "|" + object.Date_Hired + "|" + object.Qualification + "|" + object.Active;
      
      case 3:
	return "3#" + object.RFC + "|" + object.Date_Hired;
      
      default:
	System.out.println("Invalid operation");
	return "ERROR";
    }
  }
  
}

//