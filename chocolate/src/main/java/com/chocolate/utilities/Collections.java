package com.chocolate.utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

@SuppressWarnings({"RedundantSuppression", "WeakerAccess", "unused"})
public final class Collections extends UtilityClass {

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
     * @param first First Array
     * @param second Second Array
     * @param <Type> Type of the Array's contents
     */
    public static <Type> Type[] concatenate(Type[] first, Type[] second) {
        int firstLength = first.length;
        int secondLength = second.length;

        @SuppressWarnings("unchecked")
        Type[] result = (Type[]) Array.newInstance(Objects.requireNonNull(first.getClass().getComponentType()), firstLength + secondLength);
        System.arraycopy(first, 0, result, 0, firstLength);
        System.arraycopy(second, 0, result, firstLength, secondLength);

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

        public interface Map<Type, Result> { Result applyTo(Type object); }

        public interface Reduce<Type, Result> { Result reduce(Result currentValue, Type nextElement); }

        public interface Filter<Type> { boolean shouldKeep(Type object); }

        public interface Boolean<Type> { boolean value(Type object); }

        public interface Stringify<Type> { String process(Type object, String currentValue, boolean isFirst, boolean isLast); }

    }

}
