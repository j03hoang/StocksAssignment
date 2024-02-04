import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StockAnalyst implements IStockAnalyst {
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
                "<li>.*?<a href=\"/list/(.*?)\">(.*?)</a>\s?</li>"
        );
        matcher = pattern.matcher(categoryGroup);

        while (matcher.find()) {
            String link = "https://stockanalysis.com/list/" + matcher.group(1);
            subCategory_hyperLink_map.put(matcher.group(2), link);
        }
        return subCategory_hyperLink_map;
    }

    // todo make helper methods
    @Override
    public TreeMap<Double, List<String>> getTopCompaniesByChangeRate(String urlText) throws Exception {
        TreeMap<Double, List<String>> percentChange_company_map = new TreeMap<>(Collections.reverseOrder());
        String subCat_urlHtml = getUrlText(urlText);
        Pattern pattern = Pattern.compile("<tr .*?>" +
                "(.*?)*" +
                "<td .*?><a href.*?</td>" +
                "<td .*?>(.*?)</td>" +
                "(<td class=\"svelte-\\w{7}\">\\d+\\.\\d+%</td>)?" +
                ".*?" +
                "<td .*?>((-?\\d+\\.\\d+)%|-)</td>" +
                ".*?" +
                "</tr>");
        Matcher matcher = pattern.matcher(subCat_urlHtml);

        while (matcher.find()) {
            double key;
            if (Objects.equals(matcher.group(4), "-")) {
                key = 0.0;
            } else {
                key = Double.parseDouble(matcher.group(5));
            }
            String value = matcher.group(2);

            if (!percentChange_company_map.containsKey(key)) {
                List<String> valueList = new LinkedList<>();
                valueList.add(value);
                percentChange_company_map.put(key, valueList);
            } else {
                percentChange_company_map.get(key).add(value);
            }
        }

        return percentChange_company_map;
    }

    // todo make helper methods
    public TreeMap<Double, List<String>> getTopCompaniesETF(String urlText) throws Exception {
        TreeMap<Double, List<String>> percentChange_company_map = new TreeMap<>(Collections.reverseOrder());
        String subCat_urlHTML = getUrlText(urlText);
        Pattern pattern = Pattern.compile(
                "ETFs</h2>.*?" +
                        "</tbody></table>"
        );
        Matcher matcher = pattern.matcher(subCat_urlHTML);
        matcher.find();
        String ETF_table = matcher.group();

        pattern = Pattern.compile("<tr .*?>" +
                "<td .*?>.*?</td>" +
                "<td .*?>.*?</td>" +
                "<td .*?>(.*?)</td>" +
                ".*?" +
                "<td .*?>((-?\\d+\\.\\d+)%|-)</td>" +
                ".*?" +
                "</tr>");

        matcher = pattern.matcher(ETF_table);
        while (matcher.find()) {
            double key;
            if (matcher.group(2) == null){
                key = 0.0;
            } else {
                key = Double.parseDouble(matcher.group(3));
            }
            String value = matcher.group(1);

            if (!percentChange_company_map.containsKey(key)) {
                List<String> valueList = new LinkedList<>();
                valueList.add(value);
                percentChange_company_map.put(key, valueList);
            } else {
                percentChange_company_map.get(key).add(value);
            }
        }

        return percentChange_company_map;
    }
}
