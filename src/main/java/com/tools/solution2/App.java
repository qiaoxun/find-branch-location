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
import java.util.stream.Stream;

public class App {
    private static  File ppmProjectFile = new File("C:\\Project");
    private static File[] ppmProjectArr = ppmProjectFile.listFiles(pathName -> {
        if (pathName.toString().startsWith("C:\\Project\\ppm") && !pathName.toString().equals("C:\\Project\\ppmInstall")) {
            return true;
        }
        return false;
    });

    public static void main( String [] args ) {
        if (null != args && args.length > 0) {
            findBranchLocation(args[0]);
        } else {
            while (true) {
                System.out.println("Please input your branch mark");
                Scanner scanner = new Scanner(System.in);
                String branchMark = scanner.nextLine();
                if (StringUtil.isBlank(branchMark)) {
                    System.exit(0);
                }
                findBranchLocation(branchMark);
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
            System.out.println(destFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Sorry, we didn't find a project location that contains " + branchMark + "!");
        }
    }


}
