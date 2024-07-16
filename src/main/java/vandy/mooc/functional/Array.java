package vandy.mooc.functional;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@SuppressWarnings({"unchecked", "JavadocBlankLines", "JavadocDeclaration", "SuspiciousSystemArraycopy"})
public class Array<E>
        implements Iterable<E> {
    // ... (existing code remains unchanged)

    public Array(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        this.mElementData = new Object[initialCapacity];
    }

    public Array(Collection<? extends E> c) {
        Object[] a = c.toArray();
        if ((mSize = a.length) != 0) {
            mElementData = Arrays.copyOf(a, mSize, Object[].class);
        } else {
            mElementData = EMPTY_ELEMENTDATA;
        }
    }

    public boolean isEmpty() {
        return mSize == 0;
    }

    public int size() {
        return mSize;
    }

    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < mSize; i++)
                if (mElementData[i] == null)
                    return i;
        } else {
            for (int i = 0; i < mSize; i++)
                if (o.equals(mElementData[i]))
                    return i;
        }
        return -1;
    }

    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(mSize + numNew);
        System.arraycopy(a, 0, mElementData, mSize, numNew);
        mSize += numNew;
        return numNew != 0;
    }

    public boolean addAll(Array<E> array) {
        int numNew = array.size();
        ensureCapacityInternal(mSize + numNew);
        System.arraycopy(array.mElementData, 0, mElementData, mSize, numNew);
        mSize += numNew;
        return numNew != 0;
    }

    public E remove(int index) {
        rangeCheck(index);
        E oldValue = (E) mElementData[index];
        int numMoved = mSize - index - 1;
        if (numMoved > 0)
            System.arraycopy(mElementData, index + 1, mElementData, index, numMoved);
        mElementData[--mSize] = null;
        return oldValue;
    }

    public void rangeCheck(int index) throws IndexOutOfBoundsException {
        if (index >= mSize || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + mSize);
    }

    public E get(int index) {
        rangeCheck(index);
        return (E) mElementData[index];
    }

    public E set(int index, E element) {
        rangeCheck(index);
        E oldValue = (E) mElementData[index];
        mElementData[index] = element;
        return oldValue;
    }

    public boolean add(E element) {
        ensureCapacityInternal(mSize + 1);
        mElementData[mSize++] = element;
        return true;
    }

    protected void ensureCapacityInternal(int minCapacity) {
        if (mElementData == EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        if (minCapacity - mElementData.length > 0) {
            int oldCapacity = mElementData.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity - minCapacity < 0)
                newCapacity = minCapacity;
            mElementData = Arrays.copyOf(mElementData, newCapacity);
        }
    }

    public Iterator<E> iterator() {
        return new ArrayIterator();
    }

    public class ArrayIterator implements Iterator<E> {
        int cursor = 0;
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor != mSize;
        }

        @Override
        public E next() {
            if (cursor >= mSize)
                throw new NoSuchElementException();
            lastRet = cursor;
            return (E) mElementData[cursor++];
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            Array.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }
    }

    public void replaceAll(UnaryOperator<E> operator) {
        for (int i = 0; i < mSize; i++) {
            mElementData[i] = operator.apply((E) mElementData[i]);
        }
    }

    public void forEach(Consumer<? super E> action) {
        for (int i = 0; i < mSize; i++) {
            action.accept((E) mElementData[i]);
        }
    }

    public List<E> asList() {
        List<E> list = new ArrayList<>(mSize);
        for (int i = 0; i < mSize; i++) {
            list.add((E) mElementData[i]);
        }
        return list;
    }
}