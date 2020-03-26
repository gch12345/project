package crawler;

import dao.Project;
import dao.ProjectDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadCrawler extends Crawler {
    public static void main(String[] args) throws IOException {
        ThreadCrawler crawler = new ThreadCrawler();
        String html = crawler.getPage("https://github.com/akullpp/awesome-java/blob/master/README.md");
        List<Project> projects = crawler.parseProjectList(html);
        List<Future<?>> taskResults = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (Project project : projects) {
            Future<?> taskResult = executorService.submit(new CrawlerTask(project, crawler));
            taskResults.add(taskResult);
        }
        for (Future<?> taskResult : taskResults) {
            try {
                taskResult.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        ProjectDao projectDao = new ProjectDao();
        for (Project project : projects) {
            projectDao.save(project);
        }
    }

    static class CrawlerTask implements Runnable {
        private Project project;
        private ThreadCrawler threadCrawler;

        public CrawlerTask(Project project, ThreadCrawler threadCrawler) {
            this.project = project;
            this.threadCrawler = threadCrawler;
        }

        @Override
        public void run() {
            try {
                System.out.println("crawing " + project.getName() + "...");
                String jsonString = threadCrawler.getRepoInfo(project.getUrl());
                if (jsonString != null) {
                    threadCrawler.parseRepoInfo(jsonString, project);
                }
                System.out.println("crawing " + project.getName() + "done");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
