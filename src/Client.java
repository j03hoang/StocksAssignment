import java.util.*;
import java.util.Scanner;

public class Client {
    static StockAnalyst businessSide = new StockAnalyst();
    static Scanner scanner = new Scanner(System.in);

    private static void printCategories(List<String> categories) {
        for (int i = 0; i < categories.size(); i++) {
            System.out.println(i + ": " + categories.get(i));
        }
    }

    private static void printSubCategories(Map<String, String> map) {
        int i = 0;
        for (String keys : map.keySet()) {
            System.out.println(i++ + ": " + keys);
        }
    }

    private static String getSubCategoryLink(Map<String, String> map, int userSelection) {
        Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
        int h = 0;
        String link = null;
        while (h <= userSelection && itr.hasNext()) {
            Map.Entry<String, String> entry = itr.next();
            link = entry.getValue();
            h++;
        }
        System.out.println(link);
        return link;
    }

    private static void printTopCompanies(Map<Double, List<String>> map, int topCount) {
        Iterator<Map.Entry<Double, List<String>>> itr2 = map.entrySet().iterator();
        int iterationCount = 0;
        while (iterationCount < topCount && itr2.hasNext()) {
            Map.Entry<Double, List<String>> entry = itr2.next();
            if (entry.getValue().size() > 1) {
                Iterator<String> valueItr = entry.getValue().iterator();

                while (iterationCount < topCount && valueItr.hasNext()) {
                    System.out.println(valueItr.next() + " " + entry.getKey() + "%");
                    iterationCount++;
                }

            } else {
                System.out.println(entry.getValue().get(0) + " " + entry.getKey() + "%");
                iterationCount++;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        int userSelection = 0;
        String mainPage = businessSide.getUrlText();
        List<String> categories = businessSide.getStocksListCategories(mainPage);

        while (userSelection == 0) {
            System.out.println("##---------------------------------------------------------------");
            System.out.println("These are the available stock list categories, please choose one:");
            printCategories(categories);
            userSelection = scanner.nextInt();

            System.out.println("##---------------------------------------------------------------");
            Map<String, String> map = businessSide.getStocksListsInListCategory(mainPage, categories.get(userSelection));
            System.out.println("These are the available stock lists within this category, please choose a key:");
            printSubCategories(map);
            userSelection = scanner.nextInt();

            System.out.println("##---------------------------------------------------------------");
            String link = getSubCategoryLink(map, userSelection);
            Map<Double, List<String>> map2 = businessSide.getTopCompaniesByChangeRate(link);
            System.out.println("How many of the top companies do you care to see?");
            userSelection = scanner.nextInt();
            printTopCompanies(map2, userSelection);
            if (businessSide.getUrlText(link).contains("ETFs</h2>")) { //todo
                Map<Double, List<String>> map3 = businessSide.getTopCompaniesETF(link);
                printTopCompanies(map3, userSelection);
            }

            System.out.println("##---------------------------------------------------------------");
            System.out.println("Enter 0 to continue, 1 to exit.");
            userSelection = scanner.nextInt();
        }


    }
}
