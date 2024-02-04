import java.util.*;
import java.util.Scanner;

public class Client {
    private static void printCategories(List<String> categories) {
        for (int i = 0; i < categories.size(); i++) {
            System.out.println(i + ": " + categories.get(i));
        }
    }

    private static void printSubCategories(Map<String, String> map) {
        int i = 0;
        for (String subCat : map.keySet()) {
            System.out.println(i++ + ": " + subCat);
        }
    }

    private static String getSubCategoryLink(Map<String, String> map, int userSelection) {
        Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
        int h = 0;
        String subCatLink = null;
        while (h++ <= userSelection && itr.hasNext()) {
            Map.Entry<String, String> entry = itr.next();
            subCatLink = entry.getValue();
        }
        System.out.println("Chosen sub-category: " + subCatLink);
        return subCatLink;
    }

    private static void printTopCompanies(Map<Double, List<String>> map, int topCount) {
        Iterator<Map.Entry<Double, List<String>>> mapItr = map.entrySet().iterator();
        int iterationCount = 0;
        while (iterationCount < topCount && mapItr.hasNext()) {
            Map.Entry<Double, List<String>> entry = mapItr.next();

            if (entry.getValue().size() > 1) {
                Iterator<String> listItr = entry.getValue().iterator();

                while (iterationCount < topCount && listItr.hasNext()) {
                    System.out.println(listItr.next() + " " + entry.getKey() + "%");
                    iterationCount++;
                }

            } else {
                System.out.println(entry.getValue().get(0) + " " + entry.getKey() + "%");
                iterationCount++;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        StockAnalyst businessSide = new StockAnalyst();
        Scanner scanner = new Scanner(System.in);
        int userSelection = 0;
        String mainPage = businessSide.getUrlText();
        List<String> categories = businessSide.getStocksListCategories(mainPage);

        while (userSelection == 0) {
            System.out.println("##----------------------------------------------------------------------------");
            System.out.println("These are the available stock list categories, please choose one:");
            printCategories(categories);
            userSelection = scanner.nextInt();

            System.out.println("##----------------------------------------------------------------------------");
            Map<String, String> map = businessSide.getStocksListsInListCategory(mainPage, categories.get(userSelection));
            System.out.println("These are the available stock lists within this category, please choose a key:");
            printSubCategories(map);
            userSelection = scanner.nextInt();

            System.out.println("##----------------------------------------------------------------------------");
            String link = getSubCategoryLink(map, userSelection);
            Map<Double, List<String>> map2 = businessSide.getTopCompaniesByChangeRate(link);
            System.out.println("How many of the top companies do you care to see?");
            userSelection = scanner.nextInt();
            printTopCompanies(map2, userSelection);
            if (businessSide.getUrlText(link).contains("ETFs</h2>")) { //todo
                Map<Double, List<String>> map3 = businessSide.getTopCompaniesETF(link);
                printTopCompanies(map3, userSelection);
            }

            System.out.println("##----------------------------------------------------------------------------");
            System.out.println("Enter 0 to continue, 1 to exit.");
            userSelection = scanner.nextInt();

        }


    }
}
