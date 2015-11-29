package com.skov.timeRegForrest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by alsk on 26-11-2015.
 */
public class IdioticDesktShortcutsRemover {

    public static void main(String[] args) {
        doDelete();
    }

    static void doDelete() {
        delFile("\\\\deltedata\\userdata\\alsk\\SET-alsk\\Desktop\\WinSCP.lnk");
        delFile("\\\\deltedata\\userdata\\alsk\\SET-alsk\\Desktop\\VLC media player.lnk");
        delFile("\\\\deltedata\\userdata\\alsk\\SET-alsk\\Desktop\\VitalSource Bookshelf.lnk");
        delFile("\\\\deltedata\\userdata\\alsk\\SET-alsk\\Desktop\\Salamander 2.5 RC3.lnk");
        delFile("\\\\deltedata\\userdata\\alsk\\SET-alsk\\Desktop\\Google Chrome.lnk");
        delFile("\\\\deltedata\\userdata\\alsk\\SET-alsk\\Desktop\\Altova XMLSpy 2015.lnk");

    }

    static void delFile(String path) {
        try {
            System.out.println("exists path: " + new File(path).exists() + " - " + path);
            Files.delete(Paths.get(path));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
