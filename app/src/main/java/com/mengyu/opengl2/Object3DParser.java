package com.mengyu.opengl2;


/**
 * 封装后的3D模型解析工具
 */
public class Object3DParser {

    protected ModelActivity parent;
    private Object3DData object;

    public Object3DParser(ModelActivity main) {
        this.parent = main;
    }

    public void parse3D() {
        // Load object
        if (parent.getParamFile() != null || parent.getParamAssetDir() != null) {
            Object3DBuilder builder = new Object3DBuilder();
            builder.paseModel(parent, parent.getParamFile(), parent.getParamAssetDir(), parent.getParamAssetFilename(), new Object3DBuilder.Callback() {
                @Override
                public void onLoadStart() {
                    //解析开始

                }

                @Override
                public void onLoadComplete(Object3DData data) {
                    //解析完成
                    addObject(data);
                }

            });
        }
    }

    protected void addObject(Object3DData obj) {
        this.object = obj;
        requestRender();//请求重新绘制
    }

    private void requestRender() {
        parent.getgLView().requestRender();
    }

    public Object3DData getObjects() {
        return object;
    }

    private void startLoading(){

    }

    private void endLoading(){

    }


}
