package edu.ustb.sei.mde.compare.web;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class MyGit {

    @Test
    void shallowCloneTest() throws IOException, InterruptedException {
        String remoteString = "http://101.43.149.150/lyt/new_project.git";
        String localString = "C:\\Users\\10242\\Desktop\\newppp";
        String branchName = "branch-lyt";
        shallowCloneDepth1(localString, branchName, remoteString);
    }

    @Test
    void fetchTest() throws IOException, InterruptedException {
        String remoteString = "http://101.43.149.150/lyt/new_project.git";
        String localString = "C:\\Users\\10242\\Desktop\\newppp";
        String SHA1 = "4ac098307345b7c2209dae77b45a10aad3cfe7eb";

        fetchBySHA(localString, remoteString, SHA1);
    }

    /**
     * 多向合并时，对于非模型文件需要进行cherry-pick。
     * 当然，虽然模型文件也合并了，但之后的模型合并生成的会覆盖。
     * cherry-pick
     * @param localString
     * @param commitSHA
     * @throws IOException
     * @throws InterruptedException
     */
    public static void cherryPick(String localString, String commitSHA) throws IOException, InterruptedException {
        Path directory = Paths.get(localString);
        runCommand(directory, "git", "fetch", "--depth", "1", "origin", commitSHA);
    	runCommand(directory, "git", "cherry-pick", commitSHA);        
    }
    
    
    
    

    /**
     * 拉取远程分支
     * @param localString
     * @param branchName
     * @throws IOException
     * @throws InterruptedException
     */
    public static void pull(String localString, String branchName) throws IOException, InterruptedException {
        Path directory = Paths.get(localString);
        runCommand(directory, "git", "pull", "origin", branchName);
    }

    /**
     * 浅克隆
     * @param localString
     * @param branchName
     * @param remoteString
     * @throws IOException
     * @throws InterruptedException
     */
    public static void shallowCloneDepth1(String localString, String branchName, String remoteString) throws IOException, InterruptedException {
        Path directory = Paths.get(localString);
        runCommand(directory.getParent(), "git", "clone", "--depth", "1", "-b", branchName, remoteString, directory.getFileName().toString());
        System.out.println("clone done");
    }

    /**
     * 提交到远程库
     * 浅克隆后，无法用 JGit 的 push
     * @param localString
     * @param message
     * @param branchName
     * @throws IOException
     * @throws InterruptedException
     */
    public static void commitAndPush(String localString, String message, String branchName) throws IOException, InterruptedException {
        Path directory = Paths.get(localString);
        runCommand(directory, "git", "add", "-A");
        runCommand(directory, "git", "commit", "-m", message);
        runCommand(directory, "git", "push", "origin", branchName);
    }
    
    /**
     * push to remote specific branch
     * @param localString
     * @param branchName
     * @throws IOException
     * @throws InterruptedException
     */
    public static void push(String localString, String branchName) throws IOException, InterruptedException {
        Path directory = Paths.get(localString);
        runCommand(directory, "git", "push", "origin", branchName);
    }


    /**
     * 根据指定的 SHA 获取版本数据
     * @param localString
     * @param remoteString
     * @param SHA1
     * @throws IOException
     * @throws InterruptedException
     */
    public static void fetchBySHA(String localString, String remoteString, String SHA1) throws IOException, InterruptedException {
        Path directory = Paths.get(localString);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        runCommand(directory, "git", "init");
        runCommand(directory, "git", "remote", "add", "origin", remoteString);
        runCommand(directory, "git", "fetch", "--depth", "1", "origin", SHA1);
        runCommand(directory, "git", "checkout", "FETCH_HEAD");
    }

    public static void runCommand(Path directory, String... command) throws IOException, InterruptedException {
        Objects.requireNonNull(directory, "directory");
        if (!Files.exists(directory)) {
            throw new RuntimeException("can't run command in non-existing directory '" + directory + "'");
        }
        ProcessBuilder pb = new ProcessBuilder()
                .command(command)
                .directory(directory.toFile());
        Process p = pb.start();
        StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "");
        StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(), "OUTPUT");
        outputGobbler.start();
        errorGobbler.start();
        int exit = p.waitFor();
        errorGobbler.join();
        outputGobbler.join();
        if (exit != 0) {
            throw new AssertionError(String.format("runCommand returned %d", exit));
        }
    }

    private static class StreamGobbler extends Thread {

        private final InputStream is;
        private final String type;

        private StreamGobbler(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(type + "> " + line);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

}
