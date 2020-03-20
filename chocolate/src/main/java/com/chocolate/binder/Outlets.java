package com.chocolate.binder;

import android.app.Activity;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.chocolate.utilities.Array;
import com.chocolate.utilities.UtilityClass;
import com.chocolate.utilities.Utils;

import java.lang.reflect.Field;
import java.util.Objects;

@SuppressWarnings({"WeakerAccess", "unused"}) public final class Outlets extends UtilityClass {

    // Methods.....
    public static void bind(@NonNull Activity activity) {
        bind(activity, activity.getWindow().getDecorView());
    }

    public static void bind(@NonNull Object object, @NonNull View view) {
        Field[] fields = object.getClass().getDeclaredFields();
        if (fields.length == 0) return;
        Class rClass = null;
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Outlet.class)) continue;
            @IdRes int id = Objects.requireNonNull(field.getAnnotation(Outlet.class)).id();
            if (id != Integer.MIN_VALUE) {
                setValue(field, object, view, id);
                continue;
            }
            if (rClass == null) rClass = findRIdsClass(object);
            try {
                id = (int) rClass.getField(field.getName()).get(object);
                setValue(field, object, view, id);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull private static Class findRIdsClass(@NonNull Object object) {
        if (object instanceof IdClassProvider) return ((IdClassProvider) object).getIdClass();
        String packageName = Objects.requireNonNull(object.getClass().getPackage()).getName();
        Array<String> array = new Array<>(packageName.split("\\."));
        while (!array.isEmpty()) {
            StringBuilder rPackage = new StringBuilder();
            for (String component : array) {
                rPackage.append(component).append(".");
            }
            rPackage.append("R$id");
            Class rClass = Utils.java.getClassByName(rPackage.toString());
            if (rClass != null) return rClass;
            array.remove(array.size()-1);
        }
        throw new RuntimeException("Could not find R class for package " + Objects.requireNonNull(object.getClass().getPackage()).getName());
    }

    private static void setValue(@NonNull Field field, @NonNull Object object, @NonNull View view, @IdRes int id) {
        try {
            field.set(object, view.findViewById(id));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // Interfaces.....
    public interface IdClassProvider {
        Class getIdClass();
    }

}
