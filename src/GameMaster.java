import enums.Cars;
import enums.Houses;
import enums.Jobs;
import game_data.DataFields;
import game_data.DataFile;
import interfaces.EnumData;
import utils.GameUtils;
import utils.MenuManager;
import game_data.SaveManager;

import java.awt.*;
import java.util.Scanner;

// GameMaster is a singleton class and only one can exist at any given time. Access using GameMaster.getInstance()
// It is essentially a facade class for the console to have all the data needed to run the game in one place
public class GameMaster
{
    private static GameMaster instance;
    private DataFields gameData;
    private SaveManager save;
    private GameMaster()
    {
        runMainMenu();
    }
    public static GameMaster getInstance()
    {
        if(instance == null) instance = new GameMaster();
        return instance;
    }

    public void runMainMenu() // MAIN MENU
    {
        int input = MenuManager.mainMenu();
        switch (input)
        {
            case 1:
                this.save = new SaveManager(); // Play Option
                this.gameData = this.save.getDataFile().getData();
                runGameMenu();
                break;
            case 2:
                System.out.println("How to play?");
                break;
            case 3:
                System.out.println("View saves");
                String readSaveFile = new SaveManager().getSaveFileData();
                System.out.println("Your save: \n" + readSaveFile + "\n");
                break;
            case 4:
                System.exit(0);
                break;
            default: // Secret code entry, Easter egg #2
                if(input == 2534)
                {
                    System.out.println("Data management mode entered.");
                    runDataManagementMode(new SaveManager());
                }
        }
    }
    public void upgradeItem()
    { // TODO: Finish upgradeItem by adding the ability to buy items and update the save file
        Scanner input = new Scanner(System.in);
        int choice = MenuManager.upgradeShopMenu();
        boolean purchaseSuccessful = false;
        switch(choice)
        {
            case 1:
                System.out.println("What house would you like to buy?\n");
                Houses currentHouse = this.gameData.getHouse();
                int currentHouseOrd = currentHouse.getHouseNumber();
                currentHouse.listAll(currentHouseOrd);
                break;
            case 2:
                Cars currentCar = this.gameData.getCar(); // Car that is currently owned
                int upgradeChoice = currentCar.upgrade();
                purchaseSuccessful = makePurchase(currentCar, upgradeChoice);
                break;
            case 3:
                runGameMenu();
                break;
            default:
                System.out.println("Invalid input. Please try again.");
                upgradeItem();
        }
        System.out.println();
    }

    // type: The enum constant currently owned by user in the field being upgraded
    // upgradeChoice: The menu choice(ordinal) of the requested upgrade
    private boolean makePurchase(EnumData type, int upgradeChoice) // Helper method for upgradeItem returns whether the purchase was successful. upgradeChoice is the ordinal of the enum to upgrade to
    {
        int money = this.gameData.getMoney();
        EnumData upgrade = null;
        if(type instanceof Cars) {
            Cars currentCar = (Cars) type;
            upgrade = GameUtils.getCarAt(upgradeChoice); // TODO implement for all types
        }
        else if(type instanceof Houses) {

        }
        else if(type instanceof Jobs) {

        } else {

        }
        System.out.println("Want to upgrade to: " + upgrade + "?");
        int price = upgrade.getPrice(); // TODO implement actually purchasing all items
        if(price > money) {
            System.out.println("You do not have enough money to buy this item.");
        } else {
            this.gameData.setMoney(money - price);
            System.out.println("Purchase successful.");
            return true;
        }
        return false;
    }

    public void runDataManagementMode(SaveManager saveToChange) // When this method is called, GameMaster does not yet have a SaveManager, so one is passed as a parameter for temporary use
    {
        Scanner inputs = new Scanner(System.in);
        int dataToManipulate = MenuManager.dataManagementMenu(); // 1-> MONEY | 2-> HOUSE | 3-> CAR | 4-> JOB | 5-> BACK
        if(dataToManipulate == 5) // User wants to go back to main menu
        {
            runMainMenu();
            return;
        }
        System.out.print("Enter new value: ");
        String newValue = inputs.nextLine();

        switch (dataToManipulate)
        {
            case 1: // occurs when user wants to change money
                int newMoney = Integer.parseInt(newValue);
                saveToChange.getDataFile().updateDataField(newMoney);
                break;
            default: // default occurs when user wants to change a value that is represented by an enum
                saveToChange.getDataFile().updateDataField(newValue);
                break;
        }
        saveToChange.saveToFile();
        System.out.println("Changes saved");
        runDataManagementMode(saveToChange);
    }

    public void runGameMenu() // GAME MENU
    {
        int requestedMenu = MenuManager.gameMenu();
        if(requestedMenu == 1) { // HOME MENU 1: VIEW HOUSE 2: VIEW CAR 3: VIEW JOB 4: SAVE/EXIT 5: BACK
            runHome();
        } else if(requestedMenu == 2) { // CASINO MENU 1: PLAY BLACKJACK 2: PLAY ROULETTE 3: PLAY COIN-FLIPS 4: VIEW PRIZES 5: BACK
            runCasino();
        } else if(requestedMenu == 3) { // JOB MENU 1: WORK 2: VIEW JOB DESCRIPTION 3: ASK FOR A RAISE 4: BACK
            runJob();
        } else if(requestedMenu == 4) { // UPGRADE MENU: 1: HOUSE 2: CAR
            upgradeItem();
        }

    }
     public void runCasino()
     {
        int input = MenuManager.casinoMenu();
        System.out.println();
        switch(input)
        {
            case 1: // PLAY BLACKJACK
                System.out.println("Play Blackjack");
                break;
            case 2: // PLAY SLOTS
                System.out.println("Play Roulette");
                break;
            case 3: // PLAY ROULETTE
                System.out.println("Play Coin-flips");
                break;
            case 4: // VIEW PRIZES
                System.out.println("View prizes");
            case 5: // BACK
                System.out.println("Back");
                runGameMenu();
        }
     }
     public void runHome()
     {
         int input = MenuManager.houseMenu();
         System.out.println();
         switch(input) // This is the option of the respective menu chosen. E.G. 1 if user chose 1st option of "Home" menu
         {
             case 1: // VIEW HOUSE
                 System.out.println("Your house: " + this.save.getDataFile().getData().getHouse() + "\n");
                 break;
             case 2: // VIEW CAR
                 System.out.println("Your car: " + this.save.getDataFile().getData().getCar() + "\n");
                 break;
             case 3: // SAVE/EXIT
                 save.saveToFile();
                 boolean exitOrNot = yesNoInput("Game saved, would you like to exit?");

                 if (exitOrNot) System.exit(0);
                 break;
             case 4: // BACK
                 runGameMenu();

         }
            runHome();
     }

     /* JOB */
     public void runJob()
     {
        int input = MenuManager.jobMenu();
            System.out.println();
            switch(input)
            {
                case 1: // WORK
                    System.out.println("Work");
                    break;
                case 2: // VIEW JOB DESCRIPTION
                    System.out.println("Your job: " + this.save.getDataFile().getData().getJob() + "\n");
                    break;
                case 3: // ASK FOR A RAISE
                    System.out.println("Ask for a raise");
                    break;
                case 4: // BACK
                    System.out.println("Back");
                    runGameMenu();
            }
     }

    /* END JOB */

     /* HELPER METHODS */
     private boolean yesNoInput(String prompt) // return true if yes, false if no
     {
         Scanner sc = new Scanner(System.in);
         System.out.println(prompt + " (Y/N)");
         String input = sc.nextLine();

         if(input.toLowerCase().startsWith("y"))
         {
             return true;
         } else if(input.toLowerCase().startsWith("n"))
         {
             return false;
         } else {
             System.out.println("Invalid input, please try again");
             return yesNoInput(sc.nextLine());
         }
     }

     private SaveManager temporarySaveManagement()
     {
         return new SaveManager();
     }

}
