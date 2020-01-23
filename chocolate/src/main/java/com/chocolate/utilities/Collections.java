package com.chocolate.utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

@SuppressWarnings({"RedundantSuppression", "WeakerAccess", "unused"})
public final class Collections extends UtilityClass {

    /**
     * Calls a provided function on every element of the collection providing it with the corresponding index and object
     * @param collection The collection in which to iterate over
     * @param predicate Function to execute on every iteration
     * @param <Type> Type of the collection objects
     */
    public static <Type> void foreach(@NotNull Collection<Type> collection, @NotNull Predicate.Foreach<Type> predicate) {
        int index = 0;
        for (Type type : collection) {
            try {
                predicate.iteration(index, type);
            } catch (Predicate.Foreach.BreakException breakException) {
                break;
            }
            index++;
        }
    }

    /**
     * Calls a provided function on every element of the collection and returns a new collection with the results
     * @param collection The collection in which to iterate over
     * @param predicate Function to execute on every iteration
     * @param <Type> Type of the collection objects
     */
    public static <Type, Result, Return extends Collection<Result>> Return map(@NonNull Collection<Type> collection, @NonNull Return initial, @NonNull Predicate.Map<Type, Result> predicate) {
        for (Type object : collection) initial.add(predicate.applyTo(object));
        return initial;
    }

    /**
     * Calls a provided function on every element of the collection expecting the user to populate a new collection in every iteration
     * @param collection The collection in which to iterate over
     * @param predicate Function to execute on every iteration
     * @param <Type> Type of the collection objects
     */
    public static <Type, NewCollection> NewCollection map(@NonNull Collection<Type> collection, @NonNull NewCollection newCollection, @NonNull Predicate.ManualMap<Type, NewCollection> predicate) {
        for (Type object : collection) predicate.applyTo(object, newCollection);
        return newCollection;
    }

    /**
     * Performs a Map operation only asking for the corresponding Key for each Value
     * @param collection The collection in which to iterate over
     * @param predicate Function to execute on every iteration
     * @param <Key> Type of the expected map's key
     * @param <Value> Type of the collection objects
     */
    public static <Key, Value> HashMap<Key, Value> autoMap(@NonNull Collection<Value> collection, @NonNull Predicate.AutoMap<Key, Value> predicate) {
        return map(collection, new HashMap<>(), (value, map) -> map.put(predicate.applyTo(value), value));
    }

    /**
     * Calls a provided function with every element of the collection and if it returns true keeps it and if it returns false it removes it
     * @param collection The collection in which to iterate over
     * @param predicate Function to execute on every iteration to determine if an objects stays or leaves
     * @param <Type> Type of the collection objects
     */
    public static <Type, Return extends Collection<Type>> Return filter(@NonNull Return collection, @NonNull Predicate.Filter<Type> predicate) {
        Iterator<Type> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!predicate.shouldKeep(iterator.next())) iterator.remove();
        }
        return collection;
    }

    /**
     * Apply a function against an accumulator and each value of the collection (from left-to-right) as to reduce it to a single value
     * @param collection The collection in which to iterate over
     * @param initialValue The initial value to calculate the result with provided by the caller
     * @param predicate Function to execute on every iteration
     * @param <Type> Type of the collection objects
     * @param <Result> Type of the desired result
     */
    public static <Type, Result> Result reduce(@NonNull Collection<Type> collection, Result initialValue, @NonNull Predicate.Reduce<Type, Result> predicate) {
        for (Type object : collection) {
            initialValue = predicate.reduce(initialValue, object);
        }
        return initialValue;
    }

    /**
     * Removes every null pointer inside the provided collection
     * @param collection The collection in which to iterate over
     * @param <Type> Type of the collection objects
     */
    public static <Type, Return extends Collection<Type>> Return flatten(@NonNull Return collection) {
        Iterator<Type> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null) iterator.remove();
        }
        return collection;
    }

    /**
     * Calls a provided function on every element of the collection and returns a new collection with the results filtering out the null values
     * @param collection The collection in which to iterate over
     * @param predicate Function to execute on every iteration
     * @param <Type> Type of the collection objects
     */
    public static <Type, Result, Return extends Collection<Result>> Return flatMap(@NonNull Collection<Type> collection, @NonNull Return initial, @NonNull Predicate.Map<Type, Result> predicate) {
        for (Type object : collection) {
            Result result = predicate.applyTo(object);
            if (result != null) initial.add(result);
        }
        return initial;
    }

    /**
     * Returns true if at least one element in the collection satisfies the provided testing function
     * @param collection The collection in which to iterate over
     * @param predicate Function to execute on every iteration
     * @param <Type> Type of the collection objects
     */
    public static <Type> boolean some(@NonNull Collection<Type> collection, @NonNull Predicate.Boolean<Type> predicate) {
        for (Type object : collection) {
            if (predicate.value(object)) return true;
        }
        return false;
    }

    /**
     * Returns true if every element in the collection satisfies the provided testing function
     * @param collection The collection in which to iterate over
     * @param predicate Function to execute on every iteration
     * @param <Type> Type of the collection objects
     */
    public static <Type> boolean every(@NonNull Collection<Type> collection, @NonNull Predicate.Boolean<Type> predicate) {
        for (Type object : collection) {
            if (!predicate.value(object)) return false;
        }
        return true;
    }

    /**
     * Finds the first element inside a collection that matches the test. If not found returns null
     * @param collection The collection in which to iterate over
     * @param predicate Function to execute on every iteration
     * @param <Type> Type of the collection objects
     */
    @Nullable public static <Type, CollectionType extends Collection<Type>> Type find(@NonNull CollectionType collection, @NonNull Predicate.Boolean<Type> predicate) {
        Found<Type> found = findIndexed(collection, predicate);
        if (found != null) return found.value;
        return null;
    }

    /**
     * Finds the first element inside a collection that matches the test inside an (index, value) object. If not found returns null
     * @param collection The collection in which to iterate over
     * @param predicate Function to execute on every iteration
     * @param <Type> Type of the collection objects
     */
    @Nullable public static <Type, CollectionType extends Collection<Type>> Found<Type> findIndexed(@NonNull CollectionType collection, @NonNull Predicate.Boolean<Type> predicate) {
        int i = 0;
        for (Type object : collection) {
            if (predicate.value(object)) return new Found<>(i, object);
            i++;
        }
        return null;
    }

    /**
     * Returns the compilation of the strings provided by the predicate
     * @param collection The collection in which to iterate over
     * @param predicate Function to execute on every iteration
     * @param <Type> Type of the collection objects
     */
    public static <Type, CollectionType extends Collection<Type>> String stringify(@NonNull CollectionType collection, @NonNull Predicate.Stringify<Type> predicate) {
        String value = "";
        boolean isFirst = true;
        Iterator<Type> iterator = collection.iterator();
        while (iterator.hasNext()) {
            value += predicate.process(iterator.next(), value, isFirst, !iterator.hasNext());
            isFirst = false;
        }
        return value;
    }

    /**
     * Returns the string representation of a collection with a predefined format, using the .toString() method
     * @param collection The collection in which to iterate over
     * @param <Type> Type of the collection objects
     */
    public static <Type, CollectionType extends Collection<Type>> String stringify(@NonNull CollectionType collection) {
        return collection.isEmpty() ? "[]" : stringify(collection, (object, currentValue, isFirst, isLast) -> (isFirst ? "[" : "") + object.toString() + (isLast ? "]" :  ", "));
    }

    /**
     * Returns the concatenation between two arrays
     * @param arrays The arrays to concatenate
     * @param <Type> Type of the Array's contents
     */
    @SafeVarargs public static <Type> Type[] concatenate(Type[]... arrays) {
        if (arrays.length == 0) throw new IllegalStateException("Must provide at least one array to concatenate");

        int length = 0;
        for (Type[] array : arrays) {
            length += array.length;
        }

        @SuppressWarnings("unchecked")
        Type[] result = (Type[]) Array.newInstance(Objects.requireNonNull(arrays[0].getClass().getComponentType()), length);

        int position = 0;
        for (Type[] array : arrays) {
            System.arraycopy(array, 0, result, position, array.length);
            position += array.length;
        }

        return result;
    }

    // Classes.....
    public static final class Found<Type> {

        @SuppressWarnings("FieldCanBeLocal") private final int index;
        private final Type value;

        private Found(int index, Type value) {
            this.index = index;
            this.value = value;
        }

    }

    public static final class Predicate extends UtilityClass {

        public interface Foreach<Type> {

            // Methods.....
            @SuppressWarnings("RedundantThrows") void iteration(int index, Type object) throws BreakException;
            default void _break() throws BreakException { throw new BreakException(); }

            // Classes.....
            class BreakException extends Exception {}

        }

        public interface Map<Type, Result> { Result applyTo(Type object); }

        public interface ManualMap<Type, NewCollection> { void applyTo(Type object, NewCollection newCollection); }

        public interface AutoMap<Key, Value> { Key applyTo(Value value); }

        public interface Reduce<Type, Result> { Result reduce(Result currentValue, Type nextElement); }

        public interface Filter<Type> { boolean shouldKeep(Type object); }

        public interface Boolean<Type> { boolean value(Type object); }

        public interface Stringify<Type> { String process(Type object, String currentValue, boolean isFirst, boolean isLast); }

    }

}
