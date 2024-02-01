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
        StringBuilder text = new StringBuilder();

        while ((inputLine = inputStream.readLine()) != null) {
            text.append(inputLine).append("\n");
        }

        inputStream.close();
        return text.toString();
    }

    @Override
    public List<String> getStocksListCategories(String urlText) {
        List<String> result = new LinkedList<>();

        Pattern pattern = Pattern.compile(
                "<h2 class.*?>(.*?)</h2>"
        );

        Matcher matcher = pattern.matcher(urlText);

        while (matcher.find()) {
            System.out.println(matcher.group(1)); // todo remove
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
        System.out.println(categoryGroup); // todo remove

        pattern = Pattern.compile(
                "<li>.*?<a href=\"/list/(.*?)\">(.*?)</a>\s?</li>"
        );
        matcher = pattern.matcher(categoryGroup);

        while (matcher.find()) {
            String link = "https://stockanalysis.com/list/" + matcher.group(1);
            subCategory_hyperLink_map.put(matcher.group(2), link);
            System.out.println(link); // todo remove
            System.out.println(matcher.group(2)); // todo remove
        }
        return subCategory_hyperLink_map;
    }

    @Override
    public TreeMap<Double, String> getTopCompaniesByChangeRate(String urlText, int topCount) throws Exception {
        Map<String, Double> percentChange_company_map = new TreeMap<>();
        String subCat_urlText = getUrlText(urlText);
        Pattern pattern = Pattern.compile("<tr .*?>" +
                "<td .*?>.*?</td>" +
                "<td .*?>.*?</td>" +
                "<td .*?>(.*?)</td>" +
                "<td .*?>.*?</td>" +
                "<td .*?>.*?</td>" +
                "<td .*?>(.*?)%</td>" +
                "" +
                "</tr>");
        Matcher matcher = pattern.matcher(subCat_urlText);
        return null;
    }
}
