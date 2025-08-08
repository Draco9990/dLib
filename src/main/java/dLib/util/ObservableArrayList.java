package dLib.util;

import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.events.localevents.RunnableEvent;
import dLib.util.events.localevents.TriConsumerEvent;

import java.util.ArrayList;
import java.util.Collection;

public class ObservableArrayList<E> extends ArrayList<E> {
    public BiConsumerEvent<ArrayList<E>, ArrayList<E>> postChangeEvent = new BiConsumerEvent();                         public static TriConsumerEvent<ObservableArrayList<?>, ArrayList<?>, ArrayList<?>> postChangeGlobalEvent = new TriConsumerEvent<>();
    public ConsumerEvent<ArrayList<E>> postItemsAddedEvent = new ConsumerEvent<>();                                     public static BiConsumerEvent<ObservableArrayList<?>, ArrayList<?>> postItemsAddedGlobalEvent = new BiConsumerEvent<>();
    public ConsumerEvent<ArrayList<E>> postItemsRemovedEvent = new ConsumerEvent<>();                                   public static BiConsumerEvent<ObservableArrayList<?>, ArrayList<?>> postItemsRemovedGlobalEvent = new BiConsumerEvent<>();

    private void onChanged(ArrayList<E> addedItems, ArrayList<E> removedItems) {
        postChangeEvent.invoke(addedItems, removedItems);
        postChangeGlobalEvent.invoke(this, addedItems, removedItems);

        if(!addedItems.isEmpty()){
            postItemsAddedEvent.invoke(addedItems);
            postItemsAddedGlobalEvent.invoke(this, addedItems);
        }

        if(!removedItems.isEmpty()){
            postItemsRemovedEvent.invoke(removedItems);
            postItemsRemovedGlobalEvent.invoke(this, removedItems);
        }
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
        if(result){
            ArrayList<E> addedItems = new ArrayList<>();
            addedItems.add(e);
            onChanged(addedItems, new ArrayList<>());
        }
        return result;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);

        ArrayList<E> addedItems = new ArrayList<>();
        addedItems.add(element);
        onChanged(addedItems, new ArrayList<>());
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = super.addAll(c);
        if (result){
            ArrayList<E> addedItems = new ArrayList<>(c);
            onChanged(addedItems, new ArrayList<>());
        }
        return result;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean result = super.addAll(index, c);
        if (result){
            ArrayList<E> addedItems = new ArrayList<>(c);
            onChanged(addedItems, new ArrayList<>());
        }
        return result;
    }

    @Override
    public E remove(int index) {
        E result = super.remove(index);

        ArrayList<E> removedItems = new ArrayList<>();
        removedItems.add(result);
        onChanged(new ArrayList<>(), removedItems);

        return result;
    }

    @Override
    public boolean remove(Object o) {
        boolean result = super.remove(o);

        if(result){
            ArrayList<E> removedItems = new ArrayList<>();
            removedItems.add((E) o);
            onChanged(new ArrayList<>(), removedItems);
        }

        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        ArrayList<E> removedItems = new ArrayList<>();
        for (Object item : c) {
            if (contains(item)) {
                removedItems.add((E) item);
            }
        }

        boolean result = super.removeAll(c);

        if(result){
            onChanged(new ArrayList<>(), removedItems);
        }

        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        ArrayList<E> removedItems = new ArrayList<>();
        for (E item : this) {
            if (!c.contains(item)) {
                removedItems.add(item);
            }
        }

        boolean result = super.retainAll(c);

        if(result){
            onChanged(new ArrayList<>(), removedItems);
        }

        return result;
    }

    @Override
    public void clear() {
        if (!isEmpty()) {
            ArrayList<E> removedItems = new ArrayList<>(this);

            super.clear();

            onChanged(new ArrayList<>(), removedItems);
        }
    }

    @Override
    public E set(int index, E element) {
        E result = super.set(index, element);

        onChanged(new ArrayList<>(), new ArrayList<>());

        return result;
    }
}
