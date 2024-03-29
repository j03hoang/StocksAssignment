import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockAnalyst implements IStockAnalyst {

    /** public member functions */
    public String getUrlText() throws Exception {
        return getUrlText(WEB_URL);
    }

    @Override
    public String getUrlText(String url) throws Exception {
        URLConnection stockListURL = new URL(url).openConnection();
        stockListURL.setRequestProperty(
                "user-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11"
        );

        BufferedReader inputStream = new BufferedReader(new InputStreamReader(stockListURL.getInputStream()));
        String inputLine;
        StringBuilder urlHtml = new StringBuilder();

        while ((inputLine = inputStream.readLine()) != null) {
            urlHtml.append(inputLine).append("\n");
        }

        inputStream.close();
        return urlHtml.toString();
    }

    @Override
    public List<String> getStocksListCategories(String urlText) {
        List<String> result = new LinkedList<>();

        Pattern pattern = Pattern.compile(
                "<h2 class.*?>(.*?)</h2>"
        );

        Matcher matcher = pattern.matcher(urlText);

        while (matcher.find()) {
            result.add(matcher.group(1));
        }
        return result;
    }

    @Override
    public Map<String, String> getStocksListsInListCategory(String urlText, String stockCategoryName) {
        Map<String, String> subCategory_hyperLink_map = new HashMap<>();

        Pattern pattern = Pattern.compile(
                        stockCategoryName + ".*?" + "</ul>"
        );
        Matcher matcher = pattern.matcher(urlText);

        matcher.find();
        String categoryGroup = matcher.group();

        pattern = Pattern.compile(
                "<li.*?>.*?<a href=\"((/etf)?/list/.*?)\">(.*?)</a>\s?</li>"
        );
        matcher = pattern.matcher(categoryGroup);

        while (matcher.find()) {
            String link = "https://stockanalysis.com" + matcher.group(1);
            subCategory_hyperLink_map.put(matcher.group(3), link);
        }
        return subCategory_hyperLink_map;
    }

    @Override
    public TreeMap<Double, List<String>> getTopCompaniesByChangeRate(String urlText, int topCount) throws Exception {
        TreeMap<Double, List<String>> percentChange_company_map;
        String subCat_urlHtml = getUrlText(urlText);
        Pattern pattern = Pattern.compile(
                "<table .*?>.*?" +
                        "</table>"
        );
        Matcher matcher = pattern.matcher(subCat_urlHtml);

        matcher.find();
        String regularStock_table = matcher.group();
        percentChange_company_map = compileIntoMapPercentPattern(regularStock_table);
        printTopCompanies(percentChange_company_map, topCount);

        if (matcher.find()) { // case: two tables to report
            TreeMap<Double, List<String>> ETF_map;
            String ETF_table = matcher.group();
            ETF_map = compileIntoMapPercentPattern(ETF_table);
            printTopCompanies(ETF_map, topCount);

            percentChange_company_map.putAll(ETF_map);
        }
        return percentChange_company_map;
    }

    /** helper functions */
    private static void printTopCompanies(TreeMap<Double, List<String>> map, int topCount) {
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

    private static TreeMap<Double, List<String>> compileIntoMapPercentPattern(String tableText) {
        TreeMap<Double, List<String>> percentChange_company_map = new TreeMap<>(Collections.reverseOrder());
        Pattern pattern = Pattern.compile("<tr .*?>" +
                "(.*?)*" +
                "<td .*?><a href.*?</td>" +
                "<td .*?>(.*?)</td>" +
                "(<td class=\"svelte-\\w{7}\">\\d+\\.\\d+%</td>)?" + // case: there was a prior percentage
                ".*?" +
                "<td .*?>((-?\\d+\\.\\d+)%|-)</td>" +
                ".*?" +
                "</tr>");
        Matcher matcher = pattern.matcher(tableText);

        while (matcher.find()) {
            double key;
            if (Objects.equals(matcher.group(4), "-")) { // case: no change
                key = 0.0;
            } else {
                key = Double.parseDouble(matcher.group(5));
            }
            String value = matcher.group(2);

            addListToMap(percentChange_company_map, key, value);
        }
        return percentChange_company_map;
    }

    private static void addListToMap(TreeMap<Double, List<String>> map, Double key, String value) {
        if (!map.containsKey(key)) {
            List<String> valueList = new LinkedList<>();
            valueList.add(value);
            map.put(key, valueList);
        } else {
            map.get(key).add(value);
        }
    }
}
