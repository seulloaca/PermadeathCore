package com.permadeathcore.NMS;

import com.permadeathcore.Main;
import com.permadeathcore.NMS.VersionManager;
import com.permadeathcore.NMS.Versions.ClassFinder.*;

public class NMSFinder {

    private Main instance;

    public NMSFinder(Main instance) {

        this.instance = instance;
    }

    public Object getNMSHandler() {

        if (VersionManager.isRunning14()) {

            return new ClassFinder_1_14_R1().findNmsHandler();
        }

        if (VersionManager.isRunning15()) {

            return new ClassFinder_1_15_R1().findNmsHandler();
        }

        if (VersionManager.getVersion().equalsIgnoreCase("1_16_R1")) {

            return new ClassFinder_1_16_R1().findNmsHandler();
        }

        if (VersionManager.getVersion().equalsIgnoreCase("1_16_R2")) {

            return new ClassFinder_1_16_R2().findNmsHandler();
        }

        if (VersionManager.getVersion().equalsIgnoreCase("1_16_R3")) {
            return new ClassFinder_1_16_R3().findNmsHandler();
        }

        return null;
    }

    public Object getNMSAccesor() {

        if (VersionManager.isRunning14()) {
            return new ClassFinder_1_14_R1().findNmsAccesor();
        }

        if (VersionManager.isRunning15()) {

            return new ClassFinder_1_15_R1().findNmsAccesor();
        }

        if (VersionManager.getVersion().equalsIgnoreCase("1_16_R1")) {

            return new ClassFinder_1_16_R1().findNmsAccesor();
        }

        if (VersionManager.getVersion().equalsIgnoreCase("1_16_R2")) {

            return new ClassFinder_1_16_R2().findNmsAccesor();
        }

        if (VersionManager.getVersion().equalsIgnoreCase("1_16_R3")) {
            return new ClassFinder_1_16_R3().findNmsAccesor();
        }

        return null;
    }

    public Object getCustomBlock() {

        if (VersionManager.isRunning14()) {

            return new ClassFinder_1_14_R1().findCustomBlock();
        }

        if (VersionManager.isRunning15()) {

            return new ClassFinder_1_15_R1().findCustomBlock();
        }

        if (VersionManager.getVersion().equalsIgnoreCase("1_16_R1")) {

            return new ClassFinder_1_16_R1().findCustomBlock();
        }

        if (VersionManager.getVersion().equalsIgnoreCase("1_16_R2")) {
            return new ClassFinder_1_16_R2().findCustomBlock();
        }

        if (VersionManager.getVersion().equalsIgnoreCase("1_16_R3")) {

            return new ClassFinder_1_16_R3().findCustomBlock();
        }

        return null;
    }
}
