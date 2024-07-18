package top.srcrs.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;

/**
 * 扫描task包下的所有的class文件
 * @author srcrs
 * @Time 2020-10-13
 */
@Slf4j
public abstract class PackageScanner {

    /**
     * 将 . 路径换为 / 路径
     * @param packageName 包名
     * @author srcrs
     * @Time 2020-10-13
     */
    public void scannerPackage(String packageName) {
        String packagePath = packageName.replace('.', '/');
        /* 真正的路径中是以 / 或 \ 相隔，此路径以 \ 相隔 */
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            /* 得到绝对路径 */
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                File root = new File(url.toURI());
                /* 将url转化成uri，以此创建File对象，后面解析就变成了对File的处理 */
                scannerDirectory(root, packageName);
            }
        } catch (Exception e) {
            log.error("💔扫包错误 : ", e);
        }
    }

    /**
     * 递归搜索target路径下所有的class文件
     * @param currentFile java文件生成的class文件路径
     * @param packageName 包名
     * @author srcrs
     * @Time 2020-10-13
     */
    private void scannerDirectory(File currentFile, String packageName) {
        if (!currentFile.isDirectory()) {
            return;
        }
        File[] files = currentFile.listFiles();
        /* 得到File对象类型的【完整路径】的数组 */
        for (File file : files != null ? files : new File[0]) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String fileName = file.getName().replace(".class", "");
                /* 去除掉后缀 .class */
                String className = packageName + "." + fileName;
                /* 得到带包名的类，为取得元类对象做准备 */
                dealClass(className);
                /* 将得到的元类对象通过抽象方法参数传递给用户，以便用户后续操作。 */
            } else if (file.isDirectory()) {
                scannerDirectory(file, packageName + "." + file.getName());
                /* 此处采用递归，只要是目录就继续往下一层遍历，直到file.isFile()为true,且以.class结尾 */
            }
        }
    }

    /**
     * 获得真实的className
     * @param className className
     */
    public abstract void dealClass(String className);
}
