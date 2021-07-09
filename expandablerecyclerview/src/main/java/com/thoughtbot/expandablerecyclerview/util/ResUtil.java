package com.thoughtbot.expandablerecyclerview.util;

import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.RawFileEntry;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import java.io.IOException;
import java.util.Optional;

/**
 * A class to get different types of values.
 */
public class ResUtil {
    private ResUtil() {
    }

    /**
     * get the path from id.
     *
     * @param context the context
     * @param id the id
     * @return the path from id
     */
    public static String getPathById(Context context, int id) {
        String path = "";
        if (context == null) {
            return path;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            return path;
        }
        try {
            path = manager.getMediaPath(id);
        } catch (IOException | NotExistException | WrongTypeException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * get the color.
     *
     * @param context the context
     * @param id the id
     * @return the color
     */
    public static int getColor(Context context, int id) {
        int result = 0;
        if (context == null) {
            return result;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            return result;
        }
        try {
            result = manager.getElement(id).getColor();
        } catch (IOException | NotExistException | WrongTypeException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get the dimen value.
     *
     * @param context the context
     * @param id the id
     * @return get the float dimen value
     */
    public static float getDimen(Context context, int id) {
        float result = 0;
        if (context == null) {
            return result;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            return result;
        }
        try {
            result = manager.getElement(id).getFloat();
        } catch (IOException | NotExistException | WrongTypeException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get the dimen value.
     *
     * @param context the context
     * @param id the id
     * @return get the int dimen value
     */
    public static int getIntDimen(Context context, int id) {
        float value = getDimen(context, id);
        return (int) (value >= 0 ? value + 0.5f : value - 0.5f);
    }

    /**
     * get string.
     *
     * @param context the context
     * @param id the string id
     * @return string of the given id
     */
    public static String getString(Context context, int id) {
        String result = "";
        if (context == null) {
            return result;
        }
        ResourceManager manager = context.getResourceManager();
        if (manager == null) {
            return result;
        }
        try {
            result = manager.getElement(id).getString();
        } catch (IOException | NotExistException | WrongTypeException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get the pixel map.
     *
     * @param context the context
     * @param id the id
     * @return the pixel map
     */
    public static Optional<PixelMap> getPixelMap(Context context, int id) {
        String path = getPathById(context, id);
        if (context == null || TextUtils.isEmpty(path)) {
            return Optional.empty();
        }
        RawFileEntry assetManager = context.getResourceManager().getRawFileEntry(path);
        ImageSource.SourceOptions options = new ImageSource.SourceOptions();
        options.formatHint = "image/png";
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        try {
            Resource asset = assetManager.openRawFile();
            ImageSource source = ImageSource.create(asset, options);
            return Optional.ofNullable(source.createPixelmap(decodingOptions));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
