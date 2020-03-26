package crawler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import dao.Project;
import dao.ProjectDao;
import okhttp3.*;
import org.jetbrains.annotations.TestOnly;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Crawler {
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Gson gson = new GsonBuilder().create();
    public static void main(String[] args) throws IOException {
        Crawler crawler = new Crawler();
        String html = crawler.getPage("https://github.com/akullpp/awesome-java/blob/master/README.md");
        List<Project> projects = crawler.parseProjectList(html);
        for (int i = 0; i < projects.size(); i++) {
            try {
                Project project = projects.get(i);
                String jsonString = crawler.getRepoInfo(project.getUrl());
//            System.out.println(jsonString);
                if (jsonString != null) {
                    crawler.parseRepoInfo(jsonString, project);
                    System.out.println(project);
                    ProjectDao projectDao = new ProjectDao();
                    projectDao.save(project);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getPage(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if (!response.isSuccessful()) {
            System.out.println("请求失败！");
            return null;
        }
        return response.body().string();
    }

    public List<Project> parseProjectList(String html) {
        ArrayList<Project> result = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("li");

        for (Element li : elements) {
            Elements allLink = li.getElementsByTag("a");
            if (allLink.size() == 0) {
                continue;
            }
            Element link = allLink.get(0);
            String url = link.attr("href");
            if (!url.startsWith("https://github.com")) {
                continue;
            }
            Project project = new Project();
            project.setName(link.text());
            project.setUrl(url);
            project.setDescription(li.text());
            result.add(project);
        }
        return result;
    }

    public String getRepoInfo(String Url) throws IOException {
        String username = "gch12345";
        String password = "gch19980506";

        String credential = Credentials.basic(username, password);
        String url = "https://api.github.com/repos/" + Url.substring(19);
        Request request = new Request.Builder().url(url).header("Authorization", credential).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if (!response.isSuccessful()) {
            System.out.println("访问 Github API 失败！url = " + url);
            return null;
        }
        return response.body().string();
    }

    public void parseRepoInfo(String jsonString, Project project) {
        Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap<String, Object> hashMap = gson.fromJson(jsonString, type);
        Double starCount = (Double) hashMap.get("stargazers_count");
        project.setStarCount(starCount.intValue());
        Double forkCount = (Double)hashMap.get("forks_count");
        project.setForkCount(forkCount.intValue());
        Double openedIssueCount = (Double)hashMap.get("open_issues_count");
        project.setOpenIssueCount(openedIssueCount.intValue());
    }

}
