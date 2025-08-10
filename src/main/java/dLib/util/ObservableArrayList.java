package dLib.util;

import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.events.localevents.TriConsumerEvent;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ObservableArrayList<E> extends ArrayList<E> {

    public BiConsumerEvent<ArrayList<E>, ArrayList<E>> postChangeEvent = new BiConsumerEvent<>();                       public static TriConsumerEvent<ObservableArrayList<?>, ArrayList<?>, ArrayList<?>> postChangeGlobalEvent = new TriConsumerEvent<>();
    public ConsumerEvent<ArrayList<E>> postItemsAddedEvent = new ConsumerEvent<>();                                     public static BiConsumerEvent<ObservableArrayList<?>, ArrayList<?>> postItemsAddedGlobalEvent = new BiConsumerEvent<>();
    public ConsumerEvent<ArrayList<E>> postItemsRemovedEvent = new ConsumerEvent<>();                                   public static BiConsumerEvent<ObservableArrayList<?>, ArrayList<?>> postItemsRemovedGlobalEvent = new BiConsumerEvent<>();

    private void onChanged(ArrayList<E> addedItems, ArrayList<E> removedItems) {
        postChangeEvent.invoke(addedItems, removedItems);
        postChangeGlobalEvent.invoke(this, addedItems, removedItems);

        if (!addedItems.isEmpty()) {
            postItemsAddedEvent.invoke(addedItems);
            postItemsAddedGlobalEvent.invoke(this, addedItems);
        }

        if (!removedItems.isEmpty()) {
            postItemsRemovedEvent.invoke(removedItems);
            postItemsRemovedGlobalEvent.invoke(this, removedItems);
        }
    }

    // ---------------- Constructors ----------------
    public ObservableArrayList(int initialCapacity) { super(initialCapacity); }
    public ObservableArrayList() { super(); }
    public ObservableArrayList(Collection<? extends E> c) { super(c); }

    // ---------------- Basic mutators ----------------
    @Override
    public boolean add(E e) {
        boolean result = super.add(e);
        if (result) {
            ArrayList<E> added = new ArrayList<E>(1);
            added.add(e);
            onChanged(added, new ArrayList<E>());
        }
        return result;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        ArrayList<E> added = new ArrayList<E>(1);
        added.add(element);
        onChanged(added, new ArrayList<E>());
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = super.addAll(c);
        if (result) {
            onChanged(new ArrayList<E>(c), new ArrayList<E>());
        }
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean result = super.addAll(index, c);
        if (result) {
            onChanged(new ArrayList<E>(c), new ArrayList<E>());
        }
        return result;
    }

    @Override
    public E remove(int index) {
        E result = super.remove(index);
        ArrayList<E> removed = new ArrayList<E>(1);
        removed.add(result);
        onChanged(new ArrayList<E>(), removed);
        return result;
    }

    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);
        if (result) {
            ArrayList<E> removed = new ArrayList<E>(1);
            removed.add((E) o);
            onChanged(new ArrayList<E>(), removed);
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        ArrayList<E> removedItems = new ArrayList<E>();
        for (Object item : c) {
            if (contains(item)) {
                removedItems.add((E) item);
            }
        }
        boolean result = super.removeAll(c);
        if (result) {
            onChanged(new ArrayList<E>(), removedItems);
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        ArrayList<E> removedItems = new ArrayList<E>();
        for (E item : this) {
            if (!c.contains(item)) {
                removedItems.add(item);
            }
        }
        boolean result = super.retainAll(c);
        if (result) {
            onChanged(new ArrayList<E>(), removedItems);
        }
        return result;
    }

    @Override
    public void clear() {
        if (!isEmpty()) {
            ArrayList<E> removedItems = new ArrayList<E>(this);
            super.clear();
            onChanged(new ArrayList<E>(), removedItems);
        }
    }

    @Override
    public E set(int index, E element) {
        E oldValue = super.set(index, element);
        onChanged(new ArrayList<E>(), new ArrayList<E>()); // treat as "changed"
        return oldValue;
    }

    // ---------------- Iterator wrappers ----------------
    @Override
    public Iterator<E> iterator() {
        return wrapIterator(super.iterator());
    }

    @Override
    public ListIterator<E> listIterator() {
        return wrapListIterator(super.listIterator());
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return wrapListIterator(super.listIterator(index));
    }

    private Iterator<E> wrapIterator(final Iterator<E> it) {
        return new Iterator<E>() {
            E lastReturned;

            public boolean hasNext() { return it.hasNext(); }
            public E next() { lastReturned = it.next(); return lastReturned; }

            public void remove() {
                it.remove();
                if (lastReturned != null) {
                    ArrayList<E> removed = new ArrayList<E>(1);
                    removed.add(lastReturned);
                    onChanged(new ArrayList<E>(), removed);
                    lastReturned = null;
                }
            }
        };
    }

    private ListIterator<E> wrapListIterator(final ListIterator<E> it) {
        return new ListIterator<E>() {
            E lastReturned;

            public boolean hasNext() { return it.hasNext(); }
            public E next() { lastReturned = it.next(); return lastReturned; }
            public boolean hasPrevious() { return it.hasPrevious(); }
            public E previous() { lastReturned = it.previous(); return lastReturned; }
            public int nextIndex() { return it.nextIndex(); }
            public int previousIndex() { return it.previousIndex(); }

            public void remove() {
                it.remove();
                if (lastReturned != null) {
                    ArrayList<E> removed = new ArrayList<E>(1);
                    removed.add(lastReturned);
                    onChanged(new ArrayList<E>(), removed);
                    lastReturned = null;
                }
            }

            public void set(E e) {
                it.set(e);
                onChanged(new ArrayList<E>(), new ArrayList<E>());
            }

            public void add(E e) {
                it.add(e);
                ArrayList<E> added = new ArrayList<E>(1);
                added.add(e);
                onChanged(added, new ArrayList<E>());
            }
        };
    }

    // ---------------- subList wrapper ----------------
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        final List<E> parentView = super.subList(fromIndex, toIndex);
        final ObservableArrayList<E> outer = this;

        return new AbstractList<E>() {
            public E get(int index) { return parentView.get(index); }
            public int size() { return parentView.size(); }

            public E set(int index, E element) {
                E oldValue = parentView.set(index, element);
                outer.onChanged(new ArrayList<E>(), new ArrayList<E>());
                return oldValue;
            }

            public void add(int index, E element) {
                parentView.add(index, element);
                ArrayList<E> added = new ArrayList<E>(1);
                added.add(element);
                outer.onChanged(added, new ArrayList<E>());
            }

            public E remove(int index) {
                E removed = parentView.remove(index);
                ArrayList<E> removedItems = new ArrayList<E>(1);
                removedItems.add(removed);
                outer.onChanged(new ArrayList<E>(), removedItems);
                return removed;
            }

            public Iterator<E> iterator() {
                return outer.wrapIterator(parentView.iterator());
            }

            public ListIterator<E> listIterator(int index) {
                return outer.wrapListIterator(parentView.listIterator(index));
            }
        };
    }
}
