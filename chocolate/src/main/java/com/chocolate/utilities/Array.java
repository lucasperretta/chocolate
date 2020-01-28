package com.chocolate.utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

@SuppressWarnings("unused")
public class Array<Type> extends ArrayList<Type> {

    // Constructors.....
    @SafeVarargs public Array(int initialCapacity, Type... objects) {
        super(initialCapacity);
        this.addAll(Arrays.asList(objects));
    }

    @SafeVarargs public Array(Type... objects) {
        super();
        this.addAll(Arrays.asList(objects));
    }

    public Array(@NonNull Collection<? extends Type> c) {
        super(c);
    }

    // Methods.....
    @Nullable public Type first() {
        if (size() == 0) return null;
        return get(0);
    }

    @Nullable public Type last() {
        if (size() == 0) return null;
        return get(size()-1);
    }

    @Nullable public Type getNextTo(Type object) {
        int index = indexOfNext(object);
        return index == -1 ? null : get(index);
    }

    @SuppressWarnings("WeakerAccess") public int indexOfNext(Type object) {
        int index = indexOf(object) + 1;
        return index == size() ? -1 : index;
    }

    @Nullable public Type getPreviousTo(Type object) {
        int index = indexOfPrevious(object);
        return index == -1 ? null : get(index);
    }

    @SuppressWarnings("WeakerAccess") public int indexOfPrevious(Type object) {
        int index = indexOf(object) - 1;
        return index < 0 ? -1 : index;
    }

    // Collections Method Extensions.....
    public void foreach(@NotNull Collections.Predicate.Foreach<Type> predicate) {
        Collections.foreach(this, predicate);
    }

    public <Result, Return extends Collection<Result>> Return map(@NonNull Return initial, @NonNull Collections.Predicate.Map<Type, Result> predicate) {
        return Collections.map(this, initial, predicate);
    }

    public <NewCollection> NewCollection map(@NonNull NewCollection newCollection, @NonNull Collections.Predicate.ManualMap<Type, NewCollection> predicate) {
        return Collections.map(this, newCollection, predicate);
    }

    public <Key> HashMap<Key, Type> autoMap(@NonNull Collections.Predicate.AutoMap<Key, Type> predicate) {
        return Collections.autoMap(this, predicate);
    }

    public Array<Type> filter(@NonNull Collections.Predicate.Filter<Type> predicate) {
        return Collections.filter(this, predicate);
    }

    public <Result> Result reduce(Result initialValue, @NonNull Collections.Predicate.Reduce<Type, Result> predicate) {
        return Collections.reduce(this, initialValue, predicate);
    }

    public Array<Type> flatten() {
        return Collections.flatten(this);
    }

    public <Result, Return extends Array<Result>> Return flatMap(@NonNull Return initial, @NonNull Collections.Predicate.Map<Type, Result> predicate) {
        return Collections.flatMap(this, initial, predicate);
    }

    public boolean some(@NonNull Collections.Predicate.Boolean<Type> predicate) {
        return Collections.some(this, predicate);
    }

    public boolean every(@NonNull Collections.Predicate.Boolean<Type> predicate) {
        return Collections.every(this, predicate);
    }

    @Nullable public Type find(@NonNull Collections.Predicate.Boolean<Type> predicate) {
        return Collections.find(this, predicate);
    }

    @Nullable public Collections.Found<Type> findIndexed(@NonNull Collections.Predicate.Boolean<Type> predicate) {
        return Collections.findIndexed(this, predicate);
    }

    public String stringify(@NonNull Collections.Predicate.Stringify<Type> predicate) {
        return Collections.stringify(this, predicate);
    }

    public String stringify() {
        return Collections.stringify(this);
    }

}
