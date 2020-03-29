package crawler;

import dao.Project;
import dao.ProjectDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadCrawler extends Crawler {
    private static volatile List<Project> projects;
    private static volatile List<Integer> list = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        ThreadCrawler crawler = new ThreadCrawler();
        while (true) {
            String html = crawler.getPage("https://github.com/akullpp/awesome-java/blob/master/README.md");
            projects = crawler.parseProjectList(html);
            List<Future<?>> taskResults = new ArrayList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            int size = projects.size();
            for (int i = 0; i < size; i++) {
                Project project = projects.get(i);
                Future<?> taskResult = executorService.submit(new CrawlerTask(project, crawler, i));
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
            list.sort(Comparator.naturalOrder());
            for (int i = 0; i < list.size(); i++) {
                projects.remove(list.get(i) - i);
            }
            int count = 0;
            for (int i = 0; i < 10; i++) {
                if (projects.get(i).getStarCount() == 0) {
                    count++;
                }
            }
            if (count < 1) {
                break;
            }
        }
        ProjectDao projectDao = new ProjectDao();
        for (Project project : projects) {
            projectDao.save(project);
        }
    }

    static class CrawlerTask implements Runnable {
        private Project project;
        private ThreadCrawler threadCrawler;
        private int index;

        public CrawlerTask(Project project, ThreadCrawler threadCrawler, int index) {
            this.project = project;
            this.threadCrawler = threadCrawler;
            this.index = index;
        }

        @Override
        public void run() {
            try {
                System.out.println("crawing " + project.getName() + "...");
                String jsonString = threadCrawler.getRepoInfo(project.getUrl());
                if (jsonString != null) {
                    threadCrawler.parseRepoInfo(jsonString, project);
                }
                else {
                    synchronized (ThreadCrawler.class) {
                        list.add(index);
                    }
                }
                System.out.println("crawing " + project.getName() + "   done");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
