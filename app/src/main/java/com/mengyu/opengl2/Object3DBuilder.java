package com.mengyu.opengl2;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;

import com.mengyu.opengl2.WavefrontLoader.FaceMaterials;
import com.mengyu.opengl2.WavefrontLoader.Faces;
import com.mengyu.opengl2.WavefrontLoader.Material;
import com.mengyu.opengl2.WavefrontLoader.Materials;
import com.mengyu.opengl2.WavefrontLoader.Tuple3;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;


public final class Object3DBuilder {


    public interface Callback {
        void onLoadError(Exception ex);

        void onLoadComplete(Object3DData data);

        void onBuildComplete(Object3DData data);
    }

    private static float[] DEFAULT_COLOR = {1.0f, 1.0f, 0, 1.0f};

    private Object3DEntity object3DEntity;

    public Object3D getDrawer() throws IOException {

        if (object3DEntity == null) {
            object3DEntity = new Object3DEntity();
        }
        return object3DEntity;
    }

    public static Object3DData generateArrays(AssetManager assets, Object3DData obj) throws IOException {

        Faces faces = obj.getFaces(); // model faces
        FaceMaterials faceMats = obj.getFaceMats();
        Materials materials = obj.getMaterials();

        if (faces == null) {
            return obj;
        }

        final FloatBuffer vertexArrayBuffer = createNativeByteBuffer(faces.getVerticesReferencesCount() * 3 * 4).asFloatBuffer();
        obj.setVertexArrayBuffer(vertexArrayBuffer);
        obj.setDrawUsingArrays(true);

        final FloatBuffer vertexBuffer = obj.getVerts();
        final IntBuffer indexBuffer = faces.getIndexBuffer();
        for (int i = 0; i < faces.getVerticesReferencesCount(); i++) {
            vertexArrayBuffer.put(i * 3, vertexBuffer.get(indexBuffer.get(i) * 3));
            vertexArrayBuffer.put(i * 3 + 1, vertexBuffer.get(indexBuffer.get(i) * 3 + 1));
            vertexArrayBuffer.put(i * 3 + 2, vertexBuffer.get(indexBuffer.get(i) * 3 + 2));
        }

        final FloatBuffer vertexNormalsArrayBuffer = createNativeByteBuffer(faces.getSize() * 3 * 3 * 4).asFloatBuffer();
        ;
        obj.setVertexNormalsArrayBuffer(vertexNormalsArrayBuffer);
        final FloatBuffer vertexNormalsBuffer = obj.getNormals();
        if (vertexNormalsBuffer != null && vertexNormalsBuffer.capacity() > 0) {
            for (int n = 0; n < faces.facesNormIdxs.size(); n++) {
                int[] normal = faces.facesNormIdxs.get(n);
                for (int i = 0; i < normal.length; i++) {
                    vertexNormalsArrayBuffer.put(n * 9 + i * 3, vertexNormalsBuffer.get(normal[i] * 3));
                    vertexNormalsArrayBuffer.put(n * 9 + i * 3 + 1, vertexNormalsBuffer.get(normal[i] * 3 + 1));
                    vertexNormalsArrayBuffer.put(n * 9 + i * 3 + 2, vertexNormalsBuffer.get(normal[i] * 3 + 2));
                }
            }
        } else {
            final float[] v0 = new float[3], v1 = new float[3], v2 = new float[3];
            for (int i = 0; i < faces.getIndexBuffer().capacity(); i += 3) {
                try {
                    v0[0] = vertexBuffer.get(faces.getIndexBuffer().get(i) * 3);
                    v0[1] = vertexBuffer.get(faces.getIndexBuffer().get(i) * 3 + 1);
                    v0[2] = vertexBuffer.get(faces.getIndexBuffer().get(i) * 3 + 2);

                    v1[0] = vertexBuffer.get(faces.getIndexBuffer().get(i + 1) * 3);
                    v1[1] = vertexBuffer.get(faces.getIndexBuffer().get(i + 1) * 3 + 1);
                    v1[2] = vertexBuffer.get(faces.getIndexBuffer().get(i + 1) * 3 + 2);

                    v2[0] = vertexBuffer.get(faces.getIndexBuffer().get(i + 2) * 3);
                    v2[1] = vertexBuffer.get(faces.getIndexBuffer().get(i + 2) * 3 + 1);
                    v2[2] = vertexBuffer.get(faces.getIndexBuffer().get(i + 2) * 3 + 2);

                    float[] normal = Math3DUtils.calculateFaceNormal2(v0, v1, v2);

                    vertexNormalsArrayBuffer.put(i * 3, normal[0]);
                    vertexNormalsArrayBuffer.put(i * 3 + 1, normal[1]);
                    vertexNormalsArrayBuffer.put(i * 3 + 2, normal[2]);
                    vertexNormalsArrayBuffer.put(i * 3 + 3, normal[0]);
                    vertexNormalsArrayBuffer.put(i * 3 + 4, normal[1]);
                    vertexNormalsArrayBuffer.put(i * 3 + 5, normal[2]);
                    vertexNormalsArrayBuffer.put(i * 3 + 6, normal[0]);
                    vertexNormalsArrayBuffer.put(i * 3 + 7, normal[1]);
                    vertexNormalsArrayBuffer.put(i * 3 + 8, normal[2]);
                } catch (BufferOverflowException ex) {
                    throw new RuntimeException("Error calculating mormal for face [" + i / 3 + "]");
                }
            }
        }


        FloatBuffer colorArrayBuffer = null;
        if (materials != null) {
            Log.i("Object3DBuilder", "Reading materials...");
            materials.readMaterials(obj.getCurrentDir(), obj.getAssetsDir(), assets);
        }

        if (materials != null && !faceMats.isEmpty()) {
            Log.i("Object3DBuilder", "Processing face materials...");
            colorArrayBuffer = createNativeByteBuffer(4 * faces.getVerticesReferencesCount() * 4)
                    .asFloatBuffer();
            boolean anyOk = false;
            float[] currentColor = DEFAULT_COLOR;
            for (int i = 0; i < faces.getSize(); i++) {
                if (faceMats.findMaterial(i) != null) {
                    Material mat = materials.getMaterial(faceMats.findMaterial(i));
                    if (mat != null) {
                        currentColor = mat.getKdColor() != null ? mat.getKdColor() : currentColor;
                        anyOk = anyOk || mat.getKdColor() != null;
                    }
                }
                colorArrayBuffer.put(currentColor);
                colorArrayBuffer.put(currentColor);
                colorArrayBuffer.put(currentColor);
            }
            if (!anyOk) {
                Log.i("Object3DBuilder", "Using single color.");
                colorArrayBuffer = null;
            }
        }
        obj.setVertexColorsArrayBuffer(colorArrayBuffer);


        String texture = null;
        byte[] textureData = null;
        if (materials != null && !materials.materials.isEmpty()) {

            // TODO: process all textures
            for (Material mat : materials.materials.values()) {
                if (mat.getTexture() != null) {
                    texture = mat.getTexture();
                    break;
                }
            }
            if (texture != null) {
                if (obj.getCurrentDir() != null) {
                    File file = new File(obj.getCurrentDir(), texture);
                    Log.i("Object3DBuilder", "Loading texture '" + file + "'...");
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    FileInputStream fis = new FileInputStream(file);
                    IOUtils.copy(fis, bos);
                    fis.close();
                    textureData = bos.toByteArray();
                    bos.close();
                } else {
                    String assetResourceName = obj.getAssetsDir() + "/" + texture;
                    Log.i("Object3DBuilder", "Loading texture '" + assetResourceName + "'...");
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    InputStream fis = assets.open(assetResourceName);
                    IOUtils.copy(fis, bos);
                    fis.close();
                    textureData = bos.toByteArray();
                    bos.close();
                }
            } else {
                Log.i("Object3DBuilder", "Found material(s) but no texture");
            }
        } else {
            Log.i("Object3DBuilder", "No materials -> No texture");
        }


        ArrayList<Tuple3> texCoords = obj.getTexCoords();
        if (texCoords != null && texCoords.size() > 0) {

            Log.i("Object3DBuilder", "Allocating/populating texture buffer...");
            FloatBuffer textureCoordsBuffer = createNativeByteBuffer(texCoords.size() * 2 * 4).asFloatBuffer();
            for (Tuple3 texCor : texCoords) {
                textureCoordsBuffer.put(texCor.getX());
                textureCoordsBuffer.put(obj.isFlipTextCoords() ? 1 - texCor.getY() : texCor.getY());
            }

            Log.i("Object3DBuilder", "Populating texture array buffer...");
            FloatBuffer textureCoordsArraysBuffer = createNativeByteBuffer(2 * faces.getVerticesReferencesCount() * 4).asFloatBuffer();
            obj.setTextureCoordsArrayBuffer(textureCoordsArraysBuffer);

            try {
                boolean anyTextureOk = false;
                String currentTexture = null;
                int counter = 0;
                for (int i = 0; i < faces.facesTexIdxs.size(); i++) {
                    if (!faceMats.isEmpty() && faceMats.findMaterial(i) != null) {
                        Material mat = materials.getMaterial(faceMats.findMaterial(i));
                        if (mat != null && mat.getTexture() != null) {
                            currentTexture = mat.getTexture();
                        }
                    }
                    boolean textureOk = false;
                    if (currentTexture != null && currentTexture.equals(texture)) {
                        textureOk = true;
                    }
                    int[] text = faces.facesTexIdxs.get(i);
                    for (int j = 0; j < text.length; j++) {
                        if (textureData == null || textureOk) {
                            anyTextureOk = true;
                            textureCoordsArraysBuffer.put(counter++, textureCoordsBuffer.get(text[j] * 2));
                            textureCoordsArraysBuffer.put(counter++, textureCoordsBuffer.get(text[j] * 2 + 1));
                        } else {
                            textureCoordsArraysBuffer.put(counter++, 0f);
                            textureCoordsArraysBuffer.put(counter++, 0f);
                        }
                    }
                }

                if (!anyTextureOk) {
                    Log.i("Object3DBuilder", "Texture is wrong. Applying global texture");
                    counter = 0;
                    for (int j = 0; j < faces.facesTexIdxs.size(); j++) {
                        int[] text = faces.facesTexIdxs.get(j);
                        for (int i = 0; i < text.length; i++) {
                            textureCoordsArraysBuffer.put(counter++, textureCoordsBuffer.get(text[i] * 2));
                            textureCoordsArraysBuffer.put(counter++, textureCoordsBuffer.get(text[i] * 2 + 1));
                        }
                    }
                }
            } catch (Exception ex) {
                Log.e("Object3DBuilder", "Failure to load texture coordinates", ex);
            }
        }
        obj.setTextureData(textureData);

        return obj;
    }

    private static ByteBuffer createNativeByteBuffer(int length) {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(length);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        return bb;
    }

    public static void loadV6AsyncParallel(final Activity parent, final File file, final String assetsDir, final String assetName,
                                           final Callback callback) {
        final String modelId = file != null ? file.getName() : assetName;
        final File currentDir = file != null ? file.getParentFile() : null;
        if (modelId.toLowerCase().endsWith(".obj")) {
            WavefrontLoader2.loadAsync(parent, currentDir, assetsDir, modelId, callback);
        }
    }
}

