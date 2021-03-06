package CompanyOriented.Pinterest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/*
Given getFiles(Directory dir), getDirectories(Directory dir) APIs - a directory may contain files and directories,
return all files with the same content.

 */
/*
We can get all files recursively, and compare content on the fly with the help of map <hash of file content,
java.io.File.hashCode()
The method returns a hash code  for this abstract pathname.
 */
public class CompareFiles {

    public static void main(String[] args) {

        File file1 = new File("file1");
        file1.content = "I am looking forward to it.";

        File file2 = new File("file2");
        file2.content = "I am Xinrong.";

        File file3 = new File("file3");
        file3.content = "I am looking forward to it";

        Directory dir = new Directory();
        dir.fileList.add(file1);

        Directory dirInner = new Directory();
        dirInner.fileList.add(file2);
        dirInner.fileList.add(file3);
        dir.directoryList.add(dirInner);

        List<List<File>> resultList = getSameFiles(dir);
        for (List<File> files : resultList) {
            for (File file : files) {
                System.out.print(file.fileName + ", ");
            }
            System.out.println();
        }
    }


    public static List<List<File>> getSameFiles(Directory rootDirectory) {
        Map<String, List<File>> map = new HashMap<>(); // Key: hashcode of content (generated by Message Digest Algorithm MD5), value: list of files with the same hashcode.
        getSameFilesRecur(rootDirectory, map);
        return new ArrayList<>(map.values());
    }

    private static void getSameFilesRecur(Directory dir, Map<String, List<File>> map) {
        List<File> files = getFiles(dir);
        for (File file : files) {
            String code = getMD5HashCode(file);
            map.putIfAbsent(code, new ArrayList<>());
            map.get(code).add(file);
        }
        List<Directory> directories = getDirectories(dir);
        for (Directory directory : directories) {
            getSameFilesRecur(directory, map);
        }
    }

    private static String getMD5HashCode(File file) {
        byte[] digestBytes = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(file.content.getBytes());
            digestBytes = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digestBytes != null ? new String(digestBytes) : "";
    }

    static class File {
        String fileName;
        String content;

        public File(String fileName) {
            this.fileName = fileName;
        }
    }

    static class Directory {
        List<File> fileList;
        List<Directory> directoryList;

        public Directory() {
            fileList = new LinkedList<>();
            directoryList = new LinkedList<>();
        }
    }

    public static List<File> getFiles(Directory dir) {
        return dir.fileList;
    }

    public static List<Directory> getDirectories(Directory dir) {
        return dir.directoryList;
    }
}
