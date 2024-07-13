package hammurabi.docs.matuszek;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Hammurabi {
    Random rand = new Random();
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new Hammurabi().playGame();

    }

    void playGame() {
        //Declare Local Variables Here: Grain, Population, etc.
        //Statements go after the declaration

        int population = 100;
        int grain = 2800;
        int acresOfLand = 1000;
        int landValue = 19; //bushels/acre
        int year = 1;
        boolean gameOn = true;

        while (gameOn && year <= 10) {
            System.out.printf("\n0 Great Hammurabi!\n" +
                            "You are in year %d of your ten year rule.\n", year);

                //Display previous year's statistics
            if (year > 1) {
                System.out.printf("In the previous year %d people starved to death.\n", previousStarvationDeaths);
                System.out.printf("In the previous year %d people entered the kingdom.\n", previousImmigrants);
            }

            //Display current statistics
            System.out.printf("The population is now %d.\n", population);
            int harvest = harvest(askHowManyAcresToPlant(acresOfLand, population, grain));
            int ratsEaten = grainEatenByRats(grain);
            grain -= ratsEaten;

            System.out.printf("We harvested %d bushels at 3 bushels per acre.\n", harvest);
            System.out.printf("Rats destroyed %d bushels, leaving %d bushels in storage.\n", ratsEaten, grain);
            System.out.printf("The city owns %d acres of land.\n", acresOfLand);
            System.out.printf("Land is currently worth %d bushels per acre.\n", landValue);

            //Calculate and update game state
            int plagueDeaths = plagueDeaths(population);
            int bushelsToFeed = askHowMuchGrainToFeedPeople(grain);
            int starvationDeaths = starvationDeaths(population, bushelsToFeed);
            grain -= bushelsToFeed;

            int immigrants = immigrants(population, acresOfLand, grain);
            int acresToBuy = askHowManyAcresToBuy(landValue, grain);
            acresOfLand += acresToBuy;
            grain -= acresToBuy * landValue;

            int acresToSell = askHowManyAcresToSell(acresOfLand);
            acresOfLand -= acresToSell;
            grain += acresToSell * landValue;

            population += immigrants - plagueDeaths - starvationDeaths;

            //Check for game over condition
            boolean isUprising = uprising(population, starvationDeaths);
            if (isUprising) {
                printSummary(plagueDeaths, starvationDeaths, immigrants, harvest, ratsEaten, landValue);
                System.out.println("You have been overthrown! Game OVER MATE!!!");
                gameOn = false;
                break;
            }

            //Print new summary
            printSummary(plagueDeaths, starvationDeaths, immigrants, harvest, ratsEaten, landValue);

            //Update previous year statistics
            previousStarvationDeaths = starvationDeaths;
            previousImmigrants = immigrants;

            //Increment Year
            year++;
        }

        //Final Summary after 10 years
        finalSummary(population, acresOfLand);
        scanner.close();
    }

    public int grainEatenByRats(int grain) {
        // Simulate rats eating 10% to 30% of grain
        int percentEaten = rand.nextInt(21) + 10;
        return (int) Math.round(grain * (percentEaten / 100.0));
    }

    public int harvest(int acresToPlant) {
        // Simulate harvest yield: 1 to 6 bushels per acre
        return rand.nextInt(6) + 1;
    }

    public int immigrants(int population, int acresOfLand, int grain) {
        // Simulate immigrants based on available land and grain surplus
        int maxImmigrants = Math.min(100 - population, grain / 20);
        return rand.nextInt(maxImmigrants + 1);
    }

    public boolean uprising(int population, int starvationDeaths) {
        // Uprising occurs if more than 45 people starve
        return starvationDeaths > 45;
    }

    public int plagueDeaths(int population) {
        // Simulate plague deaths: 15% of population
        if (rand.nextDouble() < 0.15) {
            return (int) (population * 0.15);
        } else {
            return 0;
        }
    }

    public int starvationDeaths(int population, int bushelsToFeed) {
        // Simulate starvation deaths; 20 people die for every 20 bushels short
        int bushelsNeeded = population * 20;
        int shortfall = bushelsNeeded - bushelsToFeed;
        return (shortfall > 0) ? (int) Math.ceil((double) shortfall / 20) : 0;
    }

    private void printSummary(int plagueDeaths, int starvationDeaths, int immigrants, int harvest, int ratsEaten, int landValue) {
        System.out.printf("\nSummary of the year:\n" +
                        "Plague deaths: %d\n" +
                        "Starvation deaths: %d\n" +
                        "Immigrants: %d\n" +
                        "Harvest: %d\n" +
                        "Grain eaten by rats: %d\n" +
                        "Land value: %d bushels per acre\n",
                plagueDeaths, starvationDeaths, immigrants, harvest, ratsEaten, landValue);
    }

    private void finalSummary(int population, int acresOfLand) {
        System.out.printf("\nFinal Summary:\n" +
                "Population: %d\n" +
                "Acres of land: %d\n", population, acresOfLand);
    }

    private int askHowMuchGrainToFeedPeople(int bushels) {
        System.out.printf("How much grain do you want to feed the people (up to %d)? ", bushels);
        return getNumber(0, bushels);
    }

    private int askHowManyAcresToBuy(int price, int bushels) {
        System.out.printf("How many acres do you want to buy (up to %d)? ", bushels / price);
        return getNumber(0, bushels / price);
    }

    private int askHowManyAcresToSell(int acresOwned) {
        System.out.printf("How many acres do you want to sell (currently owning %d)? ", acresOwned);
        return getNumber(0, acresOwned);
    }

    private int askHowManyAcresToPlant(int acresOwned, int population, int bushels) {
        int maxAcresToPlant = Math.min(acresOwned, population * 10);
        int maxBushelsToPlant = bushels - (2 * maxAcresToPlant);
        System.out.printf("How many acres do you want to plant (up to %d)? ", maxAcresToPlant);
        return getNumber(0, maxAcresToPlant);
    }

    private int getNumber(int min, int max) {
        while (true) {
            try {
                int input = scanner.nextInt();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Invalid input. Please enter a number within the valid range.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Clear Invalid Input
            }
        }
    }

    // Variables to store previous year's statistics
    private int previousStarvationDeaths;
    private int previousImmigrants;

    //Method to simulate random land cost between 17 and 23 bushels per acre
    public int newCostOfLand() {
        return rand.nextInt(7) + 17; // returns a random number between 17 and 23
    }
}















//
//    askHowManyAcresToBuy(19, bushels);
//            askHowManyAcresToSell(acresOwned);
//            askHowMuchGrainToFeedPeople(acresOwned, Population, bushels);
//            askHowManyAcresToPlant(acresOwned, population, bushels);
//            plagueDeaths(population);
//            starvationDeaths(population, this.bushels);
//            uprising(population, askHowManyPeopleStarved:0);
//            immigrants(population, acresOwned, this.bushels);
//        }
//
//        scanner.close();
//    }
//
//    private int askHowManyGrainToFeedPeople(int grain) {
//    }
//
//    public int howManyAcresToBuy(int price, int bushels) {
//        System.out.println("HOW MANY ACRES DO YOU IWSH TO BUY? ");
//        int userInput = scanner.nextInt();
//        price = userInput * 19;
//        if (bushels >= price) {
//            this.bushels -= price;
//            this.acresOwned += userInput;
//        }
//
//
//        for (year = 0; year <= 10; year++) {
//
//            printYearlySummary(year, population, grain, acresOfLand, landValue);
//
//            int acresToBuy = askHowManyAcresToBuy(landValue, grain); // "HOW MANY ACRES DO YOU WISH TO BUY?"
//            acresOfLand += acresToBuy;
//            grain -= acresToBuy * landValue;
//
//            int acresToSell = askHowManyAcresToSell(acresOfLand); // "HOW MANY ACRES DO YOU WISH TO SELL?"
//            acresOfLand -= acresToSell;
//            grain += acresToSell * landValue;
//
//            int bushelsToFeed = askHowManyAcresToSell(acresOfLand); //"HOW MANY BUSHELS DO YOU WISH TO FEED YOUR PEOPLE?"
//            grain -= bushelsToFeed;
//
//            int acresToPlant = askHowManyAcresToPlant(acresOfLand, population, grain); //"HOW MANY ACRES DO YOU WISH TO PLANT WITH SEED?"
//            grain -= acresToPlant * 2;
//
//            int plagueDeaths = plagueDeaths(population);
//            int starvationDeaths = starvationDeaths(population, bushelsToFeed);
//            boolean isUprising = uprising(population, starvationDeaths);
//            int immigrants = immigrants(population, acresOfLand, grain);
//            int harvest = harvest(acresToPlant);
//            int ratsEaten = grainEatenByRats(grain);
//            landValue = newCostOfLand();
//
//            if (isUprising) {
//                printSummary(plagueDeaths, starvationDeaths, immigrants, harvest, ratsEaten, landValue);
//                System.out.println("You have been overthrown! Game over mate.");
//                break;
//            }
//
//            printSummary(plagueDeaths, starvationDeaths, immigrants, harvest, ratsEaten, landValue);
//        }
//        finalSummary(population, acresOfLand);
//    }
//
//    private void printYearlySummary (int year, int population, int grain, int acresOfLand, int landValue) {
//        System.out.println("0 great Hummarabi!");
//        System.out.printf("You are in year %d of your ten year rule.\n", year - 1);
//        System.out.printf("In the previous year %d people starved to death.\n", 0);
//        System.out.printf("In the previous year %d people entered the kingdon.\n", 5);
//        System.out.printf("The population is now %d.\n", population);
//        System.out.printf("We harvested %d bushels at %d bushels per acre.\n", 3000, 3);
//        System.out.printf("Rats destroyed %d bushels, leaving %d bushels in storage.\n", 200, grain);
//        System.out.printf("The city owns %d acores of land.\n", acresOfLand);
//        System.out.printf("Land is currently worth %d bushels per acres.\n", landValue);
//        System.out.println();
//    }
//
//
//    private int getNumber(String message, int min, int max) {
//        while (true) {
//            System.out.print(message);
//            try {
//                int input = scanner.nextInt();
//                if (input >= min && input <= max) {
//                    return input;
//                } else {
//                    System.out.println("Invalid input. Please enter a number within the valid range.");
//                }
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid input. Please enter a valid number.");
//                scanner.next(); // Clear invalid input
//            }
//        }
//    }
//
//    private int plagueDeaths(int population) {
//        return 0;
//    }
//
//    private int immigrants(int population, int acresOfLand, int grain) {
//        return 0;
//    }
//
//    private void finalSummary(int population, int acresOfLand) {
//    }
//
//    //Methods
//    private int newCostOfLand() {
//        return 0;
//    }
//
//    private int grainEatenByRats(int grain) {
//        return 0;
//    }
//
//    private void printSummary(int plagueDeaths, int starvationDeaths, int immigrants, int harvest, int ratsEaten, int landValue) {
//    }
//
//    private int harvest(int acresToPlant) {
//        return 0;
//    }
//
//    private boolean uprising(int population, int starvationDeaths) {
//        return false;
//    }
//
//    // Place holder method for starvationDeaths as listed above --> for game
//    private int starvationDeaths(int population, int bushelsToFeed) {
//        return 0;
//    }
//
//}

//
//public int starvationDeaths(int population, int bushelsFedToPeople) {
//    int amountOfBushelsNeed = population * 20;
//    int amountOfBushelsNeeded = 0;
//    if(bushelsFedToPeople>=amountOfBushelsNeeded){
//        // bushelsFedToPeople -= amountOfBushelsNeeded;
//        return 0;
//    }
//
//    int bushelsShort = amountOfBushelsNeed - bushelsFedToPeople;
//    double amountOfPeopleStarved = (double) bushelsShort / 20;
//    double amountOfPeopleStarved = 0;
//    double numberCeil = Math.ceil(amountOfPeopleStarved);
//    return(int) numberCeil;
//}