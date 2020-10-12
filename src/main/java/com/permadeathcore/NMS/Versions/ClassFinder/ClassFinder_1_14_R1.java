package com.permadeathcore.NMS.Versions.ClassFinder;

import com.permadeathcore.NMS.ClassFinder;
import com.permadeathcore.NMS.Versions.NMSAccesor.NMSAccesor_1_14_R1;
import com.permadeathcore.NMS.Versions.NMSHandler.NMSHandler_1_14_R1;
import com.permadeathcore.TheBeginning.Block.CustomBlock_1_14_R1;

public class ClassFinder_1_14_R1 implements ClassFinder {
    @Override
    public Object findNmsHandler() {
        return new NMSHandler_1_14_R1();
    }

    @Override
    public Object findNmsAccesor() {
        return new NMSAccesor_1_14_R1();
    }

    @Override
    public Object findCustomBlock() {
        return new CustomBlock_1_14_R1();
    }
}
