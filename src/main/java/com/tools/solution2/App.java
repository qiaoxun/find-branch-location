package com.tools.solution2;

import com.tools.utils.StringUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.io.IOException;
import java.util.*;
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
                }
                System.err.println("Please input your branch mark : ");
                Scanner scanner = new Scanner(System.in);
                String branchMark = scanner.nextLine();
                if (StringUtil.isBlank(branchMark)) {
                    System.exit(0);
                }

                // if the service had already ran once, and there is no other action in next 10 seconds, just exit.
                if (null != future) {
                    future.cancel(true);
                }

                List<PathAndNameEntity> panList = findBranchLocation(branchMark);
                chooseWhichOne(panList, branchMark);
                // increase it by 1
                atomicInteger.incrementAndGet();
            }
        }

    }

    /**
     * loop all projects in ppmProjectArr, and loop all branches in every project
     * @param branchMark
     * @return
     */
    public static List<PathAndNameEntity> findBranchLocation(String branchMark) {
        List<PathAndNameEntity> matchedLocation = new LinkedList<>();

        Stream.of(ppmProjectArr).forEach(file -> {
            String path = file.getAbsolutePath();
            try {
                Git git = GitUtil.getGit(path + "/.git");
                ListBranchCommand listBranchCommand = git.branchList();
                List<Ref> branchList = listBranchCommand.call();
                branchList.stream().forEach(ref -> {
                    String name = ref.getName();
                    if (!StringUtil.isBlank(name) && name.contains(branchMark)) {
                        name = name.substring(name.lastIndexOf("/") + 1);
                        PathAndNameEntity pan = new PathAndNameEntity();
                        pan.setPath(path);
                        pan.setBranchName(name);
                        matchedLocation.add(pan);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        });

        return matchedLocation;
    }

    /**
     * let use to choose which to open
     * @param matchedLocation
     * @param branchMark
     */
    public static void chooseWhichOne(List<PathAndNameEntity> matchedLocation, String branchMark) {

        if (null == matchedLocation || matchedLocation.isEmpty()) {
            printNotFoundMsg(branchMark);
            return;
        }

        if (matchedLocation.size() == 1) {
            openPath(matchedLocation.get(0).getPath(), branchMark);
            return;
        }

        System.err.println("Please select one or more indexes to open (using comma to separate every branch);");
        AtomicInteger index = new AtomicInteger();
        matchedLocation.forEach(pae -> System.out.println(index.getAndAdd(1) + " : " + pae.getBranchName() + " (" + pae.getPath() + ")"));
        Scanner scanner = new Scanner(System.in);
        String chosenIndex = scanner.nextLine();

        if (StringUtil.contains(chosenIndex, ",")) {
            String[] indexArr = chosenIndex.split(",");
            for (String idx : indexArr) {
                dealWithInput(idx, branchMark, matchedLocation);
            }
        } else {
            dealWithInput(chosenIndex, branchMark, matchedLocation);
        }
    }

    /**
     * open the path
     * @param path
     * @param branchMark
     */
    private static void openPath(String path, String branchMark) {
        try {
            Runtime.getRuntime().exec("cmd /c start " + path);
        } catch (IOException e) {
            printNotFoundMsg(branchMark);
        }
    }

    /**
     * print not found message
     * @param branchMark
     */
    private static void printNotFoundMsg(String branchMark) {
        System.out.println("Sorry, we didn't find a project location that contains " + branchMark + "!");
    }

    /**
     * parse string to int
     * @param index
     * @return
     */
    private static int parseIntForInput(String index) {
        try {
            return Integer.parseInt(index);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * parse input and open the location
     * @param input
     * @param branchMark
     * @param matchedLocation
     */
    private static void dealWithInput(String input, String branchMark, List<PathAndNameEntity> matchedLocation) {
        int i = parseIntForInput(input);
        if (i != -1) {
            PathAndNameEntity pan = matchedLocation.get(i);
            System.out.println("Opening " + pan.getPath());
            openPath(pan.getPath(), branchMark);
        } else {
            System.out.println(i + " is not a number, please input numbers!!");
        }
    }

}
