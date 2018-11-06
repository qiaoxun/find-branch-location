package com.tools.solution2;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class GitUtil {

    public static Repository getRepository(String path) throws IOException {
        Repository resp = new FileRepositoryBuilder().setGitDir(new File(path)).build();
        return resp;
    }

    public static Git getGit(String path) throws IOException {
        Repository resp = new FileRepositoryBuilder().setGitDir(new File(path)).build();
        return new Git(resp);
    }
}
