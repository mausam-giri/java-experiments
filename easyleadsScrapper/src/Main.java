import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.*;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main{

    private static final String USER_AGENT = null;
    private static final String PATHNAME_HTML = "." + File.separator + "html_files";
    private static final int MAX_THREADS = 4;

    public static void main(String[] args) {
        Instant start = Instant.now();
       // load and save data
        String[] URLS = {
                "https://www.easyleadz.com/company/tata-consultancy-services-tcs",
                "https://www.easyleadz.com/company/ibm",
                "https://www.easyleadz.com/company/wipro",
                "https://www.easyleadz.com/company/ernst-and-young-services-pvt-ltd",
                "https://www.easyleadz.com/company/larsen-amp-toubro",
                "https://www.easyleadz.com/company/hewlett-packard-enterprise",
                "https://www.easyleadz.com/company/jpmorgan-chase-co",
                "https://www.easyleadz.com/company/standard-chartered-securities-india-ltd",
                "https://www.easyleadz.com/company/government-of-india",
                "https://www.easyleadz.com/company/lt-infotech",
                "https://www.easyleadz.com/company/mahindra-and-mahindra"
        };

        ExecutorService downloadExecutor = Executors.newFixedThreadPool(MAX_THREADS);
        List<CompletableFuture<Void>> downloadedFutures = new ArrayList<>();
        for(String url: URLS){
            if(checkHtmlExists(url)) continue;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try{
                    getHtml_SaveFile(url);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }, downloadExecutor);
            downloadedFutures.add(future);
        }

        CompletableFuture<Void> allDownloadedFuture = CompletableFuture.allOf(downloadedFutures.toArray(new CompletableFuture[0]));
        allDownloadedFuture.join();
        downloadExecutor.shutdown();

        List<File> htmlFiles = getAllHtmlFiles();
        ExecutorService parseExecutor = Executors.newFixedThreadPool(MAX_THREADS);
        List<CompletableFuture<JSONObject>> parseFutures = htmlFiles.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return parseHtmlFile(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, parseExecutor))
                .collect(Collectors.toList());

        CompletableFuture<Void> allParseFuture = CompletableFuture.allOf(parseFutures.toArray(new CompletableFuture[0]));
        List<JSONObject> parsedDataList = allParseFuture.thenApply(v -> parseFutures.stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                        .join();

        parseExecutor.shutdown();

        JSONArray companyDataArray = new JSONArray(parsedDataList);
        ScrapedData saveData = new ScrapedData(companyDataArray);
        saveData.writeDataToJson();

        Instant end = Instant.now();
        Duration Elapsed = Duration.between(start, end);
        System.out.println("Time taken: " + Elapsed.toMillis());
    }

    private static List<File> getAllHtmlFiles(){
        File directory = new File(PATHNAME_HTML);
        File[] files = directory.listFiles();
        List<File> htmlFiles = new ArrayList<>();
        if(files != null){
            for(File file: files){
                if(file.isFile() && file.getName().endsWith(".html")){
                    htmlFiles.add(file);
                }
            }
        }
        return htmlFiles;
    }

    private static JSONObject parseHtmlFile(File file) throws IOException {
        String filename = file.getName().replace(".html", "");
        Document doc = Jsoup.parse(file);
        String title = doc.title();
        System.out.println("Parsing: "+ title);

        JSONObject companyDataObject = new JSONObject();

        JSONObject companySectionDataObject = new JSONObject();
        companySectionDataObject.put("overview", getOverviewData(doc));
        companySectionDataObject.put("top-contact", getEmployeesData(doc));
        companySectionDataObject.put("faq-section", getFaqData(doc));
        companySectionDataObject.put("tech-stack", getTechStackData(doc));
        companySectionDataObject.put("similar-company", getSimilarCompanyData(doc));
        companyDataObject.put(filename, companySectionDataObject);

        return companyDataObject;
    }
    private static JSONObject getOverviewData(Document doc){
        System.out.println("\t Parsing Overview data");
        JSONObject overviewDataObject = new JSONObject();
        try {
            Elements overviewTable = doc.select("div#overview table");

            for(Element table: overviewTable){
                Elements rows = table.select("tbody tr");

                for(Element row: rows){

                    Element th = row.select("th").first();
                    Element td = row.select("td").first();

                    JSONObject socialHandlesDataObj = new JSONObject();

                    if(th != null && td != null && !th.text().isEmpty()) {
                        
                        if(th.text().contains("Website")){
                            String link = Objects.requireNonNull(td.selectFirst("a")).attr("href");
                            overviewDataObject.put(th.text(), link);
                        } else if (th.text().contains("Social Handles")) {
                            Elements socialLinks = td.select("a");
                            if(!socialLinks.isEmpty()){
                                for(Element link: socialLinks){
                                    String href = link.attr("href");
                                    String handleName = rootDomainFromURL(href);
                                    socialHandlesDataObj.put(handleName, href);
                                }
                                overviewDataObject.put(th.text(), socialHandlesDataObj);
                            }
                        }else{
                            overviewDataObject.put(th.text(), td.text());
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println("Error "+ e);
        }
        return overviewDataObject;
    }

    private static JSONObject getEmployeesData(Document doc){
        System.out.println("\t Parsing Top Contacts");
        JSONObject employeeDataObject = new JSONObject();
        try{
            Element employeeTable = doc.select("div#topContacts table").first();

            if(employeeTable != null){
                JSONArray headerArray = new JSONArray();
                Elements employeeHeader = employeeTable.select("thead tr th");
                for(Element header: employeeHeader){
                    if(!header.text().isEmpty()) headerArray.put(header.text());
                }

                Elements employeeDataRow = employeeTable.select("tbody tr");
                JSONArray dataArray = new JSONArray();
                for(Element row: employeeDataRow){
                    Elements rowData = row.select("td");
                    ArrayList<String> rowArray = new ArrayList<>();
                    for(Element data: rowData){
                        if(!data.text().isEmpty()) rowArray.add(data.text());
                    }
                    dataArray.put(rowArray);
                }
                employeeDataObject.put("headers", headerArray);
                employeeDataObject.put("data", dataArray);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return employeeDataObject;
    }

    private static JSONArray getFaqData(Document doc){
        System.out.println("\t Parsing FAQ data");
        JSONArray faqDataArray = new JSONArray();
        try{
//            Element faqSection = doc.select("div#faq div.faq-cont").first();
            Elements faqs = doc.select("div.container-mini");

            for(Element faq:faqs){
                JSONObject faqDataObject = new JSONObject();
                Element questionElement = faq.selectFirst("[itemprop=name]");
                Element answerElement = faq.selectFirst("[itemprop=text]");

                if(questionElement != null && answerElement != null) {
                    faqDataObject.put("question", questionElement.text());
                    faqDataObject.put("answer", answerElement.text());
                    faqDataArray.put(faqDataObject);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return faqDataArray;
    }
    private static JSONArray getTechStackData(Document doc){
        JSONArray techStackArray = new JSONArray();
        Element techSection = doc.selectFirst("div#technologyStack");
        if(techSection == null) return null;
        Elements techStack = techSection.select("a");
        for(Element stack: techStack){
            if(!stack.attr("href").isEmpty())
                techStackArray.put(stack.attr("href"));
        }
        return techStackArray;
    }
    private static JSONArray getSimilarCompanyData(Document doc){
        try{
            Element tableSection = doc.selectFirst("div#similarCompany table");
            JSONArray dataArray = new JSONArray();
            if(tableSection != null){
                Elements rows = tableSection.select("tbody tr");
                Elements headerColumns = tableSection.select("thead th");
                for (Element row : rows) {
                    JSONObject dataObject = new JSONObject();
                    Elements dataColumns = row.select("td");

                    for (int i = 0; i < dataColumns.size(); i++) {
                        Element dataColumn = dataColumns.get(i);
                        Element headerColumn = headerColumns.get(i);

                        if (dataColumn.select("a").isEmpty()) {
                            dataObject.put(headerColumn.text(), dataColumn.text());
                        } else {
                            String url = dataColumn.select("a").attr("href");
                            dataObject.put(headerColumn.text(), dataColumn.text());
                            dataObject.put("link", url);
                        }
                    }

                    dataArray.put(dataObject);
                }
            }
            return dataArray;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private static void getHtml_SaveFile(String url) throws IOException {
        System.out.println("Getting data from :" + url);
        

//        Document doc = response.parse();
        String filename = getFilenameFromUrl(url) + ".html";
        Path htmlDir = Paths.get(PATHNAME_HTML);
        if(!Files.exists(htmlDir)){
            Files.createDirectory(htmlDir);
        }
        Path file = htmlDir.resolve(filename);
        try {
            AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(file,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            ByteBuffer buffer = ByteBuffer.wrap(doc.outerHtml().getBytes());
            fileChannel.write(buffer, 0, null, new CompletionHandler<Integer, Object>() {
                @Override
                public void completed(Integer result, Object attachment) {
                    System.out.println("Saved file successfully: " + file);
                    try {
                        fileChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.err.println("Error while writing file: " + exc.getMessage());
                    try {
                        fileChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Saved file successfully: " + file);
    }

    // Utilities
    @org.jetbrains.annotations.NotNull
    private static String getFilenameFromUrl(String url) {
        String[] urlParts = url.split("/");
        String lastSubstring = urlParts[urlParts.length - 1];
        return lastSubstring.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
    private static boolean checkHtmlExists(String url){
        String filename = getFilenameFromUrl(url);
        File file = new File(PATHNAME_HTML, filename+".html");
        return file.exists();

    }

    private static String rootDomainFromURL(String url){
        try{
            URI uri = new URI(url);
            String host = uri.getHost();
            String[] parts = host.split("\\.");
            int length = parts.length;
            return parts[length - 2];
        }catch(URISyntaxException e){
            e.printStackTrace();
            return url;
        }
    }
}