import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) throws Exception {
        // get main 9 categories
        // select one
        // get sub categories
        // // put into map
        // select one
//        StockAnalyst test = new StockAnalyst();
//        String a = test.getUrlText();
//        System.out.println(a);
//
//        List<String> list = test.getStocksListCategories(a);
//
//        System.out.println();
//
//        Map<String, String> testMap = test.getStocksListsInListCategory(a, "Other Lists");

//        Map<Double, String> map = new TreeMap<>(Collections.reverseOrder());
//        map.put(123.4, "a");
//        map.put(5.4, "b");
//        map.put(12.4, "b");
//        map.put(14.4, "b");
//

//        Map<Double, List<String>> percentChange_company_map
//                = test.getTopCompaniesByChangeRate("https://stockanalysis.com/list/biggest-companies/");
//        Map<Double, List<String>> percentChange_company_map = new TreeMap<>(Collections.reverseOrder());
//        String subCat_urlText = test.getUrlText("https://stockanalysis.com/list/mega-cap-stocks/");
//        Pattern pattern = Pattern.compile("<tr .*?>" +
//                "<td .*?>.*?</td>" +
//                "<td .*?>.*?</td>" +
//                "<td .*?>(.*?)</td>" +
//                "<td .*?>.*?</td>" +
//                "<td .*?>.*?</td>" +
//                "<td .*?>((.*?)%|-)</td>" +
//                ".*?" +
//                "</tr>");
//        Matcher matcher = pattern.matcher(subCat_urlText);
//        while (matcher.find()) {
//            if (matcher.group(2).length() > 8) {
//                System.out.println("skip");
//                continue;
//            }
//            System.out.println("a" + matcher.group(3).substring(0, matcher.group(2).length() - 1));
//            System.out.println("b" + matcher.group(1));
//            double key = Double.parseDouble(matcher.group(3));
//            String value = matcher.group(1);
//            if (!percentChange_company_map.containsKey(key)) {
//                List<String> valueList = new ArrayList<>();
//                valueList.add(value);
//                percentChange_company_map.put(key, valueList);
//            } else {
//                percentChange_company_map.get(key).add(value);
//            }
//        }
//
//        Map<Double, List<String>> map = test.getTopCompaniesByChangeRate("https://stockanalysis.com/list/mega-cap-stocks/");
//        addA(map,14.5, "shabba");
//        addA(map,14.5, "sdasdaa");
//        addA(map,12.5, "habba");
//        addA(map,11.5, "ahabba");
//        addA(map,2.0, "JohnnyNguyenhabba");
//
//        Iterator<Map.Entry<Double, List<String>>> itr = percentChange_company_map.entrySet().iterator();
//        int iterationCount = 0;
//        while (iterationCount < 3 && itr.hasNext()) {
//            Map.Entry<Double, List<String>> entry = itr.next();
//            if (entry.getValue().size() > 1) {
//                Iterator<String> valueItr = entry.getValue().iterator();
//
//                while (iterationCount < 3 && valueItr.hasNext()) {
//                    System.out.println(entry.getKey() + " " + valueItr.next());
//                    iterationCount++;
//                }
//
//            } else {
//                System.out.println(entry.getKey() + " " + entry.getValue().get(0));
//                iterationCount++;
//            }
//        }
    }

    public static void addA(Map<Double, List<String>> map, Double a, String b) {
        if (!map.containsKey(a)) {
            List<String> list2 = new ArrayList<>();
            list2.add(b);
            map.put(a, list2);
        } else {
            map.get(a).add(b);
        }
    }
}