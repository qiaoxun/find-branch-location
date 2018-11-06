package com.tools.solution1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommandUtils {
    /**
     * get results from executed command
     * @param command
     * @return
     * @throws IOException
     */
    public static List<String> executeCommandAndGetResult(String command, File dir) throws IOException {
        Process process = Runtime.getRuntime().exec(command, null, dir);
        List<String> allResult = new ArrayList<>();
        try(InputStream is = process.getInputStream();BufferedReader br = new BufferedReader(new InputStreamReader(is));){
            String line;
            while ((line = br.readLine()) != null) {
                if (null != line && !"".equals(line)) {
                    allResult.add(line);
                }
            }
        }
        return allResult;
    }
}
