package com.tools.solution2;

import com.tools.utils.StringUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class App {
    private static File ppmProjectFile = new File("C:\\Project");
    private static File[] ppmProjectArr = ppmProjectFile.listFiles(pathName -> {
        if (pathName.toString().startsWith("C:\\Project\\ppm") && !pathName.toString().equals("C:\\Project\\ppmInstall")) {
            return true;
        }
        return false;
    });

    // count how many times it runs
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main( String [] args ) {
//        Thread exitThread = null;
        Future future = null;
        if (null != args && args.length > 0) {
            findBranchLocation(args[0]);
        } else {
            while (true) {

                // if the service had already ran once, and there is no other action in next 10 seconds, just exit.
                if (atomicInteger.intValue() >= 1) {
                    future = executorService.submit(() -> {
                        boolean isInterrupted = false;
                        try {
                            Thread.sleep(10 * 1000);
                        } catch (InterruptedException e) {
                            isInterrupted = true;
                        }
                        if (!isInterrupted)
                            System.exit(0);
                    });
                    // if the service had already ran once, and there is no other action in next 10 seconds, just exit.
//                    exitThread = new Thread(() -> {
//                        boolean isInterrupted = false;
//                        try {
//                            Thread.sleep(5 * 1000);
//                        } catch (InterruptedException e) {
//                            isInterrupted = true;
//                        }
//                        if (!isInterrupted)
//                            System.exit(0);
//                    });
//                    exitThread.start();
                }
                System.out.println("Please input your branch mark");
                Scanner scanner = new Scanner(System.in);
                String branchMark = scanner.nextLine();
                if (StringUtil.isBlank(branchMark)) {
                    System.exit(0);
                }

                // if the service had already ran once, and there is no other action in next 10 seconds, just exit.
//                if (null != exitThread) {
//                    exitThread.interrupt();
//                    executorService.
//                }
                // if the service had already ran once, and there is no other action in next 10 seconds, just exit.
                if (null != future) {
                    future.cancel(true);
                }

                findBranchLocation(branchMark);

                // increase it by 1
                atomicInteger.incrementAndGet();
            }
        }

    }

    public static void findBranchLocation(String branchMark) {
        Optional<File> optional = Stream.of(ppmProjectArr).filter(file -> {
            String path = file.getAbsolutePath();
            try {
                Git git = GitUtil.getGit(path + "/.git");
                ListBranchCommand listBranchCommand = git.branchList();
                List<Ref> branchList = listBranchCommand.call();
                Optional<Ref> optionalRef = branchList.stream().filter(ref -> {
                    String name = ref.getName();
                    if (!StringUtil.isBlank(name) && name.contains(branchMark))
                        return true;
                    return false;
                }).findFirst();
                if (optionalRef.isPresent())
                    return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
            return false;
        }).findFirst();

        try {
            File destFile = optional.orElseGet(null);
            String path = destFile.getAbsolutePath();
            System.out.println(path);
//            Runtime.getRuntime().exec("explorer.exe /select, " + path + "\\");
            Runtime.getRuntime().exec("cmd /c start " + path);
        } catch (Exception e) {
            System.out.println("Sorry, we didn't find a project location that contains " + branchMark + "!");
        }
    }
}
