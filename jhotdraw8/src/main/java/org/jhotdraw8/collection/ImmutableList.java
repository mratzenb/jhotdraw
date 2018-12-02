package org.jhotdraw8.collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classes with this interface guarantee that the content of the list is immutable.
 *
 * @param <E> the element type
 */
public interface ImmutableList<E> extends ReadOnlyList<E>, ImmutableCollection<E> {

    @Nonnull
    public static <T> ImmutableList<T> add(@Nullable ReadOnlyCollection<T> collection, T item) {
        if (collection == null || collection.isEmpty()) {
            return ImmutableList.of(item);
        }
        Object[] a = new Object[collection.size() + 1];
        a = collection.toArray(a);
        a[a.length - 1] = item;
        return new ImmutableArrayList<>(true, a);
    }

    @Nonnull
    public static <T> ImmutableList<T> add(@Nullable Collection<T> collection, T item) {
        if (collection == null || collection.isEmpty()) {
            return ImmutableList.of(item);
        }
        Object[] a = new Object[collection.size() + 1];
        a = collection.toArray(a);
        a[a.length - 1] = item;
        return new ImmutableArrayList<>(true, a);
    }

    @Nonnull
    public static <T> ImmutableList<T> add(@Nullable Collection<T> collection, int index, T item) {
        if (collection == null || collection.isEmpty() && index == 0) {
            return ImmutableList.of(item);
        }
        Object[] a = new Object[collection.size()];
        a = collection.toArray(a);
        Object[] b = new Object[a.length + 1];
        System.arraycopy(a, 0, b, 0, index);
        System.arraycopy(a, index, b, index + 1, a.length - index);
        b[index] = item;
        return new ImmutableArrayList<>(true, b);
    }

    @Nonnull
    public static <T> ImmutableList<T> add(@Nullable ReadOnlyCollection<T> collection, int index, T item) {
        if (collection == null || collection.isEmpty() && index == 0) {
            return ImmutableList.of(item);
        }
        Object[] a = new Object[collection.size()];
        a = collection.toArray(a);
        Object[] b = new Object[a.length + 1];
        System.arraycopy(a, 0, b, 0, index);
        System.arraycopy(a, index, b, index + 1, a.length - index);
        b[index] = item;
        return new ImmutableArrayList<>(true, b);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> emptyList() {
        return (ImmutableArrayList<T>) ImmutableArrayList.EMPTY;
    }

    @Nonnull
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> ImmutableList<T> of(T... items) {
        // FIXME we should copy the items array, because caller might keep reference on mutable items array
        return items.length == 0 ? emptyList() : new ImmutableArrayList<>(true, items);
    }

    @Nonnull
    public static <T> ImmutableList<T> ofCollection(Collection<? extends T> collection) {
        return collection.isEmpty() ? emptyList() : new ImmutableArrayList<T>(collection);
    }

    @Nonnull
    public static <T> ImmutableList<T> ofCollection(ReadOnlyCollection<? extends T> collection) {
        if (collection instanceof ImmutableList) {
            //noinspection unchecked
            return (ImmutableList<T>) collection;
        }
        return collection.isEmpty() ? emptyList() : new ImmutableArrayList<T>(collection);
    }

    @Nonnull
    public static <T> ImmutableList<T> ofArray(@Nonnull Object[] a, int offset, int length) {
        return length == 0 ? emptyList() : new ImmutableArrayList<>(a, offset, length);
    }

    @Nonnull
    public static <T> ImmutableList<T> remove(@Nullable ReadOnlyCollection<T> collection, int index) {
        if (collection == null || collection.size() == 1 && index == 0) {
            return emptyList();
        }
        Object[] a = new Object[collection.size()];
        a = collection.toArray(a);
        Object[] b = new Object[a.length - 1];
        System.arraycopy(a, 0, b, 0, index);
        System.arraycopy(a, index + 1, b, index, b.length - index);
        return new ImmutableArrayList<>(true, b);
    }

    @Nonnull
    public static <T> ImmutableList<T> remove(@Nullable Collection<T> collection, int index) {
        if (collection == null || collection.size() == 1 && index == 0) {
            return emptyList();
        }
        Object[] a = new Object[collection.size()];
        a = collection.toArray(a);
        Object[] b = new Object[a.length - 1];
        System.arraycopy(a, 0, b, 0, index);
        System.arraycopy(a, index + 1, b, index, b.length - index);
        return new ImmutableArrayList<>(true, b);
    }

    @Nonnull
    public static <T> ImmutableList<T> remove(@Nullable Collection<T> collection, T item) {
        if (collection == null || collection.size() == 1 && collection.contains(item)) {
            return emptyList();
        }
        if (collection instanceof List) {
            @SuppressWarnings("unchecked")
            List<T> list = (List) collection;
            return remove(list, list.indexOf(item));
        } else {
            List<T> a = new ArrayList<>(collection);// linear
            a.remove(item);// linear
            return new ImmutableArrayList<>(a);// linear
        }
    }

    public static <T> ImmutableList<T> set(ReadOnlyCollection<T> collection, int index, T item) {
        Object[] a = new Object[collection.size()];
        a = collection.toArray(a);
        a[index] = item;
        return new ImmutableArrayList<>(true, a);
    }

    public static <T> ImmutableList<T> set(Collection<T> collection, int index, T item) {
        Object[] a = new Object[collection.size()];
        a = collection.toArray(a);
        a[index] = item;
        return new ImmutableArrayList<>(true, a);
    }
    public static <E> ImmutableList<E> removeAll(ReadOnlyCollection<E> list, Collection<? extends E> collection) {
        int n = list.size();
        Object[] a = new Object[n];
        int j = 0;
        for (E e : list) {
            if (!collection.contains(e)) {
                a[j++] = e;
            }
        }

        return new ImmutableArrayList<E>(a, 0, j);
    }
}
