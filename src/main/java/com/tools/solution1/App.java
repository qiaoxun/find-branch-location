package com.tools.solution1;

import com.tools.utils.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        String branchMark = "";
        if (null != args && args.length > 0) {
            branchMark = args[0];
        } else {
            while (StringUtil.isBlank(branchMark)) {
                System.out.println("Please input your branch mark");
                Scanner scanner = new Scanner(System.in);
                branchMark = scanner.nextLine();
            }
        }
        findBranchLocation(branchMark);
    }

    /**
     *
     * @param branchMark id like 66978
     */
    public static void findBranchLocation(String branchMark) {
        final StringBuilder dest = new StringBuilder();
        File ppmProjectFile = new File("C:\\Project");

        File[] ppmProjectArr = ppmProjectFile.listFiles(pathName -> {
            if (pathName.toString().startsWith("C:\\Project\\ppm") && !pathName.toString().equals("C:\\Project\\ppmInstall")) {
                return true;
            }
            return false;
        });

        Optional<File> optional = Stream.of(ppmProjectArr).filter(file -> {
            String path = file.getAbsolutePath();
            try {
                List<String> resultList = CommandUtils.executeCommandAndGetResult("git branch", file);
                resultList.forEach(result -> {
                    if (result.contains(branchMark)) {
                        dest.append(path);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (dest.length() > 0) {
                return true;
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
