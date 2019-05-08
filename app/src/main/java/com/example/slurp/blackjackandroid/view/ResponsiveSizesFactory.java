package com.example.slurp.blackjackandroid.view;

// singleton
public class ResponsiveSizesFactory {
    private static ResponsiveSizesFactory instance;

    public static synchronized ResponsiveSizesFactory getInstance(){
        if(instance == null)
            instance = new ResponsiveSizesFactory();
        return instance;
    }

    public ResponsiveSizes createResponsiveSizes(float devicePixelDensityScale){

        ResponsiveSizes responsiveSizes = null;

        if(devicePixelDensityScale >= 0.75) // ldpi

        if(devicePixelDensityScale >= 1.0) // mdpi : baseline


        if(devicePixelDensityScale >= 1.5) // hdpi base
            responsiveSizes = new HdpiSizes();

        if(devicePixelDensityScale >= 2.0) // xhdpi base
            responsiveSizes = new XhdipSizes();

        if(devicePixelDensityScale >= 3.0) // xxhdpi base
            responsiveSizes = new XxhdpiSizes();

        return responsiveSizes;
    }
}
