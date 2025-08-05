package dLib.util;

import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.events.localevents.RunnableEvent;

import java.util.ArrayList;
import java.util.Collection;

public class ObservableArrayList<E> extends ArrayList<E> {
    public RunnableEvent onChange = new RunnableEvent();

    private void onChanged() {
        onChange.invoke();
    }

    // --- Mutating methods overridden below ---


    public ObservableArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public ObservableArrayList() {
        super();
    }

    public ObservableArrayList(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public boolean add(E e) {
        boolean result = super.add(e);
        if (result) onChanged();
        return result;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        onChanged();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = super.addAll(c);
        if (result) onChanged();
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean result = super.addAll(index, c);
        if (result) onChanged();
        return result;
    }

    @Override
    public E remove(int index) {
        E result = super.remove(index);
        onChanged();
        return result;
    }

    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);
        if (result) onChanged();
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = super.removeAll(c);
        if (result) onChanged();
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean result = super.retainAll(c);
        if (result) onChanged();
        return result;
    }

    @Override
    public void clear() {
        if (!isEmpty()) {
            super.clear();
            onChanged();
        }
    }

    @Override
    public E set(int index, E element) {
        E result = super.set(index, element);
        onChanged();
        return result;
    }
}
